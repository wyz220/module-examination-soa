package com.msht.examination.authorization.manager.impl;

import org.springframework.stereotype.Component;

import com.msht.framework.common.utils.StringUtils;
import com.msht.examination.authorization.manager.TokenManager;
import com.msht.examination.authorization.model.TokenModel;
import com.msht.examination.utils.JedisServiceUtils;

import java.util.UUID;

/**
 * 通过Redis存储和验证token的实现类
 * @author lindaofen
 *
 */
@Component
public class RedisTokenManager implements TokenManager {

	@Override
	public TokenModel createToken(long userId) {
        //使用uuid作为源token
        String token = UUID.randomUUID().toString().replace("-", "");
        TokenModel model = new TokenModel(userId, token);
        //存储到redis并设置过期时间
        
        JedisServiceUtils.set(token, JedisServiceUtils.EXPIRE_MONTH, String.valueOf(userId), 0, true);
        return model;
	}

	@Override
    public TokenModel getToken(String authentication) {
        if (StringUtils.isBlank(authentication)) {
            return null;
        }
        String userId = JedisServiceUtils.get(authentication, 0);
        if (StringUtils.isBlank(userId)){
            return null;
        }
        
        TokenModel model = new TokenModel(Long.valueOf(userId), authentication);
        JedisServiceUtils.set(model.getToken(), JedisServiceUtils.EXPIRE_MONTH, userId, 0, true);
        return model;

    }
    
	@Override
	public boolean checkToken(TokenModel model) {
        if (model == null) {
            return false;
        }
        String userId = JedisServiceUtils.get(model.getToken(), 0);
        if (userId == null) {
            return false;
        }
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        JedisServiceUtils.set(model.getToken(), JedisServiceUtils.EXPIRE_MONTH, userId, 0, true);
        return true;
	}
	

    @Override
    public void deleteToken(String token) {
    	JedisServiceUtils.delByKey(token, 0);
    }

}
