package es.upm.dit.adsw.ajedrez.analizadores;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

import org.junit.jupiter.api.Test;

import es.upm.dit.adsw.ajedrez25.modelo.*;
import es.upm.dit.adsw.ajedrez25.analizadores.*;
 

public class AnalizadorBasicoTest {

	@Test
	void ordenarTablerosTest1() {
		String[] tablerosStr = {"....r...p....k...p.....p.......r..P.p.....Pp....P.....bK.....q..",
		 "....rk.......pppp........r..b...............K..n........q.......",
		 "r.b.k..r........p.N..bpp...P.p......pq..P.N.R..P.PP..PP.R..Q..K.",
		 "rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR",
		 ".nbqk.nrppppppbp......p..................PN..N..P.PPPPPPR.BQKB.R"};
		List<Tablero> tableros = new ArrayList<>();
		for (String s : tablerosStr) {
			tableros.add(new Tablero(s));
		}
		Partida p = new Partida("a", "b", 1,1, "link",  tableros);
		List<Partida> ps = new ArrayList<>();
		ps.add(p);
		AnalizadorBasico ab = new AnalizadorBasico(ps);
		
		assertEquals(ab.getTableros().get(0).getPuntuacionGeneral(), -29);
		assertEquals(ab.getTableros().get(1).getPuntuacionGeneral(), -24);
		assertEquals(ab.getTableros().get(2).getPuntuacionGeneral(), 0);
		assertEquals(ab.getTableros().get(3).getPuntuacionGeneral(), 2);
		assertEquals(ab.getTableros().get(4).getPuntuacionGeneral(), 5);
	}
	
	@Test
	void ordenarTablerosTest2() {
		String[] tablerosStr = {".nbqk.nrppppppbp......p..................PN..N..P.PPPPPPR.BQKB.R",
		                        ".nbqk.nr.pppppbpp.....p..................PN..N..P.PPPPPPR.BQKB.R",  
		                        "rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR"};
		List<Tablero> tableros = new ArrayList<>();
		for (String s : tablerosStr) {
			tableros.add(new Tablero(s));
		}
		Partida p = new Partida("a", "b", 1,1, "link",  tableros);
		List<Partida> ps = new ArrayList<>();
		ps.add(p);
		AnalizadorBasico ab = new AnalizadorBasico(ps);
		
		assertEquals(ab.getTableros().get(0).getPuntuacionGeneral(), 0);
		assertEquals(ab.getTableros().get(1).getPuntuacionGeneral(), 5);
		assertEquals(ab.getTableros().get(2).getPuntuacionGeneral(), 5);
		System.out.println();
		assertEquals(ab.getTableros().get(1), new Tablero(".nbqk.nrppppppbp......p..................PN..N..P.PPPPPPR.BQKB.R"));
		assertEquals(ab.getTableros().get(2), new Tablero(".nbqk.nr.pppppbpp.....p..................PN..N..P.PPPPPPR.BQKB.R"));
		
	}
	
	@Test
	void buscarTablerosPuntuacion() {
		List<Tablero> t1 = new ArrayList<>();
		Partida p1 = new Partida("a", "b", 1,1, "link",  t1);
		List<Partida> ps1 = new ArrayList<>();
		ps1.add(p1);
		AnalizadorBasico ab1 = new AnalizadorBasico(ps1);
		assertEquals(ab1.buscarTablerosPorPuntuacion(0), 0);
		
		String[] tablerosStr = {"....rk.......pppp........r..b...............K..n........q.......",  // -29
				 "....r...p....k...p.....p.......r..P.p.....Pp....P.....bK.....q..",  // -24
				 "rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR",  // 0
				 "r.b.k..r........p.N..bpp...P.p......pq..P.N.R..P.PP..PP.R..Q..K.",  // +2
				 ".nbqk.nrppppppbp......p..................PN..N..P.PPPPPPR.BQKB.R",  // +5
				 ".nbqk.nr.pppppbpp.....p..................PN..N..P.PPPPPPR.BQKB.R"};
		List<Tablero> tableros = new ArrayList<>();
		for (String s : tablerosStr) {
		tableros.add(new Tablero(s));
		}
		Partida p2 = new Partida("a", "b", 1,1, "link",  tableros);
		List<Partida> ps2 = new ArrayList<>();
		ps2.add(p2);
		AnalizadorBasico ab2 = new AnalizadorBasico(ps2);
		assertEquals(ab2.buscarTablerosPorPuntuacion(0), 2);
		assertEquals(ab2.buscarTablerosPorPuntuacion(3), 4);
		assertEquals(ab2.buscarTablerosPorPuntuacion(-30), 0);
		assertEquals(ab2.buscarTablerosPorPuntuacion(7), 6);
		assertEquals(ab2.buscarTablerosPorPuntuacion(5), 4);
	}
	
	
}
