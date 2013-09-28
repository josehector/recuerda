package es.app.recuerda.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import es.app.recuerda.entidades.Recuerdo;
import es.app.recuerda.entidades.Relacion;

public class DBRecuerdo extends OrmLiteSqliteOpenHelper{

	private static final String DATABASE_NAME = "recuerdo.db";
    private static final int DATABASE_VERSION = 1;
 
    private Dao<Recuerdo, Integer> recuerdoDao;
    private Dao<Relacion, Integer> relacionDao;
 
    public DBRecuerdo(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Recuerdo.class);
            TableUtils.createTable(connectionSource, Relacion.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
