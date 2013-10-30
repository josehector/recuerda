package es.app.recuerda;

import java.util.HashMap;
import java.util.Map;

import es.app.recuerda.entidades.Recuerdo;

public class Partida {
	public static final int NUM_OPCIONES = 4;

	private boolean nuevoJuego;
	private Recuerdo pregunta;
	private Map<Integer, Recuerdo> opciones = new HashMap<Integer, Recuerdo>(
			NUM_OPCIONES);

	public Partida() {
		nuevoJuego = true;
		pregunta = null;
	}
	
	
	public boolean isNuevoJuego() {
		return nuevoJuego;
	}


	public void setNuevoJuego(boolean nuevoJuego) {
		this.nuevoJuego = nuevoJuego;
	}


	public Recuerdo getPregunta() {
		return pregunta;
	}

	public void setPregunta(Recuerdo pregunta) {
		this.pregunta = pregunta;
	}

	public Map<Integer, Recuerdo> getOpciones() {
		return opciones;
	}

	public void setOpciones(Map<Integer, Recuerdo> opciones) {
		this.opciones = opciones;
	}

}
