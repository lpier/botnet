package bot;

public class Orden {
	private String accion;
	private String objetivo;
	
	
	public Orden() {

	}

	public Orden(String accion) {
		super();
		this.accion = accion;
	}

	public Orden(String accion, String objetivo) {
		super();
		this.accion = accion;
		this.objetivo = objetivo;
	}
	
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	public String getObjetivo() {
		return objetivo;
	}
	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}
	
	
	
}
