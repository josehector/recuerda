package es.app.recuerda;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * An activity representing a list of Recuerdos. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link RecuerdoDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link RecuerdoListFragment} and the item details (if present) is a
 * {@link RecuerdoDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link RecuerdoListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class RecuerdoListActivity extends FragmentActivity implements
		RecuerdoListFragment.Callbacks {

	private static final String TAG = "RecuerdoListActivity";

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private AlertDialog.Builder dialogJuego;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Inicio Recuerda");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recuerdo_list);

		dialogJuego = new AlertDialog.Builder(this);

		if (findViewById(R.id.recuerdo_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((RecuerdoListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.recuerdo_list))
					.setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link RecuerdoListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(RecuerdoDetailFragment.ARG_ITEM_ID, id);
			RecuerdoDetailFragment fragment = new RecuerdoDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.recuerdo_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, RecuerdoDetailActivity.class);
			detailIntent.putExtra(RecuerdoDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recuerda, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			Log.i(TAG, "Nuevo recuerdo!");
			Intent asistente = new Intent(this, AsistenteActivity.class);
			startActivityForResult(asistente, 1);
			return true;
		case R.id.action_game:
			Log.i(TAG, "Jugar!");
			RecuerdaApp recuerdaApp = (RecuerdaApp) getApplication();
			if (recuerdaApp.getRecuerdos() != null
					&& recuerdaApp.getRecuerdos().size() >= Partida.NUM_OPCIONES) {
				Intent juego = new Intent(this, JuegoActivity.class);
				startActivityForResult(juego, 1);
			} else {
				Log.i(TAG, "No existen los suficientes recuerdos para jugar");
				dialogJuego.setMessage(R.string.msg_dialog_nojuego);
				dialogJuego.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
				dialogJuego.show();
			}

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			this.recreate();
		} else if (resultCode == JuegoActivity.NO_RECUERDOS) {
			Log.i(TAG, "No se ha podido jugar por falta de recuerdos");
		}
	}

}
