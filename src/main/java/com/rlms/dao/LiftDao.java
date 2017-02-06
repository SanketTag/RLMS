package com.rlms.dao;

import java.util.List;

import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsLiftMaster;


public interface LiftDao {	
	public List<RlmsLiftCustomerMap> getAllLiftsForCustomers(List<Integer> listOfCuistomers);

	Integer saveLiftM(RlmsLiftMaster liftMaster);

	Integer saveLiftCustomerMap(RlmsLiftCustomerMap liftCustomerMap);

	List<RlmsLiftCustomerMap> getAllLiftsToBeApproved();

	RlmsLiftCustomerMap getLiftCustomerMapByLiftId(Integer liftId);

	void updateLiftM(RlmsLiftMaster liftMaster);

	void updateLiftCustomerMap(RlmsLiftCustomerMap liftCustomerMap);

}
