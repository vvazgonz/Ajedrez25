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
	
	public AnalizadorBasico (List<Partida> partidas) {
		this.jugadoresPartida = new HashMap<>();
		this.partidas = partidas;
		for (Partida p: partidas) {
			for (Tablero t : p.getTurnos()) {
					tableros.add(t);
			}
			if (jugadoresPartida.containsKey(p.getJugadorBlancas())) {
	            jugadoresPartida.get(p.getJugadorBlancas()).add(p);
	        } else {
	            List<Partida> partidasJugador = new ArrayList<>();
	            partidasJugador.add(p);
	            jugadoresPartida.put(p.getJugadorBlancas(), partidasJugador);
	        }
		}
		jugadores = new ArrayList<>(jugadoresPartida.keySet());
		ordenarTableros();
	}
	
	public List<Partida> getPartidas() {
		return partidas;
	}
	
	public List<Tablero> getTableros() {
		return tableros;
	}
	
	public void ordenarTableros() {
		ordenarTableros(tableros);
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
		int mediana = tableros.size()/2;
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
	
	public int getPartidasGanadasPor(String jugador) {
		return -1;
	}
	
	public static void main(String[] args) throws Exception {
	    LectorPartidas lector = new LectorPartidas("data/puntuacion.txt");
	    long t = System.currentTimeMillis();
	    AnalizadorBasico basico = new AnalizadorBasico(lector.getPartidas());
	    LOGGER.info("Tiempo de análisis: " + (System.currentTimeMillis() - t) + " ms");
	    LOGGER.info("hola "+ basico.buscarTablerosPorPuntuacion(+5));
	    //LOGGER.info("El mayor tablero es: "  + 	basico.getMayorTablero() + "con una puntuación de " + basico.getMayorTablero().getPuntuacionGeneral());
	    LOGGER.info("Número de turnos de la partida más corta: " + basico.getNTurnosPartidaMasCorta());
	    //LOGGER.info("Puntuación mediana: " + basico.getPuntuacionMediana());
	    //LOGGER.info("Partidas ganadas por mahdii: " + basico.getPartidasGanadasPor("TrialB"));
	    //LOGGER.info("Mejor jugador: " + basico.getMejorJugador() + " con " + basico.getPartidasGanadasPor(basico.getMejorJugador()) + " victorias");
	}
}
