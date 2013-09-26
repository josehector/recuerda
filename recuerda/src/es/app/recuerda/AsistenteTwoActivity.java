package es.app.recuerda;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.GetChars;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

public class AsistenteTwoActivity extends Activity {	
	
	private static final String TAG = "AsistenteTwo";
	
	private AlertDialog.Builder builder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asistente_two);
		
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

}
