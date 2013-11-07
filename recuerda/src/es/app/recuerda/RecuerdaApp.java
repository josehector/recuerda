package es.app.recuerda;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import es.app.recuerda.entidades.Recuerdo;
import es.app.recuerda.util.Util;

public class RecuerdaApp extends Application {
	private static final int NUM_FRASES = 27;
	private List<Recuerdo> recuerdos;
	private Partida partida;
	private Map<Integer, Integer> mapaFrases;
	
	public RecuerdaApp() {
		recuerdos = null;
		partida = new Partida();
		inicializarFrases();
		
	}
	
	private void inicializarFrases() {
		mapaFrases = new HashMap<Integer, Integer>(NUM_FRASES);
		mapaFrases.put(R.string.frase1, R.string.autor_frase1);
		mapaFrases.put(R.string.frase2, R.string.autor_frase2);
		mapaFrases.put(R.string.frase3, R.string.autor_frase3);
		mapaFrases.put(R.string.frase4, R.string.autor_frase4);
		mapaFrases.put(R.string.frase5, R.string.autor_frase5);
		mapaFrases.put(R.string.frase6, R.string.autor_frase6);
		
		mapaFrases.put(R.string.frase7, R.string.autor_frase7);
		mapaFrases.put(R.string.frase8, R.string.autor_frase8);
		mapaFrases.put(R.string.frase9, R.string.autor_frase9);
		mapaFrases.put(R.string.frase10, R.string.autor_frase10);
		mapaFrases.put(R.string.frase11, R.string.autor_frase11);
		mapaFrases.put(R.string.frase12, R.string.autor_frase12);
		
		mapaFrases.put(R.string.frase13, R.string.autor_frase13);
		mapaFrases.put(R.string.frase14, R.string.autor_frase14);
		mapaFrases.put(R.string.frase15, R.string.autor_frase15);
		mapaFrases.put(R.string.frase16, R.string.autor_frase16);
		mapaFrases.put(R.string.frase17, R.string.autor_frase17);
		mapaFrases.put(R.string.frase18, R.string.autor_frase18);
		
		mapaFrases.put(R.string.frase19, R.string.autor_frase19);
		mapaFrases.put(R.string.frase20, R.string.autor_frase20);
		mapaFrases.put(R.string.frase21, R.string.autor_frase21);
		mapaFrases.put(R.string.frase22, R.string.autor_frase22);
		mapaFrases.put(R.string.frase23, R.string.autor_frase23);
		mapaFrases.put(R.string.frase24, R.string.autor_frase24);
		
		mapaFrases.put(R.string.frase25, R.string.autor_frase25);
		mapaFrases.put(R.string.frase26, R.string.autor_frase26);
		mapaFrases.put(R.string.frase27, R.string.autor_frase27);
		
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
	
	/**
	 * Devuelve una frase y autor aleatorios (sobre la memoria).
	 * 
	 * @return Array con los identifcadores de la frase y su autor.
	 */
	public int[] getFrase() {
		int aleatorio = Util.aleatorio(0, NUM_FRASES - 1);
		int frase = (Integer) mapaFrases.keySet().toArray()[aleatorio];
		int autor = mapaFrases.get(frase);
		int[] valor = new int[2];
		valor[0] = frase;
		valor[1] = autor;
		return valor;		
	}
	
	/**
	 * Devuelve una frase y autor aleatorios (sobre errores).
	 * 
	 * @return Array con los identifcadores de la frase y su autor.
	 */
	public int[] getFraseError() {
		int[] valor = new int[2];
		valor[0] = R.string.frase1_error;
		valor[1] = R.string.autor_frase1_error;
		return valor;
	}
	
}
