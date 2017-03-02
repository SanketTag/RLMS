package com.rlms.service;

import java.util.List;

import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.model.RlmsLiftCustomerMap;

public interface LiftService {

	public List<RlmsLiftCustomerMap> getAllLiftsForBranch(Integer companyBranchMapId);

	String validateAndAddNewLiftDtls(LiftDtlsDto dto, UserMetaInfo metaInfo);
	
	public String approveLift(LiftDtlsDto liftDtlsDto, UserMetaInfo metaInfo);

	List<LiftDtlsDto> getLiftsToBeApproved();
	
	public List<LiftDtlsDto> getLiftDetailsForBranch(LiftDtlsDto liftDtlsDto, UserMetaInfo metaInfo);
	
	public String uploadPhoto(LiftDtlsDto dto);
}
