package application;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class OperazioneExtended implements Serializable {

	private int id;
	private Date data;
	private int cliente;
	private String numerofattura;
	private String trasportatore;
	private Date datacarico;
	private String speddoganale;
	private String mrn;
	private String informazioni;
	private boolean ok;
	private String statodest;
	
	private String nomecliente;
	private String emailcliente;
	
	public OperazioneExtended(int id, Date data, int cliente, String numerofattura, String trasportatore,
			Date datacarico, String speddoganale, String mrn, String informazioni, boolean ok, String statodest,
			String nomecliente, String emailcliente) {
		super();
		this.id = id;
		this.data = data;
		this.cliente = cliente;
		this.numerofattura = numerofattura;
		this.trasportatore = trasportatore;
		this.datacarico = datacarico;
		this.speddoganale = speddoganale;
		this.mrn = mrn;
		this.informazioni = informazioni;
		this.ok = ok;
		this.statodest = statodest;
		this.nomecliente = nomecliente;
		this.emailcliente = emailcliente;
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

	public String getTrasportatore() {
		return trasportatore;
	}

	public void setTrasportatore(String trasportatore) {
		this.trasportatore = trasportatore;
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

	public String getStatodest() {
		return statodest;
	}

	public void setStatodest(String statodest) {
		this.statodest = statodest;
	}

	public String getNomecliente() {
		return nomecliente;
	}

	public void setNomecliente(String nomecliente) {
		this.nomecliente = nomecliente;
	}

	public String getEmailcliente() {
		return emailcliente;
	}

	public void setEmailcliente(String emailcliente) {
		this.emailcliente = emailcliente;
	}

	@Override
	public String toString() {
		return "OperazioneExtended [id=" + id + ", data=" + data + ", cliente=" + cliente + ", numerofattura="
				+ numerofattura + ", trasportatore=" + trasportatore + ", datacarico=" + datacarico + ", speddoganale="
				+ speddoganale + ", mrn=" + mrn + ", informazioni=" + informazioni + ", ok=" + ok + ", statodest="
				+ statodest + ", nomecliente=" + nomecliente + ", emailcliente=" + emailcliente + "]";
	}
}
