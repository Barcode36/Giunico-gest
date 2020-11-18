package application;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("serial")
public class Operazione implements Serializable {

	private int id;
	private Date data;
	private int cliente;
	private String numerofattura;
	private BigDecimal importofattura;
	private BigDecimal importobonifico;
	private boolean esclusodocsdoganali;
	private String note;
	private int numerocolli;
	private String tipoimballo;
	private float pesolordo;
	private String notetrasportatore;
	private String trasportatore;
	private boolean stampacmr;
	private Date datacarico;
	private String speddoganale;
	private String mrn;
	private String informazioni;
	private boolean ok;
	private String nomedest;
	private String indirizzodest;
	private String capdest;
	private String cittadest;
	private String statodest;
	
	public Operazione(int id, Date data, int cliente, String numerofattura, BigDecimal importofattura, BigDecimal importobonifico,
			boolean esclusodocsdoganali, String note, int numerocolli, String tipoimballo, float pesolordo,
			String notetrasportatore, String trasportatore, boolean stampacmr, Date datacarico, String speddoganale,
			String mrn, String informazioni, boolean ok, String nomedest, String indirizzodest, String capdest,
			String cittadest, String statodest) {

		this.id = id;
		this.data = data;
		this.cliente = cliente;
		this.numerofattura = numerofattura;
		this.importofattura = importofattura;
		this.importobonifico = importobonifico;
		this.esclusodocsdoganali = esclusodocsdoganali;
		this.note = note;
		this.numerocolli = numerocolli;
		this.tipoimballo = tipoimballo;
		this.pesolordo = pesolordo;
		this.notetrasportatore = notetrasportatore;
		this.trasportatore = trasportatore;
		this.stampacmr = stampacmr;
		this.datacarico = datacarico;
		this.speddoganale = speddoganale;
		this.mrn = mrn;
		this.informazioni = informazioni;
		this.ok = ok;
		this.nomedest = nomedest;
		this.indirizzodest = indirizzodest;
		this.capdest = capdest;
		this.cittadest = cittadest;
		this.statodest = statodest;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int getCliente() {
		return cliente;
	}

	public void setCliente(int cliente) {
		this.cliente = cliente;
	}

	public String getNumerofattura() {
		return numerofattura;
	}

	public void setNumerofattura(String numerofattura) {
		this.numerofattura = numerofattura;
	}

	public BigDecimal getImportofattura() {
		return importofattura;
	}

	public void setImportofattura(BigDecimal importofattura) {
		this.importofattura = importofattura;
	}

	public BigDecimal getImportobonifico() {
		return importobonifico;
	}

	public void setImportobonifico(BigDecimal importobonifico) {
		this.importobonifico = importobonifico;
	}

	public boolean isEsclusodocsdoganali() {
		return esclusodocsdoganali;
	}

	public void setEsclusodocsdoganali(boolean esclusodocsdoganali) {
		this.esclusodocsdoganali = esclusodocsdoganali;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getNumerocolli() {
		return numerocolli;
	}

	public void setNumerocolli(int numerocolli) {
		this.numerocolli = numerocolli;
	}

	public String getTipoimballo() {
		return tipoimballo;
	}

	public void setTipoimballo(String tipoimballo) {
		this.tipoimballo = tipoimballo;
	}

	public float getPesolordo() {
		return pesolordo;
	}

	public void setPesolordo(float pesolordo) {
		this.pesolordo = pesolordo;
	}

	public String getNotetrasportatore() {
		return notetrasportatore;
	}

	public void setNotetrasportatore(String notetrasportatore) {
		this.notetrasportatore = notetrasportatore;
	}

	public String getTrasportatore() {
		return trasportatore;
	}

	public void setTrasportatore(String trasportatore) {
		this.trasportatore = trasportatore;
	}

	public boolean isStampacmr() {
		return stampacmr;
	}

	public void setStampacmr(boolean stampacmr) {
		this.stampacmr = stampacmr;
	}

	public Date getDatacarico() {
		return datacarico;
	}

	public void setDatacarico(Date datacarico) {
		this.datacarico = datacarico;
	}

	public String getSpeddoganale() {
		return speddoganale;
	}

	public void setSpeddoganale(String speddoganale) {
		this.speddoganale = speddoganale;
	}

	public String getMrn() {
		return mrn;
	}

	public void setMrn(String mrn) {
		this.mrn = mrn;
	}

	public String getInformazioni() {
		return informazioni;
	}

	public void setInformazioni(String informazioni) {
		this.informazioni = informazioni;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String getNomedest() {
		return nomedest;
	}

	public void setNomedest(String nomedest) {
		this.nomedest = nomedest;
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
		return "Operazioni [id=" + id + ", data=" + data + ", cliente=" + cliente + ", numerofattura=" + numerofattura
				+ ", importofattura=" + importofattura + ", importobonifico=" + importobonifico
				+ ", esclusodocsdoganali=" + esclusodocsdoganali + ", note=" + note + ", numerocolli=" + numerocolli
				+ ", tipoimballo=" + tipoimballo + ", pesolordo=" + pesolordo + ", notetrasportatore="
				+ notetrasportatore + ", trasportatore=" + trasportatore + ", stampacmr=" + stampacmr + ", datacarico="
				+ datacarico + ", speddoganale=" + speddoganale + ", mrn=" + mrn + ", informazioni=" + informazioni
				+ ", ok=" + ok + ", nomedest=" + nomedest + ", indirizzodest=" + indirizzodest + ", capdest=" + capdest
				+ ", cittadest=" + cittadest + ", statodest=" + statodest + "]";
	}
	
}
