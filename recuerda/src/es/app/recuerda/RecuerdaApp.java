package es.app.recuerda;

import java.util.List;

import es.app.recuerda.entidades.Recuerdo;
import es.app.recuerda.entidades.WraperRecuerdo;

import android.app.Application;

public class RecuerdaApp extends Application {
	private List<Recuerdo> recuerdos;
	
	public RecuerdaApp() {
		recuerdos = null;
	}

	public List<Recuerdo> getRecuerdos() {
		return recuerdos;
	}

	public void setRecuerdos(List<Recuerdo> recuerdos) {
		this.recuerdos = recuerdos;
	}
	
	public Recuerdo getItem(String id) {
		if (getRecuerdos() != null) {
			for (Recuerdo recuerdo : getRecuerdos()) {
				if (String.valueOf(recuerdo.getId()).equals(id)) {
					return recuerdo;
				}
			}
		}
		return null;
	}

}
