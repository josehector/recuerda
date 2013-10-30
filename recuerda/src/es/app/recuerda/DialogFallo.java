package es.app.recuerda;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

public class DialogFallo extends DialogFragment {
	private static final String TAG = DialogCorrecto.class.getName();

	public static DialogFallo newInstance(int msg) {
		DialogFallo frag = new DialogFallo();
		Bundle args = new Bundle();
		args.putInt("msg", msg);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int msg = getArguments().getInt("msg");
		Dialog dialogFallo = new Dialog(getActivity(), android.R.style.Theme_Translucent);
		dialogFallo.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogFallo.setContentView(R.layout.dialog_fallo);
		TextView txtView = (TextView) dialogFallo.findViewById(R.id.tvDialogFallo);
		txtView.setText(msg);
		
		ImageButton imgBtn = (ImageButton) dialogFallo.findViewById(R.id.btnOkDialogFallo);
	    imgBtn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				dismiss();				
			}
		});

		return dialogFallo;
	}
}
