package es.app.recuerda.entidades;


/**
 * Clase principal que describe el recuerdo y la relación con éste.
 * 
 *  Guardará también la foto y el audio asociado, aunque ambos se 
 *  guardarán en la SD.
 *
 */
public class Recuerdo {

	private int id;
	private String nombre;
	private Relacion relacion;
	
	public Recuerdo() {
		this.id = -1;
		this.nombre = null;
		this.relacion = null;
	}
	
	public Recuerdo(int id, String nombre, Relacion relacion) {		
		this.id = id;
		this.nombre = nombre;
		this.relacion = relacion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Relacion getRelacion() {
		return relacion;
	}

	public void setRelacion(Relacion relacion) {
		this.relacion = relacion;
	}

}
