package com.rights.batches;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.log4j.Logger;

public class AMCMonitor {

	private static final Logger logger = Logger.getLogger(AMCMonitor.class);
	
	public static void main(String arg[])
	{
		System.out.println("Batch start");
		ApplicationContext context = new ClassPathXmlApplicationContext("BatchBeans.xml");
		 logger.debug(context);
		 AMCMonitor geoCodingBatch = (AMCMonitor) context.getBean("aMCMonitor");
		 try {
		//	geoCodingBatch.executeGeoCodingBatch();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug(e.getStackTrace());
		}
	}
}
