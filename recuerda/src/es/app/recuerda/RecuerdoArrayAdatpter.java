package es.app.recuerda;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import es.app.recuerda.entidades.WraperRecuerdo;

public class RecuerdoArrayAdatpter extends ArrayAdapter<WraperRecuerdo> {
	
	private static final String TAG = "RecuerdoArrayAdapter";
	private final Context context;
	private final List<WraperRecuerdo> values;
 
	public RecuerdoArrayAdatpter(Context context, List<WraperRecuerdo> items) {
		super(context, R.layout.element_list, items);
		this.context = context;
		this.values = items;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.element_list, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.recuerdoNameLbl);
		textView.setText(values.get(position).getRecuerdo().getNombre());
		
		// Change icon based on name
		String s = values.get(position).getRecuerdo().getNombre();
 
		Log.i(TAG, "getView -> " + s);
 
		return rowView;
	}
}
