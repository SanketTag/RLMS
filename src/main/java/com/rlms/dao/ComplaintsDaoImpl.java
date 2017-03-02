package com.rlms.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rlms.constants.RLMSConstants;
import com.rlms.model.RlmsComplaintMaster;
import com.rlms.model.RlmsComplaintTechMapDtls;

@Repository
public class ComplaintsDaoImpl implements ComplaintsDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	public List<RlmsComplaintTechMapDtls> getAllComplaintsAssigned(Integer userRoleId, List<Integer> statusList){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsComplaintTechMapDtls.class)
				 .add(Restrictions.eq("userRoles.userRoleId", userRoleId))
				 .add(Restrictions.in("status", statusList))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 List<RlmsComplaintTechMapDtls> listOfAllcomplaints = criteria.list();
		 return listOfAllcomplaints;
	}
	
	public List<RlmsComplaintMaster> getAllComplaintsForBranchOrCustomer(Integer branchCompanyMapId, Integer branchCustomerMapId, List<Integer> statusList){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsComplaintMaster.class);
		 criteria.createAlias("liftCustomerMap.branchCustomerMap", "bcm");
		 criteria.createAlias("bcm.companyBranchMapDtls", "cbm");
				 if(null != branchCompanyMapId){
					 criteria.add(Restrictions.eq("cbm.companyBranchMapId", branchCompanyMapId));
				 }
				 if(null != branchCustomerMapId){
					 criteria.add(Restrictions.eq("bcm.branchCustomerMapId", branchCustomerMapId));
				 }
				 criteria.add(Restrictions.in("status", statusList));
				 criteria.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 List<RlmsComplaintMaster> listOfAllcomplaints = criteria.list();
		 return listOfAllcomplaints;
	}
	
	public Integer saveComplaintM(RlmsComplaintMaster complaintMaster){
		Integer complaintsId = (Integer) this.sessionFactory.getCurrentSession().save(complaintMaster);
		return complaintsId;
	}
	
	public void mergeComplaintM(RlmsComplaintMaster complaintMaster){
		this.sessionFactory.getCurrentSession().merge(complaintMaster);
	}
	
	public void saveComplaintTechMapDtls(RlmsComplaintTechMapDtls complaintTechMapDtls){
		this.sessionFactory.getCurrentSession().save(complaintTechMapDtls);
	}
	
	@SuppressWarnings("unchecked")
	public List<RlmsComplaintMaster> getAllComplaintsForGivenCriteria(Integer branchCompanyMapId, Integer branchCustomerMapId,List<Integer> listOfLiftCustoMapId,  List<Integer> statusList, Date fromDate, Date toDate){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsComplaintMaster.class);
		 criteria.createAlias("liftCustomerMap.branchCustomerMap", "bcm");
		 criteria.createAlias("bcm.companyBranchMapDtls", "cbm");
				 if(null != branchCompanyMapId){
					 criteria.add(Restrictions.eq("cbm.companyBranchMapId", branchCompanyMapId));
				 }
				 if(null != branchCustomerMapId && !RLMSConstants.MINUS_ONE.getId().equals(branchCustomerMapId)){
					 criteria.add(Restrictions.eq("bcm.branchCustoMapId", branchCustomerMapId));
				 }
				 if(null != listOfLiftCustoMapId && !listOfLiftCustoMapId.isEmpty()){
					 criteria.add(Restrictions.in("liftCustomerMap.liftCustomerMapId", listOfLiftCustoMapId));
				 }
				 if(null != fromDate && null != toDate){
					 criteria.add(Restrictions.ge("registrationDate", fromDate));
					 criteria.add(Restrictions.le("registrationDate", toDate));
				 }
				 if(null != statusList && !statusList.isEmpty()){
					 criteria.add(Restrictions.in("status", statusList));
				 }
				 criteria.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 List<RlmsComplaintMaster> listOfAllcomplaints = criteria.list();
		 return listOfAllcomplaints;
	}
	
	@SuppressWarnings("unchecked")
	public RlmsComplaintTechMapDtls getComplTechMapObjByComplaintId(Integer complaintId){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsComplaintTechMapDtls.class)
				 .add(Restrictions.eq("complaintMaster.complaintId", complaintId));
		 RlmsComplaintTechMapDtls complaintMapDtls = (RlmsComplaintTechMapDtls) criteria.uniqueResult();
		 return complaintMapDtls;
	}
	
	@SuppressWarnings("unchecked")
	public RlmsComplaintMaster getComplaintMasterObj(Integer complaintId){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsComplaintMaster.class)
				 .add(Restrictions.eq("complaintId", complaintId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 RlmsComplaintMaster complaintMaster = (RlmsComplaintMaster) criteria.uniqueResult();
		 return complaintMaster;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RlmsComplaintMaster> getAllComplaintsByMemberId(Integer memberId){
		 Session session = this.sessionFactory.getCurrentSession();
		 Criteria criteria = session.createCriteria(RlmsComplaintMaster.class)
				 .add(Restrictions.eq("createdBy", memberId))
				 .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 List<RlmsComplaintMaster> listOfAllComplaints =  criteria.list();
		 return listOfAllComplaints;
	}
	
}
