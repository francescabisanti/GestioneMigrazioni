package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulatore {
	//Modello --> qual è lo stato del sistema ad ogni passo
	private Graph <Country, DefaultEdge> grafo; //mi serve durante la simulazione
	
	
	//tipi di evento --> coda Prioritaria
	private PriorityQueue <Evento> queue;
	//è fondamentale se aggiungo degli eventi mentre sto simulando
	//nel rivers il numero di eventi era fisso, nomn ne aggiungevo altri, 
	//avevo le date e scorrevo quelli
	//qua ogni volta che le persone si muovono, devo generare nuovi eventi
	
	
	
	// Parametri della simulazione
	private int N_MIGRANTI=1000;
	private Country partenza;
	
	//valori in output
	
	private int T=-1; //lo imposto inizialmente a meno1, è il numero di passi
	//private List <CountryAndNumber>stanziali;
	private Map <Country, Integer> stanziali;
	
	//devo inizializzare il simulatore
	public void init(Country country, Graph<Country, DefaultEdge>grafo) {
		this.partenza=country; //inizio con quello che ci passano da fxml
		
		this.grafo=grafo;
		
		//devo ora impostare lo stato iniziale
		this.T=1; //da meno uno lo metto al primo tempo.
		//this.stanziali=new LinkedList <CountryAndNumber>();
		//mi immagino che durante la simulazione debba modificare il numero di persone
		//stanziali di uno stato, che possono anche tornare indietro
		//soluzione--> mappa perchè devo avere un metodo veloce per recuperare il paese e modificare il numero di stanziali.
		this.stanziali= new HashMap <>();
		
		for(Country c:this.grafo.vertexSet()) {
			stanziali.put(c, 0);
			//Inizialmente inizializzo tutti i paesi con il numero di stanziali a 0
		}
		//creo la coda
		this.queue= new PriorityQueue<Evento>();
		//inserisco il primo EVENTO
		this.queue.add(new Evento(T,//tempo è uno
				partenza, //lo stato
				N_MIGRANTI)//Nel primo evento ho che nello stato selezionato arrivano 1000 migranti
				);
	}
	
	public void run() {
		//finchè la coda non si svuota -->
		//prendo un evento per volta e lo eseguo
		Evento e;
		while((e=this.queue.poll())!=null) {
			//simulo l'evento e
			
			//devo avere sempre il giusto T
			this.T=e.getT();
			
			//mi recupero il numero di persone
			int nPersone=e.getN();
			//anche lo Stato
			Country stato=e.getCountry();
			//Devo recuperarmi i vicini, dove le persone si sposteranno
			List<Country> vicini= Graphs.neighborListOf(this.grafo, stato);
			
			//il numero di persone che si spostano è del 50%
			//nPersone/2 si spostano
			//di queste si dividono il parti uguali negli stati vicini
			int migrantiPerStato=(nPersone/2)/vicini.size();
			//caso particolare -->
			//50% delle persone è minore del numero di vicini
			//--> nessuno si sposta, anche perchè la divisione 
			//arrotondata a intero mi darebbe 0
			
			if(migrantiPerStato >0) {
				//le persone si possono muovere
				for(Country confinante: vicini) {
					queue.add(new Evento (e.getT()+1,//quello di ora piu uno
							confinante,//si spostano nello stato confinante
							migrantiPerStato));//numero persone che si spostano
				}
			}
			int stanziali=nPersone-migrantiPerStato*vicini.size(); //in questo modo
			//considero tutti, anche per l'esempio del testo di 40/3
			//così conto gli stanziali come differenza tra tutti e quelli che si spostano
			//negli stati vicini.
			
			//devo sovrascrivere la mappa con il nuovo valore di stanziali di quello stato
			this.stanziali.put(stato,this.stanziali.get(stato)+ stanziali);
		}
	}
	
	public Map <Country, Integer> getStanziali(){
		return this.stanziali;
	}
	
	public Integer getT() {
		return this.T;
	}
}
