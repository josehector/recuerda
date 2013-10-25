package es.app.recuerda.db;

import java.io.File;
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import es.app.recuerda.R;
import es.app.recuerda.entidades.Recuerdo;
import es.app.recuerda.entidades.Relacion;
import es.app.recuerda.util.Constantes;

public class DBRecuerdo extends OrmLiteSqliteOpenHelper{

	private static final String TAG = "DBRecuerdo";
	private static final String DATABASE_NAME = "recuerdo.db";
    private static final int DATABASE_VERSION = 1;
 
    private Dao<Recuerdo, Integer> recuerdoDao;
    private Dao<Relacion, Integer> relacionDao;
    private String[] relaciones;
 
    public DBRecuerdo(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        relaciones = context.getResources().getStringArray(R.array.relaciones_array);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
        	prepare();
            TableUtils.createTable(connectionSource, Recuerdo.class);
            TableUtils.createTable(connectionSource, Relacion.class);
            for(int i = 0; i<relaciones.length; i++) {
            	getRelacionDao().create(new Relacion(i + 1, relaciones[i]));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Prepara la carpeta que guardará las imagenes y los audios.
     * Elimina los ficheros si hubiera una instalación antigua.
     */
    private void prepare() {
		File carpetaRecuerda = new File(Constantes.RUTA_APP);
		if (carpetaRecuerda.exists()) {
			Log.i(TAG, "Ya existe la carpeta creada.");
			File[] archivos = carpetaRecuerda.listFiles();
			for (File archivo : archivos) {
				boolean valor = archivo.delete();
				Log.i(TAG, "Borrando fichero " + archivo.getPath() + "......." + (valor ? "OK" : "No se pudo."));
			}
		} else {
			carpetaRecuerda.mkdirs();
		}
	}
 
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        onCreate(db, connectionSource);
    }
 
    public Dao<Recuerdo, Integer> getRecuerdoDao() throws SQLException {
        if (recuerdoDao == null) {
        	recuerdoDao = getDao(Recuerdo.class);
        }
        return recuerdoDao;
    }
 
    public Dao<Relacion, Integer> getRelacionDao() throws SQLException {
        if (relacionDao == null) {
        	relacionDao = getDao(Relacion.class);
        }
        return relacionDao;
    }
 
    @Override
    public void close() {
        super.close();
        recuerdoDao = null;
        relacionDao = null;
    }
}
