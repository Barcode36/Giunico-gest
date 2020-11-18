package application;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ClienteFatturato {

	private String numero;
	private BigDecimal fatturato_gennaio;
	private BigDecimal fatturato_febbraio;
	private BigDecimal fatturato_marzo;
	private BigDecimal fatturato_aprile;
	private BigDecimal fatturato_maggio;
	private BigDecimal fatturato_giugno;
	private BigDecimal fatturato_luglio;
	private BigDecimal fatturato_agosto;
	private BigDecimal fatturato_settembre;
	private BigDecimal fatturato_ottobre;
	private BigDecimal fatturato_novembre;
	private BigDecimal fatturato_dicembre;
	
	public ClienteFatturato(Integer numero) {
		if (numero == 0) this.numero = "RIEPILOGO";
		else this.numero = String.valueOf(numero);
		this.fatturato_gennaio = BigDecimal.ZERO;
		this.fatturato_febbraio = BigDecimal.ZERO;
		this.fatturato_marzo = BigDecimal.ZERO;
		this.fatturato_aprile = BigDecimal.ZERO;
		this.fatturato_maggio = BigDecimal.ZERO;
		this.fatturato_giugno = BigDecimal.ZERO;
		this.fatturato_luglio = BigDecimal.ZERO;
		this.fatturato_agosto = BigDecimal.ZERO;
		this.fatturato_settembre = BigDecimal.ZERO;
		this.fatturato_ottobre = BigDecimal.ZERO;
		this.fatturato_novembre = BigDecimal.ZERO;
		this.fatturato_dicembre = BigDecimal.ZERO;
	}
	
	public void populateFatturati(HashMap<Integer, BigDecimal> map) {
		for (Map.Entry<Integer, BigDecimal> pair: map.entrySet()) {
			int mese = pair.getKey();
			BigDecimal importo = pair.getValue();
			
			switch (mese) {
				case 1: fatturato_gennaio = fatturato_gennaio.add(importo);
					break;
				case 2: fatturato_febbraio = fatturato_febbraio.add(importo);
					break;
				case 3: fatturato_marzo = fatturato_marzo.add(importo);
					break;
				case 4: fatturato_aprile = fatturato_aprile.add(importo);
					break;
				case 5: fatturato_maggio = fatturato_maggio.add(importo);
					break;
				case 6: fatturato_giugno = fatturato_giugno.add(importo);
					break;
				case 7: fatturato_luglio = fatturato_luglio.add(importo);
					break;
				case 8: fatturato_agosto = fatturato_agosto.add(importo);
					break;
				case 9: fatturato_settembre = fatturato_settembre.add(importo);
					break;
				case 10: fatturato_ottobre = fatturato_ottobre.add(importo);
					break;
				case 11: fatturato_novembre = fatturato_novembre.add(importo);
					break;
				case 12: fatturato_dicembre = fatturato_dicembre.add(importo);
					break;
			}
		}
	}
	
	public String getNumero() {
		return numero;
	}

	public BigDecimal getFatturato_gennaio() {
		return fatturato_gennaio;
	}

	public BigDecimal getFatturato_febbraio() {
		return fatturato_febbraio;
	}

	public BigDecimal getFatturato_marzo() {
		return fatturato_marzo;
	}

	public BigDecimal getFatturato_aprile() {
		return fatturato_aprile;
	}

	public BigDecimal getFatturato_maggio() {
		return fatturato_maggio;
	}

	public BigDecimal getFatturato_giugno() {
		return fatturato_giugno;
	}

	public BigDecimal getFatturato_luglio() {
		return fatturato_luglio;
	}

	public BigDecimal getFatturato_agosto() {
		return fatturato_agosto;
	}

	public BigDecimal getFatturato_settembre() {
		return fatturato_settembre;
	}

	public BigDecimal getFatturato_ottobre() {
		return fatturato_ottobre;
	}

	public BigDecimal getFatturato_novembre() {
		return fatturato_novembre;
	}

	public BigDecimal getFatturato_dicembre() {
		return fatturato_dicembre;
	}

	@Override
	public String toString() {
		return "ClientiFatturato [numero=" + numero + ", fatturato_gennaio=" + fatturato_gennaio
				+ ", fatturato_febbraio=" + fatturato_febbraio + ", fatturato_marzo=" + fatturato_marzo
				+ ", fatturato_aprile=" + fatturato_aprile + ", fatturato_maggio=" + fatturato_maggio
				+ ", fatturato_giugno=" + fatturato_giugno + ", fatturato_luglio=" + fatturato_luglio
				+ ", fatturato_agosto=" + fatturato_agosto + ", fatturato_settembre=" + fatturato_settembre
				+ ", fatturato_ottobre=" + fatturato_ottobre + ", fatturato_novembre=" + fatturato_novembre
				+ ", fatturato_dicembre=" + fatturato_dicembre + "]";
	}
}
