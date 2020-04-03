package it.polito.tdp.corsi.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.corsi.model.Corso;
import it.polito.tdp.corsi.model.Studente;

public class CorsoDAO {
	//Definisco un metodo che mi permetta di controllare se un corso esiste o non esiste
	public boolean esisteCorso (String codins) {
		String sql = "select * from corso where codins = ?"; 

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, codins);
			ResultSet rs = st.executeQuery();

			//ATTENZIONE CON DUE RETURN, 2 CONNECTION.CLOSE
			if (rs.next()) { //c'è un valore nel result set ritorna true
				conn.close();
				return true;
			}
			else {
				conn.close();
				return false;
			}
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//deve fornire il metodo per recuperare i corsi di un determinato periodo didattico
	
	public List <Corso> getCorsiByPeriodo (Integer pd) {
		String sql = "select * from corso where pd = ?"; //place older che andremo a riempire tramite metodo e no tramite la concatenazione di stringhe
		List <Corso> result = new ArrayList <>();
		
		try {
			//ci facciamo dare una connection
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, pd);
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				//nel corso estraggo i campi di un corso
				Corso c = new Corso (rs.getString("codins"), rs.getInt("crediti"), rs.getString("nome"), rs.getInt("pd"));
				result.add(c);
			}
			
			conn.close(); //CHIUDERE LA CONNESSIONE
			
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public Map <Corso, Integer> getIscrittiByPeriodo (Integer pd){
		//per non sbagliare la group by, prendo tutti i campi che ho messo prima della clausola di aggregazione e li metto nella GROUP BY
		String sql= "select c.codins, c.nome, c.crediti, c.pd, COUNT(*) as tot from corso as c, iscrizione i where c.codins = i.codins and c.pd = ? group by c.codins, c.nome, c.crediti, c.pd ";
		Map <Corso, Integer> result = new HashMap <Corso, Integer>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, pd);
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				Corso c = new Corso (rs.getString("codins"), rs.getInt("crediti"), rs.getString("nome"), rs.getInt("pd"));
				Integer n = rs.getInt("tot");
				result.put(c, n);
			}
			
			conn.close();
			
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
		
	}
	
	//due possibilita, o il codice gia modellato come parametro o l'oggetto corso riempito solo nel codice
	public List <Studente> getStudentiByCorso (Corso corso){
		String sql = "select s.matricola, s.nome, s.cognome, s.CDS " + 
				"from studente as s, iscrizione as i " + 
				"where s.matricola=i.matricola and i.codins= ?";
		
		List <Studente> studenti = new LinkedList <Studente>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, corso.getCodins());
			
			ResultSet rs=st.executeQuery();
			
			while (rs.next()) {
				Studente s= new Studente (rs.getInt("matricola"), rs.getString("nome"), rs.getString("cognome"), rs.getString("CDS"));
				studenti.add(s);
			}
			conn.close();
			
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return studenti;
	}
	
	public Map <String, Integer> getDivisioneCDS (Corso c){
		String sql = "select s.CDS, COUNT(*) as tot " + 
				"from studente as s, iscrizione as i " + 
				"where s.matricola=i.matricola and s.cds <> \"\" and i.codins= ? " + 
				"group by s.CDS";
		Map <String, Integer> statistiche = new HashMap <String,Integer>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, c.getCodins());

			ResultSet rs=st.executeQuery();
			
			while (rs.next()) {
				statistiche.put(rs.getString("CDS"), rs.getInt("tot")); //tot è il risultato della funzione di aggregazione
			}
			conn.close();
			
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return statistiche;
	}


}
