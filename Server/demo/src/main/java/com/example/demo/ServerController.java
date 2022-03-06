package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domainobject.Classe;
import com.example.demo.domainobject.Studente;
import com.example.demo.domainobject.ValutazioneStudente;

@RestController
@RequestMapping(path = "androidAppServer")
public class ServerController {
	@Autowired
	JdbcTemplate queryObject;
	
	@RequestMapping
	public String metodo() {
		System.out.println("something arrived");
		return "Ciao";
	}
	
	@RequestMapping("classiDocente")
	public List<Classe> classiDocente(@RequestParam(name = "idDocente") long idDocente) {
		System.out.println("Id docente inviato: " + idDocente);
		return queryObject.query("SELECT DISTINCT C.*\r\n"
				+ "From classe as C,insegnamento as I,docente as D\r\n"
				+ "Where D.matricola = I.id_docente\r\n"
				+ "And I.id_classe = C.id\r\n"
				+ "And D.matricola = ?",new Object[]{idDocente},(resultSet,rowNum)->{
					Classe classe = new Classe();
					classe.setId(resultSet.getLong("id"));
					classe.setNomeClasse(resultSet.getString("nome_classe"));
					classe.setLatitudine(resultSet.getDouble("latitudine"));
					classe.setLongitudine(resultSet.getDouble("longitudine"));
					return classe;				
				});
	}
	
	
	@RequestMapping("autentica")
	public Long autentica(@RequestParam String user,@RequestParam String password) {
		System.out.println("Autenticazione: " + user + " " + password);
		try{
			return queryObject.queryForObject("SELECT D.matricola\r\n"
				+ "From docente as D\r\n"
				+ "WHERE D.user_id = ?\r\n"
				+ "AND D.password = PASSWORD(?)\r\n",new Object[] {user,password},Long.class);
		}catch(Exception e) {
			throw e;
		}
	}
	
	
	@RequestMapping("studentiClasse")
	public List<Studente> studentiClasse(@RequestParam long idClasse){
		return queryObject.query("SELECT *\r\n"
				+ "From studente\r\n"
				+ "Where id_classe = ?", new Object[] {idClasse}, (resultSet,numRow)->{
					Studente studente = new Studente();
					studente.setMatricola(resultSet.getLong("matricola"));
					studente.setNome(resultSet.getString("nome"));
					studente.setCognome(resultSet.getString("cognome"));
					return studente;
				});
	}
	
	@RequestMapping("valutazioniStudente")
	public List<ValutazioneStudente> valutazioniStudente(@RequestParam long idStudente,@RequestParam int quadrimestre, @RequestParam(required = false) Long idDocente){
		
		List<ValutazioneStudente> s = null;
		if(idDocente != null) {
			s = getValutazioniDelDocente(idStudente, quadrimestre,idDocente);
		}else {
			s = getTutteValutazioniStudente(idStudente, quadrimestre);
		}
	
		
		for(ValutazioneStudente valutazione : s) {
			List<ValutazioneStudente.IndicatoriValori> listaIndicatoriValutazione = queryObject.query("SELECT V.id,I.nome_indicatore,V.valore\r\n"
					+ "FROM valori_valutazione as V,indicatore as I\r\n"
					+ "where V.id_indicatore  = I.id\r\n"
					+ "And V.id_valutazione = ?", new Object[] {valutazione.getIdValutazione()},(resultSet,numRow)->{
						ValutazioneStudente.IndicatoriValori t = new ValutazioneStudente.IndicatoriValori();
						t.setIdIndicatoreValore(resultSet.getLong("id"));
						t.setNomeIndicatore(resultSet.getString("nome_indicatore"));
						t.setValore(resultSet.getInt("valore"));
						return t;
					});
			valutazione.setIndicatori(listaIndicatoriValutazione);
		}
		return s;
	}

