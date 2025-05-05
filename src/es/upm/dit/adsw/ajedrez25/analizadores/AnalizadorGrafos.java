package es.upm.dit.adsw.ajedrez25.analizadores;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Logger;

import es.upm.dit.adsw.ajedrez25.io.LectorPartidas;
import es.upm.dit.adsw.ajedrez25.modelo.*;
import es.upm.dit.adsw.ajedrez25.modelo.grafo.*;

public class AnalizadorGrafos {
	
		private static final Logger LOGGER = Logger.getLogger(AnalizadorBasico.class.getName());
		Map<Tablero, Nodo> nodos = new HashMap<>();
		Set<Enlace> enlaces = new HashSet<Enlace>();
		private int nNodos, nAristas = 0;
		private PrintWriter logWriter;
		Set<Nodo> mates = new HashSet<>();
		
		
		
		/**
		 * @param partidas
		 */
		public AnalizadorGrafos(List<Partida> partidas) {
			for (Partida p : partidas) {
				List<Tablero> tableros = p.getTurnos();
				for (int i = 0; i < tableros.size()-1; i ++) {
					Tablero t1 = tableros.get(i);
					Tablero t2 = tableros.get(i+1);
					
					if (!nodos.containsKey(t1)) {
						Nodo n = new Nodo(t1);
						nodos.put(t1, n);
					}
						
					if (!nodos.containsKey(t2)) {
						Nodo n = new Nodo(t2);
						nodos.put(t2, n);
					}
						
					Nodo n1 = nodos.get(t1);
					Nodo n2 = nodos.get(t2);
					
					if (t1.getMate())
						mates.add(n1);
					if (t2.getMate())
						mates.add(n2);
					
					Enlace e = n1.addEnlaceA(n2);
		            n2.addEnlace(e);
		            enlaces.add(e);
				}
			}
			nNodos = nodos.size();
			nAristas = enlaces.size();
		}
		
		
		public int getNumeroNodos() {
			return nNodos;
		}
		
		public Nodo getNodoMasEnlaces() {
			Nodo max = nodos.get(Tablero.tableroBasico());
			for (Tablero t : nodos.keySet()) {
				Nodo n = nodos.get(t);
				if (n.getEnlaces().size()>max.getEnlaces().size())
					max = n;
			}
			return max;
		}
		
		/*
		 * public List<Nodo> NodosCaminoMasCorto(Nodo n1, Nodo n2) { Map<Nodo, Integer>
		 * distancias = new HashMap<>(); Map<Nodo, List<Nodo>> predecesores = new
		 * HashMap<>(); Set<Nodo> visitados = new HashSet<>();
		 * 
		 * distancias.put(n1, 0); List<Nodo> pred0 = new ArrayList<>(); pred0.add(n1);
		 * predecesores.put(n1, pred0);
		 * 
		 * 
		 * recorrer(distancias, predecesores, visitados, n1, 0); return
		 * predecesores.get(n2); }
		 * 
		 * private void recorrer(Map<Nodo, Integer> distancias, Map<Nodo, List<Nodo>>
		 * predecesores, Set<Nodo> visitados, Nodo nodo, int distancia) { if
		 * (visitados.contains(nodo)) return; visitados.add(nodo);
		 * System.out.println("Visitando nodo: " + nodo); for (Enlace e :
		 * nodo.getEnlacesSalientes()) { Nodo destino = e.getDestino(); if
		 * (!distancias.containsKey(destino) || distancias.get(destino) > distancia + 1)
		 * { distancias.put(destino, distancia + 1); List<Nodo> pred = new
		 * ArrayList<>(predecesores.get(nodo)); pred.add(destino);
		 * predecesores.put(destino, pred); recorrer(distancias, predecesores,
		 * visitados, destino, distancia + 1); } } }
		 */

		public List<Nodo> NodosCaminoMasCorto(Nodo n1, Nodo n2) {
		    Map<Nodo, Integer> distancias = new HashMap<>();
		    Map<Nodo, List<Nodo>> predecesores = new HashMap<>();
		    Set<Nodo> visitados = new HashSet<>();
		    distancias.put(n1, 0);
		    predecesores.put(n1, new ArrayList<>(Arrays.asList(n1)));
		    
		    Queue<Nodo> cola = new LinkedList<>();
		    cola.add(n1);
		    visitados.add(n1);

		    while (!cola.isEmpty()) {
		        Nodo nodo = cola.poll();
		        if (nodo.equals(n2)) {
		            break;
		        }

		        Set<Enlace> hijos = nodo.getEnlacesSalientes();
		        if (hijos != null) {
		            for (Enlace e : hijos) {
		                Nodo destino = e.getDestino();

		                if (!visitados.contains(destino)) {
		                    visitados.add(destino);
		                    cola.add(destino);
		                    if (!distancias.containsKey(destino) || distancias.get(destino) > distancias.get(nodo) + 1) {
		                        distancias.put(destino, distancias.get(nodo) + 1);
		                        List<Nodo> pred = new ArrayList<>(predecesores.get(nodo));
		                        pred.add(destino);
		                        predecesores.put(destino, pred);
		                    }
		                }
		            }
		        }
		    }
		    return predecesores.get(n2);
		}
		
	
		
