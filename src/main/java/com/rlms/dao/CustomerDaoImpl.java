package com.rlms.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rlms.constants.RLMSConstants;
import com.rlms.model.RlmsBranchCustomerMap;
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
	public List<RlmsBranchCustomerMap> getAllCustomersForBranches(List<Integer> listOfBranchCompanyMapId) {
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsBranchCustomerMap.class)
				 .add(Restrictions.in("companyBranchMapDtls.companyBranchMapId", listOfBranchCompanyMapId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 List<RlmsBranchCustomerMap> listOfCustomers = criteria.list();
		 return listOfCustomers;
	}

	@Override
	public Integer saveCustomerM(RlmsCustomerMaster customerMaster) {
		// TODO Auto-generated method stub
		Integer customerId = (Integer) this.sessionFactory.getCurrentSession().save(customerMaster);
		return customerId;
		
	}

}
