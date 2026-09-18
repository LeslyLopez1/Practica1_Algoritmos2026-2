package blackjack.practica1_alg20262.Modelo;

public class Movimiento {
    private String tipo;
    private int jugador;

    public Movimiento(String tipo, int jugador) {
        this.tipo = tipo;
        this.jugador=jugador;
    }

    public String getTipo() {
        return tipo;
    }

    public int getJugador() {
        return jugador;
    }

    @Override
    public String toString() {
        return tipo + "( Jugador" + (jugador + 1) + ")";
    }
}
