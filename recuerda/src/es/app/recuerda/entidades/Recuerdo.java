package es.app.recuerda.entidades;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Clase principal que describe el recuerdo y la relación con éste.
 * 
 *  Guardará también la foto y el audio asociado, aunque ambos se 
 *  guardarán en la SD.
 *
 */

@DatabaseTable
public class Recuerdo {
	public static final String ID = "_id";
    public static final String NOMBRE = "nombre";
    public static final String RELACION = "relacion";

    @DatabaseField(generatedId = true, columnName = ID)
	private int id;
    
    @DatabaseField(columnName = NOMBRE)
	private String nombre;
    
    @DatabaseField(foreign = true, columnName = RELACION)
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
