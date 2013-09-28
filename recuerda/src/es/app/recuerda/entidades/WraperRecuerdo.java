package es.app.recuerda.entidades;

import java.io.File;

import android.graphics.Bitmap;

public class WraperRecuerdo {
	private Recuerdo recuerdo;
	private Bitmap imagen;
	private File audio;

	public WraperRecuerdo() {
		this.recuerdo = null;
		this.imagen = null;
		this.audio = null;
	}

	public WraperRecuerdo(Recuerdo recuerdo, Bitmap imagen, File audio) {
		super();
		this.recuerdo = recuerdo;
		this.imagen = imagen;
		this.audio = audio;
	}

	public Recuerdo getRecuerdo() {
		return recuerdo;
	}

	public void setRecuerdo(Recuerdo recuerdo) {
		this.recuerdo = recuerdo;
	}

	public Bitmap getImagen() {
		return imagen;
	}

	public void setImagen(Bitmap imagen) {
		this.imagen = imagen;
	}

	public File getAudio() {
		return audio;
	}

	public void setAudio(File audio) {
		this.audio = audio;
	}

}
