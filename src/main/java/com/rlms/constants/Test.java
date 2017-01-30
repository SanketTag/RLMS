package com.rlms.constants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class Test {

	

	
	
	    public void run() 
	    {
	        try 
	        {
	        	ByteArrayOutputStream baos = new ByteArrayOutputStream ();
	        	DataOutputStream dos = new DataOutputStream (baos);
	        	dos.writeInt (3);
	        	byte[] data = baos.toByteArray();
	        	
	        	System.out.println(data);
	        	
	        	ByteArrayInputStream bais = new ByteArrayInputStream (data);
	        	DataInputStream dis = new DataInputStream (bais);
	        	int j = dis.readInt();
	        	System.out.println(j);
	        }
	        catch(Exception e) 
	        {
	            e.printStackTrace();
	        }
	    }
	    public static void main(String[] args) 
	    {
	    	Test app = new Test();
	        app.run();
	    }
	
}
