package com.zlee.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;

/**
 * @author z-Lee
 * @date 2023-01-29-20:10
 */
public class JwtUtil {
    /**
     * 秘钥：需要妥善保存，一旦泄露token有可能被破解
     */
    private static final String SECRET = "#$@!FX()!JF%JS$KLS*KFH##%#QPCIXMSJ";
    /**
     * 签名算法：推荐使用HMAC256
     */
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    /**
     * 设置默认过期时间 1000*60*60=3600000=1h
     */
    private static final Long EXPIRE_DATE = 3600000L;

    /**
     * 获取token信息
     *
     * @param claimMap 自定义的条件
     * @return token令牌
     */
    public static String createToken(HashMap<String, String> claimMap) {
        return createToken(claimMap, EXPIRE_DATE);
    }

    /**
     * @param claimMap 自定义条件
     * @param expire   过期时间 单位：毫秒
     * @return token令牌
     */
    public static String createToken(HashMap<String, String> claimMap, Long expire) {
        JWTCreator.Builder builder = JWT.create();
        claimMap.forEach(builder::withClaim);
        builder.withExpiresAt(new Date(System.currentTimeMillis() + expire));
        return builder.sign(ALGORITHM);
    }

    /**
     * 获取DecodedJWT
     *
     * @param token token令牌
     * @return DecodedJWT
     */
    public static DecodedJWT parseToken(String token) {
        return JWT.require(ALGORITHM).build().verify(token);
    }

    /**
     * 获取DecodedJWT
     *
     * @param algorithm 指定算法
     * @param token     token令牌
     * @return DecodedJWT
     */
    public static DecodedJWT parseToken(Algorithm algorithm, String token) {
        return JWT.require(algorithm).build().verify(token);
    }
}
