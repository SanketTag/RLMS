package com.rlms.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rlms.constants.RLMSConstants;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsComplaintMaster;
import com.rlms.model.RlmsComplaintTechMapDtls;
import com.rlms.model.RlmsEventDtls;
import com.rlms.model.RlmsLiftAmcDtls;
import com.rlms.model.RlmsSiteVisitDtls;
import com.rlms.model.RlmsUserRoles;

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
	public List<RlmsComplaintMaster> getAllComplaintsForGivenCriteria(
			Integer branchCompanyMapId, Integer branchCustomerMapId,
			List<Integer> listOfLiftCustoMapId, List<Integer> statusList,
			Date fromDate, Date toDate,Integer callType) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(RlmsComplaintMaster.class);
		criteria.createAlias("liftCustomerMap.branchCustomerMap", "bcm");
		criteria.createAlias("bcm.companyBranchMapDtls", "cbm");
		if (null != branchCompanyMapId) {
			criteria.add(Restrictions.eq("cbm.companyBranchMapId",
					branchCompanyMapId));
		}
		if (null != branchCustomerMapId
				&& !RLMSConstants.MINUS_ONE.getId().equals(branchCustomerMapId)) {
			criteria.add(Restrictions.eq("bcm.branchCustoMapId",
					branchCustomerMapId));
		}
		if (null != listOfLiftCustoMapId && !listOfLiftCustoMapId.isEmpty()) {
			criteria.add(Restrictions.in("liftCustomerMap.liftCustomerMapId",
					listOfLiftCustoMapId));
		}
		if (null != fromDate && null != toDate) {
			criteria.add(Restrictions.ge("registrationDate", fromDate));
			criteria.add(Restrictions.le("registrationDate", toDate));
		}
		if (null != statusList && !statusList.isEmpty()) {
			criteria.add(Restrictions.in("status", statusList));
		}
		if(null != callType){
			 criteria.add(Restrictions.eq("callType", callType));
		 }
		criteria.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		List<RlmsComplaintMaster> listOfAllcomplaints = criteria.list();
		return listOfAllcomplaints;
	}

	@SuppressWarnings("unchecked")
	public RlmsComplaintTechMapDtls getComplTechMapObjByComplaintId(
			Integer complaintId) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(
				RlmsComplaintTechMapDtls.class).add(
				Restrictions.eq("complaintMaster.complaintId", complaintId));
		RlmsComplaintTechMapDtls complaintMapDtls = (RlmsComplaintTechMapDtls) criteria
				.uniqueResult();
		return complaintMapDtls;
	}

	@SuppressWarnings("unchecked")
	public List<RlmsUserRoles> getAllUserWithRoleFor(
			List<Integer> commpBranchMapId, Integer spocRoleId) {
		Session session = this.sessionFactory.getCurrentSession();
		/*
		 * Criteria criteria = session.createCriteria(RlmsUserRoles.class)
		 * .add(Restrictions.in("rlmsCompanyBranchMapDtls.companyBranchMapId",
		 * commpBranchMapId))
		 * .add(Restrictions.eq("rlmsSpocRoleMaster.spocRoleId", spocRoleId));
		 * //.add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId()));
		 * List<RlmsUserRoles> listOfAllTechnicians = criteria.list();
		 */
		String str = "";
		for (Integer mapId : commpBranchMapId) {
			if (StringUtils.isEmpty(str)) {
				str = str.concat(String.valueOf(mapId));
			} else {
				str = str.concat("," + mapId);
			}
		}

		String sql = "select a.* from rlms_db.rlms_user_roles a where (a.user_id,a.updated_date,a.company_branch_map_id,a.spoc_role_id) in (SELECT user_id,max(updated_date) max_date,company_branch_map_id,spoc_role_id FROM rlms_db.rlms_user_roles where company_branch_map_id in ("
				+ str
				+ ") and spoc_role_id="
				+ spocRoleId
				+ " and user_id=a.user_id and updated_date < now() order by ABS(DATEDIFF( updated_date, now()))) group by a.user_id,a.company_branch_map_id,a.spoc_role_id";
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(RlmsUserRoles.class);
		List listOfAllTechnicians = query.list();
		return listOfAllTechnicians;
	}

	@SuppressWarnings("unchecked")
	public List<RlmsCompanyBranchMapDtls> getAllBranchesForDashboard(
			Integer companyId) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session
				.createCriteria(RlmsCompanyBranchMapDtls.class);
		criteria.add(Restrictions.eq("rlmsCompanyMaster.companyId", companyId));
		// criteria.add(Restrictions.eq("activeFlag",
		// RLMSConstants.ACTIVE.getId()));
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = criteria.list();
		return listOfAllBranches;

	}

	@SuppressWarnings("unchecked")
	public List<RlmsCompanyBranchMapDtls> getAllBranchDtlsForDashboard(
			List<Integer> ListOfCompanyIds) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session
				.createCriteria(RlmsCompanyBranchMapDtls.class);
		criteria.add(Restrictions.in("rlmsCompanyMaster.companyId",
				ListOfCompanyIds));
		// criteria.add(Restrictions.eq("activeFlag",
		// RLMSConstants.ACTIVE.getId()));
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = criteria.list();
		return listOfAllBranches;

	}

	public RlmsCompanyBranchMapDtls getCompanyBranchMapDtlsForDashboard(
			Integer compBranchMapId) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(
				RlmsCompanyBranchMapDtls.class).add(
				Restrictions.eq("companyBranchMapId", compBranchMapId));
		// .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId())
		RlmsCompanyBranchMapDtls companyBranchMapDtls = (RlmsCompanyBranchMapDtls) criteria
				.uniqueResult();
		return companyBranchMapDtls;
	}

	@Override
	public List<RlmsEventDtls> getAllEventDtlsForDashboard(
			List<Integer> companyBranchIds) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(RlmsEventDtls.class).add(
				Restrictions.in("liftCustomerMapId", companyBranchIds));
		// .add(Restrictions.eq("activeFlag", RLMSConstants.ACTIVE.getId())
		List<RlmsEventDtls> eventDtls = criteria.list();
		return eventDtls;
	}
	
	@Override
	public void saveEventDtls(RlmsEventDtls eventDtls){
		this.sessionFactory.getCurrentSession().save(eventDtls);
	}
}
