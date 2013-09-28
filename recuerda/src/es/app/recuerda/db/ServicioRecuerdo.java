package es.app.recuerda.db;

import java.sql.SQLException;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import es.app.recuerda.entidades.Recuerdo;
import es.app.recuerda.entidades.Relacion;
import es.app.recuerda.entidades.WraperRecuerdo;

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

}
