
package com.maan.eway.auth.token;


import static com.maan.eway.auth.token.Constants.SIGNING_KEY;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.maan.eway.bean.LoginMaster;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final String SECRET = "MaanSavor@123456";
	

	public String getUsernameFromToken(String token) throws Exception {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) throws Exception {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws Exception {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    
	private Claims getAllClaimsFromToken(String token) throws Exception {
		String username = decrypt(token);
		String decoded = new String(Base64.getDecoder().decode(username));
		return Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(decoded).getBody();
	}

    private Boolean isTokenExpired(String token,HttpServletRequest req) {
    	Date expiration = new Date();
    	boolean time = false;
    	String value = req.getSession().getAttribute(token)==null?"":req.getSession().getAttribute(token).toString();
    	if(StringUtils.isNotBlank(value)) {
    		DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
    		try {
    		expiration = formatter.parse(value);
    		long addMinuteTime = expiration.getTime();  
    		expiration = new Date(addMinuteTime + (50*60*1000));
    		time = expiration.before(new Date());
    		if(!time) {
    		req.getSession().setAttribute(token,new Date());
    		}
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	}else {
    		req.getSession().setAttribute(token,new Date());
    	}
        return time;
    }

    public String generateToken(LoginMaster user) throws Exception {
        return doGenerateToken(user.getLoginId());
    }
      
   public String doGenerateToken(String loginid) throws Exception {
        Claims claims = Jwts.claims().setSubject(loginid);
        claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer("http://devglan.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3000000 )) 
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
        String encoded = Base64.getEncoder().encodeToString(token.getBytes());
        return  encrypt(encoded);
        //return token;
    }
   
   public static String encrypt(String data) throws Exception {
       SecretKeySpec key = new SecretKeySpec(SECRET.getBytes(), "AES");
       Cipher cipher = Cipher.getInstance("AES");
       cipher.init(Cipher.ENCRYPT_MODE, key);
       byte[] encrypted = cipher.doFinal(data.getBytes());
       return Base64.getEncoder().encodeToString(encrypted);
   }
   
   public static String decrypt(String encryptedData) throws Exception {
	    SecretKeySpec key = new SecretKeySpec(SECRET.getBytes(), "AES");
	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.DECRYPT_MODE, key);
	    byte[] decoded = Base64.getDecoder().decode(encryptedData);
	    return new String(cipher.doFinal(decoded));
	}

   public Boolean validateToken(String token, UserDetails userDetails,HttpServletRequest req) throws Exception {
//       final String username = decrypt(token);
//       String decoded = new String(Base64.getDecoder().decode(username));
       return (token.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token,req));
   }
}
