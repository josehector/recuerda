package es.app.recuerda.entidades;


/**
 * 
 * Clase que relaciona el recuerdo con algo.
 * Lo normal que sea con familiares.
 *
 */
public class Relacion {
	private int id;
	private String nombre;

	public Relacion() {
		this.id = -1;
		this.nombre = null;
	}

	public Relacion(int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
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

}
