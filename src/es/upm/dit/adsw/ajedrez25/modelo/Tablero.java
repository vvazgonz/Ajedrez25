package es.upm.dit.adsw.ajedrez25.modelo;

import java.util.Arrays;

/**
 * Esta clase representa un tablero de ajedrez. Contiene información sobre las
 * piezas y el estado del tablero.
 */
public class Tablero implements Comparable {

    private Pieza[][] matrizPiezas; // Matriz que representa las piezas en el tablero.
	private boolean mate = false; // Indica si el tablero está en situación de jaque mate.

    /**
     * Crea un tablero de ajedrez con las piezas en su posición inicial.
     * 
     * @return un objeto Tablero con las piezas en su posición inicial
     */
    public static Tablero tableroBasico() {
        return new Tablero("rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR");
    }

    public int compareTo(Object t) {
    	return this.getPuntuacionGeneral() - ((Tablero)t).getPuntuacionGeneral();
    }
    
    /**
     * Crea un tablero de ajedrez sin piezas.
     */
    public Tablero() {
        matrizPiezas = new Pieza[8][8];
    }

    /**
     * Crea un tablero de ajedrez con las piezas en la posición especificada.
     * 
     * @param tableroSerializado una cadena de 64 caracteres que representa la
     *                           posición de las piezas en el tablero
     */
    public Tablero(String tableroSerializado) {
        // Completar en laboratorio 1
    	
    	matrizPiezas = new Pieza[8][8];
    	int col = 0;
    	int fil = 0;
    	for (int i = 0; i < tableroSerializado.length(); i++) {
    		if (fil == 8) {
    			fil = 0;
    			col++;
    		}
    		if (tableroSerializado.charAt(i) != '.') {
    			matrizPiezas[col][fil] = new Pieza(tableroSerializado.charAt(i));
    		}
    		fil++;
    	}
    	
    }
    
    
    public int getPuntacionBando(Bando b) {
    	int suma = 0;
    	for (int i = 0; i<matrizPiezas.length; i++) {
    		for (int j = 0; i < matrizPiezas[i].length; j++) {
    			switch (matrizPiezas[i][j].getTipo()) {
    			case PEON:
    				suma+= 1;
    				break;
    			case CABALLO:
    				suma+= 3;
    				break;
    			case ALFIL:
    				suma += 3;
    				break;
    			case TORRE:
    				suma+= 5;
    				break;
    			case REINA:
    				suma += 9;
    				break;
    			case REY:
    				suma += 100;
    				break;
	    		}
	    	}
	    }
    	return suma;
    }

    /**
     * Devuelve el array bidimensional que representa el tablero.
     * 
     * @return el array bidimensional que representa el tablero
     */
    public Pieza[][] getMatrizPiezas() {
        return this.matrizPiezas;
    }

    /**
     * Devuelve una cadena de texto que representa el tablero.
     * 
     * @return una cadena que representa el tablero
     */
    public String toString() {
        StringBuilder representacion = new StringBuilder();
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Pieza pieza = matrizPiezas[fila][columna];
                if (pieza != null) {
                    representacion.append(pieza.toChar());
                } else {
                    representacion.append('.');
                }
            }
        }
        return representacion.toString();
    }


    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrizPiezas);
    }

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		Tablero other = (Tablero) obj;
		return Arrays.deepEquals(matrizPiezas, other.matrizPiezas);
	}

	/**
     * Verifica si el tablero contiene una pieza específica.
     * 
     * @param pieza la pieza que se busca
     * @return true si la pieza está presente en el tablero, false en caso contrario
     */
    public boolean contienePieza(Pieza pieza) {
        for (int fila = 0; fila < matrizPiezas.length; fila++) {
            for (int columna = 0; columna < matrizPiezas[fila].length; columna++) {
                if (pieza.equals(matrizPiezas[fila][columna])) {
                    return true;
                }
            }
        }
        return false;
    }

	public boolean getMate() {
		return mate;
	}

	public void setMate(boolean mate) {
		this.mate = mate;
	}
	
	public int getPuntuacionBando(Bando b) {
        int suma = 0;
        for (int i = 0; i < matrizPiezas.length; i++) {
            for (int j = 0; j<matrizPiezas[i].length; j++) {
                if (matrizPiezas[i][j] != null) {
                    if (b ==  matrizPiezas[i][j].getBando()) {
                        if (matrizPiezas[i][j].getTipo() == TipoPieza.REY)
                        suma += 100;
                        else if (matrizPiezas[i][j].getTipo() == TipoPieza.REINA)
                            suma += 9;
                        else if (matrizPiezas[i][j].getTipo() == TipoPieza.TORRE)
                            suma += 5;
                        else if (matrizPiezas[i][j].getTipo() == TipoPieza.ALFIL)
                            suma += 3;
                        else if (matrizPiezas[i][j].getTipo() == TipoPieza.CABALLO)
                            suma += 3;
                        else if (matrizPiezas[i][j].getTipo() == TipoPieza.PEON)
                            suma += 1;
                    }
                }

            }
        }
        return suma;
    }

    public int getPuntuacionGeneral() {
        return getPuntuacionBando(Bando.BLANCAS) - getPuntuacionBando(Bando.NEGRAS);
    }
}
