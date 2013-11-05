package es.app.recuerda;

import android.app.Dialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class DialogoGrabando extends DialogFragment {
	private static final String TAG = DialogoGrabando.class.getName();

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.grabar_audio);
		final Chronometer crono = (Chronometer) dialog.findViewById(R.id.chronometer);
		LinearLayout llDialogGrabar = (LinearLayout) dialog.findViewById(R.id.llDialogGrabar);		
		llDialogGrabar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((AsistenteTwoActivity)getActivity()).parar(null);
				crono.stop();
				dismiss();				
			}
		});
		crono.setBase(SystemClock.elapsedRealtime());
		crono.start();
		return dialog;
	}
	
	

}
