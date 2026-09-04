package blackjack.practica1_alg20262.Vista;

import blackjack.practica1_alg20262.Modelo.DeckOfCards.CartaInglesa;
import blackjack.practica1_alg20262.Modelo.Juego21;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Juego21FX {

    private int anchoCarta = 78;
    private int[] posicionX = {30, 30, 380, 380};
    private int[] posicionY = {40, 280, 280, 40};
    private int dealerX = 640;
    private int dealerY = 40;

    private VBox[] zonasJugadores;
    private Label[] titulosJugadores;
    private HBox[] cartasJugadores;

    private Label tituloDealer;
    private HBox cartasDealer;

    private Label mensajeLabel;
    private Button pedirBtn;
    private Button quedarseBtn;
    private Button nuevo1Btn;
    private Button nuevo2Btn;
    private Button nuevo3Btn;
    private Button nuevo4Btn;
    private Scene escena;

    public Juego21FX() {
        construirPantalla();
    }

    private void construirPantalla() {
        Pane mesa = new Pane();
        mesa.setStyle("-fx-background-color: #0b6623;"); // verde de mesa

        //jugadores, los que no jueguen se ocultan
        zonasJugadores = new VBox[4];
        titulosJugadores = new Label[4];
        cartasJugadores = new HBox[4];

        for (int i = 0; i < 4; i++) {
            titulosJugadores[i] = new Label("Jugador " + (i + 1));
            cartasJugadores[i] = new HBox(8);

            zonasJugadores[i] = new VBox(5);
            zonasJugadores[i].getChildren().add(titulosJugadores[i]);
            zonasJugadores[i].getChildren().add(cartasJugadores[i]);
            zonasJugadores[i].setLayoutX(posicionX[i]);
            zonasJugadores[i].setLayoutY(posicionY[i]);

            mesa.getChildren().add(zonasJugadores[i]);
        }

        //dealer
        tituloDealer = new Label("Dealer");
        tituloDealer.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        cartasDealer = new HBox(8);

        VBox zonaDealer = new VBox(5);
        zonaDealer.getChildren().add(tituloDealer);
        zonaDealer.getChildren().add(cartasDealer);
        zonaDealer.setLayoutX(dealerX);
        zonaDealer.setLayoutY(dealerY);
        mesa.getChildren().add(zonaDealer);

        //mensaje resultado
        mensajeLabel = new Label();
        mensajeLabel.setStyle("-fx-text-fill: yellow; -fx-font-size: 14px; -fx-font-weight: bold;");

        //botones de juego
        pedirBtn = new Button("Pedir carta");
        quedarseBtn = new Button("Quedarse");

        //botones partida con 1-4 jugadores
        Label nuevoLabel = new Label("Nuevo juego con:");
        nuevoLabel.setStyle("-fx-text-fill: white;");
        nuevo1Btn = new Button("1 jugador");
        nuevo2Btn = new Button("2 jugadores");
        nuevo3Btn = new Button("3 jugadores");
        nuevo4Btn = new Button("4 jugadores");

        HBox botones = new HBox(12, pedirBtn, quedarseBtn,
                nuevoLabel, nuevo1Btn, nuevo2Btn, nuevo3Btn, nuevo4Btn);
        botones.setAlignment(Pos.CENTER);

        //parte de abajo mensaje + botones
        VBox parteDeAbajo = new VBox(8);
        parteDeAbajo.getChildren().add(mensajeLabel);
        parteDeAbajo.getChildren().add(botones);
        parteDeAbajo.setAlignment(Pos.CENTER);
        parteDeAbajo.setPadding(new Insets(10));
        parteDeAbajo.setStyle("-fx-background-color: #094f1c;"); // verde más oscuro

        BorderPane raiz = new BorderPane();
        raiz.setCenter(mesa);
        raiz.setBottom(parteDeAbajo);

        escena = new Scene(raiz, 1000, 660);
    }

    public void actualizarPantalla(Juego21 juego) {
        //jugadores
        for (int i = 0; i < 4; i++) {
            if (i < juego.getCantidadJugadores()) {
                zonasJugadores[i].setVisible(true);
                dibujarZonaDeJugador(juego, i);
            } else {
                zonasJugadores[i].setVisible(false);
            }
        }

        //dealer
        cartasDealer.getChildren().clear();
        for (CartaInglesa carta : juego.getManoDealer()) {
            cartasDealer.getChildren().add(crearVistaDeCarta(carta));
        }
        if (juego.isTerminado()) {
            tituloDealer.setText("Dealer: " + juego.getPuntosDealer() + " puntos");
        } else {
            tituloDealer.setText("Dealer: ? puntos");
        }

        //mensaje  botones
        if (juego.isTerminado()) {
            mensajeLabel.setText(juego.getResultado());
            pedirBtn.setDisable(true);
            quedarseBtn.setDisable(true);
        } else {
            mensajeLabel.setText("Turno del Jugador " + (juego.getTurno() + 1));
            pedirBtn.setDisable(false);
            quedarseBtn.setDisable(false);
        }
    }

    private void dibujarZonaDeJugador(Juego21 juego, int i) {
        int puntos = juego.getPuntosJugador(i);
        String texto = "Jugador " + (i + 1) + ": " + puntos + " puntos";
        if (puntos > 21) {
            texto = texto + " (se pasó)";
        }
        titulosJugadores[i].setText(texto);

        //jugador en turno amarillo el resto blancos
        if (!juego.isTerminado() && i == juego.getTurno()) {
            titulosJugadores[i].setStyle("-fx-text-fill: yellow; -fx-font-size: 15px; -fx-font-weight: bold;");
        } else {
            titulosJugadores[i].setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        }

        cartasJugadores[i].getChildren().clear();
        for (CartaInglesa carta : juego.getManoJugador(i)) {
            cartasJugadores[i].getChildren().add(crearVistaDeCarta(carta));
        }
    }

    private ImageView crearVistaDeCarta(CartaInglesa carta) {
        String nombreArchivo;
        if (carta.isFaceup()) {
            nombreArchivo = nombreArchivoDeCarta(carta);
        } else {
            nombreArchivo = "back.png";
        }

        Image imagen = new Image(getClass().getResourceAsStream("/Cartas/" + nombreArchivo));
        ImageView vista = new ImageView(imagen);
        vista.setFitWidth(anchoCarta);
        vista.setPreserveRatio(true);
        return vista;
    }

    private String nombreArchivoDeCarta(CartaInglesa carta) {
        String palo = carta.getPalo().name();
        String letraPalo;
        if (palo.equals("CORAZON")) {
            letraPalo = "c";
        } else if (palo.equals("DIAMANTE")) {
            letraPalo = "d";
        } else if (palo.equals("PICA")) {
            letraPalo = "p";
        } else {
            letraPalo = "t";
        }

        int valor = carta.getValor();
        String valorTexto;
        if (valor == 11) {
            valorTexto = "j";
        } else if (valor == 12) {
            valorTexto = "q";
        } else if (valor == 13) {
            valorTexto = "k";
        } else {
            valorTexto = "" + valor;
        }

        return letraPalo + valorTexto + ".png";
    }

    //Getters para controlador botones
    public Button getPedirBtn() {
        return pedirBtn;
    }

    public Button getQuedarseBtn() {
        return quedarseBtn;
    }

    public Button getNuevo1Btn() {
        return nuevo1Btn;
    }

    public Button getNuevo2Btn() {
        return nuevo2Btn;
    }

    public Button getNuevo3Btn() {
        return nuevo3Btn;
    }

    public Button getNuevo4Btn() {
        return nuevo4Btn;
    }

    public Scene getEscena() {
        return escena;
    }
}