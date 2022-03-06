package com.example.demo.domainobject;

public class Classe {
	public long id;
	public String nomeClasse;
	public double longitudine;
	public double latitudine;

	
	
	
	public Classe() {
		super();
	}
	
	
	public Classe(long id, String nomeClasse, double longitudine, double latitudine) {
		super();
		this.id = id;
		this.nomeClasse = nomeClasse;
		this.longitudine = longitudine;
		this.latitudine = latitudine;
	}


	public String getNomeClasse() {
		return nomeClasse;
	}
	public void setNomeClasse(String nomeClasse) {
		this.nomeClasse = nomeClasse;
	}
	public double getLatitudine() {
		return latitudine;
	}
	public void setLatitudine(double latitudine) {
		this.latitudine = latitudine;
	}
	public double getLongitudine() {
		return longitudine;
	}
	public void setLongitudine(double longitudine) {
		this.longitudine = longitudine;
	}
	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	@Override
	public String toString() {
		return "Classe [id=" + id + ", nomeClasse=" + nomeClasse + ", longitudine=" + longitudine + ", latitudine="
				+ latitudine + "]";
	}


	
	
}
