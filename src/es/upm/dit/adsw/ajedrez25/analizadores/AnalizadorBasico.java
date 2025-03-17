package es.upm.dit.adsw.ajedrez25.analizadores;

import java.util.*;
import java.util.logging.Logger;

import es.upm.dit.adsw.ajedrez25.io.LectorPartidas;
import es.upm.dit.adsw.ajedrez25.modelo.*;

/**
 * Analizador básico de partidas de ajedrez.
 * Esta clase analiza las partidas leídas, permitiendo obtener estadísticas,
 * ordenar tableros y realizar consultas específicas sobre jugadores y puntuaciones.
 */
public class AnalizadorBasico {

	private static final Logger LOGGER = Logger.getLogger(LectorPartidas.class.getName());
	List<Partida> partidas;
	List<Tablero> tableros = new ArrayList<Tablero>();
	Map<String, List<Partida>> jugadoresPartida = new HashMap<String, List<Partida>>();
	List<String> jugadores;
	Map<Tablero, Integer> TablerosRep = new HashMap<>();
	
	public AnalizadorBasico (List<Partida> partidas) {
		this.jugadoresPartida = new HashMap<>();
		this.partidas = partidas;
		
		for (Partida p: partidas) {
			for (Tablero t : p.getTurnos()) {
					tableros.add(t);
					if (!TablerosRep.containsKey(t)) 
						TablerosRep.put(t, 1);
					else 
						TablerosRep.replace(t, TablerosRep.get(t)+1);
			}
			if (jugadoresPartida.containsKey(p.getJugadorBlancas())) {
	            jugadoresPartida.get(p.getJugadorBlancas()).add(p);
	        } else {
	            List<Partida> partidasJugador = new ArrayList<>();
	            partidasJugador.add(p);
	            jugadoresPartida.put(p.getJugadorBlancas(), partidasJugador);
	        }
			if (jugadoresPartida.containsKey(p.getJugadorNegras())) {
	            jugadoresPartida.get(p.getJugadorNegras()).add(p);
	        } else {
	            List<Partida> partidasJugador = new ArrayList<>();
	            partidasJugador.add(p);
	            jugadoresPartida.put(p.getJugadorNegras(), partidasJugador);
	        }
		}
		jugadores = new ArrayList<>(jugadoresPartida.keySet());
	}
	
	public List<Partida> getPartidas() {
		return partidas;
	}
	
	public List<Tablero> getTableros() {
		return tableros;
	}
	
	private  void ordenarTableros(List<Tablero> list) {
		 if (list.size() < 2)
		 return;
		 int m = list.size() / 2;
		 List<Tablero> left =
		 new ArrayList<>(list.subList(0, m));
		 List<Tablero> right =
		 new ArrayList<>(list.subList(m, list.size()));
		 ordenarTableros(left);
		 ordenarTableros(right);
		 list.clear();
		 while (left.size() > 0 && right.size() > 0) {
		 Tablero sl = left.get(0);
		 Tablero sr = right.get(0);
		 if (sl.compareTo(sr) < 0)
		 list.add(left.remove(0));
		 else
		 list.add(right.remove(0));
		 }
		 while (left.size() > 0)
		 list.add(left.remove(0));
		 while (right.size() > 0)
		 list.add(right.remove(0));
		}
	
	public int buscarTablerosPorPuntuacion(int puntuacion) {
        int inicio = 0;
        int fin = tableros.size() - 1;
		
        while (inicio <= fin) {
        	int medio = (inicio + fin) /2;
            int puntuacionMedio = tableros.get(medio).getPuntuacionGeneral();
            
            if (puntuacionMedio == puntuacion) {
            	while (medio > 0 && tableros.get(medio -1).getPuntuacionGeneral() == puntuacion) {
            		medio--;
            	}
            	return medio;
            }
            else if (puntuacionMedio < puntuacion)
            	inicio = medio +1;
            else 
            	fin = medio-1;
        }
        return inicio;
    }

	
	
