package blackjack.practica1_alg20262.Modelo;

import blackjack.practica1_alg20262.Modelo.DeckOfCards.CartaInglesa;
import blackjack.practica1_alg20262.Modelo.DeckOfCards.Mazo;

import java.util.ArrayList;
import java.util.Collections;

public class Juego21 {

    private Mazo mazo;
    private ArrayList<CartaInglesa> manoJugador1;
    private ArrayList<CartaInglesa> manoJugador2;
    private ArrayList<CartaInglesa> manoJugador3;
    private ArrayList<CartaInglesa> manoJugador4;
    private ArrayList<CartaInglesa> manoDealer;
    private int cantidadJugadores;
    private int turno;
    private boolean terminado;
    private String resultado;

    //PILA
    private Pila<Movimiento> movimientos;

    public Juego21() {
        iniciarNuevoJuego(1);
    }

    /*
     * reparte las cartas a los jugadores y dealer 2c/u
     * la primera carta del dealer queda boca abajo y la otra boca arriba
     * cantidad de jugadores puede ser de 1-4 + dealer
     */
    public void iniciarNuevoJuego(int cantidad) {
        cantidadJugadores = cantidad;
        mazo = new Mazo();
        manoJugador1 = new ArrayList<>();
        manoJugador2 = new ArrayList<>();
        manoJugador3 = new ArrayList<>();
        manoJugador4 = new ArrayList<>();
        manoDealer = new ArrayList<>();
        turno = 0;
        terminado = false;
        resultado = "";
        movimientos = new Pila<>(50);

        for (int i = 0; i < cantidadJugadores; i++) {
            darCartaAJugador(i);
            darCartaAJugador(i);
        }
        darCartaAlDealer(false);
        darCartaAlDealer(true);
    }

    //metodo para pedir la mano del jugador en turno
    private ArrayList<CartaInglesa> getMano(int i) {
        if (i == 0) {
            return manoJugador1;
        } else if (i == 1) {
            return manoJugador2;
        } else if (i == 2) {
            return manoJugador3;
        } else {
            return manoJugador4;
        }
    }


    private void darCartaAlDealer(boolean bocaArriba) {
        CartaInglesa carta = mazo.obtenerUnaCarta();
        if (bocaArriba) {
            carta.makeFaceUp();
        } else {
            carta.makeFaceDown();
        }
        manoDealer.add(carta);
    }

    //metodo para pedir carta si el jugador lo cree necesario
    public void pedirCarta() {
        if (terminado) {
            return;
        }
        movimientos.push(new Movimiento("pedir",turno));
        darCartaAJugador(turno);
        if (getPuntosJugador(turno) > 21) {
            pasarTurno();
        }
    }

    //jugador en turno se queda con las cartas de su mano y pasa
    public void quedarse() {
        if (terminado) {
            return;
        }
        movimientos.push(new Movimiento("quedarse",turno));
        pasarTurno();
    }

    private void pasarTurno() {
        turno = turno + 1;
        if (turno >= cantidadJugadores) {
            jugarTurnoDelDealer();
        }
    }

    //este metodo es para el dealer hacerlo de forma "automática"
    private void jugarTurnoDelDealer() {
        for (CartaInglesa carta : manoDealer) {
            carta.makeFaceUp();
        }
        //mientras los puntos sea menos de 17 pide carta
        while (getPuntosDealer() < 17) {
            darCartaAlDealer(true);
        }
        terminado = true;
        armarResultado();
    }

    //revisa si una mano es Blackjack 21 exacto
    private boolean esBlackjack(ArrayList<CartaInglesa> mano) {
        return mano.size() == 2 && calcularPuntos(mano) == 21;
    }

    /**
     * en este metodo se define si hay mano ganadora:
     * opcion 1 si alguien tiene Blackjack
     * opcion 2 si no ganan los que se acerquen mas al 21 sin pasarse de 21
     */
    private boolean esGanadora(ArrayList<CartaInglesa> mano, int mejorPuntaje, boolean hayBlackjack) {
        if (hayBlackjack) {
            return esBlackjack(mano);
        }
        int puntos = calcularPuntos(mano);
        return puntos <= 21 && puntos == mejorPuntaje;
    }

