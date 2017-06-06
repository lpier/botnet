
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
	private static String secreto = "master";

	public static void main(String[] args) {

		String status = darOrden(args[0], args[1]);
		System.out.println("He dado esta orden: " + args[0]);
		System.out.println(status);
		return;

	}

	private static String darOrden(String accion, String objetivo) {
		String status = "";
		String orden = accion + "*" + objetivo;
		System.out.println("ESTA ES LA ORDEN: " + orden + " <-----");
		ServiceClient cliente = null;
		try {
			cliente = new ServiceClient();
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		options.setTo(new EndpointReference(urlTablon));
		options.setAction("urn:command");
		options.activate(getConf());
		cliente.setOptions(options);

		OMElement metodo = omFactory.createOMElement("command", omNameSpace);

		OMElement parametro = omFactory.createOMElement("orden", omNameSpace);
		parametro.setText(orden);
		metodo.addChild(parametro);
		
		parametro = omFactory.createOMElement("secreto", omNameSpace);
		parametro.setText(secreto);
		metodo.addChild(parametro);

		OMElement respuesta = null;
		try{
			respuesta = cliente.sendReceive(metodo);
		}catch(AxisFault e){
			
			e.printStackTrace();
		}
		status = respuesta.getFirstElement().getText();
		
		try {
			cliente.cleanup();
			cliente.cleanupTransport();
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		
		return status;
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
