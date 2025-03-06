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
	
	public AnalizadorBasico (List<Partida> partidas) {
		this.partidas = partidas;
		for (Partida p: partidas) {
			for (Tablero t : p.getTurnos()) {
					tableros.add(t);
			}
		}
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
	
	public Tablero getMayorTablero() {
		return tableros.get(tableros.size()-1);
	}
	
	public int getPuntuacionMediana( ) {
		if (tableros.size()> 0)
			return tableros.get((int)(tableros.size()/2)).getPuntuacionGeneral();
		else
			return 0;
	}
	
	public static void main(String[] args) throws Exception {
	    LectorPartidas lector = new LectorPartidas("data/partidas.txt");
	    long t = System.currentTimeMillis();
	    AnalizadorBasico basico = new AnalizadorBasico(lector.getPartidas());
	    LOGGER.info("Tiempo de análisis: " + (System.currentTimeMillis() - t) + " ms");
	    LOGGER.info("El mayor tablero es: "  + 	basico.getMayorTablero() + "con una puntuación de " + basico.getMayorTablero().getPuntuacionGeneral());
	    //LOGGER.info("Número de turnos de la partida más corta: " + basico.getNumTurnosPartidaMasCorta());
	    LOGGER.info("Puntuación mediana: " + basico.getPuntuacionMediana());
	    //LOGGER.info("Partidas ganadas por mahdii: " + basico.getPartidasGanadasPor("mahdii"));
	    //LOGGER.info("Mejor jugador: " + basico.getMejorJugador() + " con " + basico.getPartidasGanadasPor(basico.getMejorJugador()) + " victorias");
	}
}
