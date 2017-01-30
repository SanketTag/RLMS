package com.rlms.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rlms.constants.RLMSConstants;
import com.rlms.model.RlmsCustomerMaster;

@Repository("customerDao")
public class CustomerDaoImpl implements CustomerDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public RlmsCustomerMaster getCustomerByEmailId(String emailId) {
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsCustomerMaster.class)
				 .add(Restrictions.eq("emailID", emailId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 return (RlmsCustomerMaster)criteria.uniqueResult();
	}

	@Override
	public void saveCustomerM(RlmsCustomerMaster customerMaster) {
		// TODO Auto-generated method stub
		this.sessionFactory.getCurrentSession().save(customerMaster);
		
	}

}
