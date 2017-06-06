/**
 * TablonSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.4  Built on : Oct 21, 2016 (10:47:34 BST)
 */
package org.apache.ws.axis2;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.ServiceLifeCycle;

/**
 *  TablonSkeleton java skeleton for the axisService
 */
public class TablonSkeleton implements TablonSkeletonInterface, ServiceLifeCycle {
	public static String orden = "";
	public static String secretoMaster = "master";
	public static String secretoBot = "bot";
	
	/**
     * Auto generated method signature
     *
     * @param obey
     * @return obeyResponse
     */
    public org.apache.ws.axis2.ObeyResponse obey(org.apache.ws.axis2.Obey obey) {
       ObeyResponse response = new ObeyResponse();
       
       if(!secretoBot.equals(obey.getSecreto())){
    	   response.set_return("uy, mecachis");
       }else{
    	   response.set_return(orden);
    	   System.out.println("ESTA ES LA ORDEN :" + orden + "<-----");
       }
       return response;
    	
    }

    
    
    /**
     * Auto generated method signature
     *
     * @param command
     * @return commandResponse
     */
    public org.apache.ws.axis2.CommandResponse command(
        org.apache.ws.axis2.Command command) {
    	CommandResponse response = new CommandResponse();
   
    	if(!secretoMaster.equals(command.getSecreto())){
    		response.set_return("uy, mecachis!");
    	}else{
        	orden = command.getOrden();
        	System.out.println(orden);
    		response.set_return("ok");
    	}
      	
    	return response;		
    }



	@Override
	public void shutDown(ConfigurationContext arg0, AxisService arg1) {
		orden = "descansa";
	}



	@Override
	public void startUp(ConfigurationContext arg0, AxisService arg1) {
		// TODO Auto-generated method stub
		
	}
}
