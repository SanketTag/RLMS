package com.rlms.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rlms.constants.RLMSConstants;
import com.rlms.constants.Status;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsLiftMaster;
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RlmsLiftCustomerMap> getAllLiftsToBeApproved(){		
			 Session session = this.sessionFactory.getCurrentSession();
			 Criteria criteria = session.createCriteria(RlmsLiftCustomerMap.class)
					 .createAlias("liftMaster", "LM")
					 .add(Restrictions.eq("LM.status", Status.PENDING_FOR_APPROVAL.getStatusId()))
					 .add(Restrictions.eq("LM.activeFlag", RLMSConstants.INACTIVE.getId()));
			 List<RlmsLiftCustomerMap> listOfLifts = criteria.list();
			 return listOfLifts;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public RlmsLiftCustomerMap getLiftCustomerMapByLiftId(Integer liftId){		
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsLiftCustomerMap.class)
				 .add(Restrictions.eq("liftMaster.liftId", liftId));
		 RlmsLiftCustomerMap liftCustomerMap = (RlmsLiftCustomerMap) criteria.uniqueResult();
		 return liftCustomerMap;
	
	}
	
	@Override
	public Integer saveLiftM(RlmsLiftMaster liftMaster){
		return (Integer)this.sessionFactory.getCurrentSession().save(liftMaster);		
	}
	
	@Override
	public Integer saveLiftCustomerMap(RlmsLiftCustomerMap liftCustomerMap){
		return (Integer) this.sessionFactory.getCurrentSession().save(liftCustomerMap);
	}
	
	@Override
	public void updateLiftM(RlmsLiftMaster liftMaster){
		this.sessionFactory.getCurrentSession().update(liftMaster);		
	}
	
	@Override
	public void updateLiftCustomerMap(RlmsLiftCustomerMap liftCustomerMap){
		this.sessionFactory.getCurrentSession().update(liftCustomerMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<RlmsLiftCustomerMap> getAllLiftsForBranchs(Integer branchCompanyId){		
			 Session session = this.sessionFactory.getCurrentSession();
			 Criteria criteria = session.createCriteria(RlmsLiftCustomerMap.class)
					 .createAlias("branchCustomerMap.companyBranchMapDtls", "branchCompanyMap")
					 .add(Restrictions.eq("branchCompanyMap.companyBranchMapId", branchCompanyId))
					 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
			 List<RlmsLiftCustomerMap> listOfAllLifts = criteria.list();
			 return listOfAllLifts;
		
	}
	
	@Override
	public RlmsLiftCustomerMap getLiftCustomerMapById(Integer liftCustomerMapId){		
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsLiftCustomerMap.class)
				 .add(Restrictions.eq("liftCustomerMapId", liftCustomerMapId));
		 RlmsLiftCustomerMap liftCustomerMap = (RlmsLiftCustomerMap) criteria.uniqueResult();
		 return liftCustomerMap;
	
	}
	
	@SuppressWarnings("unchecked")
	public List<RlmsLiftCustomerMap> getAllLiftsForBranchsOrCustomer(LiftDtlsDto dto){		
			 Session session = this.sessionFactory.getCurrentSession();
			 Criteria criteria = session.createCriteria(RlmsLiftCustomerMap.class)
					 .createAlias("branchCustomerMap.companyBranchMapDtls", "branchCompanyMap");
					 if(null != dto.getBranchCustomerMapId() && !RLMSConstants.MINUS_ONE.getId().equals(dto.getBranchCustomerMapId())){
						 criteria.add(Restrictions.eq("branchCustomerMap.branchCustoMapId", dto.getBranchCustomerMapId()));
					 }
					 if(null != dto.getBranchCompanyMapId()){
						 criteria.add(Restrictions.eq("branchCompanyMap.companyBranchMapId", dto.getBranchCompanyMapId()));
					 }
					 criteria.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
			 List<RlmsLiftCustomerMap> listOfAllLifts = criteria.list();
			 return listOfAllLifts;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<RlmsLiftCustomerMap> getAllLiftsForCustomres(List<Integer> branchCustoMapId){		
			 Session session = this.sessionFactory.getCurrentSession();
			 Criteria criteria = session.createCriteria(RlmsLiftCustomerMap.class);				 
					  criteria.add(Restrictions.in("branchCustomerMap.branchCustoMapId", branchCustoMapId));					
					 criteria.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
			 List<RlmsLiftCustomerMap> listOfAllLifts = criteria.list();
			 return listOfAllLifts;
		
	}
}
