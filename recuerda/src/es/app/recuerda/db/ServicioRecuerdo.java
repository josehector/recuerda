package es.app.recuerda.db;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import es.app.recuerda.entidades.Recuerdo;
import es.app.recuerda.entidades.Relacion;
import es.app.recuerda.entidades.WraperRecuerdo;
import es.app.recuerda.util.Constantes;

public class ServicioRecuerdo {
	private static final String TAG = "ServicioRecuerdo";
	
	private DBRecuerdo dbRecuerdo;
	
	public ServicioRecuerdo(Context context) {
		dbRecuerdo = OpenHelperManager.getHelper(context, DBRecuerdo.class);
	}	
	
	public void guardar(WraperRecuerdo wpRecuerdo) {
		Recuerdo recuerdo = wpRecuerdo.getRecuerdo();
		Relacion relacion = recuerdo.getRelacion();
		try {
			if (relacion.getId() == -1) {
				Dao<Relacion, Integer> daoRelacion = dbRecuerdo.getDao(Relacion.class);
				daoRelacion.create(relacion);
			}
			Dao<Recuerdo, Integer> daoRecuerdo = dbRecuerdo.getDao(Recuerdo.class);
			int idRecuerdo = daoRecuerdo.create(recuerdo);
			Log.i(TAG, "idRecuerdo: " + idRecuerdo);
			storeImage(wpRecuerdo.getImagen(), Constantes.PREFIJO_IMG + idRecuerdo + Constantes.EXTENSION_IMG);	
			if (wpRecuerdo.getAudio() != null) {
				storeFile(wpRecuerdo.getAudio(), Constantes.PREFIJO_AUDIO + idRecuerdo
						+ Constantes.EXTENSION_AUDIO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
		}
	}
	
	public Relacion getRelacion(int idRelacion) {
		Relacion relacion = null;
		try {
			Dao<Relacion, Integer> daoRelacion = dbRecuerdo.getDao(Relacion.class);
			relacion = daoRelacion.queryForId(idRelacion);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
		}
		return relacion;
	}
	
	public void cerrar() {
		if (dbRecuerdo != null) {
			OpenHelperManager.releaseHelper();
			dbRecuerdo = null;
		}
	}
	
	private boolean storeFile(File audio, String filename) {		
		String filePath = Constantes.RUTA_APP + "/" + filename;
		BufferedOutputStream bufDestino = null;
		BufferedInputStream bufOrigen = null;
		try {
			
			FileOutputStream fileDestino = new FileOutputStream(filePath);
			bufDestino = new BufferedOutputStream(fileDestino);
			
			FileInputStream fileOrigen = new FileInputStream(audio);
			bufOrigen = new BufferedInputStream(fileOrigen);					
			
			 // Bucle para leer de un fichero y escribir en el otro.
            byte [] array = new byte[1000];
            int leidos = bufOrigen.read(array);
            while (leidos > 0)
            {
            	bufDestino.write(array,0,leidos);
                leidos=bufOrigen.read(array);
            }
 
            // Cierre de los ficheros
            //bufDestino.close();
            //bufOrigen.close();
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} finally {
			try {
				if (bufDestino != null) {
					bufDestino.close();
				}
				if (bufOrigen != null) {
					bufOrigen.close();
				}
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		return true;
	}
	
	private void prepare() {		
		File sdIconStorageDir = new File(Constantes.RUTA_APP);

		//create storage directories, if they don't exist
		sdIconStorageDir.mkdirs();

	}
	
	private boolean storeImage(Bitmap imageData, String filename) {
		//get path to external storage (SD card)
		prepare();		
		try {
			String filePath = Constantes.RUTA_APP + "/" + filename;
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);

			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

			//choose another format if PNG doesn't suit you
			imageData.compress(CompressFormat.PNG, 100, bos);

			bos.flush();
			bos.close();
			Log.i(TAG, "Imagen guardada");

		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error saving image file: " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.e(TAG, "Error saving image file: " + e.getMessage());
			return false;
		}

		return true;
	}

}
