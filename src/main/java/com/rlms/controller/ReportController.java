package com.rlms.controller;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rlms.constants.RlmsErrorType;
import com.rlms.constants.Status;
import com.rlms.contract.AMCDetailsDto;
import com.rlms.contract.CompanyDtlsDTO;
import com.rlms.contract.ResponseDto;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.RunTimeException;
import com.rlms.exception.ValidationException;
import com.rlms.service.CompanyService;
import com.rlms.service.ReportService;
import com.rlms.utils.PropertyUtils;

@Controller
@RequestMapping("/report")
public class ReportController extends BaseController{

	@Autowired
	private ReportService reportService;
	
	private static final Logger logger = Logger.getLogger(ComplaintController.class);
	
	 @RequestMapping(value = "/getAMCDetailsForLifts", method = RequestMethod.POST)
	    public @ResponseBody List<AMCDetailsDto> getAMCDetailsForLifts(@RequestBody AMCDetailsDto dto) throws RunTimeException, ValidationException {
	        
		 List<AMCDetailsDto> listOFAmcDtls = null;
	        try{
	        	logger.info("In getAMCDetailsForLifts method");
	        	listOFAmcDtls = this.reportService.getAMCDetailsForLifts(dto);
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOFAmcDtls;
	    }
	 
	 @RequestMapping(value = "/addAMCDetailsForLift", method = RequestMethod.POST)
	    public @ResponseBody ResponseDto addAMCDetailsForLift(@RequestBody AMCDetailsDto dto) throws RunTimeException, ValidationException {
	        
		 ResponseDto responseDto = new ResponseDto();
	        try{
	        	logger.info("In addAMCDetailsForLift method");
	        	responseDto.setResponse(this.reportService.addAMCDetailsForLift(dto, Status.UNDER_AMC.getStatusId(), this.getMetaInfo()));
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return responseDto;
	    }
}
