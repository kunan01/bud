package com.bud.code.controller;

import com.alibaba.fastjson.JSONObject;
import com.bud.code.common.api.vo.Result;
import com.bud.code.common.constant.CommonConstant;
import com.bud.code.common.constant.DefContants;
import com.bud.code.entity.dto.SysLoginModel;
import com.bud.code.entity.sysuser.SysUser;
import com.bud.code.service.ISysUserService;
import com.bud.code.util.JwtUtil;
import com.bud.code.util.PasswordUtil;
import com.bud.code.util.RedisUtil;
import com.bud.code.util.encryption.EncryptedString;
import com.bud.code.util.oConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author scott
 * @since 2018-12-17
 */
@Slf4j
@Api(tags="用户登录")
@RestController
@RequestMapping("/sys")
public class LoginController {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation("登录接口")
    @PostMapping(value = "/login")
    public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel) throws Exception {
        Result<JSONObject> result = new Result<JSONObject>();
        String userName = sysLoginModel.getUserName();
        String password = sysLoginModel.getPassword();

        //1. 校验用户是否有效
        SysUser sysUser = sysUserService.getUserByName(userName);
        result = sysUserService.checkUserIsEffective(sysUser);
        if(!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
        String userPassword = PasswordUtil.encrypt(userName, password, sysUser.getSalt());
        String sysPassword = sysUser.getPassword();
        if (!sysPassword.equals(userPassword)) {
            result.error500("用户名或密码错误");
            return result;
        }

        //用户登录信息
        userInfo(sysUser, result);
        return result;
    }

    /**
     * 退出登录
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "/logout")
    public Result<Object> logout(HttpServletRequest request,HttpServletResponse response) {
        //用户退出逻辑
        String token = request.getHeader(DefContants.X_ACCESS_TOKEN);
        if(oConvertUtils.isEmpty(token)) {
            return Result.error("退出登录失败！");
        }
        String username = JwtUtil.getUsername(token);
        SysUser sysUser = sysUserService.getUserByName(username);
        if(sysUser!=null) {
            log.info(" 用户名:  "+sysUser.getRealName()+",退出成功！ ");
            //清空用户Token缓存
            redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
            //清空用户权限缓存：权限Perms和角色集合
            redisUtil.del(CommonConstant.LOGIN_USER_CACHERULES_ROLE + username);
            redisUtil.del(CommonConstant.LOGIN_USER_CACHERULES_PERMISSION + username);
            return Result.ok("退出登录成功！");
        }else {
            return Result.error("无效的token");
        }
    }

    /**
     * 用户信息
     *
     * @param sysUser
     * @param result
     * @return
     */
    private Result<JSONObject> userInfo(SysUser sysUser, Result<JSONObject> result) {
        String sysPassword = sysUser.getPassword();
        String userName = sysUser.getUserName();
        // 生成token
        String token = JwtUtil.sign(userName, sysPassword);
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        // 设置超时时间
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);

        // 获取用户部门信息
        JSONObject obj = new JSONObject();
        obj.put("token", token);
        obj.put("userInfo", sysUser);
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }

    /**
     * 获取加密字符串
     * @return
     */
    @GetMapping(value = "/getEncryptedString")
    public Result<Map<String,String>> getEncryptedString(){
        Result<Map<String,String>> result = new Result<Map<String,String>>();
        Map<String,String> map = new HashMap<String,String>();
        map.put("key", EncryptedString.key);
        map.put("iv", EncryptedString.iv);
        result.setResult(map);
        return result;
    }

}