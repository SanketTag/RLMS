package com.rlms.dao;

import java.util.List;

import com.rlms.model.RlmsLiftCustomerMap;


public interface LiftDao {	
	public List<RlmsLiftCustomerMap> getAllLiftsForCustomers(List<Integer> listOfCuistomers);

}
