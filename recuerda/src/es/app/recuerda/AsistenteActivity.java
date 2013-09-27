package es.app.recuerda;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class AsistenteActivity extends Activity {

	private ImageButton imgBtnRecuerdo;
	private Drawable imgRecuerdo;
	private Bitmap selectedImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asistente);
				
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
		if(resultCode == RESULT_OK){
            try {
                Uri imageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);                
                selectedImage = BitmapFactory.decodeStream(imageStream);
                int width = imgBtnRecuerdo.getWidth();
                int heigth = imgBtnRecuerdo.getHeight();
                imgRecuerdo = new BitmapDrawable(getResources(), redimensionarImagenMaximo(selectedImage, width, heigth));
                imgBtnRecuerdo.setImageDrawable(imgRecuerdo);   
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
	}
	
	public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth){
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
	            Log.i("ActionBar", "Siguiente!");
	            if (imgRecuerdo == null) {
	            	Toast.makeText(this, "Debe seleccionar una imagen", Toast.LENGTH_SHORT).show();
	            } else {
	            	Intent siguiente = new Intent(this, AsistenteTwoActivity.class);
	            	//siguiente.putExtra("IMG_RECUERDO", imgRecuerdo);	     
	            	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            	selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos); 
	            	byte[] b = baos.toByteArray();
	            	siguiente.putExtra("IMG_SELECTED", b);
	            	startActivity(siguiente);
	            }
	            return true;	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
