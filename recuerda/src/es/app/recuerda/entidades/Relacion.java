package es.app.recuerda.entidades;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * 
 * Clase que relaciona el recuerdo con algo.
 * Lo normal que sea con familiares.
 *
 */

@DatabaseTable
public class Relacion {
	
	public static final String ID = "_id";
    public static final String NOMBRE = "nombre";
	
    @DatabaseField(generatedId = true, columnName = ID)
	private int id;
    
    @DatabaseField(columnName = NOMBRE)
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

	@Override
	public String toString() {
		return getNombre();
	}
	
	

}
