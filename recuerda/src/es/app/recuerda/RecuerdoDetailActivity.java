package es.app.recuerda;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;

/**
 * An activity representing a single Recuerdo detail screen. This activity is
 * only used on handset devices. On tablet-size devices, item details are
 * presented side-by-side with a list of items in a {@link RecuerdoListActivity}
 * .
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link RecuerdoDetailFragment}.
 */
public class RecuerdoDetailActivity extends FragmentActivity {

	private static final String TAG = "RecuerdoDetailActivity";
	
	private RecuerdoDetailFragment fragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recuerdo_detail);

		// Show the Up button in the action bar.
		if (Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD_MR1){
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}		

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(RecuerdoDetailFragment.ARG_ITEM_ID, getIntent()
					.getStringExtra(RecuerdoDetailFragment.ARG_ITEM_ID));
			fragment = new RecuerdoDetailFragment();
			fragment.setArguments(arguments);			
			getSupportFragmentManager().beginTransaction()
					.add(R.id.recuerdo_detail_container, fragment).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Log.i(TAG, "volver");
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			if (fragment != null && fragment.getPlayer() != null) {
				fragment.getPlayer().stop();
			}
			
			NavUtils.navigateUpTo(this, new Intent(this,
					RecuerdoListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
