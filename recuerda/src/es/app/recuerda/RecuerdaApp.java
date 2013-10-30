package es.app.recuerda;

import java.util.List;

import es.app.recuerda.entidades.Recuerdo;
import es.app.recuerda.entidades.WraperRecuerdo;

import android.app.Application;

public class RecuerdaApp extends Application {
	private List<Recuerdo> recuerdos;
	private Partida partida;
	
	public RecuerdaApp() {
		recuerdos = null;
		partida = new Partida();
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

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}
	
}
