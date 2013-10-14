package es.app.recuerda;

import java.util.List;

import es.app.recuerda.entidades.WraperRecuerdo;

import android.app.Application;

public class RecuerdaApp extends Application {
	private List<WraperRecuerdo> recuerdos;
	
	public RecuerdaApp() {
		recuerdos = null;
	}

	public List<WraperRecuerdo> getRecuerdos() {
		return recuerdos;
	}

	public void setRecuerdos(List<WraperRecuerdo> recuerdos) {
		this.recuerdos = recuerdos;
	}
	
	public WraperRecuerdo getItem(String id) {
		if (getRecuerdos() != null) {
			for (WraperRecuerdo wr : getRecuerdos()) {
				if (String.valueOf(wr.getRecuerdo().getId()).equals(id)) {
					return wr;
				}
			}
		}
		return null;
	}

}
