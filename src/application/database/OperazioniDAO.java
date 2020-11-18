package application.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import application.Operazione;
import application.OperazioneExtended;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class OperazioniDAO {

Connection connection = null;
	
	public OperazioniDAO() {
		try {
			connection = DBConnector.getConnection();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*** SELECT QUERY ***/	
	public int countOperazioni() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM operazioni");
		ResultSet rs = preparedStatement.executeQuery();
		
		int operazioni = 0;
		while (rs.next()) {
			operazioni = rs.getInt("count");
		}
		return operazioni;
	}
	
	public ObservableList<Operazione> getAllOperazioniOrderById() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM operazioni ORDER BY data DESC");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<Operazione> list_operazioni = FXCollections.observableArrayList();
		
		while (rs.next()) {
			list_operazioni.add(new Operazione(rs.getInt("id"), rs.getDate("data"), rs.getInt("cliente"), rs.getString("numerofattura"), rs.getBigDecimal("importofattura"), rs.getBigDecimal("importobonifico"), rs.getBoolean("esclusodocsdoganali"), rs.getString("note"), rs.getInt("numerocolli"), rs.getString("tipoimballo"), rs.getFloat("pesolordo"), rs.getString("notetrasportatore"), rs.getString("trasportatore"), rs.getBoolean("stampacmr"), rs.getDate("datacarico"), rs.getString("speddoganale"), rs.getString("mrn"), rs.getString("informazioni"), rs.getBoolean("ok"), rs.getString("nomedest"), rs.getString("indirizzodest"), rs.getString("capdest"), rs.getString("cittadest"), rs.getString("statodest")));
		}
		
		return list_operazioni;
	}
	
	public ObservableList<Operazione> getAllOperazioniOrderByIdNoStampaCMR() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT o.* FROM operazioni o, clienti c WHERE c.numero=o.cliente AND stampacmr=? AND numerofattura<>'' ORDER BY c.nome ASC, o.data DESC");
		preparedStatement.setBoolean(1, false);
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<Operazione> list_operazioni = FXCollections.observableArrayList();
		
		while (rs.next()) {			
			list_operazioni.add(new Operazione(rs.getInt("id"), rs.getDate("data"), rs.getInt("cliente"), rs.getString("numerofattura"), rs.getBigDecimal("importofattura"), rs.getBigDecimal("importobonifico"), rs.getBoolean("esclusodocsdoganali"), rs.getString("note"), rs.getInt("numerocolli"), rs.getString("tipoimballo"), rs.getFloat("pesolordo"), rs.getString("notetrasportatore"), rs.getString("trasportatore"), rs.getBoolean("stampacmr"), rs.getDate("datacarico"), rs.getString("speddoganale"), rs.getString("mrn"), rs.getString("informazioni"), rs.getBoolean("ok"), rs.getString("nomedest"), rs.getString("indirizzodest"), rs.getString("capdest"), rs.getString("cittadest"), rs.getString("statodest")));
		}
		
		return list_operazioni;
	}
	
	public ObservableList<Operazione> getAllOperazioniOrderByIdNoStampaCMRFromDate(Date data_iniziale) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM operazioni WHERE stampacmr=? AND data>=? AND numerofattura<>'' ORDER BY data DESC");
		preparedStatement.setBoolean(1, false);
		java.sql.Date date_temp = new java.sql.Date(data_iniziale.getTime());
		preparedStatement.setString(2, date_temp.toString());
		System.out.println(preparedStatement.toString());
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<Operazione> list_operazioni = FXCollections.observableArrayList();
		
		while (rs.next()) {			
			list_operazioni.add(new Operazione(rs.getInt("id"), rs.getDate("data"), rs.getInt("cliente"), rs.getString("numerofattura"), rs.getBigDecimal("importofattura"), rs.getBigDecimal("importobonifico"), rs.getBoolean("esclusodocsdoganali"), rs.getString("note"), rs.getInt("numerocolli"), rs.getString("tipoimballo"), rs.getFloat("pesolordo"), rs.getString("notetrasportatore"), rs.getString("trasportatore"), rs.getBoolean("stampacmr"), rs.getDate("datacarico"), rs.getString("speddoganale"), rs.getString("mrn"), rs.getString("informazioni"), rs.getBoolean("ok"), rs.getString("nomedest"), rs.getString("indirizzodest"), rs.getString("capdest"), rs.getString("cittadest"), rs.getString("statodest")));
		}
		
		return list_operazioni;
	}
	
	public ObservableList<OperazioneExtended> getAllOperazioniOrderByIdNoOkSiStampaCMRSiUE() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT o.*, c.nome AS nomecliente, c.email AS emailcliente FROM operazioni o,clienti c,paesi p WHERE (c.numero=o.cliente AND c.paese=p.sigla) AND p.ue=? AND o.ok=? AND o.stampacmr=? AND o.numerofattura<>'' ORDER BY o.data DESC");
		preparedStatement.setBoolean(1, true);
		preparedStatement.setBoolean(2, false);
		preparedStatement.setBoolean(3, true);
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<OperazioneExtended> list_operazioni = FXCollections.observableArrayList();
		
		while (rs.next()) {			
			list_operazioni.add(new OperazioneExtended(rs.getInt("id"), rs.getDate("data"), rs.getInt("cliente"), rs.getString("numerofattura"), rs.getString("trasportatore"), rs.getDate("datacarico"), rs.getString("speddoganale"), rs.getString("mrn"), rs.getString("informazioni"), rs.getBoolean("ok"), rs.getString("statodest"), rs.getString("nomecliente"), rs.getString("emailcliente")));
		}
		
		return list_operazioni;
	}
	
	public ObservableList<OperazioneExtended> getAllOperazioniOrderByIdNoOkSiStampaCMRNoUE() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT o.*, c.nome AS nomecliente, c.email AS emailcliente FROM operazioni o,clienti c,paesi p WHERE (c.numero=o.cliente AND c.paese=p.sigla) AND p.ue=? AND o.ok=? AND o.stampacmr=? AND o.numerofattura<>'' ORDER BY o.data DESC");
		preparedStatement.setBoolean(1, false);
		preparedStatement.setBoolean(2, false);
		preparedStatement.setBoolean(3, true);
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<OperazioneExtended> list_operazioni = FXCollections.observableArrayList();
		
		while (rs.next()) {			
			list_operazioni.add(new OperazioneExtended(rs.getInt("id"), rs.getDate("data"), rs.getInt("cliente"), rs.getString("numerofattura"), rs.getString("trasportatore"), rs.getDate("datacarico"), rs.getString("speddoganale"), rs.getString("mrn"), rs.getString("informazioni"), rs.getBoolean("ok"), rs.getString("statodest"), rs.getString("nomecliente"), rs.getString("emailcliente")));}
		
		return list_operazioni;
	}
	
	public ObservableList<Operazione> getAllOperazioniByCliente(int numero_cliente) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM operazioni WHERE CAST(cliente AS CHAR) LIKE ? ORDER BY data");
		preparedStatement.setString(1, numero_cliente+"%");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<Operazione> list_operazioni = FXCollections.observableArrayList();
		
		while (rs.next()) {			
			list_operazioni.add(new Operazione(rs.getInt("id"), rs.getDate("data"), rs.getInt("cliente"), rs.getString("numerofattura"), rs.getBigDecimal("importofattura"), rs.getBigDecimal("importobonifico"), rs.getBoolean("esclusodocsdoganali"), rs.getString("note"), rs.getInt("numerocolli"), rs.getString("tipoimballo"), rs.getFloat("pesolordo"), rs.getString("notetrasportatore"), rs.getString("trasportatore"), rs.getBoolean("stampacmr"), rs.getDate("datacarico"), rs.getString("speddoganale"), rs.getString("mrn"), rs.getString("informazioni"), rs.getBoolean("ok"), rs.getString("nomedest"), rs.getString("indirizzodest"), rs.getString("capdest"), rs.getString("cittadest"), rs.getString("statodest")));
		}
		
		return list_operazioni;
	}
	
	public ObservableList<Operazione> getAllOperazioniByTrasportatore(String trasportatore) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM operazioni WHERE trasportatore LIKE ? ORDER BY data");
		preparedStatement.setString(1, trasportatore+"%");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<Operazione> list_operazioni = FXCollections.observableArrayList();
		
		while (rs.next()) {			
			list_operazioni.add(new Operazione(rs.getInt("id"), rs.getDate("data"), rs.getInt("cliente"), rs.getString("numerofattura"), rs.getBigDecimal("importofattura"), rs.getBigDecimal("importobonifico"), rs.getBoolean("esclusodocsdoganali"), rs.getString("note"), rs.getInt("numerocolli"), rs.getString("tipoimballo"), rs.getFloat("pesolordo"), rs.getString("notetrasportatore"), rs.getString("trasportatore"), rs.getBoolean("stampacmr"), rs.getDate("datacarico"), rs.getString("speddoganale"), rs.getString("mrn"), rs.getString("informazioni"), rs.getBoolean("ok"), rs.getString("nomedest"), rs.getString("indirizzodest"), rs.getString("capdest"), rs.getString("cittadest"), rs.getString("statodest")));
		}
		
		return list_operazioni;
	}
	
	public ObservableList<Operazione> getAllOperazioniByNumeroFattura(String numerofattura) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM operazioni WHERE numerofattura LIKE ? ORDER BY data");
		preparedStatement.setString(1, numerofattura+"%");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<Operazione> list_operazioni = FXCollections.observableArrayList();
		
		while (rs.next()) {			
			list_operazioni.add(new Operazione(rs.getInt("id"), rs.getDate("data"), rs.getInt("cliente"), rs.getString("numerofattura"), rs.getBigDecimal("importofattura"), rs.getBigDecimal("importobonifico"), rs.getBoolean("esclusodocsdoganali"), rs.getString("note"), rs.getInt("numerocolli"), rs.getString("tipoimballo"), rs.getFloat("pesolordo"), rs.getString("notetrasportatore"), rs.getString("trasportatore"), rs.getBoolean("stampacmr"), rs.getDate("datacarico"), rs.getString("speddoganale"), rs.getString("mrn"), rs.getString("informazioni"), rs.getBoolean("ok"), rs.getString("nomedest"), rs.getString("indirizzodest"), rs.getString("capdest"), rs.getString("cittadest"), rs.getString("statodest")));
		}
		
		return list_operazioni;
	}
	
	public ObservableList<Operazione> getAllOperazioniByClienteAndTrasportatore(int numero_cliente, String trasportatore) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM operazioni WHERE CAST(cliente AS CHAR) LIKE ? AND trasportatore LIKE ? ORDER BY data");
		preparedStatement.setString(1, numero_cliente+"%");
		preparedStatement.setString(2, trasportatore+"%");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<Operazione> list_operazioni = FXCollections.observableArrayList();
		
		while (rs.next()) {			
			list_operazioni.add(new Operazione(rs.getInt("id"), rs.getDate("data"), rs.getInt("cliente"), rs.getString("numerofattura"), rs.getBigDecimal("importofattura"), rs.getBigDecimal("importobonifico"), rs.getBoolean("esclusodocsdoganali"), rs.getString("note"), rs.getInt("numerocolli"), rs.getString("tipoimballo"), rs.getFloat("pesolordo"), rs.getString("notetrasportatore"), rs.getString("trasportatore"), rs.getBoolean("stampacmr"), rs.getDate("datacarico"), rs.getString("speddoganale"), rs.getString("mrn"), rs.getString("informazioni"), rs.getBoolean("ok"), rs.getString("nomedest"), rs.getString("indirizzodest"), rs.getString("capdest"), rs.getString("cittadest"), rs.getString("statodest")));
		}
		
		return list_operazioni;
	}
	
	public ObservableList<Operazione> getAllOperazioniByTrasportatoreAndNumeroFattura(String trasportatore, String numerofattura) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM operazioni WHERE trasportatore LIKE ? AND numerofattura LIKE ? ORDER BY data");
		preparedStatement.setString(1, trasportatore+"%");
		preparedStatement.setString(2, numerofattura+"%");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<Operazione> list_operazioni = FXCollections.observableArrayList();
		
		while (rs.next()) {			
			list_operazioni.add(new Operazione(rs.getInt("id"), rs.getDate("data"), rs.getInt("cliente"), rs.getString("numerofattura"), rs.getBigDecimal("importofattura"), rs.getBigDecimal("importobonifico"), rs.getBoolean("esclusodocsdoganali"), rs.getString("note"), rs.getInt("numerocolli"), rs.getString("tipoimballo"), rs.getFloat("pesolordo"), rs.getString("notetrasportatore"), rs.getString("trasportatore"), rs.getBoolean("stampacmr"), rs.getDate("datacarico"), rs.getString("speddoganale"), rs.getString("mrn"), rs.getString("informazioni"), rs.getBoolean("ok"), rs.getString("nomedest"), rs.getString("indirizzodest"), rs.getString("capdest"), rs.getString("cittadest"), rs.getString("statodest")));
		}
		
		return list_operazioni;
	}
	
	public ObservableList<Operazione> getAllOperazioniByClienteAndNumeroFattura(int cliente, String numerofattura) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM operazioni WHERE CAST(cliente AS CHAR) LIKE ? AND numerofattura LIKE ? ORDER BY data");
		preparedStatement.setString(1, cliente+"%");
		preparedStatement.setString(2, numerofattura+"%");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<Operazione> list_operazioni = FXCollections.observableArrayList();
		
		while (rs.next()) {			
			list_operazioni.add(new Operazione(rs.getInt("id"), rs.getDate("data"), rs.getInt("cliente"), rs.getString("numerofattura"), rs.getBigDecimal("importofattura"), rs.getBigDecimal("importobonifico"), rs.getBoolean("esclusodocsdoganali"), rs.getString("note"), rs.getInt("numerocolli"), rs.getString("tipoimballo"), rs.getFloat("pesolordo"), rs.getString("notetrasportatore"), rs.getString("trasportatore"), rs.getBoolean("stampacmr"), rs.getDate("datacarico"), rs.getString("speddoganale"), rs.getString("mrn"), rs.getString("informazioni"), rs.getBoolean("ok"), rs.getString("nomedest"), rs.getString("indirizzodest"), rs.getString("capdest"), rs.getString("cittadest"), rs.getString("statodest")));
		}
		
		return list_operazioni;
	}
	
	public ObservableList<Operazione> getAllOperazioniByClienteAndTrasportatoreAndNumeroFattura(int cliente, String trasportatore, String numerofattura) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM operazioni WHERE CAST(cliente AS CHAR) LIKE ? AND trasportatore LIKE ? AND numerofattura LIKE ? ORDER BY data");
		preparedStatement.setString(1, cliente+"%");
		preparedStatement.setString(2, trasportatore+"%");
		preparedStatement.setString(3, numerofattura+"%");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<Operazione> list_operazioni = FXCollections.observableArrayList();
		
		while (rs.next()) {			
			list_operazioni.add(new Operazione(rs.getInt("id"), rs.getDate("data"), rs.getInt("cliente"), rs.getString("numerofattura"), rs.getBigDecimal("importofattura"), rs.getBigDecimal("importobonifico"), rs.getBoolean("esclusodocsdoganali"), rs.getString("note"), rs.getInt("numerocolli"), rs.getString("tipoimballo"), rs.getFloat("pesolordo"), rs.getString("notetrasportatore"), rs.getString("trasportatore"), rs.getBoolean("stampacmr"), rs.getDate("datacarico"), rs.getString("speddoganale"), rs.getString("mrn"), rs.getString("informazioni"), rs.getBoolean("ok"), rs.getString("nomedest"), rs.getString("indirizzodest"), rs.getString("capdest"), rs.getString("cittadest"), rs.getString("statodest")));
		}
		
		return list_operazioni;
	}
	
	public ResultSet getAllOperazioniByFattureSiUE(String ft1, String ft2) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT numerofattura, data, nome, o.statodest AS stato, datacarico, trasportatore, informazioni FROM clienti, paesi, operazioni o WHERE paese=sigla AND ue=? AND o.cliente=numero AND numerofattura>=? AND numerofattura<=? ORDER BY numerofattura,data");
		preparedStatement.setBoolean(1, true);
		preparedStatement.setString(2, ft1);
		preparedStatement.setString(3, ft2);
		ResultSet rs = preparedStatement.executeQuery();
		return rs;
	}
	
	public ResultSet getAllOperazioniByFattureNoUE(String ft1, String ft2) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT numerofattura, data, nome, o.statodest AS stato, datacarico, trasportatore, speddoganale, mrn, informazioni FROM clienti, paesi, operazioni o WHERE paese=sigla AND ue=? AND o.cliente=numero AND numerofattura>=? AND numerofattura<=? ORDER BY numerofattura,data");
		preparedStatement.setBoolean(1, false);
		preparedStatement.setString(2, ft1);
		preparedStatement.setString(3, ft2);
		ResultSet rs = preparedStatement.executeQuery();
		return rs;
	}
	
	public ResultSet getAllOperazioniNoUESollecitabili() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT numerofattura, data, nome, o.statodest AS stato, datacarico, trasportatore, speddoganale, informazioni FROM clienti, paesi, operazioni o WHERE paese=sigla AND ue=? AND o.cliente=numero ORDER BY datacarico");
		preparedStatement.setBoolean(1, false);
		ResultSet rs = preparedStatement.executeQuery();
		return rs;
	}
	
	public ObservableList<String> getAllSpedDoganali() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT speddoganale FROM operazioni ORDER BY speddoganale");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<String> list_speddoganali = FXCollections.observableArrayList();
		
		while (rs.next()) {			
			list_speddoganali.add(rs.getString("speddoganale"));
		}
		
		return list_speddoganali;
	}
	
	/*** INSERT QUERY ***/
	public void addOperazione(Date data, Integer cliente, String numerofattura, BigDecimal importofattura, BigDecimal importobonifico, boolean esclusodocsdoganali, String note, int numerocolli, String tipoimballo, float pesolordo, String notetrasportatore, String trasportatore, boolean stampacmr, Date datacarico, String speddoganale, String mrn, String informazioni, boolean ok, String nomedest, String indirizzodest, String capdest, String cittadest, String statodest) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO operazioni (data, cliente, numerofattura, importofattura, importobonifico, esclusodocsdoganali, note, numerocolli, tipoimballo, pesolordo, notetrasportatore, trasportatore, stampacmr, datacarico, speddoganale, mrn, informazioni, ok, nomedest, indirizzodest, capdest, cittadest, statodest) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		//preparedStatement.setInt(1, id);
		preparedStatement.setDate(1, new java.sql.Date(data.getTime()));
		preparedStatement.setInt(2, cliente);
		preparedStatement.setString(3, numerofattura);
		preparedStatement.setBigDecimal(4, importofattura);
		preparedStatement.setBigDecimal(5, importobonifico);
		preparedStatement.setBoolean(6, esclusodocsdoganali);
		preparedStatement.setString(7, note);
		preparedStatement.setInt(8, numerocolli);
		preparedStatement.setString(9, tipoimballo);
		preparedStatement.setFloat(10, pesolordo);
		preparedStatement.setString(11, notetrasportatore);
		preparedStatement.setString(12, trasportatore);
		preparedStatement.setBoolean(13, stampacmr);
		preparedStatement.setDate(14, new java.sql.Date(datacarico.getTime()));
		preparedStatement.setString(15, speddoganale);
		preparedStatement.setString(16, mrn);
		preparedStatement.setString(17, informazioni);
		preparedStatement.setBoolean(18, ok);
		preparedStatement.setString(19, nomedest);
		preparedStatement.setString(20, indirizzodest);
		preparedStatement.setString(21, capdest);
		preparedStatement.setString(22, cittadest);
		preparedStatement.setString(23, statodest);
		
		preparedStatement.executeUpdate();
	}
	
	/*** DELETE QUERY ***/
	public int removeOperazioneById(Integer id) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM operazioni WHERE id=?");
		preparedStatement.setInt(1, id);
		
		return preparedStatement.executeUpdate();
	}
	
	/*** UPDATE QUERY ***/
	public void updateOperazioneById(Operazione o) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE operazioni SET data=?, cliente=?, numerofattura=?, importofattura=?, importobonifico=?, esclusodocsdoganali=?, note=?, numerocolli=?, tipoimballo=?, pesolordo=?, notetrasportatore=?, trasportatore=?, stampacmr=?, datacarico=?, speddoganale=?, mrn=?, informazioni=?, ok=?, nomedest=?, indirizzodest=?, capdest=?, cittadest=?, statodest=? WHERE id=?");
		preparedStatement.setDate(1, new java.sql.Date(o.getData().getTime()));
		preparedStatement.setInt(2, o.getCliente());
		preparedStatement.setString(3, o.getNumerofattura());
		preparedStatement.setBigDecimal(4, o.getImportofattura());
		preparedStatement.setBigDecimal(5, o.getImportobonifico());
		preparedStatement.setBoolean(6, o.isEsclusodocsdoganali());
		preparedStatement.setString(7, o.getNote());
		preparedStatement.setInt(8, o.getNumerocolli());
		preparedStatement.setString(9, o.getTipoimballo());
		preparedStatement.setFloat(10, o.getPesolordo());
		preparedStatement.setString(11, o.getNotetrasportatore());
		preparedStatement.setString(12, o.getTrasportatore());
		preparedStatement.setBoolean(13, o.isStampacmr());
		preparedStatement.setDate(14, new java.sql.Date(o.getDatacarico().getTime()));
		preparedStatement.setString(15, o.getSpeddoganale());
		preparedStatement.setString(16, o.getMrn());
		preparedStatement.setString(17, o.getInformazioni());
		preparedStatement.setBoolean(18, o.isOk());
		preparedStatement.setString(19, o.getNomedest());
		preparedStatement.setString(20, o.getIndirizzodest());
		preparedStatement.setString(21, o.getCapdest());
		preparedStatement.setString(22, o.getCittadest());
		preparedStatement.setString(23, o.getStatodest());
		
		preparedStatement.setInt(24, o.getId());
		
		preparedStatement.executeUpdate();
	}
	
	public void setStampaCMRById(int id, boolean stampa_cmr) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE operazioni SET stampacmr=? WHERE id=?");
		preparedStatement.setBoolean(1, stampa_cmr);
		preparedStatement.setInt(2, id);
		preparedStatement.executeUpdate();
	}
	
	public void setOkById(int id, boolean ok) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE operazioni SET ok=? WHERE id=?");
		preparedStatement.setBoolean(1, ok);
		preparedStatement.setInt(2, id);
		preparedStatement.executeUpdate();
	}
	
	public void updateInformazioniById(int id, String informazioni) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE operazioni SET informazioni=? WHERE id=?");
		preparedStatement.setString(1, informazioni);
		preparedStatement.setInt(2, id);
		preparedStatement.executeUpdate();
	}
	
	public void updateEmailById(int id, String email) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE operazioni SET email=? WHERE id=?");
		preparedStatement.setString(1, email);
		preparedStatement.setInt(2, id);
		preparedStatement.executeUpdate();
	}
	
	public void updateSpedDoganaleById(int id, String speddoganale) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE operazioni SET speddoganale=? WHERE id=?");
		preparedStatement.setString(1, speddoganale);
		preparedStatement.setInt(2, id);
		preparedStatement.executeUpdate();
	}
	
	public void updateMrnById(int id, String mrn) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE operazioni SET mrn=? WHERE id=?");
		preparedStatement.setString(1, mrn);
		preparedStatement.setInt(2, id);
		preparedStatement.executeUpdate();
	}
}
