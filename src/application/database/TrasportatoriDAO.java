package application.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import application.Trasportatore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TrasportatoriDAO {

Connection connection = null;
	
	public TrasportatoriDAO() {
		try {
			connection = DBConnector.getConnection();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*** SELECT QUERY ***/
	public int countTrasportatori() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM trasportatori");
		ResultSet rs = preparedStatement.executeQuery();
		
		int trasportatori = 0;
		while (rs.next()) {
			trasportatori = rs.getInt("count");
		}
		return trasportatori;
	}
	
	public ObservableList<Trasportatore> getAllTrasportatoriOrderByTrasportatore() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM trasportatori ORDER BY trasportatore");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<Trasportatore> list_trasportatori = FXCollections.observableArrayList();
		
		while (rs.next()) {
			list_trasportatori.add(new Trasportatore(rs.getString("trasportatore"), rs.getString("nome"), rs.getString("indirizzo"), rs.getString("cap"), rs.getString("citta"), rs.getString("paese"), rs.getString("stato"), rs.getString("partitaiva"), rs.getString("iscrizionealbo"), rs.getString("mail1ritiro"), rs.getString("mail2ritiro"), rs.getString("mail1docs"), rs.getString("mail2docs"), rs.getString("note")));
		}
		
		return list_trasportatori;
	}
	
	public ObservableList<String> getAllTrasportatoriTrasportatori() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT trasportatore FROM trasportatori ORDER BY trasportatore");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<String> list_trasportatori = FXCollections.observableArrayList();
		
		while (rs.next()) {
			list_trasportatori.add(rs.getString("trasportatore"));
		}
		
		return list_trasportatori;
	}
	
	public String getNoteByTrasportatore(String trasportatore) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT note FROM trasportatori WHERE trasportatore=?");
		preparedStatement.setString(1, trasportatore);
		ResultSet rs = preparedStatement.executeQuery();
		
		String note = null;
		
		while (rs.next()) {
			note = rs.getString("note");
		}
		
		return note;
	}
	
	public HashMap<String, String> getInfoCMRByTrasportatore(String trasportatore) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT nome,indirizzo,cap,citta,stato,partitaiva FROM trasportatori WHERE trasportatore=?");
		preparedStatement.setString(1, trasportatore);
		ResultSet rs = preparedStatement.executeQuery();
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		while (rs.next()) {
			map.put("nome", rs.getString("nome"));
			map.put("indirizzo", rs.getString("indirizzo"));
			map.put("cap", rs.getString("cap"));
			map.put("citta", rs.getString("citta"));
			map.put("stato", rs.getString("stato"));
			map.put("partitaiva", rs.getString("partitaiva"));
		}
		
		return map;
	}
	
	/*** INSERT QUERY ***/
	public void addTrasportatore(String trasportatore, String nome, String indirizzo, String cap, String citta, String paese, String stato, String partitaiva, String iscrizionealbo, String mail1ritiro, String mail2ritiro, String mail1docs, String mail2docs, String note) throws SQLException {
		
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO trasportatori (trasportatore, nome, indirizzo, cap, citta, paese, stato, partitaiva, iscrizionealbo, mail1ritiro, mail2ritiro, mail1docs, mail2docs, note) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		preparedStatement.setString(1, trasportatore);
		preparedStatement.setString(2, nome);
		preparedStatement.setString(3, indirizzo);
		preparedStatement.setString(4, cap);
		preparedStatement.setString(5, citta);
		preparedStatement.setString(6, paese);
		preparedStatement.setString(7, stato);
		preparedStatement.setString(8, partitaiva);
		preparedStatement.setString(9, iscrizionealbo);
		preparedStatement.setString(10, mail1ritiro);
		preparedStatement.setString(11, mail2ritiro);
		preparedStatement.setString(12, mail1docs);
		preparedStatement.setString(13, mail2docs);
		preparedStatement.setString(14, note);
		
		preparedStatement.executeUpdate();
	}
	
	/*** DELETE QUERY ***/
	public int removeTrasportatoreByTrasportatore(String trasportatore) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM trasportatori WHERE trasportatore=?");
		preparedStatement.setString(1, trasportatore);
		
		return preparedStatement.executeUpdate();
	}
	
	/*** UPDATE QUERY ***/
	public void updateTrasportatoreByTrasportatore(Trasportatore t) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE trasportatori SET nome=?, indirizzo=?, cap=?, citta=?, paese=?, stato=?, partitaiva=?, iscrizionealbo=?, mail1ritiro=?, mail2ritiro=?, mail1docs=?, mail2docs=?, note=? WHERE trasportatore=?");
		preparedStatement.setString(1, t.getNome());
		preparedStatement.setString(2, t.getIndirizzo());
		preparedStatement.setString(3, t.getCap());
		preparedStatement.setString(4, t.getCitta());
		preparedStatement.setString(5, t.getPaese());
		preparedStatement.setString(6, t.getStato());
		preparedStatement.setString(7, t.getPartitaiva());
		preparedStatement.setString(8, t.getIscrizionealbo());
		preparedStatement.setString(9, t.getMail1ritiro());
		preparedStatement.setString(10, t.getMail2ritiro());
		preparedStatement.setString(11, t.getMail1docs());
		preparedStatement.setString(12, t.getMail2docs());
		preparedStatement.setString(13, t.getNote());
		
		preparedStatement.setString(14, t.getTrasportatore());
		
		preparedStatement.executeUpdate();
	}
}
