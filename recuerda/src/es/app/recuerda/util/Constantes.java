package es.app.recuerda.util;

import android.os.Environment;

public class Constantes {
	public static final String NOMBRE_APP = "recuerda";
	public static final String RUTA_APP = Environment.getExternalStorageDirectory() + "/" + NOMBRE_APP + "/";
	public static final String PREFIJO_IMG = "recuerdoImg-";
	public static final String EXTENSION_IMG = ".png";
	public static final String PREFIJO_AUDIO = "recuerdoAud-";
	public static final String EXTENSION_AUDIO = ".3gp";

}
