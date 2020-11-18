package application;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Paese implements Serializable {

	private String sigla;
	private String stato;
	private boolean ue;
	
	public Paese(String sigla, String stato, boolean ue) {
		super();
		this.sigla = sigla;
		this.stato = stato;
		this.ue = ue;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public boolean isUe() {
		return ue;
	}

	public void setUe(boolean ue) {
		this.ue = ue;
	}

	@Override
	public String toString() {
		return "Paesi [sigla=" + sigla + ", stato=" + stato + ", ue=" + ue + "]";
	}
	
}
