package es.app.recuerda.db;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import es.app.recuerda.entidades.Recuerdo;
import es.app.recuerda.entidades.Relacion;
import es.app.recuerda.entidades.WraperRecuerdo;
import es.app.recuerda.exception.BBDDException;
import es.app.recuerda.util.Constantes;

public class ServicioRecuerdo {
	private static final String TAG = "ServicioRecuerdo";

	private DBRecuerdo dbRecuerdo;

	public ServicioRecuerdo(Context context) {
		dbRecuerdo = OpenHelperManager.getHelper(context, DBRecuerdo.class);
	}

	public List<Recuerdo> getListaRecuerdos() {
		List<Recuerdo> lista = null;
		try {
			Dao<Recuerdo, Integer> daoRecuerdo = dbRecuerdo
					.getDao(Recuerdo.class);
			lista = daoRecuerdo.queryForAll();
			/*FileInputStream fileImg = new FileInputStream(pathImg);
			imagen = BitmapFactory.decodeStream(fileImg, null, null);*/
				
		} catch (SQLException e) {
			Log.e(TAG, "Error al recuperar los recuerdos");
		}
		return lista;
	}

	public List<Relacion> getListaRelacion() {
		List<Relacion> lista = null;
		try {
			Dao<Relacion, Integer> daoRelacion = dbRecuerdo
					.getDao(Relacion.class);
			lista = daoRelacion.queryForAll();
		} catch (SQLException e) {
			Log.e(TAG, e.getMessage());
		}
		return lista;
	}

	public void guardar(Relacion relacion) throws BBDDException {
		try {
			if (relacion.getId() == -1) {
				Dao<Relacion, Integer> daoRelacion = dbRecuerdo
						.getDao(Relacion.class);				
				daoRelacion.create(relacion);
			}
		} catch (SQLException e) {
			Log.e(TAG, e.getMessage());
			throw new BBDDException("Error al guardar la relacion");
		}
	}

	public void guardar(WraperRecuerdo wpRecuerdo) throws BBDDException {
		Recuerdo recuerdo = wpRecuerdo.getRecuerdo();
		Relacion relacion = recuerdo.getRelacion();
		try {
			if (relacion.getId() == -1) {
				Dao<Relacion, Integer> daoRelacion = dbRecuerdo
						.getDao(Relacion.class);
				daoRelacion.create(relacion);
			}
			Dao<Recuerdo, Integer> daoRecuerdo = dbRecuerdo
					.getDao(Recuerdo.class);
			int idRecuerdo = daoRecuerdo.create(recuerdo);
			Log.i(TAG, "idRecuerdo: " + recuerdo.getId());
			storeImage(wpRecuerdo.getImagen(), Constantes.PREFIJO_IMG
					+ recuerdo.getId() + Constantes.EXTENSION_IMG);
			if (wpRecuerdo.getAudio() != null) {
				storeFile(wpRecuerdo.getAudio(), Constantes.PREFIJO_AUDIO
						+ recuerdo.getId() + Constantes.EXTENSION_AUDIO);
			}
			Log.i(TAG, "Recuerdo guardado correctamente.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
			throw new BBDDException("Error al guardar WraperRecuerdo");
		}
	}
	
	public void borrar(Recuerdo recuerdo) throws BBDDException {
		
		String pathImage = Constantes.PREFIJO_IMG
				+ recuerdo.getId() + Constantes.EXTENSION_IMG;
		String pathAudio =  Constantes.PREFIJO_AUDIO
				+ recuerdo.getId() + Constantes.EXTENSION_AUDIO;
		try {
			Dao<Recuerdo, Integer> daoRecuerdo = dbRecuerdo.getDao(Recuerdo.class);
			daoRecuerdo.delete(recuerdo);
			boolean delImg = deleteFile(pathImage);
			boolean delAud = deleteFile(pathAudio);
			Log.i(TAG, "Imagen " + (delImg ? "borrada" : "NO borrada/existe"));
			Log.i(TAG, "Audio " + (delAud ? "borrada" : "NO borrada/existe"));
		} catch (SQLException e) {
			Log.e(TAG, e.getMessage());
			throw new BBDDException("Error al borrar WraperRecuerdo");
		}		
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
			byte[] array = new byte[1000];
			int leidos = bufOrigen.read(array);
			while (leidos > 0) {
				bufDestino.write(array, 0, leidos);
				leidos = bufOrigen.read(array);
			}

			// Cierre de los ficheros
			// bufDestino.close();
			// bufOrigen.close();
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
		// create storage directories, if they don't exist
		sdIconStorageDir.mkdirs();

	}

	private boolean storeImage(Bitmap imageData, String filename) {
		// get path to external storage (SD card)
		prepare();
		try {
			String filePath = Constantes.RUTA_APP + "/" + filename;
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);

			BufferedOutputStream bos = new BufferedOutputStream(
					fileOutputStream);

			// choose another format if PNG doesn't suit you
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
	
	private boolean deleteFile(String filename) {
		String filePath = Constantes.RUTA_APP + "/" + filename;
		
		File file = new File(filePath);
		return file.delete();
		
	}

}
