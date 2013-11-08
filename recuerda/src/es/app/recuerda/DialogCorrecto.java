package es.app.recuerda;

import android.app.Dialog;
//import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogCorrecto extends DialogFragment {
	//private static final String TAG = DialogCorrecto.class.getName();
	
	public static DialogCorrecto newInstance(int[] msg) {
        DialogCorrecto frag = new DialogCorrecto();
        Bundle args = new Bundle();        
        args.putIntArray("msg", msg);
        frag.setArguments(args);
        return frag;
    }


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int[] msg = getArguments().getIntArray("msg");
		Dialog dialogCorrecto = new Dialog(getActivity(), android.R.style.Theme_Translucent);
		dialogCorrecto.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogCorrecto.setContentView(R.layout.dialog_correcto);
		final TextView txtView = (TextView) dialogCorrecto.findViewById(R.id.tvDialogCorrecto);
		txtView.setText(msg[0]);
		final TextView txtView2 = (TextView) dialogCorrecto.findViewById(R.id.tvDialogCorrecto2);
		txtView2.setText(msg[1]);

		LinearLayout llDialogCorrecto = (LinearLayout) dialogCorrecto.findViewById(R.id.llDialogCorrecto);
	    llDialogCorrecto.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				txtView2.setText("");
				txtView.setText(R.string.msg_cargando_juego);
				getActivity().finish();
				getActivity().startActivity(getActivity().getIntent());				
			}
		});

		return dialogCorrecto;
	}	

}
