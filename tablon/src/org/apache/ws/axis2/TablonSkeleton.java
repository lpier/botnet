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
    
	public static String comando = "rest";
	public static int len = 0;
    /**
     * Auto generated method signature
     *
     * @param setLen0
     * @return setLenResponse1
     */
    public org.apache.ws.axis2.SetLenResponse setLen(
        org.apache.ws.axis2.SetLen setLen0) {
        SetLenResponse response = new SetLenResponse();
        int recv = setLen0.getLen();
        response.set_return(true);
        return response;
    }

    /**
     * Auto generated method signature
     *
     * @param getLen2
     * @return getLenResponse3
     */
    public org.apache.ws.axis2.GetLenResponse getLen(
        org.apache.ws.axis2.GetLen getLen2) {
    	GetLenResponse response = new GetLenResponse();
    	response.set_return(len);
    	return response;
    }

    /**
     * Auto generated method signature
     *
     * @param setOrden4
     * @return setOrdenResponse5
     */
    public org.apache.ws.axis2.SetOrdenResponse setOrden(
        org.apache.ws.axis2.SetOrden setOrden) {
    	SetOrdenResponse response = new SetOrdenResponse();
     	comando = setOrden.getComando();
     	response.set_return(true);
     	return response;
    }

    /**
     * Auto generated method signature
     *
     * @param getComando6
     * @return getComandoResponse7
     */
    public org.apache.ws.axis2.GetComandoResponse getComando(
        org.apache.ws.axis2.GetComando getComando6) {
    	GetComandoResponse response = new GetComandoResponse();
        response.set_return(comando);
        return response;
    }

	@Override
	public void shutDown(ConfigurationContext arg0, AxisService arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startUp(ConfigurationContext arg0, AxisService arg1) {
		// TODO Auto-generated method stub
		
	}
}
