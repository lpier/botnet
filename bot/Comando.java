import java.net.MalformedURLException;
import java.net.URL;

public class Comando{
	
	private String ataque;
	private URL objetivo;
	private long fin;
	
	

	public Comando() {
	}
	
	
	public Comando(String ataque, URL objetivo, long duracion) {
		this.ataque = ataque;
		this.objetivo = objetivo;
		this.fin = System.currentTimeMillis() + duracion;
	}
	
	public Comando(byte[] comando) throws MalformedURLException{
		this(stringArray(comando)[0], new URL(stringArray(comando)[1]), Integer.parseInt(stringArray(comando)[2]));
		
	}
	
	public Comando(String comando) throws NumberFormatException, MalformedURLException{
		this(comando.split(";")[0], new URL(comando.split(";")[1]), Integer.parseInt(comando.split(";")[2]));
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
	
	public static String[] stringArray(byte[] comando){
		String[] elem = (new String(comando)).split(";");
		for(int i = 0; i<elem.length ; i++){
			elem[i] = elem[i].trim();
		}
		return elem;
		
	}


	@Override
	public String toString() {
		return "Comando [ataque=" + ataque + ", objetivo=" + objetivo.toString() + ", fin=" + fin + "]";
	}
	
	
	
}