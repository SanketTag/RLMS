package com.rlms.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConsumeRestFul {

	// http://localhost:8080/RESTfulExample/json/product/post
		public static void main(String[] args) {

		  try {

			/* 1. URL url = new URL("http://localhost:8000/RLMS/API/getAllComplaintsAssigned"); //(userRoleId 17)*/ 
			//URL url = new URL("http://localhost:8000/RLMS/API/register/registerMemeberDeviceByMblNo"); 
			/*3.*/  URL url = new URL("http://139.162.5.222:8000/RLMS/API/register/registerTechnicianDeviceByMblNo");
			 // 4.URL url = new URL("http://localhost:8000/RLMS/API/lift/getAllLiftsForMember");
			 ///5.  URL url = new URL("http://localhost:8000/RLMS/API/complaints/getAllComplaintsByMember");
			//URL url = new URL("http://localhost:8000/RLMS/API/loginIntoApp");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

		/*	2.*///String input = "{\"contactNumber\":\"8983564578\", \"latitude\":\"18.12457898\", \"longitude\":\"72.12457896\", \"appRegId\":\"AAAAGqJGmak:APA91bH5wu5DXT01MIyN2LF0n46WqR0ZXtuTCaV8qHGEe738r-fAfoIGG1ytz_k6oHiFEgo6nX9VSopGXi2qhylnjpXKdh4U-tzGoMIA78QDDqnxIVJQFo56AN1uKrmz0UiLo6_-lb3\", \"address\":\"WAKAD\"}";
			/* 3. */ String input = "{\"contactNumber\":\"9096136234\", \"latitude\":\"18.12457898\", \"longitude\":\"72.12457896\", \"appRegId\":\"AAAAGqJGmak:APA91bH5wu5DXT01MIyN2LF0n46WqR0ZXtuTCaV8qHGEe738r-fAfoIGG1ytz_k6oHiFEgo6nX9VSopGXi2qhylnjpXKdh4U-tzGoMIA78QDDqnxIVJQFo56AN1uKrmz0UiLo6_-lb3b\", \"address\":\"WAKAD\"}";
			// String input = "{\"userName\":\"admin\",\"password\":\"rlms1234\"}";
			//4 and 5. String input = "{\"memberId\":\"3\"}";
		//	String input = "{\"userRoleId\":\"17\"}";

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				//throw new RuntimeException("Failed : HTTP error code : "
				//	+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				
			}

			conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		 }

		}
}
