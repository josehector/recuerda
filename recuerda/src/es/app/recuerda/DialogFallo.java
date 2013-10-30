package es.app.recuerda;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

public class DialogFallo extends DialogFragment implements OnClickListener {
private static final String TAG = DialogCorrecto.class.getName();
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialogError = new Dialog(getActivity(), android.R.style.Theme_Translucent);		
		dialogError.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
	    dialogError.setContentView(R.layout.dialog_fallo);
	    ImageButton imgBtn = (ImageButton) dialogError.findViewById(R.id.btnOkDialogFallo);
	    imgBtn.setOnClickListener(this);

		return dialogError;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnOkDialogFallo:
			Log.i(TAG, "Aceptamos");
			dismiss();
			break;
		default:
			break;
		}
		
	}
}
