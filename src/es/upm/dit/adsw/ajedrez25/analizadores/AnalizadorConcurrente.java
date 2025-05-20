package es.upm.dit.adsw.ajedrez25.analizadores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.upm.dit.adsw.ajedrez25.io.LectorPartidas;
import es.upm.dit.adsw.ajedrez25.modelo.*;
import es.upm.dit.adsw.ajedrez25.modelo.grafo.*;

public class AnalizadorConcurrente {
	
	public static final int NUM_HEBRAS = 10; // Definimos el número de hebras
	Map<Tablero, Nodo> nodos;
	Set<Enlace> enlaces;
	PoolHebras pool;
	
	
	public AnalizadorConcurrente(LectorPartidas lectorPartidas) {
	        this(lectorPartidas.getPartidas());
	    }

	public AnalizadorConcurrente(List<Partida> partidas) {
	    AnalizadorGrafos analizadorGrafos = new AnalizadorGrafos(partidas);
	// Creamos los métodos accesores si no los tenemos ya
	    this.nodos = analizadorGrafos.getNodos();
	    this.enlaces = analizadorGrafos.getEnlaces();
	    this.pool = new PoolHebras(NUM_HEBRAS); 
	}
	
	// Definición de la clase PoolHebras
    class PoolHebras {
    	List<TareaCamino> tareas;
    	List<List<Nodo>> resultados;
    	int numHebras;
    	List<HebraWorker> hebras;
    	
        public PoolHebras(int n) {
        	this.numHebras = n;
        	resultados = new ArrayList<>();
        	tareas = new ArrayList<>();
        	hebras = new ArrayList<>();
        	for (int i = 0; i <n; i++) {
        		HebraWorker hebra = new HebraWorker(this);
        		hebras.add(hebra);
        		hebra.start();
        	}
        }
        
        public synchronized void buscarCaminos(Nodo nodoInicial) {
        	TareaCamino camino = new TareaCamino(nodoInicial, new ArrayList<>());
			addTarea(camino);

        }
        
        public synchronized void addResultado(List<Nodo> lista) {
        	resultados.add(lista);
        }
        
        
        public synchronized void addTarea(TareaCamino tarea) {
        	tareas.add(tarea);
        	notifyAll();
        }
        
        public synchronized TareaCamino getTarea(HebraWorker hebra) {
        	
        	try {
        		while(tareas.isEmpty() && hebra.seguirTrabajando) {
        	        wait(); // Esperamos a que haya tareas o a que la hebra deba parar
        	    }
        	    // Si hemos salido del bucle, significa que hay tareas o que la hebra no debe seguir trabajando
        	    if (tareas.isEmpty()) {
        	        return null; // Si no hay tareas, devolvemos null
        	    }
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
        	return tareas.remove(0);
        }
        
        public List<List<Nodo>> getResultados() {
        	List<List<Nodo>> copia = new ArrayList<>(this.resultados);
            resultados = new ArrayList<>();
            return copia;	
        	
        }
        
        public void detenerHebras() throws InterruptedException {
            synchronized (this) {
                for (HebraWorker hebra : hebras) {
                    hebra.setSeguirTrabajando(false); // Cambiamos el flag de la hebra
                }
                notifyAll(); // Despertamos a todas las hebras
            }
            for (HebraWorker hebra : hebras) {
                hebra.join(); // Esperamos a que la hebra termine
            }
        }
    }
    // Definición de la clase HebraWorker
    class HebraWorker extends Thread { // También se puede hacer con implements Runnable
    	private static int LIMITE = 12;
    	PoolHebras poolHebras;
    	boolean seguirTrabajando =true;
    	
    	public HebraWorker(PoolHebras pool) {
    		this.poolHebras = pool;
    	}
        public void run() {
            while (seguirTrabajando) {
            	 TareaCamino tarea = poolHebras.getTarea(this);
                 if (tarea == null) {
                     continue; // Si no hay tarea, volvemos al principio del bucle
                 }
                 List<Nodo> camino = new ArrayList<>();
                 camino.addAll(tarea.caminoRecorrido);
                 camino.add(tarea.nodoAVisitar);
                 if (camino.size() <= LIMITE) {
                 	if (tarea.nodoAVisitar.getTablero().getMate()){
                       poolHebras.addResultado(camino);
                 	} else {
                 		for (Enlace enlace : tarea.nodoAVisitar.getEnlacesSalientes()) {
                 			if (!camino.contains(enlace.getDestino())) {
                 				TareaCamino nuevaTarea = new TareaCamino(enlace.getDestino(), camino);
                                poolHebras.addTarea(nuevaTarea);
                                }
                        }
                        }
                    }	
            	}
        }
        
        public void setSeguirTrabajando(boolean b) {
        	this.seguirTrabajando= b;
        }
    }
    // Definición de la clase TareaCamino
    class TareaCamino {
        public final Nodo nodoAVisitar;
        public final List<Nodo> caminoRecorrido;

        public TareaCamino(Nodo nodoAVisitar, List<Nodo> caminoRecorrido) {
            this.nodoAVisitar = nodoAVisitar;
            this.caminoRecorrido = caminoRecorrido;
        }
    }

    public List<List<Nodo>> buscarCaminos(Tablero tablero, int t) {
        
    	try {
    		// Obtenemos el nodo inicial a partir del tablero
            if (!nodos.containsKey(tablero)) {
                return null; // Si el tablero no está en el grafo, devolvemos null
            }
            Nodo nodoInicial = nodos.get(tablero);
            pool.buscarCaminos(nodoInicial);
            Thread.sleep(t);
    		pool.detenerHebras();
    		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return pool.getResultados();
    }
    
    public static void main(String args[]) throws Exception {
    	LectorPartidas lp = new LectorPartidas("data/partidas.txt");
    	AnalizadorConcurrente ac = new AnalizadorConcurrente(lp);
    	List<List<Nodo>> caminos = ac.buscarCaminos(Tablero.tableroBasico(), 1000000);
    	System.out.println(caminos.size());
    }
	}
