/**
 * TablonSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.4  Built on : Oct 21, 2016 (10:47:34 BST)
 */
package org.apache.ws.axis2;

import java.io.IOException;
import java.io.InputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.ServiceLifeCycle;
import org.apache.commons.io.IOUtils;

/**
 *  TablonSkeleton java skeleton for the axisService
 */
public class TablonSkeleton implements TablonSkeletonInterface, ServiceLifeCycle {
	public static byte[] comando = "rest".getBytes();
	public static byte[] clave = "rest".getBytes();
	public static byte[] firma = "rest".getBytes();


    /**
     * Auto generated method signature
     *
     * @param getClave0
     * @return getClaveResponse1
     */
    public org.apache.ws.axis2.GetClaveResponse getClave(
        org.apache.ws.axis2.GetClave getClave) {
        GetClaveResponse response = new GetClaveResponse();
		response.set_return(DHfromBytes(clave));
		return response;

    }

    /**
     * Auto generated method signature
     *
     * @param getFirma2
     * @return getFirmaResponse3
     */
    public org.apache.ws.axis2.GetFirmaResponse getFirma(
        org.apache.ws.axis2.GetFirma getFirma) {
    	GetFirmaResponse response = new GetFirmaResponse();
    	response.set_return(DHfromBytes(firma));
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
       try {
		comando = BytesFromDH(setOrden.getArgs0());
		firma = BytesFromDH(setOrden.getArgs1());
		clave = BytesFromDH(setOrden.getArgs2());
	} catch (IOException e) {
		e.printStackTrace();
	}
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
        org.apache.ws.axis2.GetComando getComando) {
    	GetComandoResponse response = new GetComandoResponse();
    	response.set_return(DHfromBytes(comando));
    	return response;
    }
    
    private DataHandler DHfromBytes(byte[] array){
    	
    	DataSource dataSource = new ByteArrayDataSource(array);
    	DataHandler dataHandler = new DataHandler(dataSource);
    	return dataHandler;
    	
    }
    
    private byte[] BytesFromDH(DataHandler dh) throws IOException{
    	InputStream inputDH = dh.getInputStream();
    	byte[] b = IOUtils.toByteArray(inputDH);
    	return b;
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
