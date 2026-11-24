package org.example.practica3;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class VentanaGame2FX extends Application {
    private static final int MAX_RONDAS = 20;

    private SimuladorEngine engine;
    private List<CasillaEstacionFX> casillas;
    private Label lblTurnsValue;
    private Label lblReservaDados;
    private TextArea areaLog;
    private Button btnAction;

    @Override
    public void start(Stage primaryStage) {
        engine = new SimuladorEngine(9, 35);
        casillas = new ArrayList<>();

        BorderPane mainLayout = new BorderPane();


        HBox panelHeader = new HBox();
        panelHeader.setPadding(new Insets(8, 15, 8, 15));
        panelHeader.setStyle("-fx-background-color: #F5F0E6;");

        Label lblTitulo = new Label("Juego 2: Reasignación de recursos");
        lblTitulo.setFont(Font.font("Serif", FontWeight.BOLD, 20));

        lblReservaDados = new Label("Reserva de Dados Disponibles: 0 / 9");
        lblReservaDados.setFont(Font.font("SansSerif", FontWeight.BOLD, 13));
        lblReservaDados.setTextFill(Color.web("#B22222"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        panelHeader.getChildren().addAll(lblTitulo, spacer, lblReservaDados);
        mainLayout.setTop(panelHeader);


        GridPane panelTablero = new GridPane();
        panelTablero.setHgap(8);
        panelTablero.setVgap(8);
        panelTablero.setPadding(new Insets(10));
        panelTablero.setStyle("-fx-background-color: #D2CDBE;");
        panelTablero.setAlignment(Pos.CENTER);

        String[] personajes = {
                "Michael", "Dwight", "Jim", "Pam",
                "Ryan", "Stanley", "Phyllis", "Angela", "The Warehouse"
        };

        String[] rutasImagenes = {
                "/images/MichaelScott.png",
                "/images/dwigth.jpg",
                "/images/jim.jpg",
                "/images/pam.jpg",
                "/images/ryan.jpg",
                "/images/stanley.jpg",
                "/images/phyllis.jpg",
                "/images/angela.jpg",
                "/images/the_office_warehouse_workers.jpg"
        };

        List<Estacion> ests = engine.getEstaciones();
        CasillaEstacionFX[] casillasArr = new CasillaEstacionFX[9];

        for (int i = 0; i < 9; i++) {
            casillasArr[i] = new CasillaEstacionFX(ests.get(i), engine, personajes[i], rutasImagenes[i], this::actualizarUI);
            casillas.add(casillasArr[i]);
        }

        panelTablero.add(casillasArr[0], 0, 0); // Michael
        panelTablero.add(casillasArr[1], 1, 0); // Dwight
        panelTablero.add(casillasArr[2], 2, 0); // Jim
        panelTablero.add(casillasArr[3], 3, 0); // Pam
        panelTablero.add(casillasArr[4], 3, 1); // Ryan
        panelTablero.add(casillasArr[5], 3, 2); // Stanley
        panelTablero.add(casillasArr[6], 2, 2); // Phyllis
        panelTablero.add(casillasArr[7], 1, 2); // Angela
        panelTablero.add(casillasArr[8], 0, 2); // Kevin

        mainLayout.setCenter(panelTablero);

        VBox panelLateral = new VBox(8);
        panelLateral.setPadding(new Insets(12, 10, 12, 10));
        panelLateral.setStyle("-fx-background-color: #3C1E1E;");
        panelLateral.setPrefWidth(170);
        panelLateral.setAlignment(Pos.TOP_CENTER);

        Button btnReset = crearBotonLateral("Reset");
        Button btnActivity = crearBotonLateral("Activity");
        Button btnThroughput = crearBotonLateral("Throughput");
        Button btnWip = crearBotonLateral("Number in system");
        Button btnLeadTime = crearBotonLateral("Time in system");
        Button btnPerformance = crearBotonLateral("Your performance");

        Label lblTurns = new Label("Turns");
        lblTurns.setTextFill(Color.WHITE);
        lblTurns.setFont(Font.font("SansSerif", FontWeight.BOLD, 13));

        lblTurnsValue = new Label("0");
        lblTurnsValue.setTextFill(Color.WHITE);
        lblTurnsValue.setFont(Font.font("SansSerif", FontWeight.BOLD, 24));

        btnAction = new Button("Roll");
        btnAction.setStyle("-fx-background-color: #E6DC82; -fx-font-weight: bold; -fx-font-size: 15px;");
        btnAction.setPrefSize(130, 38);

        btnAction.setOnAction(e -> ejecutarCicloAccion());

        btnReset.setOnAction(e -> {
            engine.resetSimulacion(35);
            btnAction.setDisable(false);
            btnAction.setText("Roll");
            lblTurnsValue.setText("0");
            areaLog.setText("Simulación reiniciada a Turno 0.\n");
            actualizarUI();
        });

        btnPerformance.setOnAction(e -> mostrarDesempenio());
        btnThroughput.setOnAction(e -> GraficaVentanaFX.mostrar(engine.getHistorialThroughput(), "Evolución de Throughput por Ronda", "Throughput Acumulado"));
        btnWip.setOnAction(e -> GraficaVentanaFX.mostrar(engine.getHistorialWip(), "Work In Progress (WIP) en el Sistema", "Piezas en Colas"));
        btnLeadTime.setOnAction(e -> GraficaVentanaFX.mostrar(engine.getHistorialLeadTime(), "Tiempo de Ciclo Promedio (Lead Time)", "Turnos Promedio"));
        btnActivity.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Fase actual: " + engine.getFaseActual() + "\nTurno: " + engine.getRondaActual() + " / " + MAX_RONDAS);
            alert.showAndWait();
        });

        Region space = new Region();
        VBox.setVgrow(space, Priority.ALWAYS);

        panelLateral.getChildren().addAll(btnReset, btnActivity, btnThroughput, btnWip, btnLeadTime, btnPerformance, space, lblTurns, lblTurnsValue, btnAction);
        mainLayout.setRight(panelLateral);

        areaLog = new TextArea();
        areaLog.setEditable(false);
        areaLog.setPrefRowCount(3);
        mainLayout.setBottom(areaLog);

        actualizarUI();

        Scene scene = new Scene(mainLayout, 1020, 700);
        primaryStage.setTitle("Juego 2 - Reasignación de Recursos (JavaFX)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button crearBotonLateral(String texto) {
        Button b = new Button(texto);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setStyle("-fx-background-color: #D2C8B9; -fx-font-size: 11px;");
        return b;
    }

    private void ejecutarCicloAccion() {
        if (!engine.validarAsignacionDados()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Has excedido el total de dados asignados a la reserva.");
            alert.showAndWait();
            return;
        }

        if (engine.getFaseActual() == SimuladorEngine.Fase.ESPERANDO_ROLL) {
            if (engine.getRondaActual() >= MAX_RONDAS) {
                btnAction.setDisable(true);
                mostrarDesempenio();
                return;
            }
            engine.ejecutarRoll();
            btnAction.setText("Move");
            areaLog.appendText("--- Lanza dados (Turno " + (engine.getRondaActual() + 1) + ") ---\n");
        } else {
            engine.ejecutarMove();
            btnAction.setText("Roll");
            lblTurnsValue.setText(String.valueOf(engine.getRondaActual()));

            areaLog.appendText("--- Mueve piezas (Turno " + engine.getRondaActual() + ") ---\n");
            areaLog.appendText("WIP Total: " + engine.getWipTotal() + " | Throughput: " + engine.getThroughputTotal() + "\n\n");

            if (engine.getRondaActual() >= MAX_RONDAS) {
                btnAction.setText("Fin (20/20)");
                btnAction.setDisable(true);
                mostrarDesempenio();
            }
        }

        actualizarUI();
    }

    private void mostrarDesempenio() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Your Performance");
        alert.setHeaderText("Resumen Final del Desempeño (20 Rondas)");
        alert.setContentText(String.format(
                "Throughput Total Alcanzado: %d piezas\n" +
                        "WIP Remanente en Sistema: %d piezas\n" +
                        "Lead Time Promedio: %.2f turnos",
                engine.getThroughputTotal(), engine.getWipTotal(), engine.getLeadTimePromedio()
        ));
        alert.showAndWait();
    }

    private void actualizarUI() {
        lblReservaDados.setText("Reserva de Dados Disponibles: " + engine.getDadosDisponiblesEnReserva() + " / " + engine.getTotalDadosDisponibles());
        for (CasillaEstacionFX c : casillas) {
            c.actualizar();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}