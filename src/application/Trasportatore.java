package application;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Trasportatore implements Serializable {

	private String trasportatore;
	private String nome;
	private String indirizzo;
	private String cap;
	private String citta;
	private String paese;
	private String stato;
	private String partitaiva;
	private String iscrizionealbo;
	private String mail1ritiro;
	private String mail2ritiro;
	private String mail1docs;
	private String mail2docs;
	private String note;
	
	public Trasportatore(String trasportatore, String nome, String indirizzo, String cap, String citta, String paese, String stato, String partitaiva, String iscrizionealbo, String mail1ritiro, String mail2ritiro, String mail1docs, String mail2docs, String note) {
		this.trasportatore = trasportatore;
		this.nome = nome;
		this.indirizzo = indirizzo;
		this.cap = cap;
		this.citta = citta;
		this.paese = paese;
		this.stato = stato;
		this.partitaiva = partitaiva;
		this.iscrizionealbo = iscrizionealbo;
		this.mail1ritiro = mail1ritiro;
		this.mail2ritiro = mail2ritiro;
		this.mail1docs = mail1docs;
		this.mail2docs = mail2docs;
		this.note = note;
	}
	
	public String getTrasportatore() {
		return trasportatore;
	}
	public void setTrasportatore(String trasportatore) {
		this.trasportatore = trasportatore;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
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
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public String getPartitaiva() {
		return partitaiva;
	}
	public void setPartitaiva(String partitaiva) {
		this.partitaiva = partitaiva;
	}
	public String getIscrizionealbo() {
		return iscrizionealbo;
	}
	public void setIscrizionealbo(String iscrizionealbo) {
		this.iscrizionealbo = iscrizionealbo;
	}
	public String getMail1ritiro() {
		return mail1ritiro;
	}
	public void setMail1ritiro(String mail1ritiro) {
		this.mail1ritiro = mail1ritiro;
	}
	public String getMail2ritiro() {
		return mail2ritiro;
	}
	public void setMail2ritiro(String mail2ritiro) {
		this.mail2ritiro = mail2ritiro;
	}
	public String getMail1docs() {
		return mail1docs;
	}
	public void setMail1docs(String mail1docs) {
		this.mail1docs = mail1docs;
	}
	public String getMail2docs() {
		return mail2docs;
	}
	public void setMail2docs(String mail2docs) {
		this.mail2docs = mail2docs;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	@Override
	public String toString() {
		return "Trasportatori [trasportatore=" + trasportatore + ", nome=" + nome + ", indirizzo=" + indirizzo
				+ ", cap=" + cap + ", citta=" + citta + ", paese=" + paese + ", stato=" + stato + ", partitaiva="
				+ partitaiva + ", iscrizionealbo=" + iscrizionealbo + ", mail1ritiro=" + mail1ritiro + ", mail2ritiro="
				+ mail2ritiro + ", mail1docs=" + mail1docs + ", mail2docs=" + mail2docs + ", note=" + note + "]";
	}
	
}
