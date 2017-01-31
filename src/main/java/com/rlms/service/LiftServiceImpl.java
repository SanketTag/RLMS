package com.rlms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.dao.LiftDao;
import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsLiftCustomerMap;

@Service("LiftService")
public class LiftServiceImpl implements LiftService{

	@Autowired
	private LiftDao liftDao;
	
	@Autowired
	private CompanyService companyService;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RlmsLiftCustomerMap> getAllLiftsForBranch(Integer companyBranchMapId){
		List<RlmsLiftCustomerMap> liftsForBranch = new ArrayList<RlmsLiftCustomerMap>();
		List<Integer> listOfAllCustmers = new ArrayList<Integer>();
		List<RlmsBranchCustomerMap> listOfCustomersOfBranch = this.companyService.getAllCustomersOfBranch(companyBranchMapId);
		for (RlmsBranchCustomerMap rlmsBranchCustomerMap : listOfCustomersOfBranch) {
			listOfAllCustmers.add(rlmsBranchCustomerMap.getCustomerMaster().getCustomerId());
		}
		if(null != listOfAllCustmers && !listOfAllCustmers.isEmpty()){
			liftsForBranch =  this.liftDao.getAllLiftsForCustomers(listOfAllCustmers);
		}
		return liftsForBranch;
	}
	
	
	
}
