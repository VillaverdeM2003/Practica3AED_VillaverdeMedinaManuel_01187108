package org.example.practica3;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.InputStream;
import java.util.List;

public class CasillaEstacionFX extends VBox {
    private final Estacion estacion;
    private final SimuladorEngine engine;
    private final Runnable onUpdate;

    private final Label lblNombre;
    private final Label lblWip;
    private final Label lblDados;
    private final HBox panelDados;
    private final HBox panelVisualWip;
    private final StackPane contenedorAvatar;

    public CasillaEstacionFX(Estacion estacion, SimuladorEngine engine, String nombrePersonaje, String rutaImagen, Runnable onUpdate) {
        this.estacion = estacion;
        this.engine = engine;
        this.onUpdate = onUpdate;

        setSpacing(4);
        setPadding(new Insets(6));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: #EFEBE0; -fx-border-color: #B0A8A0; -fx-border-width: 1.5; -fx-border-radius: 6; -fx-background-radius: 6;");
        setPrefWidth(175);
        setMaxWidth(180);
        setPrefHeight(190);


        lblNombre = new Label(nombrePersonaje);
        lblNombre.setFont(Font.font("SansSerif", FontWeight.BOLD, 12));

        contenedorAvatar = crearContenedorAvatar(nombrePersonaje, rutaImagen);

        panelDados = new HBox(3);
        panelDados.setAlignment(Pos.CENTER);
        panelDados.setMinHeight(28);

        panelVisualWip = new HBox(1);
        panelVisualWip.setAlignment(Pos.CENTER_LEFT);
        panelVisualWip.setPrefHeight(12);
        panelVisualWip.setMaxWidth(155);
        panelVisualWip.setStyle("-fx-background-color: #E0DAD0; -fx-border-color: #C0B8A8; -fx-border-radius: 2;");

        lblWip = new Label("WIP: 0");
        lblWip.setFont(Font.font("SansSerif", FontWeight.BOLD, 11));

        Button btnMenos = new Button("-");
        btnMenos.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2 6 2 6;");
        btnMenos.setOnAction(e -> {
            if (estacion.getDadosAsignados() > 0) {
                estacion.quitarDado();
                if (onUpdate != null) onUpdate.run();
            }
        });

        lblDados = new Label("Dados: 1");
        lblDados.setFont(Font.font("SansSerif", FontWeight.NORMAL, 11));

        Button btnMas = new Button("+");
        btnMas.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2 6 2 6;");
        btnMas.setOnAction(e -> {
            if (engine.getDadosDisponiblesEnReserva() > 0) {
                estacion.agregarDado();
                if (onUpdate != null) onUpdate.run();
            }
        });

        HBox panelControles = new HBox(5, btnMenos, lblDados, btnMas);
        panelControles.setAlignment(Pos.CENTER);

        getChildren().addAll(lblNombre, contenedorAvatar, panelDados, panelVisualWip, lblWip, panelControles);
        actualizar();
    }

    private StackPane crearContenedorAvatar(String nombre, String rutaImagen) {
        StackPane pane = new StackPane();
        pane.setPrefSize(40, 40);

        Image img = intentarCargarImagen(rutaImagen);
        if (img != null) {
            ImageView iv = new ImageView(img);
            iv.setFitWidth(40);
            iv.setFitHeight(40);
            iv.setPreserveRatio(true);
            pane.getChildren().add(iv);
        } else {
            // Respaldar con badge circular de iniciales si la imagen no se encuentra en el Path
            Circle circle = new Circle(18, Color.web("#8C2B2B"));
            Label lblIniciales = new Label(obtenerIniciales(nombre));
            lblIniciales.setTextFill(Color.WHITE);
            lblIniciales.setFont(Font.font("SansSerif", FontWeight.BOLD, 11));
            pane.getChildren().addAll(circle, lblIniciales);
        }
        return pane;
    }

    private Image intentarCargarImagen(String ruta) {
        if (ruta == null || ruta.isEmpty()) return null;
        try {
            InputStream is = getClass().getResourceAsStream(ruta);
            if (is != null) return new Image(is);

            String sinSlash = ruta.startsWith("/") ? ruta.substring(1) : ruta;
            is = getClass().getClassLoader().getResourceAsStream(sinSlash);
            if (is != null) return new Image(is);

            is = getClass().getResourceAsStream("/" + sinSlash);
            if (is != null) return new Image(is);
        } catch (Exception ignored) {}
        return null;
    }

    private String obtenerIniciales(String nombre) {
        if (nombre == null || nombre.isEmpty()) return "?";
        String[] partes = nombre.trim().split("\\s+");
        if (partes.length >= 2) {
            return ("" + partes[0].charAt(0) + partes[1].charAt(0)).toUpperCase();
        }
        return ("" + partes[0].charAt(0)).toUpperCase();
    }

    public void actualizar() {
        lblDados.setText("Dados: " + estacion.getDadosAsignados());

        int wip = estacion.getColaEntrada().getTamanio();
        lblWip.setText("WIP: " + wip);

        panelVisualWip.getChildren().clear();
        int bloquesAMostrar = Math.min(wip, 18);
        for (int i = 0; i < bloquesAMostrar; i++) {
            Rectangle r = new Rectangle(5, 10, Color.web("#3A75C4"));
            panelVisualWip.getChildren().add(r);
        }

        panelDados.getChildren().clear();
        List<Integer> resultados = estacion.getResultadosDados();
        for (int res : resultados) {
            panelDados.getChildren().add(crearVistaDado(res));
        }
    }

    private StackPane crearVistaDado(int valor) {
        StackPane dadoPane = new StackPane();
        dadoPane.setPrefSize(25, 25);
        dadoPane.setStyle("-fx-background-color: #D32F2F; -fx-background-radius: 4; -fx-border-color: #B71C1C; -fx-border-radius: 3;");

        GridPane gridDots = new GridPane();
        gridDots.setAlignment(Pos.CENTER);
        gridDots.setHgap(1.5);
        gridDots.setVgap(1.5);

        boolean[][] dots = getPuntosDado(valor);
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (dots[r][c]) {
                    Rectangle dot = new Rectangle(3, 3, Color.WHITE);
                    dot.setArcWidth(3);
                    dot.setArcHeight(3);
                    gridDots.add(dot, c, r);
                } else {
                    Rectangle empty = new Rectangle(3, 3, Color.TRANSPARENT);
                    gridDots.add(empty, c, r);
                }
            }
        }

        dadoPane.getChildren().add(gridDots);
        return dadoPane;
    }

    private boolean[][] getPuntosDado(int v) {
        boolean[][] p = new boolean[3][3];
        switch (v) {
            case 1 -> p[1][1] = true;
            case 2 -> { p[0][0] = true; p[2][2] = true; }
            case 3 -> { p[0][0] = true; p[1][1] = true; p[2][2] = true; }
            case 4 -> { p[0][0] = true; p[0][2] = true; p[2][0] = true; p[2][2] = true; }
            case 5 -> { p[0][0] = true; p[0][2] = true; p[1][1] = true; p[2][0] = true; p[2][2] = true; }
            case 6 -> { p[0][0] = true; p[0][2] = true; p[1][0] = true; p[1][2] = true; p[2][0] = true; p[2][2] = true; }
        }
        return p;
    }
}