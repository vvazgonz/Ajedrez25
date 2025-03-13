package es.upm.dit.adsw.ajedrez25.laboratorios;

import java.util.*;
import java.util.logging.Logger;

import es.upm.dit.adsw.ajedrez25.io.LectorPartidas;
import es.upm.dit.adsw.ajedrez25.modelo.*;

public class Lab2 {

	HashMap<String, List<Partida>> diccionario = new HashMap<String, List<Partida>>();
	List<String> jugadores;
	private static final Logger LOGGER = Logger.getLogger(LectorPartidas.class.getName());
	
	public Lab2 (LectorPartidas lp) {
		
		for (Partida p : lp.getPartidas()) {
			for (int i = 0; i <2; i++) {
				if (!diccionario.containsKey(p.getJugadorBlancas())) {
					diccionario.put(p.getJugadorBlancas(), new ArrayList<Partida>());
				}
				if (!diccionario.containsKey(p.getJugadorNegras())) {
					diccionario.put(p.getJugadorNegras(), new ArrayList<Partida>());
				}
				
				List<Partida> pb = diccionario.get(p.getJugadorBlancas());
				List<Partida> pn = diccionario.get(p.getJugadorNegras());
				pb.add(p);
				pn.add(p);
				diccionario.replace(p.getJugadorBlancas(), pb);
				diccionario.replace(p.getJugadorNegras(), pn);
			}
		}
		jugadores = new ArrayList<>(diccionario.keySet());
		
		Collections.sort(jugadores, comparadorPartidasGanadas);
	}
	
	private static boolean jugadorHaGanado(String jugador, Partida partida) {
		if (partida.getTurnos().size() % 2 == 0) {
			return jugador == partida.getJugadorNegras();
		} else {
			return jugador == partida.getJugadorBlancas();
		}
	}
	
	Comparator<String> comparadorPartidasGanadas = new Comparator<String>() {
		@Override
		public int compare(String jugador1, String jugador2) {
			List<Partida> p1 = diccionario.get(jugador1);
			List<Partida> p2 = diccionario.get(jugador2);
			int gp1 = 0;
			int gp2 = 0;
			for (Partida p : p1) {
				if (jugadorHaGanado(jugador1, p)) {
					gp1++;
				}
			}
			for (Partida p: p2) {
				 if (jugadorHaGanado(jugador2,p)) {
						gp2++;
					}
			}
			return gp1 - gp2;
		}
	};
	
	private void pintarRanking(int top) {
		System.out.println("╔═════════════════╦══════════╦═══════════╗");
		System.out.println("║ Nombre          ║ Partidas ║ Victorias ║");
		System.out.println("╠═════════════════╬══════════╬═══════════╣");
		for (int i = jugadores.size()-1; i >= jugadores.size() -1 -top; i--) {
			List<Partida> ps = diccionario.get(jugadores.get(i)); 
			int npartidas = ps.size();
			float npartidasganadas = 0;
			
			for (Partida p : ps) {
				if (jugadorHaGanado(jugadores.get(i), p)) {
					npartidasganadas ++;
				}
			}
			float porcentaje = npartidasganadas/npartidas * 100;
			System.out.println("║"+jugadores.get(i)+"║" + npartidasganadas + "║" + porcentaje + "%"+ "║");
			//String.format("%-10s", jugadores.size());
		}
		System.out.println("╚═════════════════╩══════════╩═══════════╝");
	}
	
	public static void main(String[] args) throws Exception {
		LectorPartidas lector = new LectorPartidas("data/partidas.txt");
		Lab2 lab2 = new Lab2(lector);
		lab2.pintarRanking(10);
	}
	
}
