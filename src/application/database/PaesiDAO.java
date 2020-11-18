package application.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Paese;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PaesiDAO {

Connection connection = null;
	
	public PaesiDAO() {
		try {
			connection = DBConnector.getConnection();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*** SELECT QUERY ***/
	public int countPaesi() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM paesi");
		ResultSet rs = preparedStatement.executeQuery();
		
		int paesi = 0;
		while (rs.next()) {
			paesi = rs.getInt("count");
		}
		return paesi;
	}
	
	public ObservableList<Paese> getAllPaesiOrderBySigla() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM paesi ORDER BY sigla");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<Paese> list_paesi = FXCollections.observableArrayList();
		
		while (rs.next()) {
			list_paesi.add(new Paese(rs.getString("sigla"), rs.getString("stato"), rs.getBoolean("ue")));
		}
		
		return list_paesi;
	}
	
	public ObservableList<String> getAllSiglePaesi() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT sigla FROM paesi ORDER BY sigla");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<String> list_sigle = FXCollections.observableArrayList();
		
		while (rs.next()) {
			list_sigle.add(rs.getString("sigla"));
		}
		
		return list_sigle;
	}
	
	public ObservableList<String> getAllStatoPaesi() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT stato FROM paesi ORDER BY stato");
		ResultSet rs = preparedStatement.executeQuery();
		
		ObservableList<String> list_stati = FXCollections.observableArrayList();
		
		while (rs.next()) {
			list_stati.add(rs.getString("stato"));
		}
		
		return list_stati;
	}
	
	public String getStatoBySigla(String sigla) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT stato FROM paesi WHERE sigla=?");
		preparedStatement.setString(1, sigla);
		ResultSet rs = preparedStatement.executeQuery();
		
		String stato = null;
		
		while (rs.next()) {
			stato = rs.getString("stato");
		}
		
		return stato;
	}
	
	/*** INSERT QUERY ***/
	public void addPaese(String sigla, String stato, Boolean ue) throws SQLException {
		
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO paesi (sigla, stato, ue) VALUES (?,?,?)");
		preparedStatement.setString(1, sigla);
		preparedStatement.setString(2, stato);
		preparedStatement.setBoolean(3, ue);
		
		preparedStatement.executeUpdate();
	}
	
	/*** DELETE QUERY ***/
	public int removePaeseBySigla(String sigla) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM paesi WHERE sigla=?");
		preparedStatement.setString(1, sigla);
		
		return preparedStatement.executeUpdate();
	}
	
	/*** UPDATE QUERY ***/
	public void updatePaeseBySigla(Paese p) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE paesi SET stato=?, ue=? WHERE sigla=?");
		preparedStatement.setString(1, p.getStato());
		preparedStatement.setBoolean(2, p.isUe());
		preparedStatement.setString(3, p.getSigla());
		
		preparedStatement.executeUpdate();
	}
}
