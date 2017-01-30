package com.rlms.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rlms.constants.RLMSConstants;
import com.rlms.contract.UserMetaInfo;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsSpocRoleMaster;
@Repository
public class LiftDaoImpl implements LiftDao{
	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<RlmsLiftCustomerMap> getAllLiftsForCustomers(List<Integer> listOfCuistomers){		
			 Session session = this.sessionFactory.getCurrentSession();
			 Criteria criteria = session.createCriteria(RlmsLiftCustomerMap.class)
					 .createAlias("branchCustomerMap.customerMaster", "custo")
					 .add(Restrictions.in("custo.customerId", listOfCuistomers))
					 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
			 List<RlmsLiftCustomerMap> listOfAllLifts = criteria.list();
			 return listOfAllLifts;
		
	}
}
