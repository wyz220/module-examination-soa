package com.msht.examination.utils;

import java.util.UUID;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.msht.examination.user.entity.UserPo;


@Service
public class PwdHelper {

    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    private String algorithmName = "md5";
    private int hashIterations = 2;

    public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    public void encryptPassword(UserPo user) {

        user.setSalt(randomNumberGenerator.nextBytes().toHex());

        String newPassword = new SimpleHash(
                algorithmName,
                user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()),
                hashIterations).toHex();

        user.setPassword(newPassword);
    }
    
    public String encryptPassword(String plaintext, String salt) {

        String password = new SimpleHash(
                algorithmName,
                plaintext,
                ByteSource.Util.bytes(salt),
                hashIterations).toHex().toUpperCase();

        return password;
    }
    
    public static void main(String[] args){
    	PwdHelper helper = new PwdHelper();
    	UserPo userPo = new UserPo();
    	userPo.setPassword("123456");
    	helper.encryptPassword(userPo);
    	System.out.println(JSON.toJSON(userPo));
    	//System.out.println("b0aff300593dda5193523cb58b65def5".toUpperCase());
    	//System.out.println(UUID.randomUUID().toString().replace("-", ""));
    	System.out.println(helper.encryptPassword("123456", "5fc6f04289d530f4967a6576457a9423"));
    }
}
