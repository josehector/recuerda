package es.app.recuerda.util;

import java.io.File;

import android.util.Log;

/**
 * Clases de utilidad
 * @author josehector
 *
 */
public class Util {
	public static final String TAG = "Util";
	
	public static int calculateInSampleSize(
			int outWidth, int outHeight, Integer targetWidth, Integer targetHeight) {
		final int height = outHeight;
		final int width = outWidth;
		int inSampleSize = 1; // Valor por defecto para el valor de escalado

		Log.i(TAG, "Alto Objetivo:" + String.valueOf(targetHeight));
		Log.i(TAG, "Ancho Objetivo:" + String.valueOf(targetWidth));
		Log.i(TAG, "Alto salida:" + String.valueOf(height));
		Log.i(TAG, "Ancho salida:" + String.valueOf(width));

		if (height > targetHeight || width > targetWidth) {

			// Calculas los radios para la altura y anchura, luego te quedas con la menor
			final int widthRadio = Math.round((float)width / (float)targetWidth);
			final int heightRadio = Math.round((float)height / (float)targetHeight);

			return (widthRadio < heightRadio) ? widthRadio : heightRadio;

		}

		return inSampleSize;
	}	
	
	public static boolean existeFichero(String path) {
		File file = new File(path);
		boolean existe = file.exists();
		Log.i(TAG, "Comprobamos si el fichero " + path + " existe...." + (existe ? "SI" : "NO"));
		return existe;
	}
	
	public static int aleatorio(int x, int y) {
		int valorEntero = (int) Math.floor(Math.random()*(y-x+1)+x);  // Valor entre x e y, ambos incluidos.
		return valorEntero;
	}


}
