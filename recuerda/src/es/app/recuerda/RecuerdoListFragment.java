package es.app.recuerda;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import es.app.recuerda.db.ServicioRecuerdo;
//import es.app.recuerda.dummy.DummyContent;
import es.app.recuerda.entidades.Recuerdo;
import es.app.recuerda.entidades.WraperRecuerdo;
import es.app.recuerda.exception.BBDDException;

/**
 * A list fragment representing a list of Recuerdos. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link RecuerdoDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class RecuerdoListFragment extends ListFragment {
	private static final String TAG = "RecuerdoListFragment";
	
	private ServicioRecuerdo servicio;
	private List<Recuerdo> listaRecuerdos;
	private FragmentManager fragmentManager;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        fragmentManager = getActivity().getSupportFragmentManager();
        
        RecuerdaApp recuerdaApp = (RecuerdaApp) getActivity().getApplication();
        listaRecuerdos = recuerdaApp.getRecuerdos();
        if (listaRecuerdos == null) {
        	servicio = new ServicioRecuerdo(this.getActivity());
            listaRecuerdos = servicio.getListaRecuerdos();
            ((RecuerdaApp)getActivity().getApplicationContext()).setRecuerdos(listaRecuerdos);
            servicio.cerrar();
        }        
        setListAdapter(new RecuerdoArrayAdatpter(getActivity(), listaRecuerdos));                
    }
 

    
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public RecuerdoListFragment() {
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);
		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(String.valueOf(listaRecuerdos.get(position).getId()));
	}
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
					
					final int indice = position;
				
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage(R.string.msg_dialog_borrar_recuerdo);
					builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				               // User clicked OK button
				        	   ServicioRecuerdo servicio = new ServicioRecuerdo(getActivity());
								List<Recuerdo> lista = ((RecuerdaApp)getActivity().getApplication()).getRecuerdos(); 
								Recuerdo recuerda = lista.get(indice);
				        	   try {
				        	   servicio.borrar(recuerda);
				        	   Toast.makeText( getActivity().getBaseContext(),
				        			   "Recuerdo " + recuerda.getNombre() + " borrado.",
				        			   Toast.LENGTH_SHORT).show();
				        	   Log.i(TAG, "Recuerdo " + recuerda.getNombre() + " borrado.");
								lista.remove(indice);
								setListAdapter(new RecuerdoArrayAdatpter(getActivity(), lista));
								servicio.cerrar();
								dialog.cancel();
				        	   } catch (BBDDException e) {
									Log.e(TAG, e.getMessage());
									dialog.cancel();
									DialogError dialogError = new DialogError();
									dialogError.show(fragmentManager, "tagAlerta");		
								}
				           }
				       });
					builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				               // User cancelled the dialog
				        	   dialog.cancel();
				           }
				       });
					builder.show();

				return true;
				
			}
		});
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
}
