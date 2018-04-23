/**
 * 
 */
package com.msht.examination.user.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.msht.examination.user.dao.UserDao;
import com.msht.examination.user.entity.UserPo;
import com.msht.examination.user.entity.UserRolePo;
import com.msht.examination.user.service.RoleService;
import com.msht.examination.user.service.UserService;
import com.msht.framework.common.exception.BusinessException;
import com.msht.framework.common.utils.AliyunOSS;
import com.msht.framework.common.utils.StringUtils;
import com.msht.framework.entity.Page;

/**
 * @author lindaofen
 *
 */
@Component("userService")
@Service(interfaceName="com.msht.examination.user.service.UserService",version = "1.0.0")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
    @Autowired
    private PasswordHelper passwordHelper;
    
    @Autowired
    private RoleService roleService;
	
	@Override
	public UserPo get(UserPo po) throws BusinessException {
		return userDao.get(po);
	}

	@Override
	public void insert(UserPo po) throws BusinessException {
		passwordHelper.encryptPassword(po);
		userDao.insert(po);
		
		UserRolePo userRolePo = new UserRolePo();
		userRolePo.setUserId(po.getId());
		userRolePo.setRoleId(po.getRoleId());
		userDao.insertUserRole(userRolePo);
	}

	@Override
	public void insertBatch(List<UserPo> list) throws BusinessException {
		userDao.insertBatch(list);
	}

	@Override
	public void delete(Object id) throws BusinessException {
		userDao.delete(id);
		userDao.deleteUserRoleByUserId(id);
	}

	@Override
	public void update(UserPo po) throws BusinessException {
		if (StringUtils.isNotBlank(po.getPassword())){
			passwordHelper.encryptPassword(po);
		}
		userDao.update(po);
		
		if (po.getRole() != null){
			userDao.deleteUserRoleByUserId(po.getId());
			UserRolePo userRolePo = new UserRolePo();
			userRolePo.setUserId(po.getId());
			userRolePo.setRoleId(po.getRoleId());
			userDao.insertUserRole(userRolePo);
		}


	}

	@Override
	public List<UserPo> list(UserPo po) throws BusinessException {
		return userDao.list(po);
	}

	@Override
	public Page<UserPo> page(UserPo po) throws BusinessException {
		int count = userDao.count(po);
		po.getPage().setCount(count);
		List<UserPo> list =  userDao.page(po);
		po.getPage().setList(list);
		return po.getPage();
	}

	@Override
	public Set<String> findRoles(String username) throws BusinessException {
		UserPo param = new UserPo();
		param.setUsername(username);
        UserPo user = this.get(param);
        if(user == null) {
            return Collections.EMPTY_SET;
        }
        
        List<Long> roleIds = userDao.getUserRoleIds(user.getId());
        return roleService.findRoles(roleIds.toArray(new Long[]{}));
	}

	@Override
	public Set<String> findPermissions(String username) throws BusinessException {
		UserPo param = new UserPo();
		param.setUsername(username);
        UserPo user = this.get(param);
        if(user == null) {
            return Collections.EMPTY_SET;
        }
        
        List<Long> roleIds = userDao.getUserRoleIds(user.getId());
        return roleService.findPermissions(roleIds.toArray(new Long[]{}));
	}

	@Override
	public Long findUserRoleId(Long userId) throws BusinessException {
		List<Long> roleIds = userDao.getUserRoleIds(userId);
		if (roleIds != null && roleIds.size() > 0){
			return roleIds.get(0);
		}
		return null;
	}

	@Override
	public void batchDelete(Long[] userIds) throws BusinessException {
		for (Long userId : userIds){
			userDao.delete(userId);
			userDao.deleteUserRoleByUserId(userId);
		}
	}

	@Override
	public void modifyMemberProfile(UserPo po) throws BusinessException {
		UserPo param = new UserPo();
		param.setId(po.getId());
		UserPo oldUser = userDao.get(param);
		StringUtils.generateRandomStr(9);
		List<String> fileNames = new ArrayList<String>();
/*		if (StringUtils.isNotBlank(po.getLegalIdCardFrontImg()) && StringUtils.isNotBlank(oldUser.getLegalIdCardFrontImg())){
			fileNames.add(oldUser.getLegalIdCardFrontImg());
		}
		if (StringUtils.isNotBlank(po.getLegalIdCardBackImg()) && StringUtils.isNotBlank(oldUser.getLegalIdCardBackImg())){
			fileNames.add(oldUser.getLegalIdCardBackImg());
		}
		
		if (StringUtils.isNotBlank(po.getLegalBusLicenseFrontImg()) && StringUtils.isNotBlank(oldUser.getLegalBusLicenseFrontImg())){
			fileNames.add(oldUser.getLegalBusLicenseFrontImg());
		}
		if (StringUtils.isNotBlank(po.getLegalBusLicenseBackImg()) && StringUtils.isNotBlank(oldUser.getLegalBusLicenseBackImg())){
			fileNames.add(oldUser.getLegalBusLicenseBackImg());
		}
		
		if (StringUtils.isNotBlank(po.getLinkmanIdCardFrontImg()) && StringUtils.isNotBlank(oldUser.getLinkmanIdCardFrontImg())){
			fileNames.add(oldUser.getLinkmanIdCardFrontImg());
		}
		if (StringUtils.isNotBlank(po.getLinkmanIdCardBackImg()) && StringUtils.isNotBlank(oldUser.getLinkmanIdCardBackImg())){
			fileNames.add(oldUser.getLinkmanIdCardBackImg());
		}*/
		
		if (fileNames.size() > 0){
			AliyunOSS.deleteObjects(AliyunOSS.bucketName, fileNames);
		}
		
		userDao.update(po);
	}

	@Override
	public void delete(UserPo po) throws BusinessException {
		userDao.update(po);
		userDao.deleteUserRoleByUserId(po.getId());
	}

}