	private List<ValutazioneStudente> getValutazioniDelDocente(long idStudente, int quadrimestre,Long idDocente) {
		List<ValutazioneStudente> s = queryObject.query("SELECT V.id,I.nome_insegnamento\r\n"
				+ "From valutazione as V, insegnamento as I\r\n"
				+ "Where V.id_insegnamento = I.id \r\n"
				+ "AND I.id_docente = ?\r\n"
				+ "AND V.id_studente = ?\r\n"
				+ "AND V.quadrimestre = ?", new Object[] {idDocente,idStudente,quadrimestre},(resultSet,numRow)->{
					ValutazioneStudente eval = new ValutazioneStudente();
					eval.setIdValutazione(resultSet.getLong("id"));
					eval.setNomeInsegnamento(resultSet.getString("nome_insegnamento"));
					return eval;
				});
		return s;
	}
	
	private List<ValutazioneStudente> getTutteValutazioniStudente(long idStudente, int quadrimestre) {
		List<ValutazioneStudente> s = queryObject.query("SELECT V.id,I.nome_insegnamento\r\n"
				+ "From valutazione as V, insegnamento as I\r\n"
				+ "Where V.id_insegnamento = I.id \r\n"
				+ "AND V.id_studente = ?\r\n"
				+ "AND V.quadrimestre = ?", new Object[] {idStudente,quadrimestre},(resultSet,numRow)->{
					ValutazioneStudente eval = new ValutazioneStudente();
					eval.setIdValutazione(resultSet.getLong("id"));
					eval.setNomeInsegnamento(resultSet.getString("nome_insegnamento"));
					return eval;
				});
		return s;
	}
	
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE,value = "updateValutazioni")
	public boolean updateValoreIndicatoreValutazione(@RequestBody(required = true) Map<Integer,Long> indicatori) {
		try {
			indicatori.forEach((key,value)->{
				queryObject.update("UPDATE valori_valutazione SET valore = ? Where id = ?",value,key);
			});
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@RequestMapping("mediaComplessivaStudentiClasse")
	public List<Map<String,Object>> mediaComplessivaStudentiClasse(@RequestParam(required = true) Long idClasse){
		
		List<Map<String,Object> > result = queryObject.query("SELECT matricola,AVG(mediaInsegnamenti) as mediaComplessiva\r\n"
				+ "FROM (SELECT matricola,id_insegnamento,id_valutazione,AVG(valore) as mediaInsegnamenti\r\n"
				+ "	FROM studente as S, valutazione as V,valori_valutazione as C\r\n"
				+ "	where S.id_classe = ?\r\n"
				+ "	AND V.id_studente = S.matricola\r\n"
				+ "	AND V.quadrimestre = 0\r\n"
				+ "	AND C.id_valutazione = V.id\r\n"
				+ "	Group By V.id) as T\r\n"
				+ "GROUP By matricola", new Object[] {idClasse}, (resultSet,numRow)->{
					HashMap<String,Object> m =  new HashMap<>();
					m.put("matricola", resultSet.getLong("matricola"));
					m.put("mediaComplessiva", resultSet.getDouble("mediaComplessiva"));
					return m;
				});
		return result;
	}
	
	@RequestMapping("medieInsegnamenti")
	public List<Map<String,Object>> medieInsegnamenti(@RequestParam(required = true) Long idClasse){
		
		List<Map<String,Object> > result = queryObject.query("SELECT matricola,AVG(mediaInsegnamenti) as mediaComplessiva\r\n"
				+ "FROM (SELECT matricola,id_insegnamento,id_valutazione,AVG(valore) as mediaInsegnamenti\r\n"
				+ "	FROM studente as S, valutazione as V,valori_valutazione as C\r\n"
				+ "	where S.id_classe = ?\r\n"
				+ "	AND V.id_studente = S.matricola\r\n"
				+ "	AND V.quadrimestre = 0\r\n"
				+ "	AND C.id_valutazione = V.id\r\n"
				+ "	Group By V.id) as T\r\n"
				+ "GROUP By matricola", new Object[] {idClasse}, (resultSet,numRow)->{
					HashMap<String,Object> m =  new HashMap<>();
					m.put("matricola", resultSet.getLong("matricola"));
					m.put("mediaComplessiva", resultSet.getDouble("mediaComplessiva"));
					return m;
				});
		return result;
	}
}
