package com.rlms.dao;

import java.util.List;

import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsCompanyMaster;
import com.rlms.model.RlmsCompanyRoleMap;

public interface CompanyDao {

	public void saveCompanyM(RlmsCompanyMaster rlmsCompanyMaster);
	public RlmsCompanyMaster getCompanyByEmailID(String emailID);
	
	public List<RlmsCompanyMaster> getAllCompanies(Integer companyId);
	public RlmsCompanyMaster getCompanyById(Integer companyId);
	public RlmsCompanyBranchMapDtls getCompanyBranchMapById(Integer companyBranchMapId);
	
	//public RlmsCompanyRoleMap getCompanyRole(Integer companyId, Integer spocRoleId);
}
