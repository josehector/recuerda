package es.app.recuerda;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;
import es.app.recuerda.db.ServicioRecuerdo;
import es.app.recuerda.entidades.Recuerdo;
import es.app.recuerda.util.Util;

public class JuegoActivity extends FragmentActivity{
	private static final String TAG = JuegoActivity.class.getName();
	public static final int NO_RECUERDOS = 2;
		
	private TextView recuerdoBuscar;
	
	private List<Recuerdo> listaRecuerdos;
	private Partida partida;
	private RecuerdaApp recuerdaApp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.juego);
		recuerdaApp = (RecuerdaApp) getApplication();
		partida = recuerdaApp.getPartida();		
				
		recuerdoBuscar = (TextView) findViewById(R.id.recuerdoBuscar);
				
        if (partida.isNuevoJuego()) {
        	listaRecuerdos = recuerdaApp.getRecuerdos();
			if (listaRecuerdos == null) {
				ServicioRecuerdo servicio = new ServicioRecuerdo(this);
				listaRecuerdos = servicio.getListaRecuerdos();
				servicio.cerrar();
			}
			if (listaRecuerdos != null && listaRecuerdos.size() >= Partida.NUM_OPCIONES) {
				Log.i(TAG, "Creamos nuevo juego");
				partida.setNuevoJuego(false);				
				inicializarMapa();
				int intRespuesta = Util.aleatorio(0, listaRecuerdos.size() - 1);
				Log.i(TAG, "Numero aleatorio elegido " + intRespuesta);
				partida.setPregunta(listaRecuerdos.remove(intRespuesta));				
				int intOpcionResp = Util.aleatorio(0, Partida.NUM_OPCIONES - 1);
				List<Integer> listaKeys = new ArrayList<Integer>(
						partida.getOpciones().keySet());
				int idOpcionMapa = listaKeys.remove(intOpcionResp);
				partida.getOpciones().put(idOpcionMapa, partida.getPregunta());

				for (int i = 0; i < Partida.NUM_OPCIONES - 1; i++) {
					int intOpcion = Util
							.aleatorio(0, listaRecuerdos.size() - 1);
					Recuerdo recuerdoOpcion = listaRecuerdos.remove(intOpcion);
					int idOpcion = listaKeys.remove(0);
					Log.d(TAG, idOpcion + " - " + recuerdoOpcion.getNombre());
					partida.getOpciones().put(idOpcion, recuerdoOpcion);
				}
			} else {
				Log.i(TAG,
						"No existe los suficientes recuerdos para empezar un juego");
				setResult(NO_RECUERDOS);
				finish();
			}
		}
        if (Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD_MR1){
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setTitle("Seleccione la imagen de " + partida.getPregunta().getNombre());
		}
        
		//En el Map opciones estan las posibles respuestas de recuerdos
        recuerdoBuscar.setText(partida.getPregunta().getNombre());
    	for(int idImg : partida.getOpciones().keySet()) {
    		final Recuerdo recuerdo = partida.getOpciones().get(idImg);
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
        	imgOpcion.setOnClickListener(new View.OnClickListener() {					
				@Override
				public void onClick(View v) {
					int idImgButton = v.getId();
					Recuerdo recuerdoImgButton = partida.getOpciones().get(idImgButton);
					if (recuerdoImgButton.getId() == partida.getPregunta().getId()) {
						Log.d(TAG, "Acierto");
						partida.setNuevoJuego(true);
						partida.incrementarPartida();
						partida.acierto();
						DialogCorrecto correcto = DialogCorrecto.newInstance(recuerdaApp.getFrase());						
						//correcto.show(getFragmentManager(), "tagCorrecto");
						correcto.show(getSupportFragmentManager(), "tagCorrecto");
					} else {
						Log.d(TAG, "Fallo");
						partida.fallo();
						DialogFallo fallo = DialogFallo.newInstance(recuerdaApp.getFraseError());
						//fallo.show(getFragmentManager(), "tagFallo");
						fallo.show(getSupportFragmentManager(), "tagFallo");
					}						
				}
			});
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
			//Log.i(TAG, "Tamaño imagen reconstruida:" + mBitmap.getByteCount());	
			imgButton.setImageBitmap(mBitmap);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error al mostrar imagenes");			
		}
	}

	private void inicializarMapa() {
		partida.getOpciones().put(R.id.imgOpcion1, null);
		partida.getOpciones().put(R.id.imgOpcion2, null);
		partida.getOpciones().put(R.id.imgOpcion3, null);
		partida.getOpciones().put(R.id.imgOpcion4, null);
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
