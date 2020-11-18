package application;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Cliente implements Serializable {

	private int numero;
	private String nome;
	private String alias;
	private String indirizzo;
	private String cap;
	private String citta;
	private String paese;
	private String email;
	private String destinazione;
	private String indirizzodest;
	private String capdest;
	private String cittadest;
	private String statodest;
	
	public Cliente(int numero, String nome, String alias, String indirizzo, String cap, String citta, String paese, String email, String destinazione, String indirizzodest, String capdest, String cittadest, String statodest) {
		this.numero = numero;
		this.nome = nome;
		this.alias = alias;
		this.indirizzo = indirizzo;
		this.cap = cap;
		this.citta = citta;
		this.paese = paese;
		this.email = email;
		this.destinazione = destinazione;
		this.indirizzodest = indirizzodest;
		this.capdest = capdest;
		this.cittadest = cittadest;
		this.statodest = statodest;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getPaese() {
		return paese;
	}

	public void setPaese(String paese) {
		this.paese = paese;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDestinazione() {
		return destinazione;
	}

	public void setDestinazione(String destinazione) {
		this.destinazione = destinazione;
	}

	public String getIndirizzodest() {
		return indirizzodest;
	}

	public void setIndirizzodest(String indirizzodest) {
		this.indirizzodest = indirizzodest;
	}

	public String getCapdest() {
		return capdest;
	}

	public void setCapdest(String capdest) {
		this.capdest = capdest;
	}

	public String getCittadest() {
		return cittadest;
	}

	public void setCittadest(String cittadest) {
		this.cittadest = cittadest;
	}

	public String getStatodest() {
		return statodest;
	}

	public void setStatodest(String statodest) {
		this.statodest = statodest;
	}

	@Override
	public String toString() {
		return "Clienti [numero=" + numero + ", nome=" + nome + ", alias=" + alias + ", indirizzo=" + indirizzo
				+ ", cap=" + cap + ", citta=" + citta + ", paese=" + paese + ", email=" + email + ", destinazione="
				+ destinazione + ", indirizzodest=" + indirizzodest + ", capdest=" + capdest + ", cittadest="
				+ cittadest + ", statodest=" + statodest + "]";
	}
	
}
