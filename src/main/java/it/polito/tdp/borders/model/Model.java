package it.polito.tdp.borders.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private Graph<Country, DefaultEdge> graph ;
	private Map<Integer,Country> countriesMap ;
	private Simulatore sim;
	public Model() {
		this.countriesMap = new HashMap<>() ;
		this.sim= new Simulatore();
	}
	
	public void creaGrafo(int anno) {
		
		this.graph = new SimpleGraph<>(DefaultEdge.class) ;

		BordersDAO dao = new BordersDAO() ;
		
		//vertici
		dao.getCountriesFromYear(anno,this.countriesMap) ;
		Graphs.addAllVertices(graph, this.countriesMap.values()) ;
		
		// archi
		List<Adiacenza> archi = dao.getCoppieAdiacenti(anno) ;
		for( Adiacenza c: archi) {
			graph.addEdge(this.countriesMap.get(c.getState1no()), 
					this.countriesMap.get(c.getState2no())) ;
			
		}
	}
	//punto c
	//l'elemento della lista deve contenere due info, un country e un integer
	//creo una classe per avere tutto insieme.
	public List <CountryAndNumber> getCountryAndNumbers(){
		//creo una lista
		List <CountryAndNumber> result= new LinkedList <CountryAndNumber>();
		//devo prendere tutti gli stati del grafo e vedere quali sono i vicini
		for(Country c:this.graph.vertexSet()) {
			result.add(new CountryAndNumber(c, this.graph.degreeOf(c)));
			
		}
		
		
		//ordino la lista prima di ritornarla
		Collections.sort(result);
		return result;
	}
	
	public void Simula(Country partenza) {
		if(graph!=null) {
		sim.init(partenza, graph);
		sim.run();
		}
	}
	
	public Integer getT() {
		return sim.getT();
	}
	public List <CountryAndNumber>getStanziali(){
		Map <Country, Integer> stanziali= sim.getStanziali();
		List <CountryAndNumber> result= new LinkedList<>();
		for(Country c:stanziali.keySet()) {
			if(stanziali.get(c)>0) { //elimino quelli dove il valore è 0
			CountryAndNumber cn = new CountryAndNumber (c, stanziali.get(c));
			result.add(cn);
			
			}
		}
		Collections.sort(result);
		return result;
	}

	public Set<Country> getCountries() {
		if(this.graph!=null) {
			return this.graph.vertexSet();
		}
		return null;
	}
}
