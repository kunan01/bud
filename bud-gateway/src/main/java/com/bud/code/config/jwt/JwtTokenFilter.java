package com.bud.code.config.jwt;

import com.alibaba.fastjson.JSON;
import com.bud.code.common.api.vo.Result;
import com.bud.code.common.constant.CommonConstant;
import com.bud.code.common.constant.DefContants;
import com.bud.code.util.JwtUtil;
import com.bud.code.util.RedisUtil;
import com.bud.code.util.oConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
@Component
public class JwtTokenFilter implements GlobalFilter, Ordered {
    @Autowired
    private RedisUtil redisUtil;

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.blacklist.key.format}")
    private String jwtBlacklistKeyFormat;

    @Value("${auth.skip.urls}")
    private String[] skipAuthUrls;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String url = exchange.getRequest().getURI().getPath();
        // 跳过不需要验证的路径
        if(Arrays.asList(skipAuthUrls).contains(url)){
            return chain.filter(exchange);
        }
        // 从请求头中取出token
        String token = exchange.getRequest().getHeaders().getFirst(DefContants.AUTHORIZATION);
        // 未携带token或token在黑名单内
        if (token == null ||
                token.isEmpty() ||
                isBlackToken(token)) {
            ServerHttpResponse originalResponse = exchange.getResponse();
            originalResponse.setStatusCode(HttpStatus.OK);
            originalResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            Result<Object> result = Result.error(401, "无访问权限");
            byte[] response = JSON.toJSONString(result).getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = originalResponse.bufferFactory().wrap(response);
            return originalResponse.writeWith(Flux.just(buffer));
        }
        // 取出token包含的身份，用于业务处理
        Result result = checkUserTokenIsEffect(token);
        String userName = result.getResult().toString();
        //将现在的request，添加当前身份
        ServerHttpRequest mutableReq = exchange.getRequest().mutate().header("Authorization-UserName", userName).build();
        ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
        return chain.filter(mutableExchange);
    }

    @Override
    public int getOrder() {
        return -2;
    }

    /**
     * 判断token是否在黑名单内
     * @param token
     * @return
     */
    private boolean isBlackToken(String token){
        assert token != null;
        return redisUtil.hasKey(String.format(jwtBlacklistKeyFormat, token));
    }

    private Result checkUserTokenIsEffect(String token) {
        // 解密获得username，用于和数据库进行对比
        String userName = JwtUtil.getUserName(token);
        if (userName == null) {
            return Result.noauth("token非法无效!");
        }
        // 查询数据库获取用户信息（暂时未处理）

        // 校验token是否超时失效 & 或者账号密码是否错误
        if (!jwtTokenRefresh(token, userName, secretKey)) {
            return Result.error("Token失效，请重新登录!");
        }
        return Result.dataSuccess(userName);
    }

    /**
     * JWTToken刷新生命周期 （解决用户一直在线操作，提供Token失效问题）
     * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面(这时候k、v值一样)
     * 2、当该用户再次请求时，通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份验证
     * 3、当该用户这次请求JWTToken值还在生命周期内，则会通过重新PUT的方式k、v都为Token值，缓存中的token值生命周期时间重新计算(这时候k、v值一样)
     * 4、当该用户这次请求jwt生成的token值已经超时，但该token对应cache中的k还是存在，则表示该用户一直在操作只是JWT的token失效了，程序会给token对应的k映射的v值重新生成JWTToken并覆盖v值，该缓存生命周期重新计算
     * 5、当该用户这次请求jwt在生成的token值已经超时，并在cache中不存在对应的k，则表示该用户账户空闲超时，返回用户信息已失效，请重新登录。
     * 6、每次当返回为true情况下，都会给Response的Header中设置Authorization，该Authorization映射的v为cache对应的v值。
     * 7、注：当前端接收到Response的Header中的Authorization值会存储起来，作为以后请求token使用
     * 参考方案：https://blog.csdn.net/qq394829044/article/details/82763936
     *
     * @param userName
     * @param passWord
     * @return
     */
    public boolean jwtTokenRefresh(String token, String userName, String passWord) {
        String cacheToken = String.valueOf(redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + token));
        if (oConvertUtils.isNotEmpty(cacheToken)) {
            // 校验token有效性
            if (!JwtUtil.verify(cacheToken, userName, passWord)) {
                String newAuthorization = JwtUtil.sign(userName, passWord);
                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, newAuthorization);
                // 设置超时时间
                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);
            } else {
                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, cacheToken);
                // 设置超时时间
                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);
            }
            return true;
        }
        return false;
    }
}
