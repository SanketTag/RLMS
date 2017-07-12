package com.rlms.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rlms.constants.RLMSConstants;
import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsComplaintMaster;
import com.rlms.model.RlmsComplaintTechMapDtls;
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
}