	public Tablero getMayorTablero() {
		return tableros.get(tableros.size()-1);
	}
	
	public int getPuntuacionMediana( ) {
		int mediana = (tableros.size()-1)/2;
		if (mediana % 2 == 0) {
			return tableros.get(mediana).getPuntuacionGeneral();
		}
		else {
			int pos1 = tableros.get(mediana).getPuntuacionGeneral();
			int pos2 = tableros.get(mediana + 1).getPuntuacionGeneral();
			return (pos1 + pos2) / 2 ;
		}
	}
	
	public int getNTurnosPartidaMasCorta() {
		int min = Integer.MAX_VALUE;
		for (Partida p : partidas) {
			int nturnos = p.getTurnos().size();
			if (nturnos < min) {
				min = nturnos;
			}
		}
		return min;
	}
	
	private static boolean jugadorHaGanado(String jugador, Partida partida) {
		if (partida.getTurnos().size() % 2 == 0) {
			return jugador.equals(partida.getJugadorBlancas());
			
		} else {
			return jugador.equals(partida.getJugadorNegras());
		}
	}
	
	public int getPartidasGanadasPor(String jugador) {
		int contador = 0;
		if (jugadoresPartida.containsKey(jugador)) {
			for (Partida p : jugadoresPartida.get(jugador)) {
				if (jugadorHaGanado(jugador, p)) 
					contador++;
			}
		}
		
		return contador;
	}
	
	public String getMejorJugador() {
		String ret = jugadores.get(0);
		for (String s : jugadores) {
			if (getPartidasGanadasPor(s) > getPartidasGanadasPor(ret))
				ret = s;
		}
		return ret;
	}
	
	public Tablero getTableroPorPuntuacion(int puntuacion) {
		int inicio = 0;
		int fin = tableros.size();
		
		while (inicio < fin) {
			int medio = (inicio + fin)/2;
			Tablero t = tableros.get(medio);
			
			if (t.getPuntuacionGeneral() == puntuacion)
				return t;
			else if (t.getPuntuacionGeneral() > puntuacion) {
				fin = medio -1;
			} else if (t.getPuntuacionGeneral() < puntuacion)
				inicio = medio +1;
				
		}
		return tableros.get(inicio);
	}
	
	public Map<Tablero, Integer> getRepeticionesTablero() {
		return TablerosRep;
	}
	
	public static void main(String[] args) throws Exception {
	    LectorPartidas lector = new LectorPartidas("data/partidas.txt");
	    long t = System.currentTimeMillis();
	    AnalizadorBasico basico = new AnalizadorBasico(lector.getPartidas());
	    LOGGER.info("Tiempo de análisis: " + (System.currentTimeMillis() - t) + " ms");
	    LOGGER.info("Ordenando la lista de tableros...");
	    t = System.currentTimeMillis();
	    basico.ordenarTableros(basico.tableros);
	    LOGGER.info("Tiempo de ordenación: " + (t - System.currentTimeMillis()));
	    LOGGER.info("El mayor tablero es: "  + 	basico.getMayorTablero() + "con una puntuación de " + basico.getMayorTablero().getPuntuacionGeneral());
	    LOGGER.info("Número de turnos de la partida más corta: " + basico.getNTurnosPartidaMasCorta());
	    LOGGER.info("Puntuación mediana: " + basico.getPuntuacionMediana());
	    LOGGER.info("Partidas ganadas por TrialB: " + basico.getPartidasGanadasPor("TrialB"));
	    LOGGER.info("Mejor jugador: " + basico.getMejorJugador() + " con " + basico.getPartidasGanadasPor(basico.getMejorJugador()) + " victorias");
	    Tablero b = new Tablero("rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR");
	    LOGGER.info("El tablero básico aparece: " + basico.getRepeticionesTablero().get(b) + " veces");
	    
	}
}
