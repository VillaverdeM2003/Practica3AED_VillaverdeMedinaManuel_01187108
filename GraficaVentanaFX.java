package org.example.practica3;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;

public class GraficaVentanaFX {

    public static void mostrar(List<? extends Number> datos, String titulo, String nombreEjeY) {
        Stage stage = new Stage();
        stage.setTitle(titulo);

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Turno / Ronda");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(nombreEjeY);

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(titulo);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(nombreEjeY);

        for (int i = 0; i < datos.size(); i++) {
            series.getData().add(new XYChart.Data<>(i, datos.get(i)));
        }

        lineChart.getData().add(series);

        StackPane root = new StackPane(lineChart);
        Scene scene = new Scene(root, 620, 420);

        stage.setScene(scene);
        stage.show();
    }
}