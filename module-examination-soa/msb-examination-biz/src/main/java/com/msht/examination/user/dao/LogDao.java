/**
 * 
 */
package com.msht.examination.user.dao;

import org.springframework.stereotype.Component;

import com.msht.examination.user.entity.LogPo;
import com.msht.framework.persistent.db.dao.BaseDao;

/**
 * @author lindaofen
 * 
 *
 */
@Component
public class LogDao extends BaseDao<LogPo> {

	@Override
	public String getNamespace() {
		return "com.msht.examination.user.dao.LogDao";
	}
}