    //aqui se determina el resultado final de la partida
    private void armarResultado() {
        int puntosDealer = getPuntosDealer();

        //1. mejor puntuación sin pasarse de 21
        int mejorPuntaje = 0;
        for (int i = 0; i < cantidadJugadores; i++) {
            int puntos = getPuntosJugador(i);
            if (puntos <= 21 && puntos > mejorPuntaje) {
                mejorPuntaje = puntos;
            }
        }
        if (puntosDealer <= 21 && puntosDealer > mejorPuntaje) {
            mejorPuntaje = puntosDealer;
        }

        //2. si alguien tiene Blackjack
        boolean hayBlackjack = esBlackjack(manoDealer);
        for (int i = 0; i < cantidadJugadores; i++) {
            if (esBlackjack(getMano(i))) {
                hayBlackjack = true;
            }
        }

        //3.lista de ganadores incluyendo empates
        String ganadores = "";
        int cuantosGanan = 0;
        for (int i = 0; i < cantidadJugadores; i++) {
            if (esGanadora(getMano(i), mejorPuntaje, hayBlackjack)) {
                if (cuantosGanan > 0) {
                    ganadores = ganadores + " y ";
                }
                ganadores = ganadores + "Jugador " + (i + 1);
                cuantosGanan = cuantosGanan + 1;
            }
        }
        if (esGanadora(manoDealer, mejorPuntaje, hayBlackjack)) {
            if (cuantosGanan > 0) {
                ganadores = ganadores + " y ";
            }
            ganadores = ganadores + "el Dealer";
            cuantosGanan = cuantosGanan + 1;
        }
        //texto de estadistica de la partida en puntos
        resultado = "Dealer: " + puntosDealer;
        if (puntosDealer > 21) {
            resultado = resultado;
        }
        for (int i = 0; i < cantidadJugadores; i++) {
            int puntos = getPuntosJugador(i);
            resultado = resultado + "  |  Jugador " + (i + 1) + ": " + puntos;
            if (puntos > 21) {
                resultado = resultado;
            }
        }
        //ganador y con que tipo de jugada gano
        if (cuantosGanan == 0) {
            resultado = resultado + "\nTodos se pasaron de 21, no hay ganador.";
        } else if (cuantosGanan == 1) {
            if (hayBlackjack) {
                resultado = resultado + "\n¡Gana " + ganadores + " con Blackjack!";
            } else {
                resultado = resultado + "\n¡Gana " + ganadores + " con " + mejorPuntaje + " puntos!";
            }
        } else {
            if (hayBlackjack) {
                resultado = resultado + "\nEmpate con Blackjack entre " + ganadores + ".";
            } else {
                resultado = resultado + "\nEmpate entre " + ganadores + " con " + mejorPuntaje + " puntos.";
            }
        }
    }

    private int calcularPuntos(ArrayList<CartaInglesa> mano) {
        int total = 0;
        int cantidadDeAses = 0;

        for (CartaInglesa carta : mano) {
            int valor = carta.getValor();
            if (valor == 14) {
                //As vale 11
                total = total + 11;
                cantidadDeAses = cantidadDeAses + 1;
            } else if (valor == 11 || valor == 12 || valor == 13) {
                //J, Q y K, vale 10
                total = total + 10;
            } else {
                total = total + valor;
            }
        }
        //si se pasa de 21 cada As deja de valer 11 y ahora vale 1
        while (total > 21 && cantidadDeAses > 0) {
            total = total - 10;
            cantidadDeAses = cantidadDeAses - 1;
        }
        return total;
    }

    public int getCantidadJugadores() {
        return cantidadJugadores;
    }

    public int getTurno() {
        return turno;
    }

    public ArrayList<CartaInglesa> getManoJugador(int i) {
        return getMano(i);
    }

    public int getPuntosJugador(int i) {
        return calcularPuntos(getMano(i));
    }

    public ArrayList<CartaInglesa> getManoDealer() {
        return manoDealer;
    }
    public int getPuntosDealer() {
        return calcularPuntos(manoDealer);
    }
    public boolean isTerminado() {
        return terminado;
    }
    public String getResultado() {
        return resultado;
    }

    private void darCartaAJugador(int i) {
        CartaInglesa carta = mazo.obtenerUnaCarta();
        carta.makeFaceUp();
        getMano(i).add(carta);
    }

    //IMPLEMENTACION DE LA PILA PARA REVERTIR LOS MOVIMIENTOS
    public void deshacerMovimiento(){
        if (terminado || movimientos.pilaVacia()) {
            return;
        }

        Movimiento ultimo = movimientos.pop();
        int jugador = ultimo.getJugador();

        if (ultimo.getTipo().equals("pedir")) {
            ArrayList<CartaInglesa> mano = getMano(jugador);
            CartaInglesa carta = mano.remove(mano.size()-1);
            carta.makeFaceDown();
            //mazo.getCartas().add(0,carta);
            mazo.getCartas().add(carta);
            mazo.mezclar();
        }
        turno = jugador;
    }

    public boolean hayMovimientos(){
        return !movimientos.pilaVacia();
    }

}