package com.zlee.tofu.login.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zlee.tofu.feign.entity.TofuUser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        return createToken(claimMap, EXPIRE_DATE * 72);
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

    public static String renewToken(String token) {
        HashMap<String, String> claimMap = new HashMap<>(16);
        for (Map.Entry<String, Claim> claimEntry : parseToken(token).getClaims().entrySet()) {
            claimMap.put(claimEntry.getKey(), claimEntry.getValue().asString());
        }
        return createToken(claimMap);
    }

    /**
     * 登陆验证通过生成带基本信息的token
     * @param loginUser 当前登录用户
     * @return token
     * */
    public static String generateToken(TofuUser loginUser) {
        //认证通过生成一个token
        HashMap<String, String> claimMap = new HashMap<>(16);
        claimMap.put("userId", String.valueOf(loginUser.getUserId()));
        claimMap.put("nickName", loginUser.getNickName());
        claimMap.put("avatar", loginUser.getAvatar());
        return JwtUtil.createToken(claimMap);
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
