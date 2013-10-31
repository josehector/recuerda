package es.app.recuerda;

import java.util.HashMap;
import java.util.Map;


import es.app.recuerda.entidades.Recuerdo;

public class Partida {
	public static final int NUM_OPCIONES = 4;
	
	private static final int TRAMO0 = 0;
	private static final int TRAMO1 = 3;
	private static final int TRAMO2 = 5;
	private static final int TRAMO3 = 9;
	private static final int TRAMO4 = 15;

	private boolean nuevoJuego;
	private int numPartida;
	private int acertadas;
	private int falladas;
	private Recuerdo pregunta;
	private Map<Integer, Recuerdo> opciones = new HashMap<Integer, Recuerdo>(
			NUM_OPCIONES);

	public Partida() {
		nuevoJuego = true;
		pregunta = null;
		numPartida = 1;
		acertadas = 0;
		falladas = 0;
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


	public int getNumPartida() {
		return numPartida;
	}


	public void incrementarPartida() {
		numPartida++;
	}
	
	public void setNumPartida(int numPartida) {
		this.numPartida = numPartida;
	}


	public int getAcertadas() {
		return acertadas;
	}

	public void acierto() {
		acertadas++;
	}

	public void setAcertadas(int acertadas) {
		this.acertadas = acertadas;
	}


	public int getFalladas() {
		return falladas;
	}
	
	public void fallo() {
		falladas++;
	}


	public void setFalladas(int falladas) {
		this.falladas = falladas;
	}	

}
