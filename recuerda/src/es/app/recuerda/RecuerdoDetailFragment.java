package es.app.recuerda;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import es.app.recuerda.dummy.DummyContent;
import es.app.recuerda.entidades.Recuerdo;

/**
 * A fragment representing a single Recuerdo detail screen. This fragment is
 * either contained in a {@link RecuerdoListActivity} in two-pane mode (on
 * tablets) or a {@link RecuerdoDetailActivity} on handsets.
 */
public class RecuerdoDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Recuerdo mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public RecuerdoDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_recuerdo_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			//TODO RELLENAR CONTENIDO VER ANIMACIONES
			TextView nombreTxt = ((TextView) rootView.findViewById(R.id.recuerdo_nombre));
			TextView relacionTxt = ((TextView) rootView.findViewById(R.id.recuerdo_relacion));
			ImageView imgView = (ImageView) rootView.findViewById(R.id.imagen_recuerdo);
			nombreTxt.setText(mItem.getNombre());
			relacionTxt.setText(mItem.getRelacion().getNombre());
			
			Animation animacion = AnimationUtils.loadAnimation(getActivity(),R.anim.animacion);
			Animation animacionFadeIn = AnimationUtils.loadAnimation(getActivity(),R.anim.transparencia);
			
			relacionTxt.startAnimation(animacion);
			nombreTxt.startAnimation(animacion);
			imgView.startAnimation(animacionFadeIn);
		}

		return rootView;
	}
}
