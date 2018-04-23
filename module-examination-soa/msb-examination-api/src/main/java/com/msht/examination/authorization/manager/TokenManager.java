package com.msht.examination.authorization.manager;

import com.msht.examination.authorization.model.TokenModel;

/**
 * 对Token进行操作的接口
 * @author lindaofen
 *
 */
public interface TokenManager {

    /**
     * 创建一个token关联上指定用户
     * @param userId 指定用户的id
     * @return 生成的token
     */
    public TokenModel createToken(long userId);

    /**
     * 检查token是否有效
     * @param model token
     * @return 是否有效
     */
    public boolean checkToken(TokenModel model);

    /**
     * 从字符串中解析token
     * @param authentication 加密后的字符串
     * @return
     */
    public TokenModel getToken(String authentication);

    /**
     * 清除token
     * @param token 
     */
    public void deleteToken(String token);

}
