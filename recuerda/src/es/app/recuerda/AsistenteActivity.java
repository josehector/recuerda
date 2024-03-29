package es.app.recuerda;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import es.app.recuerda.util.Util;

public class AsistenteActivity extends Activity {
	
	private static final String TAG = "AsistenteActivity";

	private ImageButton imgBtnRecuerdo;	
	//private Bitmap selectedImage;
	private EditText etNombre;
	private String nombreRecuerdo;
	private Uri imagenRecuerdo;
	private int widthBtn;
	private int heightBtn;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asistente);
		Log.i(TAG, "Iniciamos el asistente");
		if (Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD_MR1){
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		nombreRecuerdo = "";
		etNombre = (EditText) findViewById(R.id.etAsNombre);
		etNombre.setText("");
		etNombre.setHint(getResources().getString(R.string.hint_nombre_recuerdo));
		imgBtnRecuerdo = (ImageButton) findViewById(R.id.imgAsRecuerdo);		
		
		imgBtnRecuerdo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				widthBtn = imgBtnRecuerdo.getWidth();
				heightBtn = imgBtnRecuerdo.getHeight();
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);				
			}
		});
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		
		if (requestCode == 2) {
			if (resultCode == RESULT_OK) {
				//Hemos guardado el recuerdo
				Log.i(TAG, "Se ha guardado el recuerdo y cerramos el asistente");				
				setResult(RESULT_OK);
				finish();
			}
		} else {	//Recogemos la imagen seleccionada
			if (resultCode == RESULT_OK) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				try {
					imagenRecuerdo = data.getData();
					Log.i(TAG, "Imagen seleccionada-> " + imagenRecuerdo.getPath());
					/*InputStream prueba = getContentResolver().openInputStream(imagenRecuerdo);
					Bitmap mBitmapPrueba = BitmapFactory.decodeStream(prueba);
					Log.i(TAG, "Tamaño original: " + mBitmapPrueba.getByteCount());*/					
					
					options.inJustDecodeBounds = true;
					InputStream imageStream = getContentResolver().openInputStream(imagenRecuerdo);					
					Bitmap mBitmap = BitmapFactory.decodeStream(imageStream, null, options);	
					
			 
			        options.inSampleSize = Util.calculateInSampleSize(options.outWidth, options.outHeight,
			        		 widthBtn, heightBtn);
			        Log.i(TAG, "inSampleSize: " + options.inSampleSize);
			 
			        options.inJustDecodeBounds = false;
			        imageStream = getContentResolver().openInputStream(imagenRecuerdo);
			        mBitmap = BitmapFactory.decodeStream(imageStream, null, options);
			        Log.i(TAG, "Longitud imagen reconstruida: " + mBitmap.getWidth() + "x" + mBitmap.getHeight());
					//Log.i(TAG, "Tamaño imagen reconstruida:" + mBitmap.getByteCount());							
			        imgBtnRecuerdo.setImageBitmap(mBitmap);
			
				} catch (FileNotFoundException e) {
					Log.e(TAG, e.toString());
				}
			}
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.asistente, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_siguiente:
	            Log.i(TAG, "Siguiente!");
	            nombreRecuerdo = etNombre.getText().toString();
	            if (imagenRecuerdo == null) {
	            	Toast.makeText(this, getResources().getText(R.string.msg_img_obligatorio), Toast.LENGTH_SHORT).show();
	            } else if (nombreRecuerdo == null || nombreRecuerdo.equals("")) { 
	            	Toast.makeText(this, getResources().getText(R.string.msg_nombre_obligatorio), Toast.LENGTH_SHORT).show();
	            }else {
	            	Intent siguiente = new Intent(this, AsistenteTwoActivity.class);  	            	
	            	siguiente.putExtra("IMG_SELECTED", imagenRecuerdo);
	            	siguiente.putExtra("NOMBRE_SELECTED", nombreRecuerdo);
	            	startActivityForResult(siguiente,2);
	            }
	            return true;
	        case android.R.id.home:
	        	Log.i(TAG, "Volver");
	        	setResult(RESULT_CANCELED);
	        	finish();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	

}
