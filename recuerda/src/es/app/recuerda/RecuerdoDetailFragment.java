package es.app.recuerda;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import es.app.recuerda.entidades.Recuerdo;
import es.app.recuerda.util.Util;

/**
 * A fragment representing a single Recuerdo detail screen. This fragment is
 * either contained in a {@link RecuerdoListActivity} in two-pane mode (on
 * tablets) or a {@link RecuerdoDetailActivity} on handsets.
 */
public class RecuerdoDetailFragment extends Fragment implements OnCompletionListener{
	
	private static final String TAG = "RecuerdoDetailFragment";
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Recuerdo mItem;
	private ImageView imgView;
	private MediaPlayer player;
	
	private RecuerdoDetailFragment fragment;

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
			//mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
			mItem = ((RecuerdaApp)getActivity().getApplication()).getItem(getArguments().getString(
					ARG_ITEM_ID));
			fragment = this;
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
			 imgView = (ImageView) rootView.findViewById(R.id.imagen_recuerdo);
			nombreTxt.setText(mItem.getNombre());
			relacionTxt.setText(mItem.getRelacion().getNombre());
			Log.i(TAG, "Tamaño imagen a mostrar:" + imgView.getWidth() + "x" + imgView.getHeight());
			
			ViewTreeObserver vto = imgView.getViewTreeObserver();
			vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			    public boolean onPreDraw() {
			        int finalHeight = imgView.getMeasuredHeight();
			        int finalWidth = imgView.getMeasuredWidth();
			        Log.i(TAG,"Height: " + finalHeight + " Width: " + finalWidth);	
			        
			        BitmapFactory.Options options = new BitmapFactory.Options();
			        try {
						options.inJustDecodeBounds = true;
						FileInputStream fileImg = new FileInputStream(mItem.getPathImg());			
						Bitmap mBitmap = BitmapFactory.decodeStream(fileImg, null, options);	
 
						options.inSampleSize = Util.calculateInSampleSize(options.outWidth, options.outHeight,
								finalWidth, finalHeight);
						Log.i(TAG, "inSampleSize: " + options.inSampleSize);
 
						options.inJustDecodeBounds = false;
						fileImg = new FileInputStream(mItem.getPathImg());
						mBitmap = BitmapFactory.decodeStream(fileImg, null, options);
						Log.i(TAG, "Longitud imagen reconstruida: " + mBitmap.getWidth() + "x" + mBitmap.getHeight());
						//Log.i(TAG, "Tamaño imagen reconstruida:" + mBitmap.getByteCount());										        
						imgView.setImageBitmap(mBitmap);
						Animation animacionFadeIn = AnimationUtils.loadAnimation(getActivity(),R.anim.transparencia);
						imgView.startAnimation(animacionFadeIn);							
					} catch (FileNotFoundException e) {
						Log.e(TAG, e.getMessage());
						//TODO: indicar error
					}
					if (Util.existeFichero(mItem.getPathAudio())) {
						player = new MediaPlayer();
						player.setOnCompletionListener(fragment);
						try {
							player.setDataSource(mItem.getPathAudio());			
							player.prepare();	
							Log.i(TAG, "Iniciamos el audio");
							player.start();
						} catch (IOException e) {
							Log.e(TAG, "No se ha podido reproducir el audio asociado al recuerdo " + mItem.getId());
							Log.e(TAG, e.getMessage());
						}
					}
			        imgView.getViewTreeObserver().removeOnPreDrawListener(this);
			        return true;
			    }
			});
			
			Animation animacion = AnimationUtils.loadAnimation(getActivity(),R.anim.animacion);
			
			relacionTxt.startAnimation(animacion);
			nombreTxt.startAnimation(animacion);
			
		}

		return rootView;
	}
	
	@Override
	public void onDestroyView() {
		Log.i(TAG, "onDestroyView");
		super.onDestroyView();
		if (getPlayer() != null && getPlayer().isPlaying()) {
			getPlayer().stop();
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.i(TAG, "El audio ha terminado de reproducirse");
		
	}
	
	public MediaPlayer getPlayer() {
		return player;
	}
	
}
