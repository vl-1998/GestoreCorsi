package it.polito.tdp.corsi.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.corsi.db.CorsoDAO;

public class Model {
	private CorsoDAO dao;
	
	public Model() {
		dao = new CorsoDAO();
	}

	public List <Corso> getCorsiByPeriodo (Integer pd){
		return dao.getCorsiByPeriodo(pd);
	}
	
	public Map <Corso, Integer> getIscrittiByPeriodo (Integer pd){
		return dao.getIscrittiByPeriodo(pd);
	}
	
	public List <Studente> getStudentiByCorso (Corso corso){
		return dao.getStudentiByCorso(corso);
	}
	
	public boolean esisteCorso (String codins) {
		return dao.esisteCorso(codins);
	}
	
	
	/*
	//PRIMO MODO DI SVOLGERE L'ULTIMO PUNTO, SENZA PASSARE DALLA QUERY
	public Map <String, Integer> getDivisioneCDS (Corso c){
		List <Studente> studenti = dao.getStudentiByCorso(c);
		
		Map <String, Integer> statistiche = new HashMap <String,Integer>(); //Comodo perche le chiavi nelle hashmap sono univoche
		
		//la scorro per estrarre le statistiche di cui ho bisogno
		for (Studente s :studenti) {
			
			//modifica della mappa di statistiche solo se il codice del corso esiste
				if (s.getCDS()!=null && !s.getCDS().equals("")) {
					if (statistiche.containsKey(s.getCDS())){
					statistiche.put(s.getCDS(), statistiche.get(s.getCDS())+1); //ogni volta che incontro il corso di studi incremento il numero di studenti
				} else { //se non esisteva creo la statistica per questo corso di studi
					statistiche.put(s.getCDS(), 1);
				}
			}
		}
		
		return statistiche;
	}*/
	
	//SECONDO MODO DI SVOLGERE L'ULTIMO PUNTO PASSANDO DALLA QUERY
	public Map <String, Integer> getDivisioneCDS (Corso c){
		return dao.getDivisioneCDS(c);
	}
	
	
}
