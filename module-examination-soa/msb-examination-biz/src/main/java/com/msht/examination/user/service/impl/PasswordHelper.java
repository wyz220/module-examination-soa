package com.msht.examination.user.service.impl;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.net.URLCodec;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.msht.examination.user.entity.UserPo;
import com.msht.framework.common.utils.StringUtils;


@Service
public class PasswordHelper {

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

    	if (StringUtils.isBlank(user.getSalt())){
            user.setSalt(randomNumberGenerator.nextBytes().toHex());
    	}

        String newPassword = new SimpleHash(
                algorithmName,
                user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()),
                hashIterations).toHex();

        user.setPassword(newPassword);
    }
    
    public String encryptPassword(String code) {

        String salt = randomNumberGenerator.nextBytes().toHex();

        String newPassword = new SimpleHash(
                algorithmName,
                code,
                ByteSource.Util.bytes(salt),
                hashIterations).toHex();
        
        return newPassword;

    }
    
    public static void main(String[] args) throws UnsupportedEncodingException{
    	PasswordHelper helper = new PasswordHelper();
    	UserPo userPo = new UserPo();
    	userPo.setPassword("111111");
    	userPo.setSalt("bbbab0fe32fd9f1b3d303b3bc7742dab");
    	helper.encryptPassword(userPo);
    	System.out.println(JSON.toJSON(userPo));

    	URLCodec urlCodec = new URLCodec();

/*        String enMsg =urlCodec.encode("范芳铭在做Url encode测试，123456", CharEncoding.UTF_8);
        System.out.println(enMsg);*/
        
        String base = StringUtils.generateRandomStr(StringUtils.BASE1, 108);
        System.out.println(urlCodec.encode(base, CharEncoding.UTF_8));
    }
}
