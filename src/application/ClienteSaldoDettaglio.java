package application;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("serial")
public class ClienteSaldoDettaglio implements Serializable {

	private Date data;
	private String numerofattura;
	private BigDecimal importofattura;
	private BigDecimal importobonifico;
	private String note;
	private BigDecimal saldo;
	
	public ClienteSaldoDettaglio(Date data, String numerofattura, BigDecimal importofattura, BigDecimal importobonifico, String note, BigDecimal saldo) {
		this.data = data;
		this.numerofattura = numerofattura;
		this.importofattura = importofattura;
		this.importobonifico = importobonifico;
		this.note = note;
		this.saldo = saldo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	@Override
	public String toString() {
		return "ClienteSaldoDettaglio [data=" + data + ", numerofattura=" + numerofattura + ", importofattura="
				+ importofattura + ", importobonifico=" + importobonifico + ", note=" + note + ", saldo=" + saldo + "]";
	}
}
