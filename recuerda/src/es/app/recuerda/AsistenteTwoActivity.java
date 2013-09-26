package es.app.recuerda;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.AndroidCharacter;
import android.text.GetChars;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

public class AsistenteTwoActivity extends Activity implements OnCompletionListener{	
	
	private static final String TAG = "AsistenteTwo";
	
	private AlertDialog.Builder builder;
	private MediaRecorder recorder;
    private MediaPlayer player;
    private File archivo;
    private Button btnGrabarParar;
    private ProgressBar progressBar;
    private Handler mHandler = new Handler();
    private int mProgressStatus = 0;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asistente_two);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		progressBar = (ProgressBar) findViewById(R.id.pbAudio);
		
		//btnGrabarParar = findViewById(R.id.imgBtnGrabParar);
		builder = new AlertDialog.Builder(this);
		builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "Guardar nueva relacion");
				dialog.cancel();
				
			}
		});
		builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "Cancelar nueva relacion");
				dialog.cancel();
				
			}
		});
		
		Button btnNueva = (Button) findViewById(R.id.btnNuevaRelacion);
		btnNueva.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(TAG, "Crear nueva");
				builder.setView(getLayoutInflater().inflate(R.layout.nueva_relacion, null));
				builder.show();
			}
		});
		
		Spinner spnRelacion = (Spinner) findViewById(R.id.spnRelacion);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.relaciones_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spnRelacion.setAdapter(adapter);
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
	            Log.i(TAG, "Siguiente!");	           
	            return true;	   
	        case android.R.id.home:
	        	Log.i(TAG, "Volver!");
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void grabarParar(View v) {
		Log.i(TAG, "Iniciamos la grabacion");
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        File path = new File(Environment.getExternalStorageDirectory()
                .getPath());
        try {
            archivo = File.createTempFile("temporal", ".3gp", path);
            Log.i(TAG, archivo.getPath());
        } catch (IOException e) {
        }
        recorder.setOutputFile(archivo.getAbsolutePath());
        try {
            recorder.prepare();
        } catch (IOException e) {
        }
        recorder.start();
        ImageButton btnGrabarParar = (ImageButton) v;
        btnGrabarParar.setImageDrawable(getResources().getDrawable(R.drawable.player_stop));
        btnGrabarParar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(TAG, "Paramos la grabación");
				detener(v);		     
				
			}
		});
    }
	
	public void detener(View v) {
        recorder.stop();
        recorder.release();
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        try {
            player.setDataSource(archivo.getAbsolutePath());
        } catch (IOException e) {
        }
        try {
            player.prepare();
        } catch (IOException e) {
        }  
        ImageButton btnGrabarParar = (ImageButton) v;
        btnGrabarParar.setImageDrawable(getResources().getDrawable(R.drawable.microphone2));
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

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Completado");
		
	}

}
