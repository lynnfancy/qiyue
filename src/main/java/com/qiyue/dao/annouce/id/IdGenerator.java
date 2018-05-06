package com.qiyue.dao.annouce.id;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.qiyue.util.BaseUtil;
import com.qiyue.util.DateUtil;

public class IdGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		String flowNo = "";
		try {
			String entityCode = (String) object.getClass().getField("ENTITY_CODE").get(object);
			flowNo = entityCode + DateUtil.getSystemTime() + BaseUtil.getRandomString(8, BaseUtil.TYPE_NUMBER);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flowNo;
	}

}
