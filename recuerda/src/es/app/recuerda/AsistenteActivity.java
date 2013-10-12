package es.app.recuerda;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AsistenteActivity extends Activity {
	
	private static final String TAG = "AsistenteActivity";

	private ImageButton imgBtnRecuerdo;	
	private Bitmap selectedImage;
	private EditText etNombre;
	private String nombreRecuerdo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asistente);
		Log.i(TAG, "Iniciamos el asistente");
		nombreRecuerdo = "";
		etNombre = (EditText) findViewById(R.id.etAsNombre);
		etNombre.setText("");
		etNombre.setHint(getResources().getString(R.string.hint_nombre_recuerdo));
		imgBtnRecuerdo = (ImageButton) findViewById(R.id.imgAsRecuerdo);		
		
		imgBtnRecuerdo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
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
				this.recreate();	
			}
		} else {
			if (resultCode == RESULT_OK) {
				try {
					Uri imageUri = data.getData();
					InputStream imageStream = getContentResolver()
							.openInputStream(imageUri);
					selectedImage = BitmapFactory.decodeStream(imageStream);
					setImagen(imgBtnRecuerdo, selectedImage);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void setImagen(ImageButton imgButton, Bitmap bitmap) {
		int width = imgButton.getWidth();
        int heigth = imgButton.getHeight();
        imgButton.setImageDrawable(new BitmapDrawable(getResources(), redimensionarImagenMaximo(bitmap, width, heigth)));
	}
	
	private Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth){
		   //Redimensionamos
		   int width = mBitmap.getWidth();
		   int height = mBitmap.getHeight();
		   float scaleWidth = ((float) newWidth) / width;
		   float scaleHeight = ((float) newHeigth) / height;
		   // create a matrix for the manipulation
		   Matrix matrix = new Matrix();
		   // resize the bit map
		   matrix.postScale(scaleWidth, scaleHeight);
		   // recreate the new Bitmap
		   return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
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
	            if (selectedImage == null) {
	            	Toast.makeText(this, getResources().getText(R.string.msg_img_obligatorio), Toast.LENGTH_SHORT).show();
	            } if (nombreRecuerdo == null || nombreRecuerdo.equals("")) { 
	            	Toast.makeText(this, getResources().getText(R.string.msg_nombre_obligatorio), Toast.LENGTH_SHORT).show();
	            }else {
	            	Intent siguiente = new Intent(this, AsistenteTwoActivity.class);  
	            	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            	selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos); 
	            	byte[] b = baos.toByteArray();
	            	siguiente.putExtra("IMG_SELECTED", b);
	            	siguiente.putExtra("NOMBRE_SELECTED", nombreRecuerdo);
	            	startActivityForResult(siguiente,2);
	            }
	            return true;	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	

}
