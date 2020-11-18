package application.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import application.Cliente;
import application.ClienteSaldo;
import application.ClienteSaldoDettaglio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientiDAO {

	Connection connection = null;
	
	// Constructor
	public ClientiDAO() {
		try {
			connection = DBConnector.getConnection();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*** SELECT QUERY ***/
	public int countClienti() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM clienti");
		ResultSet rs = preparedStatement.executeQuery();
		
		int clienti = 0;
		while (rs.next()) {
			clienti = rs.getInt("count");
		}
		return clienti;
	}
	
	public ObservableList<Cliente> getAllClientiOrderByNome() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM clienti ORDER BY nome");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<Cliente> list_clienti = FXCollections.observableArrayList();
		
		while (rs.next()) {
			list_clienti.add(new Cliente(rs.getInt("numero"), rs.getString("nome"), rs.getString("alias"), rs.getString("indirizzo"), rs.getString("cap"), rs.getString("citta"), rs.getString("paese"), rs.getString("email"), rs.getString("destinazione"), rs.getString("indirizzodest"), rs.getString("capdest"), rs.getString("cittadest"), rs.getString("statodest")));
		}
		
		return list_clienti;
	}
	
	public ObservableList<Integer> getAllNumeroClienti() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT numero FROM clienti ORDER BY numero");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<Integer> list_numeri = FXCollections.observableArrayList();
		
		while (rs.next()) {
			list_numeri.add(rs.getInt("numero"));
		}
		
		return list_numeri;
	}
	
	public Map<String, String> getDefaultDestinazioneInfoByNumero(int numero_cliente) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT nome, destinazione,indirizzodest,capdest,cittadest,statodest FROM clienti WHERE numero=?");
		preparedStatement.setInt(1, numero_cliente);
		ResultSet rs = preparedStatement.executeQuery();
		
		Map<String, String> dest_map = new HashMap<String, String>();
		
		while (rs.next()) {
			dest_map.put("nome", rs.getString("nome"));
			dest_map.put("destinazione", rs.getString("destinazione"));
			dest_map.put("indirizzodest", rs.getString("indirizzodest"));
			dest_map.put("capdest", rs.getString("capdest"));
			dest_map.put("cittadest", rs.getString("cittadest"));
			dest_map.put("statodest", rs.getString("statodest"));
		}
		
		return dest_map;
	}
	
	public ObservableList<ClienteSaldo> getAllClientiWithSaldoDal(Date data_iniziale) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT o.cliente AS numero, c.nome AS nome, c.alias AS alias, c.citta AS citta, c.paese AS paese, SUM(importobonifico-importofattura) AS saldo FROM clienti c, operazioni o WHERE c.numero=o.cliente AND o.data>=? GROUP BY o.cliente ORDER BY c.nome, o.cliente");
		java.sql.Date date_temp = new java.sql.Date(data_iniziale.getTime());
		preparedStatement.setString(1, date_temp.toString());
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<ClienteSaldo> list_clienti_saldo = FXCollections.observableArrayList();
		
		while (rs.next()) {
			list_clienti_saldo.add(new ClienteSaldo(rs.getInt("numero"), rs.getString("nome"), rs.getString("alias"), rs.getString("citta"), rs.getString("paese"), rs.getBigDecimal("saldo")));
		}
		
		return list_clienti_saldo;
	}
	
	public ObservableList<ClienteSaldo> getAllClientiWithSaldoDal(boolean ue, Date data_iniziale) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT o.cliente AS numero, c.nome AS nome, c.alias AS alias, c.citta AS citta, c.paese AS paese, SUM(importobonifico-importofattura) AS saldo FROM clienti c, operazioni o WHERE c.numero=o.cliente AND p.ue=? AND o.data>=? GROUP BY o.cliente ORDER BY c.nome, o.cliente");
		preparedStatement.setBoolean(1, ue);
		preparedStatement.setDate(2, new java.sql.Date(data_iniziale.getTime()));
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<ClienteSaldo> list_clienti_saldo = FXCollections.observableArrayList();
		
		while (rs.next()) {
			list_clienti_saldo.add(new ClienteSaldo(rs.getInt("numero"), rs.getString("nome"), rs.getString("alias"), rs.getString("citta"), rs.getString("paese"), rs.getBigDecimal("saldo")));
		}
		
		return list_clienti_saldo;
	}
	
	public ObservableList<ClienteSaldo> getAllClientiWithSaldo(boolean ue) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT o.cliente AS numero, c.nome AS nome, c.alias AS alias, c.citta AS citta, c.paese AS paese, SUM(importobonifico-importofattura) AS saldo FROM clienti c, operazioni o, paesi p WHERE c.numero=o.cliente AND c.paese=p.sigla AND p.ue=? GROUP BY o.cliente ORDER BY c.nome, o.cliente");
		preparedStatement.setBoolean(1, ue);
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<ClienteSaldo> list_clienti_saldo = FXCollections.observableArrayList();
		
		while (rs.next()) {
			list_clienti_saldo.add(new ClienteSaldo(rs.getInt("numero"), rs.getString("nome"), rs.getString("alias"), rs.getString("citta"), rs.getString("paese"), rs.getBigDecimal("saldo")));
		}
		
		return list_clienti_saldo;
	}
	
	public BigDecimal getSaldoCliente(int numero_cliente) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(o.importobonifico-o.importofattura) AS saldo FROM clienti c, operazioni o WHERE c.numero=o.cliente AND c.numero=? GROUP BY o.cliente");
		preparedStatement.setInt(1, numero_cliente);
		ResultSet rs = preparedStatement.executeQuery();
		
		BigDecimal saldo = BigDecimal.ZERO;
		while (rs.next()) {
			saldo = rs.getBigDecimal("saldo");
		}
		return saldo;
	}
	
	public ObservableList<ClienteSaldo> getAllClientiWithSaldo() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT o.cliente AS numero, c.nome AS nome, c.alias AS alias, c.citta AS citta, c.paese AS paese, SUM(importobonifico-importofattura) AS saldo FROM clienti c, operazioni o WHERE c.numero=o.cliente GROUP BY o.cliente ORDER BY c.nome, o.cliente");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<ClienteSaldo> list_clienti_saldo = FXCollections.observableArrayList();
		
		while (rs.next()) {
			list_clienti_saldo.add(new ClienteSaldo(rs.getInt("numero"), rs.getString("nome"), rs.getString("alias"), rs.getString("citta"), rs.getString("paese"), rs.getBigDecimal("saldo")));
		}
		
		return list_clienti_saldo;
	}
	
	public ObservableList<ClienteSaldoDettaglio> getAllClientiWithSaldoDettaglio(int numero_cliente) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT o.data AS data, o.numerofattura AS numerofattura, o.importofattura AS importofattura, o.importobonifico AS importobonifico, o.note AS note, SUM(o.importobonifico-o.importofattura) AS saldo FROM clienti c, operazioni o WHERE c.numero=o.cliente AND c.numero=? GROUP BY o.data, o.numerofattura, o.importofattura, o.importobonifico, o.note ORDER BY o.data, c.nome, o.cliente");
		preparedStatement.setInt(1, numero_cliente);
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<ClienteSaldoDettaglio> list_clienti_saldo_dettaglio = FXCollections.observableArrayList();
		
		while (rs.next()) {
			list_clienti_saldo_dettaglio.add(new ClienteSaldoDettaglio(rs.getDate("data"), rs.getString("numerofattura"), rs.getBigDecimal("importofattura"), rs.getBigDecimal("importobonifico"), rs.getString("note"), rs.getBigDecimal("saldo")));
		}
		
		return list_clienti_saldo_dettaglio;
	}
	
	public ObservableList<ClienteSaldoDettaglio> getAllClientiWithSaldoDettaglio(int numero_cliente, Date data_finale) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT o.data AS data, o.numerofattura AS numerofattura, o.importofattura AS importofattura, o.importobonifico AS importobonifico, o.note AS note, SUM(o.importobonifico-o.importofattura) AS saldo FROM clienti c, operazioni o WHERE c.numero=o.cliente AND c.numero=? AND o.data<=? GROUP BY o.data, o.numerofattura, o.importofattura, o.importobonifico, o.note ORDER BY o.data, c.nome, o.cliente");
		preparedStatement.setInt(1, numero_cliente);
		java.sql.Date date_temp = new java.sql.Date(data_finale.getTime());
		preparedStatement.setString(2, date_temp.toString());
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<ClienteSaldoDettaglio> list_clienti_saldo_dettaglio = FXCollections.observableArrayList();
		
		while (rs.next()) {
			list_clienti_saldo_dettaglio.add(new ClienteSaldoDettaglio(rs.getDate("data"), rs.getString("numerofattura"), rs.getBigDecimal("importofattura"), rs.getBigDecimal("importobonifico"), rs.getString("note"), rs.getBigDecimal("saldo")));
		}
		
		return list_clienti_saldo_dettaglio;
	}
	
	public ObservableList<ClienteSaldoDettaglio> getAllClientiWithSaldoDettaglio(Date data_iniziale, int numero_cliente) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT o.data AS data, o.numerofattura AS numerofattura, o.importofattura AS importofattura, o.importobonifico AS importobonifico, o.note AS note, SUM(o.importobonifico-o.importofattura) AS saldo FROM clienti c, operazioni o WHERE c.numero=o.cliente AND c.numero=? AND o.data>=? GROUP BY o.data, o.numerofattura, o.importofattura, o.importobonifico, o.note ORDER BY o.data, c.nome, o.cliente");
		preparedStatement.setInt(1, numero_cliente);
		java.sql.Date date_temp = new java.sql.Date(data_iniziale.getTime());
		preparedStatement.setString(2, date_temp.toString());
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<ClienteSaldoDettaglio> list_clienti_saldo_dettaglio = FXCollections.observableArrayList();
		
		while (rs.next()) {
			list_clienti_saldo_dettaglio.add(new ClienteSaldoDettaglio(rs.getDate("data"), rs.getString("numerofattura"), rs.getBigDecimal("importofattura"), rs.getBigDecimal("importobonifico"), rs.getString("note"), rs.getBigDecimal("saldo")));
		}
		
		return list_clienti_saldo_dettaglio;
	}
	
	public ObservableList<ClienteSaldoDettaglio> getAllClientiWithSaldoDettaglio(int numero_cliente, Date data_iniziale, Date data_finale) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT o.data AS data, o.numerofattura AS numerofattura, o.importofattura AS importofattura, o.importobonifico AS importobonifico, o.note AS note, SUM(o.importobonifico-o.importofattura) AS saldo FROM clienti c, operazioni o WHERE c.numero=o.cliente AND c.numero=? AND o.data>=? AND o.data<=? GROUP BY o.data, o.numerofattura, o.importofattura, o.importobonifico, o.note ORDER BY o.data, c.nome, o.cliente");
		preparedStatement.setInt(1, numero_cliente);
		java.sql.Date date_init_temp = new java.sql.Date(data_iniziale.getTime());
		preparedStatement.setString(2, date_init_temp.toString());
		
		java.sql.Date date_fine_temp = new java.sql.Date(data_finale.getTime());
		preparedStatement.setString(3, date_fine_temp.toString());
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<ClienteSaldoDettaglio> list_clienti_saldo_dettaglio = FXCollections.observableArrayList();
		
		while (rs.next()) {
			list_clienti_saldo_dettaglio.add(new ClienteSaldoDettaglio(rs.getDate("data"), rs.getString("numerofattura"), rs.getBigDecimal("importofattura"), rs.getBigDecimal("importobonifico"), rs.getString("note"), rs.getBigDecimal("saldo")));
		}
		
		return list_clienti_saldo_dettaglio;
	}
	
	public Map<String, String> getInfoByNumeroCliente(int numero_cliente) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT nome,stato,citta FROM clienti,paesi WHERE paese=sigla AND numero=?");
		preparedStatement.setInt(1, numero_cliente);
		ResultSet rs = preparedStatement.executeQuery();
		
		Map<String, String> dest_map = new HashMap<String, String>();
		
		while (rs.next()) {
			dest_map.put("nome", rs.getString("nome"));
			dest_map.put("stato", rs.getString("stato"));
			dest_map.put("citta", rs.getString("citta"));
		}
		
		return dest_map;
	}
	
	public String getNomeByNumero(int numero_cliente) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT nome FROM clienti WHERE numero=?");
		preparedStatement.setInt(1, numero_cliente);
		ResultSet rs = preparedStatement.executeQuery();
		
		String nome = "";
		while (rs.next()) {
			nome = rs.getString("nome");
		}
		return nome;
	}
	
	public HashMap<String, String> getInfoCMRByNumeroNoDest(int numero_cliente) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT nome,alias,citta,cap,indirizzo,stato,ue FROM clienti, paesi WHERE paese=sigla AND numero=?");
		preparedStatement.setInt(1, numero_cliente);
		ResultSet rs = preparedStatement.executeQuery();
		
		HashMap<String, String> map = new HashMap<String, String>();
		while (rs.next()) {
			map.put("nome", rs.getString("nome"));
			map.put("alias", rs.getString("alias"));
			map.put("citta", rs.getString("citta"));
			map.put("cap", rs.getString("cap"));
			map.put("indirizzo", rs.getString("indirizzo"));
			map.put("stato", rs.getString("stato"));
			map.put("ue", String.valueOf(rs.getBoolean("ue")));
		}
		return map;
	}
	
	public HashMap<String, String> getInfoCMRByNumeroSiDest(int numero_cliente) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT nome,ue FROM clienti, paesi WHERE paese=sigla AND numero=?");
		preparedStatement.setInt(1, numero_cliente);
		ResultSet rs = preparedStatement.executeQuery();
		
		HashMap<String, String> map = new HashMap<String, String>();
		while (rs.next()) {
			map.put("nome", rs.getString("nome"));
			map.put("ue", String.valueOf(rs.getBoolean("ue")));
		}
		return map;
	}
	
	public String getEmailByNumeroCliente(int numero_cliente) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT email FROM clienti WHERE numero=?");
		preparedStatement.setInt(1, numero_cliente);
		ResultSet rs = preparedStatement.executeQuery();
		
		String email = "";
		while (rs.next()) {
			email = rs.getString("email");
		}
		
		return email;
	}
	
	public HashMap<Integer, HashMap<Integer, BigDecimal>> getAllFatturati() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT numero,data,importofattura FROM clienti, operazioni WHERE numero=cliente GROUP BY numero,data,importofattura ORDER BY numero,data");
		ResultSet rs = preparedStatement.executeQuery();
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		HashMap<Integer, HashMap<Integer, BigDecimal>> mappa_fatturati_clienti = new HashMap<Integer, HashMap<Integer,BigDecimal>>();
		
		while (rs.next()) {
			Integer numero_cliente = rs.getInt("numero");
			
			String data = format.format(rs.getDate("data"));
			Integer mese = Integer.parseInt(data.split("/")[1]);
			
			if (mappa_fatturati_clienti.containsKey(numero_cliente)) {
				if (mappa_fatturati_clienti.get(numero_cliente).containsKey(mese)) {
					BigDecimal old_value = mappa_fatturati_clienti.get(numero_cliente).get(mese);
					mappa_fatturati_clienti.get(numero_cliente).replace(mese, rs.getBigDecimal("importofattura").add(old_value));
				}
				else {	// la mappa non contiene un valore per questo mese
					mappa_fatturati_clienti.get(numero_cliente).put(mese, rs.getBigDecimal("importofattura"));
				}
			}
			else {	// la mappa non contiene questo numero_cliente
				HashMap<Integer, BigDecimal> innerMap = new HashMap<Integer, BigDecimal>();
				innerMap.put(mese, rs.getBigDecimal("importofattura"));
				mappa_fatturati_clienti.put(numero_cliente, innerMap);
			}
		}
		
		return mappa_fatturati_clienti;
	}
	
	public HashMap<Integer, HashMap<Integer, BigDecimal>> getAllFatturati(Date data_iniziale, Date data_finale) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT numero,data,importofattura FROM clienti, operazioni WHERE numero=cliente AND data>=? AND data<=? GROUP BY numero,data,importofattura ORDER BY numero,data");
		java.sql.Date date_init_temp = new java.sql.Date(data_iniziale.getTime());
		preparedStatement.setString(1, date_init_temp.toString());
		java.sql.Date date_fin_temp = new java.sql.Date(data_finale.getTime());
		preparedStatement.setString(2, date_fin_temp.toString());
		ResultSet rs = preparedStatement.executeQuery();
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		HashMap<Integer, HashMap<Integer, BigDecimal>> mappa_fatturati_clienti = new HashMap<Integer, HashMap<Integer,BigDecimal>>();
		
		while (rs.next()) {
			Integer numero_cliente = rs.getInt("numero");
			
			String data = format.format(rs.getDate("data"));
			Integer mese = Integer.parseInt(data.split("/")[1]);
			
			if (mappa_fatturati_clienti.containsKey(numero_cliente)) {
				if (mappa_fatturati_clienti.get(numero_cliente).containsKey(mese)) {
					BigDecimal old_value = mappa_fatturati_clienti.get(numero_cliente).get(mese);
					mappa_fatturati_clienti.get(numero_cliente).replace(mese, rs.getBigDecimal("importofattura").add(old_value));
				}
				else {	// la mappa non contiene un valore per questo mese
					mappa_fatturati_clienti.get(numero_cliente).put(mese, rs.getBigDecimal("importofattura"));
				}
			}
			else {	// la mappa non contiene questo numero_cliente
				HashMap<Integer, BigDecimal> innerMap = new HashMap<Integer, BigDecimal>();
				innerMap.put(mese, rs.getBigDecimal("importofattura"));
				mappa_fatturati_clienti.put(numero_cliente, innerMap);
			}
		}
		
		return mappa_fatturati_clienti;
	}
	
	public HashMap<Integer, HashMap<Integer, BigDecimal>> getAllFatturati(int numero_cliente, Date data_iniziale, Date data_finale) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT numero,data,importofattura FROM clienti, operazioni WHERE numero=cliente AND numero=? AND data>=? AND data<=? GROUP BY numero,data,importofattura ORDER BY numero,data");
		preparedStatement.setInt(1, numero_cliente);
		java.sql.Date date_init_temp = new java.sql.Date(data_iniziale.getTime());
		preparedStatement.setString(2, date_init_temp.toString());
		java.sql.Date date_fin_temp = new java.sql.Date(data_finale.getTime());
		preparedStatement.setString(3, date_fin_temp.toString());
		ResultSet rs = preparedStatement.executeQuery();
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		HashMap<Integer, HashMap<Integer, BigDecimal>> mappa_fatturati_clienti = new HashMap<Integer, HashMap<Integer,BigDecimal>>();
		
		while (rs.next()) {			
			String data = format.format(rs.getDate("data"));
			Integer mese = Integer.parseInt(data.split("/")[1]);
			
			if (mappa_fatturati_clienti.containsKey(numero_cliente)) {
				if (mappa_fatturati_clienti.get(numero_cliente).containsKey(mese)) {
					BigDecimal old_value = mappa_fatturati_clienti.get(numero_cliente).get(mese);
					mappa_fatturati_clienti.get(numero_cliente).replace(mese, rs.getBigDecimal("importofattura").add(old_value));
				}
				else {	// la mappa non contiene un valore per questo mese
					mappa_fatturati_clienti.get(numero_cliente).put(mese, rs.getBigDecimal("importofattura"));
				}
			}
			else {	// la mappa non contiene questo numero_cliente
				HashMap<Integer, BigDecimal> innerMap = new HashMap<Integer, BigDecimal>();
				innerMap.put(mese, rs.getBigDecimal("importofattura"));
				mappa_fatturati_clienti.put(numero_cliente, innerMap);
			}
		}
		
		return mappa_fatturati_clienti;
	}
	
	public HashMap<Integer, HashMap<Integer, BigDecimal>> getAllFatturatiAl(Date data_finale) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT numero,data,importofattura FROM clienti, operazioni WHERE numero=cliente AND data<=? GROUP BY numero,data,importofattura ORDER BY numero,data");
		preparedStatement.setDate(1, new java.sql.Date(data_finale.getTime()));
		ResultSet rs = preparedStatement.executeQuery();
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		HashMap<Integer, HashMap<Integer, BigDecimal>> mappa_fatturati_clienti = new HashMap<Integer, HashMap<Integer,BigDecimal>>();
		
		while (rs.next()) {
			Integer numero_cliente = rs.getInt("numero");
			
			String data = format.format(rs.getDate("data"));
			Integer mese = Integer.parseInt(data.split("/")[1]);
			
			if (mappa_fatturati_clienti.containsKey(numero_cliente)) {
				if (mappa_fatturati_clienti.get(numero_cliente).containsKey(mese)) {
					BigDecimal old_value = mappa_fatturati_clienti.get(numero_cliente).get(mese);
					mappa_fatturati_clienti.get(numero_cliente).replace(mese, rs.getBigDecimal("importofattura").add(old_value));
				}
				else {	// la mappa non contiene un valore per questo mese
					mappa_fatturati_clienti.get(numero_cliente).put(mese, rs.getBigDecimal("importofattura"));
				}
			}
			else {	// la mappa non contiene questo numero_cliente
				HashMap<Integer, BigDecimal> innerMap = new HashMap<Integer, BigDecimal>();
				innerMap.put(mese, rs.getBigDecimal("importofattura"));
				mappa_fatturati_clienti.put(numero_cliente, innerMap);
			}
		}
		
		return mappa_fatturati_clienti;
	}
	
	public HashMap<Integer, HashMap<Integer, BigDecimal>> getAllFatturatiDal(Date data_finale) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT numero,data,importofattura FROM clienti, operazioni WHERE numero=cliente AND data>=? GROUP BY numero,data,importofattura ORDER BY numero,data");
		preparedStatement.setDate(1, new java.sql.Date(data_finale.getTime()));
		ResultSet rs = preparedStatement.executeQuery();
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		HashMap<Integer, HashMap<Integer, BigDecimal>> mappa_fatturati_clienti = new HashMap<Integer, HashMap<Integer,BigDecimal>>();
		
		while (rs.next()) {
			Integer numero_cliente = rs.getInt("numero");
			
			String data = format.format(rs.getDate("data"));
			Integer mese = Integer.parseInt(data.split("/")[1]);
			
			if (mappa_fatturati_clienti.containsKey(numero_cliente)) {
				if (mappa_fatturati_clienti.get(numero_cliente).containsKey(mese)) {
					BigDecimal old_value = mappa_fatturati_clienti.get(numero_cliente).get(mese);
					mappa_fatturati_clienti.get(numero_cliente).replace(mese, rs.getBigDecimal("importofattura").add(old_value));
				}
				else {	// la mappa non contiene un valore per questo mese
					mappa_fatturati_clienti.get(numero_cliente).put(mese, rs.getBigDecimal("importofattura"));
				}
			}
			else {	// la mappa non contiene questo numero_cliente
				HashMap<Integer, BigDecimal> innerMap = new HashMap<Integer, BigDecimal>();
				innerMap.put(mese, rs.getBigDecimal("importofattura"));
				mappa_fatturati_clienti.put(numero_cliente, innerMap);
			}
		}
		
		return mappa_fatturati_clienti;
	}
	
	public HashMap<Integer, HashMap<Integer, BigDecimal>> getAllFatturati(int numero_cliente) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT data,importofattura FROM clienti, operazioni WHERE numero=cliente AND numero=? GROUP BY numero,data,importofattura ORDER BY numero,data");
		preparedStatement.setInt(1, numero_cliente);
		ResultSet rs = preparedStatement.executeQuery();
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		HashMap<Integer, HashMap<Integer, BigDecimal>> mappa_fatturati_cliente = new HashMap<Integer, HashMap<Integer,BigDecimal>>();
		
		while (rs.next()) {			
			String data = format.format(rs.getDate("data"));
			Integer mese = Integer.parseInt(data.split("/")[1]);
			
			if (mappa_fatturati_cliente.containsKey(numero_cliente)) {
				if (mappa_fatturati_cliente.get(numero_cliente).containsKey(mese)) {
					BigDecimal old_value = mappa_fatturati_cliente.get(numero_cliente).get(mese);
					mappa_fatturati_cliente.get(numero_cliente).replace(mese, rs.getBigDecimal("importofattura").add(old_value));
				}
				else {	// la mappa non contiene un valore per questo mese
					mappa_fatturati_cliente.get(numero_cliente).put(mese, rs.getBigDecimal("importofattura"));
				}
			}
			else {	// la mappa non contiene questo numero_cliente
				HashMap<Integer, BigDecimal> innerMap = new HashMap<Integer, BigDecimal>();
				innerMap.put(mese, rs.getBigDecimal("importofattura"));
				mappa_fatturati_cliente.put(numero_cliente, innerMap);
			}
		}
		
		return mappa_fatturati_cliente;
	}
	
	public HashMap<Integer, HashMap<Integer, BigDecimal>> getAllFatturati(int numero_cliente, Date data_finale) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT data,importofattura FROM clienti, operazioni WHERE numero=cliente AND numero=? AND data<=? GROUP BY numero,data,importofattura ORDER BY numero,data");
		preparedStatement.setInt(1, numero_cliente);
		java.sql.Date date_temp = new java.sql.Date(data_finale.getTime());
		preparedStatement.setString(2, date_temp.toString());
		ResultSet rs = preparedStatement.executeQuery();
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		HashMap<Integer, HashMap<Integer, BigDecimal>> mappa_fatturati_cliente = new HashMap<Integer, HashMap<Integer,BigDecimal>>();
		
		while (rs.next()) {			
			String data = format.format(rs.getDate("data"));
			Integer mese = Integer.parseInt(data.split("/")[1]);
			
			if (mappa_fatturati_cliente.containsKey(numero_cliente)) {
				if (mappa_fatturati_cliente.get(numero_cliente).containsKey(mese)) {
					BigDecimal old_value = mappa_fatturati_cliente.get(numero_cliente).get(mese);
					mappa_fatturati_cliente.get(numero_cliente).replace(mese, rs.getBigDecimal("importofattura").add(old_value));
				}
				else {	// la mappa non contiene un valore per questo mese
					mappa_fatturati_cliente.get(numero_cliente).put(mese, rs.getBigDecimal("importofattura"));
				}
			}
			else {	// la mappa non contiene questo numero_cliente
				HashMap<Integer, BigDecimal> innerMap = new HashMap<Integer, BigDecimal>();
				innerMap.put(mese, rs.getBigDecimal("importofattura"));
				mappa_fatturati_cliente.put(numero_cliente, innerMap);
			}
		}
		
		return mappa_fatturati_cliente;
	}
	
	public HashMap<Integer, HashMap<Integer, BigDecimal>> getAllFatturati(Date data_iniziale, int numero_cliente) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT data,importofattura FROM clienti, operazioni WHERE numero=cliente AND numero=? AND data>=? GROUP BY numero,data,importofattura ORDER BY numero,data");
		preparedStatement.setInt(1, numero_cliente);
		java.sql.Date date_temp = new java.sql.Date(data_iniziale.getTime());
		preparedStatement.setString(2, date_temp.toString());
		ResultSet rs = preparedStatement.executeQuery();
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		HashMap<Integer, HashMap<Integer, BigDecimal>> mappa_fatturati_cliente = new HashMap<Integer, HashMap<Integer,BigDecimal>>();
		
		while (rs.next()) {			
			String data = format.format(rs.getDate("data"));
			Integer mese = Integer.parseInt(data.split("/")[1]);
			
			if (mappa_fatturati_cliente.containsKey(numero_cliente)) {
				if (mappa_fatturati_cliente.get(numero_cliente).containsKey(mese)) {
					BigDecimal old_value = mappa_fatturati_cliente.get(numero_cliente).get(mese);
					mappa_fatturati_cliente.get(numero_cliente).replace(mese, rs.getBigDecimal("importofattura").add(old_value));
				}
				else {	// la mappa non contiene un valore per questo mese
					mappa_fatturati_cliente.get(numero_cliente).put(mese, rs.getBigDecimal("importofattura"));
				}
			}
			else {	// la mappa non contiene questo numero_cliente
				HashMap<Integer, BigDecimal> innerMap = new HashMap<Integer, BigDecimal>();
				innerMap.put(mese, rs.getBigDecimal("importofattura"));
				mappa_fatturati_cliente.put(numero_cliente, innerMap);
			}
		}
		
		return mappa_fatturati_cliente;
	}
	
	public ObservableList<String> getAllCittaAlreadyInserted() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT citta FROM clienti ORDER BY citta");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<String> list_citta = FXCollections.observableArrayList();
		
		while (rs.next()) {
			list_citta.add(rs.getString("citta"));
		}
		
		return list_citta;
	}
	
	public ObservableList<String> getAllCittaDestAlreadyInserted() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT cittadest FROM clienti ORDER BY cittadest");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<String> list_cittadest = FXCollections.observableArrayList();
		
		while (rs.next()) {
			list_cittadest.add(rs.getString("cittadest"));
		}
		
		return list_cittadest;
	}
	
	/*** INSERT QUERY ***/
	public void addCliente(int numero, String nome, String alias, String indirizzo, String cap, String citta, String paese, String email, String destinazione, String indirizzodest, String capdest, String cittadest, String statodest) throws SQLException {
		
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO clienti (numero, nome, alias, indirizzo, cap, citta, paese, email, destinazione, indirizzodest, capdest, cittadest, statodest) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
		preparedStatement.setInt(1, numero);
		preparedStatement.setString(2, nome);
		preparedStatement.setString(3, alias);
		preparedStatement.setString(4, indirizzo);
		preparedStatement.setString(5, cap);
		preparedStatement.setString(6, citta);
		preparedStatement.setString(7, paese);
		preparedStatement.setString(8, email);
		preparedStatement.setString(9, destinazione);
		preparedStatement.setString(10, indirizzodest);
		preparedStatement.setString(11, capdest);
		preparedStatement.setString(12, cittadest);
		preparedStatement.setString(13, statodest);
		
		preparedStatement.executeUpdate();
	}
	
	/*** DELETE QUERY ***/
	public int removeClienteByNumero(int numero) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM clienti WHERE numero=?");
		preparedStatement.setInt(1, numero);
		
		return preparedStatement.executeUpdate();
	}
	
	/*** UPDATE QUERY ***/
	public void updateClienteById(Cliente c) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE clienti SET nome=?, alias=?, indirizzo=?, cap=?, citta=?, paese=?, email=?, destinazione=?, indirizzodest=?, capdest=?, cittadest=?, statodest=? WHERE numero=?");
		preparedStatement.setString(1, c.getNome());
		preparedStatement.setString(2, c.getAlias());
		preparedStatement.setString(3, c.getIndirizzo());
		preparedStatement.setString(4, c.getCap());
		preparedStatement.setString(5, c.getCitta());
		preparedStatement.setString(6, c.getPaese());
		preparedStatement.setString(7, c.getEmail());
		preparedStatement.setString(8, c.getDestinazione());
		preparedStatement.setString(9, c.getIndirizzodest());
		preparedStatement.setString(10, c.getCapdest());
		preparedStatement.setString(11, c.getCittadest());
		preparedStatement.setString(12, c.getStatodest());
		
		preparedStatement.setInt(13, c.getNumero());
		
		preparedStatement.executeUpdate();
	}
}
