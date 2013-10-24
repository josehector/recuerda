package es.app.recuerda;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;



public class DialogError extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder dialogError = new AlertDialog.Builder(getActivity());
		dialogError.setMessage(R.string.msg_dialog_error);
		dialogError.setTitle(R.string.tit_dialogError);
		dialogError.setPositiveButton(R.string.lbl_btnAceptar, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
			}
		});
		return dialogError.create();
	}

}
