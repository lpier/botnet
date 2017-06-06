
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
	private static String secreto = "bot";


	public static void main(String[] args) {

		boolean actuar = false;
		while(!actuar){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String comando = getComando();
			System.out.println("Esta es mi orden: " + comando);
			if(!comando.equals("")){
				actuar=true;
				System.out.println("voy a actuar");
			}
			
		}
		return;

	}

	private static String getComando() {
		String comando = "";
		
		ServiceClient cliente = null;
		try {
			cliente = new ServiceClient();
		} catch (AxisFault e) {
			e.printStackTrace();
		}

		// Timer timer = new Timer();
		// TimerTask busca = new TimerTask() {
		// @Override
		// public void run() {
		options.setTo(new EndpointReference(urlTablon));
		options.setAction("urn:obey");
		options.activate(getConf());
		cliente.setOptions(options);

		OMElement metodo = omFactory.createOMElement("obey", omNameSpace);
		OMElement parametro = omFactory.createOMElement("secreto", omNameSpace);
		parametro.setText(secreto);
		metodo.addChild(parametro);

		OMElement respuesta = null;
		try {
			respuesta = cliente.sendReceive(metodo);
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		System.out.println("LEIDO COMANDO : " + respuesta.getText());
		comando = getOrden(respuesta.getFirstElement().getText()).getAccion();
		try {
			cliente.cleanup();
			cliente.cleanupTransport();
		} catch (AxisFault e) {
			e.printStackTrace();
		}

		// if(!comando.equals("")){
		// System.out.println("ENTRO AQUI!");
		// timer.cancel();
		// timer.purge();
		// }

		// }
		// };
		// timer.schedule(busca, 0, 10000);

		return comando;
	}

	private static Orden getOrden(String comando) {
		Orden orden = new Orden(comando);
		return orden;
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
