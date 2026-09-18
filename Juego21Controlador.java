package blackjack.practica1_alg20262.Controlador;

import blackjack.practica1_alg20262.Modelo.Juego21;
import blackjack.practica1_alg20262.Vista.Juego21FX;

import javafx.application.Application;
import javafx.stage.Stage;

public class Juego21Controlador extends Application {

    private Juego21 modelo;
    private Juego21FX vista;

    @Override
    public void start(Stage stage) {
        modelo = new Juego21();
        vista = new Juego21FX();

        conectarBotones();
        vista.actualizarPantalla(modelo);

        stage.setTitle("Blackjack 21");
        stage.setScene(vista.getEscena());
        stage.show();
    }

    private void conectarBotones() {
        vista.getPedirBtn().setOnAction(evento -> pedirCarta());
        vista.getQuedarseBtn().setOnAction(evento -> quedarse());
        vista.getDeshacerBtn().setOnAction(evento -> deshacer());
        vista.getNuevo1Btn().setOnAction(evento -> nuevoJuego(1));
        vista.getNuevo2Btn().setOnAction(evento -> nuevoJuego(2));
        vista.getNuevo3Btn().setOnAction(evento -> nuevoJuego(3));
        vista.getNuevo4Btn().setOnAction(evento -> nuevoJuego(4));
    }

    private void pedirCarta() {
        modelo.pedirCarta();
        vista.actualizarPantalla(modelo);
    }

    private void quedarse() {
        modelo.quedarse();
        vista.actualizarPantalla(modelo);
    }

    private void deshacer() {
        modelo.deshacerMovimiento();
        vista.actualizarPantalla(modelo);
    }

    private void nuevoJuego(int cantidadJugadores) {
        modelo.iniciarNuevoJuego(cantidadJugadores);
        vista.actualizarPantalla(modelo);
    }

    public static void main(String[] args) {
        launch(args);
    }
}