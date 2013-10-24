package es.app.recuerda;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import es.app.recuerda.db.ServicioRecuerdo;
import es.app.recuerda.entidades.Recuerdo;
import es.app.recuerda.entidades.Relacion;
import es.app.recuerda.entidades.WraperRecuerdo;
import es.app.recuerda.exception.BBDDException;
import es.app.recuerda.util.Util;

public class AsistenteTwoActivity extends Activity implements
		OnCompletionListener {

	private static final String TAG = "AsistenteTwo";

	private AlertDialog.Builder builder;
	private Spinner spnRelacion;
	private MediaRecorder recorder;
	private MediaPlayer player;
	private File archivo;
	private Bitmap bmpSelected;
	private String nombreSelected;
	private Uri imagenSelected;
	private ProgressBar progressBar;
	private Handler mHandler = new Handler();
	private int mProgressStatus = 0;
	private ServicioRecuerdo servicio;
	private List<Relacion> listAdapter;
	private ImageView image;
	private ProgressDialog guardando;
	private Context context;
	private boolean grabar = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asistente_two);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		context = this;
		progressBar = (ProgressBar) findViewById(R.id.pbAudio);

		servicio = new ServicioRecuerdo(this);

		Bundle extras = getIntent().getExtras();
		nombreSelected = extras.getString("NOMBRE_SELECTED");
		imagenSelected = extras.getParcelable("IMG_SELECTED");

		guardando = new ProgressDialog(this);
		guardando.setMessage("Guardando...");

		builder = new AlertDialog.Builder(this);
		builder.setPositiveButton("Guardar",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.i(TAG, "Guardar nueva relacion");
						Relacion relacion = new Relacion();
						EditText etRelacion = (EditText) ((AlertDialog) dialog)
								.findViewById(R.id.etNewRelacion);
						Log.i(TAG, "Nuevo nombre relacion: "
								+ etRelacion.getText().toString());
						relacion.setNombre(etRelacion.getText().toString());
						try {
							servicio.guardar(relacion);
							listAdapter.add(relacion);
							((ArrayAdapter<Relacion>) spnRelacion.getAdapter())
									.notifyDataSetChanged();
							spnRelacion.setSelection(listAdapter.size() - 1);
						} catch (BBDDException e) {
							Log.e(TAG, e.getMessage());
							// TODO: indicar error al usuario
						}
						dialog.cancel();

					}
				});
		builder.setNegativeButton("Cancelar",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.i(TAG, "Cancelar nueva relacion");
						dialog.cancel();

					}
				});

		ImageButton btnNueva = (ImageButton) findViewById(R.id.btnNuevaRelacion);
		btnNueva.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(TAG, "Crear nueva");
				builder.setView(getLayoutInflater().inflate(
						R.layout.nueva_relacion, null));
				builder.show();
			}
		});

		spnRelacion = (Spinner) findViewById(R.id.spnRelacion);
		listAdapter = servicio.getListaRelacion();
		if (listAdapter == null) {
			listAdapter = new ArrayList<Relacion>();
		}

		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<Relacion> adapter = new ArrayAdapter<Relacion>(this,
				android.R.layout.simple_spinner_item, listAdapter);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spnRelacion.setAdapter(adapter);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// Truco para obtener el tamaño del imagenView y así optimizar la carga
		// de la foto
		Log.i(TAG, "onWindowFocusChanged");
		super.onWindowFocusChanged(hasFocus);
		// Here you can get the size!

		image = (ImageView) findViewById(R.id.ivResImg);
		Log.i(TAG, "Img:" + image.getWidth() + "x" + image.getHeight());
		BitmapFactory.Options options = new BitmapFactory.Options();
		InputStream imageStream = null;
		try {
			imageStream = getContentResolver().openInputStream(imagenSelected);

			options.inJustDecodeBounds = true;
			bmpSelected = BitmapFactory
					.decodeStream(imageStream, null, options);

			options.inSampleSize = Util.calculateInSampleSize(options.outWidth,
					options.outHeight, image.getWidth(), image.getHeight());
			Log.i(TAG, "inSampleSize: " + options.inSampleSize);

			options.inJustDecodeBounds = false;
			imageStream = getContentResolver().openInputStream(imagenSelected);
			bmpSelected = BitmapFactory
					.decodeStream(imageStream, null, options);
			Log.i(TAG,
					"Longitud imagen reconstruida: " + bmpSelected.getWidth()
							+ "x" + bmpSelected.getHeight());
			Log.i(TAG,
					"Tamaño imagen reconstruida:" + bmpSelected.getByteCount());

			image.setImageBitmap(bmpSelected);
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.toString());
			// TODO: indicar error al usuario
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.asistente_two, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_hecho:
			Log.i(TAG, "Guardar!");
			TaskGuardarRecuerdo guardar = new TaskGuardarRecuerdo();
			guardar.execute();					
			return true;
		case android.R.id.home:
			Log.i(TAG, "Volver!");
			setResult(RESULT_CANCELED);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class TaskGuardarRecuerdo extends AsyncTask<Void, Void, Recuerdo> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			guardando.show();

		}

		@Override
		protected Recuerdo doInBackground(Void... params) {
			Relacion relacion = (Relacion) spnRelacion.getSelectedItem();
			Recuerdo recuerdo = new Recuerdo(-1, nombreSelected, relacion);
			Bitmap bmpOriginal = null;
			WraperRecuerdo wpRecuerdo = null;
			try {
				InputStream imageStream = getContentResolver().openInputStream(
						imagenSelected);
				bmpOriginal = BitmapFactory.decodeStream(imageStream);
			} catch (FileNotFoundException e1) {
				Log.e(TAG, e1.getMessage());
			}
			wpRecuerdo = new WraperRecuerdo(recuerdo, bmpOriginal, archivo);

			try {
				servicio.guardar(wpRecuerdo);
			} catch (BBDDException e) {
				Log.e(TAG, e.getMessage());
			}
			return ((recuerdo.getId() == -1) ? null : recuerdo);
		}

		@Override
		protected void onPostExecute(Recuerdo result) {
			// TODO Auto-generated method stub
			guardando.dismiss();
			if (result != null) {
				((RecuerdaApp) getApplication()).getRecuerdos().add(result);
				setResult(RESULT_OK);
				Toast.makeText(context,
						getResources().getText(R.string.msg_recuerdo_guardado),
						Toast.LENGTH_SHORT).show();
				finish();
			} else {
				//TODO:Indicar error
			}
		}

	}

	public void grabarParar(View v) {
		if (grabar) {
			Log.i(TAG, "Iniciamos la grabacion");
			if (archivo != null) {
				//TODO: habría que indicar que el audio anterior va a ser borrado
				archivo.delete();				
			}
			ocultarPanelAudio(true);			
			recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			File path = new File(Environment.getExternalStorageDirectory()
					.getPath());
			try {
				archivo = File.createTempFile("temporal", ".3gp", path);
				archivo.deleteOnExit();
				Log.i(TAG, archivo.getPath());
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
			recorder.setOutputFile(archivo.getAbsolutePath());
			try {
				recorder.prepare();
			} catch (IOException e) {
			}
			recorder.start();
			ImageButton btnGrabarParar = (ImageButton) v;
			btnGrabarParar.setImageDrawable(getResources().getDrawable(
					R.drawable.player_stop));
			grabar = false;
		} else {
			Log.i(TAG, "Paramos la grabación");
			recorder.stop();
			recorder.release();
			player = new MediaPlayer();
			player.setOnCompletionListener(this);
			try {
				player.setDataSource(archivo.getAbsolutePath());
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
			try {
				player.prepare();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
			ImageButton btnGrabarParar = (ImageButton) v;
			btnGrabarParar.setImageDrawable(getResources().getDrawable(
					R.drawable.microphone2));
			if (archivo != null) {
				ocultarPanelAudio(false);			
			}
			grabar = true;
		}
	}
		
	

	public void reproducir(View v) {
		Log.i(TAG, "Reproducir -> " + player.getDuration());
		player.start();

		final int duracion = player.getDuration() / 1000;
		progressBar.setProgress(0);
		progressBar.setMax(duracion);

		new Thread(new Runnable() {
			public void run() {
				while (mProgressStatus < duracion) {
					mProgressStatus = player.getCurrentPosition() / 1000;

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// Update the progress bar
					mHandler.post(new Runnable() {
						public void run() {
							progressBar.setProgress(mProgressStatus);
						}
					});
				}
			}
		}).start();

	}
	
	public void borrar(View v) {
		Log.i(TAG, "Borrar audio");
		if (archivo != null) {
			archivo.delete();
			archivo = null;
		}
		ocultarPanelAudio(true);		
	}
	
	private void ocultarPanelAudio(boolean valor) {
		LinearLayout llPlayAudio = (LinearLayout) findViewById(R.id.llAsPlayAudio);
		llPlayAudio.setVisibility((valor ? LinearLayout.INVISIBLE : LinearLayout.VISIBLE));
	}

	@Override
	public void onCompletion(MediaPlayer player) {
		Log.i(TAG, "Completado");
		progressBar.setProgress(0);

	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, "OnDestroy");
		super.onDestroy();
		if (archivo != null) {
			archivo.delete();
		}		
		servicio.cerrar();
	}

}
