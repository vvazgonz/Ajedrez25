package es.upm.dit.adsw.ajedrez25.laboratorios;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import es.upm.dit.adsw.ajedrez25.analizadores.AnalizadorBasico;
import es.upm.dit.adsw.ajedrez25.io.LectorPartidas;
import es.upm.dit.adsw.ajedrez25.modelo.Partida;
import es.upm.dit.adsw.ajedrez25.modelo.Tablero;
import es.upm.dit.adsw.ajedrez25.modelo.grafo.Enlace;
import es.upm.dit.adsw.ajedrez25.modelo.grafo.Nodo;

public class Lab3 {
	private static final Logger LOGGER = Logger.getLogger(AnalizadorBasico.class.getName());
	
	Map<Tablero, Nodo> nodos = new HashMap<>();
	Set<Enlace> enlaces = new HashSet<Enlace>();
	private int nNodos = 0;
	private int nAristas = 0;
	
	public Lab3(List<Partida> partidas, int N, int M) {
		int nPar = 0;
		int nMov = 0;
		
		if (N == 0)
			N = Integer.MAX_VALUE;
		if (M == 0)
			M = Integer.MAX_VALUE;
		
		for (Partida p : partidas) {
			nMov = 0;
			if (nPar == N)
				break;
			List<Tablero> tableros = p.getTurnos();
			for (int i = 0; i < tableros.size()-1; i ++) {
				if (nMov == M)
					break;
				Tablero t1 = tableros.get(i);
				Tablero t2 = tableros.get(i+1);
				
				if (!nodos.containsKey(t1))
					nodos.put(t1,new Nodo(tableros.get(i)));
				if (!nodos.containsKey(t2))
					nodos.put(t2, new Nodo(tableros.get(i+1)));
				
				Nodo n1 = nodos.get(t1);
				Nodo n2 = nodos.get(t2);
				
				Enlace e1 = n1.addEnlaceA(n2);
	            n2.addEnlaceA(n1);
	            enlaces.add(e1);
	            nMov++;
			}
			nPar++;
		}
		nNodos = nodos.size();
		nAristas = enlaces.size();
	}
	
	public Lab3(LectorPartidas lp, int N, int M) {
		this(lp.getPartidas(), N, M);
	}
	
	public int getNodos() {
		return nNodos;
	}
	public int getAristas() {
		return nAristas;
	}
	
	private void escribirGrafo(String nombreArchivo) {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
	        // escribir la cabecera del archivo CSV
	        writer.write("origen,destino,peso\n");
	        // recorrer el grafo y escribir cada arista en el archivo
	        for (Enlace e : enlaces) {
	        	writer.write(""+ e.getOrigen().hashCode()  + "," + e.getDestino().hashCode() + "," + e.getPeso() + "\n");
	        }
	    
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void main(String args[]) throws Exception {
		LectorPartidas lp = new LectorPartidas("data/partidas.txt");
		int N = 0;
		int M = 0;
		Lab3 l3 = new Lab3(lp, N, M);
		String name = "grafo_" + M + "_" + N + ".csv";
		l3.escribirGrafo(name);
		LOGGER.info("Se ha escrito el grafo en el archivo" + name);
		LOGGER.info("El número de nodos del grafo es: " + l3.getNodos() + " y el número de enlaces es: " + l3.getAristas());
		
	}
}
