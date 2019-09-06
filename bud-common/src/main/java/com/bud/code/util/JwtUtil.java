package com.bud.code.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bud.code.common.constant.DefContants;
import com.bud.code.common.exception.SysException;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Author zsq
 * @Date 2018-07-12 14:23
 * @Desc JWT工具类
 **/
public class JwtUtil {

	@Value("${jwt.secret.key}")
	private String secretKey;

	// 过期时间30分钟
	public static final long EXPIRE_TIME = 30 * 60 * 1000;

	/**
	 * 校验token是否正确
	 *
	 * @param token  密钥
	 * @param userName 用户的密码
	 * @param secret 密码
	 * @return 是否正确
	 */
	public static boolean verify(String token, String userName, String secret) {
		try {
			// 根据密码生成JWT效验器
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).withClaim("userName", userName).build();
			// 效验TOKEN
			DecodedJWT jwt = verifier.verify(token);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	/**
	 * 获得token中的信息无需secret解密也能获得
	 *
	 * @return token中包含的用户名
	 */
	public static String getUserName(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim("userName").asString();
		} catch (JWTDecodeException e) {
			return null;
		}
	}

	/**
	 * 生成签名,30min后过期
	 *
	 * @param userName 用户名
	 * @param secret   密码
	 * @return 加密的token
	 */
	public static String sign(String userName, String secret) {
		Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
		Algorithm algorithm = Algorithm.HMAC256(secret);
		// 附带username信息
		return JWT.create().withClaim("userName", userName).withExpiresAt(date).sign(algorithm);

	}

	/**
	 * 根据request中的token获取用户账号
	 * 
	 * @param request
	 * @return
	 * @throws SysException
	 */
	public static String getUserNameByToken(HttpServletRequest request) throws SysException {
		String accessToken = request.getHeader(DefContants.AUTHORIZATION);
		String userName = getUserName(accessToken);
		if (oConvertUtils.isEmpty(userName)) {
			throw new SysException("未获取到用户");
		}
		return userName;
	}
	
}
