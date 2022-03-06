package com.example.demo.domainobject;

public class Studente {
	public long matricola;
	public String nome;
	public String cognome;
	
	
	
	public Studente() {
		super();
	}



	public Studente(long matricola, String nome, String cognome) {
		super();
		this.matricola = matricola;
		this.nome = nome;
		this.cognome = cognome;
	}



	public long getMatricola() {
		return matricola;
	}



	public void setMatricola(long matricola) {
		this.matricola = matricola;
	}



	public String getNome() {
		return nome;
	}



	public void setNome(String nome) {
		this.nome = nome;
	}



	public String getCognome() {
		return cognome;
	}



	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	
	
	
	
}	
