package es.app.recuerda;

import java.util.List;

import es.app.recuerda.entidades.Recuerdo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RecuerdoArrayAdatpter extends ArrayAdapter<Recuerdo> {
	private final Context context;
	private final List<Recuerdo> values;
 
	public RecuerdoArrayAdatpter(Context context, List<Recuerdo> items) {
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
		textView.setText(values.get(position).getNombre());
 
		// Change icon based on name
		String s = values.get(position).getNombre();
 
		System.out.println(s);
 
		return rowView;
	}
}
