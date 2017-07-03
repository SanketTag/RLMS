package com.rlms.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rlms.constants.RLMSConstants;
import com.rlms.model.RlmsLiftAmcDtls;

@Repository
public class DashboardDaoImpl implements DashboardDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<RlmsLiftAmcDtls> getAMCDetilsForLifts() {

		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(RlmsLiftAmcDtls.class);
		criteria.createAlias("liftCustomerMap", "lcm");
		criteria.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		criteria.addOrder(Order.asc("craetedDate"));
		List<RlmsLiftAmcDtls> listOFAMCdtlsForAllLifts = criteria.list();
		return listOFAMCdtlsForAllLifts;

	}
}
