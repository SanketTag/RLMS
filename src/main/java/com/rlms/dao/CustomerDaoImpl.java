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
import com.rlms.model.RlmsCustomerMemberMap;
import com.rlms.model.RlmsMemberMaster;

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
	public RlmsCustomerMaster getCustomerById(Integer customerId) {
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsCustomerMaster.class)
				 .add(Restrictions.eq("customerId", customerId))
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
	
	@Override
	public Integer saveCustomerMemberMap(RlmsCustomerMemberMap customerMemberMap) {
		// TODO Auto-generated method stub
		Integer customerId = (Integer) this.sessionFactory.getCurrentSession().save(customerMemberMap);
		return customerId;
		
	}
	
	@Override
	public Integer saveMemberM(RlmsMemberMaster memberMaster) {
		// TODO Auto-generated method stub
		Integer customerId = (Integer) this.sessionFactory.getCurrentSession().save(memberMaster);
		return customerId;
		
	}
	
	@Override
	public RlmsMemberMaster getMemberByCntNo(String phoneNumber) {
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsMemberMaster.class)
				 .add(Restrictions.eq("contactNumber", phoneNumber))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 return (RlmsMemberMaster)criteria.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<RlmsCustomerMemberMap> getAllCustomersForMember(Integer memberId){
		Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsCustomerMemberMap.class)
				 .add(Restrictions.eq("rlmsMemberMaster.memberId", memberId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 return criteria.list();
	}
	
	@Override
	public RlmsBranchCustomerMap getBranchCustomerMapByCustoId(Integer customerId){
		Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsBranchCustomerMap.class)
				 .add(Restrictions.eq("customerMaster.customerId", customerId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 
		 return (RlmsBranchCustomerMap) criteria.uniqueResult();
	}

}