		public List<Enlace> EnlacesCaminoMasCorto(Nodo n1, Nodo n2) {
			List<Nodo> nodos = NodosCaminoMasCorto(n1, n2);
			List<Enlace> enlaces = new ArrayList<>();
			for (int i = 0; i < nodos.size()-1;i++) {
				Nodo actual = nodos.get(i);
				Nodo siguiente = nodos.get(i+1);
				for (Enlace e : actual.getEnlacesSalientes()) {
					if (e.getDestino().equals(siguiente))
						enlaces.add(e);
						
			}
				
			}
			return enlaces;
			
		}
		
		public List<Enlace> CaminoPartida(Partida p) {
			List<Enlace> enlaces = new ArrayList<>();
			List<Tablero> turnos = p.getTurnos();
			for (int i = 0; i < turnos.size()-1; i++) {
				Tablero t = turnos.get(i);
				Tablero sig = turnos.get(i+1);
				if (nodos.get(t) != null) {
					Set<Enlace> salientes = nodos.get(t).getEnlacesSalientes();
					if (salientes != null)  {
						for (Enlace e : nodos.get(t).getEnlacesSalientes()) {
							if (e.getDestino().equals(nodos.get(sig)))
								enlaces.add(e);
						}
					}	
				}
				
				
			}
			return enlaces;
		}
		
		public boolean MejorRuta (Partida p) {
			Nodo ini = nodos.get(p.getTurnos().get(0));
			Nodo fin = nodos.get(p.getTurnos().get(p.getTurnos().size()-1));
			return CaminoPartida(p).equals(EnlacesCaminoMasCorto(ini, fin));
		}
		
		
		public List<Nodo> mateEnNMovimientos(Tablero t, int n) {
		    Nodo origen = nodos.get(t);
		    Map<Nodo, Integer> distancias = new HashMap<>();
		    Map<Nodo, List<Nodo>> predecesores = new HashMap<>();
		    
		    Queue<Nodo> cola = new LinkedList<>();
		    cola.add(origen);
		    distancias.put(origen, 0);
		    predecesores.put(origen, new ArrayList<>());

		    while (!cola.isEmpty()) {
		        Nodo actual = cola.poll();
		        int distanciaActual = distancias.get(actual);

		        if (distanciaActual > n) {
		            continue;
		        }

		        if (mates.contains(actual)) {
		        	List<Nodo> ret = new ArrayList<>(predecesores.get(actual));
		        	Collections.reverse(ret);
		            return ret;
		        }

		        Set<Enlace> hijos = actual.getEnlacesSalientes();
		        if (hijos != null) {
		            for (Enlace e : hijos) {
		                Nodo destino = e.getDestino();

		                if (!distancias.containsKey(destino) || distancias.get(destino) > distanciaActual + 1) {
		                    distancias.put(destino, distanciaActual + 1);
		                    
		                    List<Nodo> caminoHastaDestino = new ArrayList<>(predecesores.get(actual));
		                    caminoHastaDestino.add(destino);
		                    predecesores.put(destino, caminoHastaDestino);

		                    cola.add(destino);
		                }
		            }
		        }
		    }
		    return null;
		}

		
		public static void main(String[] args) throws Exception {
			LectorPartidas lp = new LectorPartidas("data/partidas.txt");
			long t = System.currentTimeMillis();
			AnalizadorGrafos ag = new AnalizadorGrafos(lp.getPartidas());
			LOGGER.info("El tiempo de análisis fue de: " + (System.currentTimeMillis() -t) + "ms");
			LOGGER.info("El número de nodos del grafo es: " + ag.getNumeroNodos());
			LOGGER.info("El nodo con más enlaces tiene: " + ag.getNodoMasEnlaces().getEnlaces().size());
			Tablero ta = Tablero.tableroBasico();
			LOGGER.info("sexo: \n" + ag.mateEnNMovimientos(ta, 100).size());
			
			
			/*
			 * Tablero t1 = new
			 * Tablero("rnbqk.nr.pp..p.p...p..p.p...........N....P......PRPPPPPP...QKBNR");
			 * t = System.currentTimeMillis(); List<Enlace> eln =
			 * ag.EnlacesCaminoMasCorto(ag.nodos.get(Tablero.tableroBasico()),
			 * ag.nodos.get(t1));
			 * LOGGER.info("El mínimo recorrido entre el tablero inicial y" + t1 + " es: ");
			 * for (Enlace e : eln) { System.out.println(e); }
			 * LOGGER.info("El tiempo necesario para calcular el Camino más corto fue de: "
			 * + (System.currentTimeMillis() -t) + "ms");
			 */
			
		}
}
