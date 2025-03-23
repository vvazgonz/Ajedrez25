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

	private static final Logger LOGGER = Logger.getLogger(AnalizadorBasico.class.getName());
	List<Partida> partidas;
	List<Tablero> tableros = new ArrayList<Tablero>();
	Map<String, List<Partida>> jugadoresPartida = new HashMap<String, List<Partida>>();
	List<String> jugadores;
	Map<Tablero, Integer> TablerosRep = new HashMap<>();
	List<Tablero> TablerosFrecuencia;
	//Map<String, Integer> jugadoresMinTurnos = new HashMap<>();
	
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
		for (String s : jugadores) {
			Collections.sort(jugadoresPartida.get(s), comparadorPartidaNTurnos);
		}
		TablerosFrecuencia = new ArrayList<>(TablerosRep.keySet());
		Collections.sort(TablerosFrecuencia, comparadorTablerosInt);
		//sort(this.tableros);
	    this.ordenarTableros(this.tableros);
			/*
			 * if (jugadoresMinTurnos.containsKey(p.getJugadorBlancas())) { if
			 * (p.getTurnos().size() < jugadoresMinTurnos.get(p.getJugadorBlancas()))
			 * jugadoresMinTurnos.replace(p.getJugadorBlancas(), p.getTurnos().size());
			 * 
			 * } else jugadoresMinTurnos.put(p.getJugadorBlancas(), p.getTurnos().size());
			 * 
			 * if (jugadoresMinTurnos.containsKey(p.getJugadorNegras())) { if
			 * (p.getTurnos().size() < jugadoresMinTurnos.get(p.getJugadorNegras()))
			 * jugadoresMinTurnos.replace(p.getJugadorNegras(), p.getTurnos().size());
			 * 
			 * } else jugadoresMinTurnos.put(p.getJugadorNegras(), p.getTurnos().size());
			 * }
			 */ 
	}
	
	Comparator<Partida> comparadorPartidaNTurnos = new Comparator<Partida>() {
		@Override
		public int compare(Partida p1, Partida p2) {
			int i1 = p1.getTurnos().size();
			int i2 = p2.getTurnos().size();
			return i1-i2;
		}
	};
	
	public List<Partida> getPartidas() {
		return partidas;
	}
	
	public List<Tablero> getTableros() {
		return tableros;
	}
	
	public void sort(List<Tablero> list) {
	    ordenarTableros(list);
	}
	
	public void ordenarTableros(List<Tablero> list) {
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
		 if (sl.compareTo(sr) <= 0)
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
		int i = Integer.MAX_VALUE;
		for (String s: jugadores) {
			int a = jugadoresPartida.get(s).get(0).getTurnos().size();
			if ( i > a) {
				i = a;
			}
		}
		return i;
		
		/*
		 * int min = jugadoresMinTurnos.get(jugadores.get(0)); for (String s: jugadores)
		 * { if (min > jugadoresMinTurnos.get(s)) { min = jugadoresMinTurnos.get(s); } }
		 * return min;
		 */
		
		/*
		 * int min = Integer.MAX_VALUE; for (Partida p : partidas) { int nturnos =
		 * p.getTurnos().size(); if (nturnos < min) { min = nturnos; } } return min;
		 */
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
		
		if (tableros == null) 
			return null;
		else {
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
			return null;
		}
		
	}
	
	public Map<Tablero, Integer> getRepeticionesTablero() {
		return TablerosRep;
	}
	
	
	Comparator<Tablero> comparadorTablerosInt = new Comparator<Tablero>() {
		@Override
		public int compare(Tablero t1, Tablero t2) {
			int i1 = TablerosRep.get(t1);
			int i2 = TablerosRep.get(t2);
			return i1-i2;
		}
	};
	
	/**
	 * Obtiene los N tableros que más veces aparecen en todas las partidas analizadas.
	 *
	 * @param n Número de tableros más frecuentes a obtener.
	 * @return Lista con los N tableros más frecuentes.
	 */
	public void getNTablerosMasFrecuentes(int n) {
		String p = "";
		int o = 0;
		while (o < 64) {
			p += "═";
			o++;
		}
		String s = "╔"+ p + "╦╦═════════════╗ \n║"+String.format("%-64s","Tablero") + "║║ Apariciones ║\n╠" + p +"╬╬═════════════╣";
		
		for (int i = TablerosFrecuencia.size()-1; i >= TablerosFrecuencia.size() -1 -n; i--) {
			int f = TablerosRep.get(TablerosFrecuencia.get(i)); 
			s+=("\n║"+String.format("%-64s", TablerosFrecuencia.get(i))+"║" + "║" +String.format("%-13s", f)  + "║");
			}
			
			//String.format("%-10s", jugadores.size());
		System.out.println(s+"\n╚"+ p + "╩╩═════════════╝");
	}
		/*
		 * List<Tablero> ret = new ArrayList<>();
		 * 
		 * 
		 * if (TablerosFrecuencia != null ) { if (n > TablerosFrecuencia.size()) {
		 * return ret; } for (int i = TablerosFrecuencia.size() -1; i >
		 * TablerosFrecuencia.size() -1 -n; i--) { ret.add(TablerosFrecuencia.get(i)); }
		 * } else { for (int i = 0; i <n; i++) { ret.add(null); } }
		 * 
		 * return ret;
		 *
		 *
	}

	/**
	 * Determina si un jugador ha jugado en una posición específica en alguna de sus partidas.
	 *
	 * @param jugador Nombre del jugador.
	 * @param tablero Tablero a comprobar.
	 * @return {@code true} si el jugador ha jugado en esa posición, {@code false} en caso contrario.
	 */
	
	public boolean jugadorHaJugadoTablero(String jugador, Tablero tablero) {
		for (Partida p : jugadoresPartida.get(jugador)) {
			for (Tablero t: p.getTurnos()) {
				if (t.equals(tablero))
					return true;
			}
		}
		return false;
	}

	/**
	 * Calcula el número medio de victorias por jugador.
	 *
	 * @return Un mapa donde la clave es el nombre del jugador y el valor es su número medio de victorias.
	 */
	//public Map<String, Double> getMediaVictoriasPorJugador();

	/**
	 * Obtiene el número de partidas que han finalizado con una pieza específica presente en el tablero final.
	 *
	 * @param pieza Pieza a comprobar .
	 * @return Número de partidas donde la pieza estaba presente al finalizar.
	 */
	public int getPartidasFinalizadasConPieza(Pieza pieza) {
		int c = 0;
		if (pieza != null) {
			for (Partida p: partidas) {
				if (p.getTurnos().get(p.getTurnos().size()-1).contienePieza(pieza))
					c++;
			}
		}
		return c;
	}

	/**
	 * Obtiene el número de tableros que tienen exactamente una puntuación determinada.
	 *
	 * @param puntuacion Puntuación exacta que se desea buscar.
	 * @return Cantidad de tableros con dicha puntuación.
	 */
	public int getNumeroTablerosConPuntuacion(int puntuacion) {
		int c = 0;
		for (Tablero t : tableros) {
			if (t.getPuntuacionGeneral() == puntuacion)
				c++;
		}
		return c;
	}
	

	/**
	 * Obtiene una lista de los jugadores que nunca han perdido una partida.
	 *
	 * @return Lista de nombres de jugadores invictos.
	 */
	public List<String> getJugadoresInvictos() {
		List<String> res = new ArrayList<>();
		for (String s : jugadores) {
			if (getPartidasGanadasPor(s) == jugadoresPartida.get(s).size())
				res.add(s);
		}
		return res;
	}

	/**
	 * Obtiene el número total de jugadas realizadas en todas las partidas analizadas.
	 *
	 * @return Número total de movimientos jugados.
	 */
	public int getTotalJugadasRealizadas() {
		return tableros.size() - partidas.size();
	}
	
	public static void main(String[] args) throws Exception {
	    LectorPartidas lector = new LectorPartidas("data/partidas.txt");
	    long t = System.currentTimeMillis();
	    AnalizadorBasico basico = new AnalizadorBasico(lector.getPartidas());
	    LOGGER.info("Tiempo de análisis: " + (System.currentTimeMillis() - t) + " ms");
	    LOGGER.info("" + basico.buscarTablerosPorPuntuacion(0));
	    LOGGER.info("El mayor tablero es: "  + 	basico.getMayorTablero() + "con una puntuación de " + basico.getMayorTablero().getPuntuacionGeneral());
	    LOGGER.info("Número de turnos de la partida más corta: " + basico.getNTurnosPartidaMasCorta());
	    LOGGER.info("Puntuación mediana: " + basico.getPuntuacionMediana());
	    LOGGER.info("Partidas ganadas por TrialB: " + basico.getPartidasGanadasPor("TrialB"));
	    LOGGER.info("Mejor jugador: " + basico.getMejorJugador() + " con " + basico.getPartidasGanadasPor(basico.getMejorJugador()) + " victorias");
	    Tablero b = new Tablero("rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR");
	    LOGGER.info("El tablero básico aparece: " + basico.getRepeticionesTablero().get(b) + " veces");
	    LOGGER.info("Los tableros más frecuentes son:");
	    basico.getNTablerosMasFrecuentes(10);
	}
}
