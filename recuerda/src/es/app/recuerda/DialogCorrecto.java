package es.app.recuerda;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

public class DialogCorrecto extends DialogFragment {
	private static final String TAG = DialogCorrecto.class.getName();
	
	public static DialogCorrecto newInstance(int msg) {
        DialogCorrecto frag = new DialogCorrecto();
        Bundle args = new Bundle();
        args.putInt("msg", msg);
        frag.setArguments(args);
        return frag;
    }


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int msg = getArguments().getInt("msg");
		Dialog dialogCorrecto = new Dialog(getActivity(), android.R.style.Theme_Translucent);
		dialogCorrecto.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogCorrecto.setContentView(R.layout.dialog_correcto);
		TextView txtView = (TextView) dialogCorrecto.findViewById(R.id.tvDialogCorrecto);
		txtView.setText(msg);

	    ImageButton imgBtn = (ImageButton) dialogCorrecto.findViewById(R.id.btnOkDialogCorrecto);
	    imgBtn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				getActivity().finish();
				getActivity().startActivity(getActivity().getIntent());				
			}
		});

		return dialogCorrecto;
	}	

}
