package es.app.recuerda.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.app.recuerda.entidades.Recuerdo;
import es.app.recuerda.entidades.Relacion;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<Recuerdo> ITEMS = new ArrayList<Recuerdo>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<String, Recuerdo> ITEM_MAP = new HashMap<String, Recuerdo>();

	static {
		
		Relacion relacion = new Relacion(1,"Primo");
		
		addItem(new Recuerdo(1,"Alberto Pedro Castillo López",relacion));
		addItem(new Recuerdo(2,"Jose Hector López Benavente",relacion));
		addItem(new Recuerdo(3,"Luis Tomas Brugarolas Fernandez",relacion));
		addItem(new Recuerdo(4,"Item 4",relacion));
		addItem(new Recuerdo(5,"Item 5",relacion));
		addItem(new Recuerdo(6,"Item 6",relacion));
		addItem(new Recuerdo(6,"Item 7",relacion));
		addItem(new Recuerdo(6,"Item 8",relacion));
		addItem(new Recuerdo(6,"Item 9",relacion));
		addItem(new Recuerdo(6,"Item 10",relacion));
		
	}

	private static void addItem(Recuerdo item) {
		ITEMS.add(item);
		ITEM_MAP.put(String.valueOf(item.getId()), item);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class DummyItem {
		public int id;
		public String content;

		public DummyItem(int id, String content) {
			this.id = id;
			this.content = content;
		}

		@Override
		public String toString() {
			return content;
		}
	}
}
