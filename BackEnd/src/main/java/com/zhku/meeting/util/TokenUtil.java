package com.zhku.meeting.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zhku.meeting.entities.Department;
import com.zhku.meeting.entities.Manager;

import java.util.Date;

public class TokenUtil {

    private static final long EXPIRE_TIME= 60*60*1000;
    private static final String TOKEN_SECRET="token123";  //密钥盐


    /**
     * 签名生成
     * @param object
     * @return
     */
    public static String sign(Object object){

        String token = null;
        if (object instanceof Department){
            Department department = (Department)object;
            try {
                Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
                token = JWT.create()
                        .withIssuer("auth0")
                        .withClaim("username", department.getDeptNo())
                        .withExpiresAt(expiresAt)
                        // 使用了HMAC256加密算法。
                        .sign(Algorithm.HMAC256(TOKEN_SECRET));
            } catch (Exception e){
                e.printStackTrace();
            }
            return token;
        }else{
            Manager manager = (Manager)object;
            try {
                Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
                token = JWT.create()
                        .withIssuer("auth0")
                        .withClaim("username", manager.getUsername())
                        .withExpiresAt(expiresAt)
                        // 使用了HMAC256加密算法。
                        .sign(Algorithm.HMAC256(TOKEN_SECRET));
            } catch (Exception e){
                e.printStackTrace();
            }
            return token;
        }

    }

    /**
     * 签名验证
     * @param token
     * @return
     */
    public static boolean verify(String token){
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("认证通过：");
            System.out.println("issuer: " + jwt.getIssuer());
            System.out.println("username: " + jwt.getClaim("username").asString());
            System.out.println("过期时间：      " + jwt.getExpiresAt());
            return true;
        } catch (Exception e){
            return false;
        }
    }
    public static String getNo(String token){
        String no = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            no = jwt.getClaim("username").asString();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            return no;
        }
    }

}