package es.app.recuerda;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class AsistenteActivity extends Activity {

	private ImageButton imgRecuerdo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asistente);
				
		imgRecuerdo = (ImageButton) findViewById(R.id.imgAsRecuerdo);
		imgRecuerdo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent =  new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				startActivityForResult(intent, 0);
				
			}
		});
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data.hasExtra("data")) {		    
		    imgRecuerdo.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
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
	            Log.i("ActionBar", "Siguiente!");
	            return true;	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
