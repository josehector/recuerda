package es.app.recuerda;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.app.recuerda.db.ServicioRecuerdo;
import es.app.recuerda.entidades.Recuerdo;
import es.app.recuerda.util.Util;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

public class JuegoActivity extends Activity{
	private static final String TAG = JuegoActivity.class.getName();
	private static final int NUM_OPCIONES = 4;

	private ImageButton imgOpcion1;
	private ImageButton imgOpcion2;
	private ImageButton imgOpcion3;
	private ImageButton imgOpcion4;
	private TextView recuerdoBuscar;
	
	private List<Recuerdo> listaRecuerdos;
	private Map<Integer,Recuerdo> opciones = new HashMap<Integer, Recuerdo>(NUM_OPCIONES);
	private Recuerdo pregunta;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.juego);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		/*imgOpcion1 = (ImageButton) findViewById(R.id.imgOpcion1);
		imgOpcion2 = (ImageButton) findViewById(R.id.imgOpcion2);
		imgOpcion3 = (ImageButton) findViewById(R.id.imgOpcion3);
		imgOpcion4 = (ImageButton) findViewById(R.id.imgOpcion4);*/
		recuerdoBuscar = (TextView) findViewById(R.id.recuerdoBuscar);
				
        if (listaRecuerdos == null) {
        	ServicioRecuerdo servicio = new ServicioRecuerdo(this);
            listaRecuerdos = servicio.getListaRecuerdos();           
            servicio.cerrar();
        }
        if (listaRecuerdos != null && listaRecuerdos.size() >= NUM_OPCIONES) {
        	inicializarMapa();
        	int intRespuesta = Util.aleatorio(0, listaRecuerdos.size()-1);
        	Log.i(TAG, "Numero aleatorio elegido " + intRespuesta);
        	pregunta = listaRecuerdos.remove(intRespuesta);
        	recuerdoBuscar.setText(pregunta.getNombre());
        	int intOpcionResp = Util.aleatorio(0, NUM_OPCIONES-1);
        	List<Integer> listaKeys = new ArrayList<Integer>(opciones.keySet());
        	int idOpcionMapa = listaKeys.remove(intOpcionResp);
        	opciones.put(idOpcionMapa, pregunta);
        	
        	for (int i = 0; i < NUM_OPCIONES - 1; i++) {
        		int intOpcion = Util.aleatorio(0, listaRecuerdos.size()-1);
        		Recuerdo recuerdoOpcion = listaRecuerdos.remove(intOpcion);
        		int idOpcion = listaKeys.remove(0);
        		Log.d(TAG, idOpcion + " - " + recuerdoOpcion.getNombre());
        		opciones.put(idOpcion, recuerdoOpcion);
        	}
        	//En el Map opciones estan las posibles respuestas de recuerdos
        	for(int idImg : opciones.keySet()) {
        		final Recuerdo recuerdo = opciones.get(idImg);
        		final ImageButton imgOpcion = (ImageButton) findViewById(idImg);
            	ViewTreeObserver vto = imgOpcion.getViewTreeObserver();
            	vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
					@Override
					public boolean onPreDraw() {
						cargarOpcion(recuerdo.getPathImg(), imgOpcion);
						imgOpcion.getViewTreeObserver().removeOnPreDrawListener(this);						
						return false;
					}            		
            	});
        	}

        	
        } else {
        	Log.i(TAG, "No existe los suficientes recuerdos para empezar un juego");
        }
	}
	
	private void cargarOpcion(String imgPath, ImageButton imgButton) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		Bitmap mBitmap = null;
		try {
			options.inJustDecodeBounds = true;
			FileInputStream fileImg = new FileInputStream(imgPath);			
			mBitmap = BitmapFactory.decodeStream(fileImg, null, options);	

			options.inSampleSize = Util.calculateInSampleSize(options.outWidth, options.outHeight,
					imgButton.getWidth(), imgButton.getHeight());
			Log.i(TAG, "inSampleSize: " + options.inSampleSize);

			options.inJustDecodeBounds = false;
			fileImg = new FileInputStream(imgPath);
			mBitmap = BitmapFactory.decodeStream(fileImg, null, options);
			Log.i(TAG, "Longitud imagen reconstruida: " + mBitmap.getWidth() + "x" + mBitmap.getHeight());
			Log.i(TAG, "Tamaño imagen reconstruida:" + mBitmap.getByteCount());	
			imgButton.setImageBitmap(mBitmap);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error al mostrar imagenes");
		}
	}
	
	private void inicializarMapa() {
		opciones.put(R.id.imgOpcion1, null);
		opciones.put(R.id.imgOpcion2, null);
		opciones.put(R.id.imgOpcion3, null);
		opciones.put(R.id.imgOpcion4, null);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.recuerda, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {		
		case android.R.id.home:
			Log.i(TAG, "Volver!");
			setResult(RESULT_CANCELED);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
