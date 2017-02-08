package com.rlms.controller;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rlms.constants.RlmsErrorType;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.RunTimeException;
import com.rlms.service.ComplaintsService;
import com.rlms.utils.PropertyUtils;

@Controller
@RequestMapping("/complaint")
public class ComplaintController {

	@Autowired
	private ComplaintsService complaintsService;
	
	
	private static final Logger logger = Logger.getLogger(ComplaintController.class);
	
	@RequestMapping(value = "/getListOfComplaints", method = RequestMethod.POST)
	 public List<ComplaintsDto> getListOfComplaints(@RequestBody ComplaintsDtlsDto dto) throws RunTimeException{
		 List<ComplaintsDto> listOfComplaints = null;
		 
		 try{
	        	logger.info("Method :: getListOfComplaints");
	        	listOfComplaints = this.complaintsService.getListOfComplaints(dto);
	        	
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOfComplaints;
	 }
}
