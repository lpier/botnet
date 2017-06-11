import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

public class Master {

	private static OMFactory omFactory = OMAbstractFactory.getOMFactory();
	private static OMNamespace omNameSpace = omFactory.createOMNamespace("http://ws.apache.org/axis2", "nsTablon");
	private static Options options = new Options();
	private static String urlTablon = "http://192.168.0.13:7090/axis2/services/Tablon/";

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("orden;objetivo;duracion");
		String info = in.nextLine();
		boolean status = publicarComando(info);
		if (status != false) {
			System.out.println("EXITO");
		}
	}

	public static boolean publicarComando(String info) {

		ServiceClient cliente = null;
		try {
			cliente = new ServiceClient();
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		options.setTo(new EndpointReference(urlTablon));
		options.setAction("urn:setOrden");
		options.activate(getConf());
		cliente.setOptions(options);

		OMElement metodo = omFactory.createOMElement("setOrden", omNameSpace);
		OMElement parametro = omFactory.createOMElement("comando", omNameSpace);
		parametro.setText(info);
		metodo.addChild(parametro);

		OMElement response = null;

		try {
			response = cliente.sendReceive(metodo);
		} catch (AxisFault e) {
			e.printStackTrace();
			System.out.println("Ups...");
			return false;
		}
		boolean status = Boolean.parseBoolean(response.getFirstElement().getText());
		return status;

	}

	public static void generarClave() {
		Cipher cipher = null;
		byte[] password = null;
		SecretKeySpec sKey = null;
		try {
			password = "JavaJavaJavaJava".getBytes("UTF-8");
			byte[] pkey = "keykeykekeykeykekeykeykekeykeyke".getBytes("UTF-8");
			sKey = new SecretKeySpec(pkey, "AES");

			cipher = Cipher.getInstance("AES/ECB/NoPadding");
			System.out.println(" --> + " + new String(password));

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] cText = new byte[password.length];
		try {
			cipher.init(Cipher.ENCRYPT_MODE, sKey);
			int cLen = cipher.update(password, 0, password.length, cText, 0);
			cLen += cipher.doFinal(cText, cLen);
			System.out.println("encriptado : " + cText.toString().getBytes("UTF-8"));
			String encoded = new String(cText, "ISO-8859-1");
			System.out.println(encoded);
			desencriptar(encoded,cLen);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ShortBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public static void desencriptar(String cText, int cLen){
		Cipher cipher = null;
		byte[] plainText = new byte[cLen];
		SecretKeySpec sKey = null;
		byte[] pkey;
		try {
			pkey = "keykeykekeykeykekeykeykekeykeyke".getBytes("UTF-8");
		
		sKey = new SecretKeySpec(pkey, "AES");
		cipher = Cipher.getInstance("AES/ECB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, sKey);
		byte[] encoded = cText.getBytes("ISO-8859-1");
		int plen = cipher.update(encoded, 0, cLen, plainText, 0);
		plen += cipher.doFinal(plainText, plen);
		System.out.println(" obtenida --> " + new String(plainText));
		
		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ShortBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static ConfigurationContext getConf() {

		ConfigurationContext configurationContext = null;
		try {
			configurationContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
		} catch (AxisFault e) {
			e.printStackTrace();
		}

		MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();

		// establecer máximo de conexiones en la pool
		params.setDefaultMaxConnectionsPerHost(20);
		multiThreadedHttpConnectionManager.setParams(params);
		HttpClient httpClient = new HttpClient(multiThreadedHttpConnectionManager);
		configurationContext.setProperty(HTTPConstants.CACHED_HTTP_CLIENT, httpClient);
		return configurationContext;
	}

}