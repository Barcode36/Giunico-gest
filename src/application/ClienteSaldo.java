package application;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class ClienteSaldo implements Serializable {

	private int numero;
	private String nome;
	private String alias;
	private String citta;
	private String paese;
	private BigDecimal saldo;
	
	public ClienteSaldo(int numero, String nome, String alias, String citta, String paese, BigDecimal saldo) {
		this.numero = numero;
		this.nome = nome;
		this.alias = alias;
		this.citta = citta;
		this.paese = paese;
		this.saldo = saldo;
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

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	@Override
	public String toString() {
		return "ClienteSaldo [numero=" + numero + ", nome=" + nome + ", alias=" + alias + ", citta=" + citta
				+ ", paese=" + paese + ", saldo=" + saldo + "]";
	}
}
