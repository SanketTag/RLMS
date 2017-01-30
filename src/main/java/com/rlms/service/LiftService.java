package com.rlms.service;

import java.util.List;

import com.rlms.model.RlmsLiftCustomerMap;

public interface LiftService {

	public List<RlmsLiftCustomerMap> getAllLiftsForBranch(Integer companyBranchMapId);
}
