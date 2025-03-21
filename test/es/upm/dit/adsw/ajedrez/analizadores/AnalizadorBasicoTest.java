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
		assertEquals(ab.getTableros().get(1), ".nbqk.nrppppppbp......p..................PN..N..P.PPPPPPR.BQKB.R");
		assertEquals(ab.getTableros().get(2), ".nbqk.nr.pppppbpp.....p..................PN..N..P.PPPPPPR.BQKB.R");
		
	}
	
	@Test
	void buscarTablerosPuntuacion() {
		List<Tablero> t1 = new ArrayList<>();
	}
	
	
}
