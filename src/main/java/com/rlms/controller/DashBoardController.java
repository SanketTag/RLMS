package com.rlms.controller;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rlms.constants.RlmsErrorType;
import com.rlms.contract.AMCDetailsDto;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.RunTimeException;
import com.rlms.exception.ValidationException;
import com.rlms.service.DashboardService;
import com.rlms.utils.PropertyUtils;

@Controller
@RequestMapping("/dashboard")
public class DashBoardController extends BaseController{

	@Autowired
	private DashboardService dashboardService;
	
	private static final Logger logger = Logger.getLogger(ComplaintController.class);
	
	 @RequestMapping(value = "/getAMCDetails", method = RequestMethod.GET)
	    public @ResponseBody List<AMCDetailsDto> getAMCDetailsForDashboard() throws RunTimeException, ValidationException {
	        
		 List<AMCDetailsDto> listOFAmcDtls = null;
	        try{ 
	        	logger.info("In getAMCDetailsForDashboard method");
	        	listOFAmcDtls = this.dashboardService.getAMCDetailsForDashboard();
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOFAmcDtls;
	    }	 
}
