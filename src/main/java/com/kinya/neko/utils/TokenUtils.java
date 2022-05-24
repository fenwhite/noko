package com.kinya.neko.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * @author ：white
 * @description：Token管理类
 * @date ：2022/4/12
 */
public class TokenUtils {
    // temporarily
    public static final String ISSUER = "miku";
    // token AES encrypt info
    public static final String KEY_ALIAS = "token";
    public static final String KEY_PASS = "tokem";

    public static String create(String id){
        JwtBuilder builder = Jwts.builder();
        builder.setSubject(id)
               .setIssuer(ISSUER)
               .signWith(SignatureAlgorithm.HS256,
                       EncryptUtil.getAESecretKey(KEY_ALIAS, KEY_PASS));
        return builder.compact();
    }

    public static String invalid(){
        JwtBuilder builder = Jwts.builder();
        Date now = new Date(System.currentTimeMillis());
        builder.setExpiration(DateUtils.addDays(now ,-1))
               .setIssuer(ISSUER)
               .signWith(SignatureAlgorithm.HS256,
                       EncryptUtil.getAESecretKey(KEY_ALIAS, KEY_PASS));
        return builder.compact();
    }

    public static Claims prase(String token) {
        Jwt<Header, Claims> headerClaimsJwt = Jwts.parser()
                .setSigningKey(EncryptUtil.getAESecretKey(KEY_ALIAS, KEY_PASS))
                .parseClaimsJwt(token);
        return headerClaimsJwt.getBody();
    }

    public static boolean check(Claims props){
        String subject = props.getSubject();
        if(StringUtils.isBlank(subject))
            return false;
        Date expiration = props.getExpiration();
        if(expiration!=null) {
            Date now = new Date(System.currentTimeMillis());
            return now.before(expiration);
        }
        return true;
    }
}
