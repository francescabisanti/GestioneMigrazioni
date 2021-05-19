package it.polito.tdp.borders.model;

public class Evento implements Comparable <Evento> {
	//di quanti tipi di evento ho bisogno???
	//posso prevedere l'evento di "n persone arrivano in uno stato"
	//quando succede, poi genero altri eventi
	
	private int t; //attributo su cui ordino gli eventi
	private Country country;
	private int n; //numero di persone arrivate al tempo t nello stato country
	
	
	public Evento(int t, Country country, int n) {
		super();
		this.t = t;
		this.country = country;
		this.n = n;
	}


	public int getT() {
		return t;
	}


	public void setT(int t) {
		this.t = t;
	}


	public Country getCountry() {
		return country;
	}


	public void setCountry(Country country) {
		this.country = country;
	}


	public int getN() {
		return n;
	}


	public void setN(int n) {
		this.n = n;
	}


	@Override
	public int compareTo(Evento o) {
		
		return this.t-o.t;
	}
	
	

}
