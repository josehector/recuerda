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
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

public class JuegoActivity extends Activity{
	private static final String TAG = JuegoActivity.class.getName();
		
	private TextView recuerdoBuscar;
	
	private List<Recuerdo> listaRecuerdos;
	private Partida partida;
	private Activity juegoActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.juego);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		juegoActivity = this;
		RecuerdaApp recuerdaApp = (RecuerdaApp) getApplication();
		partida = recuerdaApp.getPartida();
				
		recuerdoBuscar = (TextView) findViewById(R.id.recuerdoBuscar);
				
        if (partida.isNuevoJuego()) {
			if (listaRecuerdos == null) {
				//Se podría obtener la lista del contexto ya que antes de llegar aquí se carga la lista de recuerdos
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
				setResult(RESULT_CANCELED);
				finish();
			}
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
						//Acierto
						partida.setNuevoJuego(true);
						//desactivarOnClick();
						DialogCorrecto correcto = new DialogCorrecto();						
						correcto.show(getFragmentManager(), "tagCorrecto");
						//finish();
						//startActivity(getIntent());
					} else {
						//Error
						DialogFallo fallo = new DialogFallo();
						fallo.show(getFragmentManager(), "tagFallo");
					}						
				}
			});
    	}
	}
	
	private void desactivarOnClick() {
		ImageButton imgOpcion1 = (ImageButton) findViewById(R.id.imgOpcion1);
		imgOpcion1.setOnClickListener(null);
		ImageButton imgOpcion2 = (ImageButton) findViewById(R.id.imgOpcion2);
		imgOpcion2.setOnClickListener(null);
		ImageButton imgOpcion3 = (ImageButton) findViewById(R.id.imgOpcion3);
		imgOpcion3.setOnClickListener(null);
		ImageButton imgOpcion4 = (ImageButton) findViewById(R.id.imgOpcion4);
		imgOpcion4.setOnClickListener(null);
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
