package org.example.practica3;

public class Pieza {
    private static int contadorId = 1;
    private final int id;
    private final int turnoEntrada;

    public Pieza(int turnoEntrada) {
        this.id = contadorId++;
        this.turnoEntrada = turnoEntrada;
    }

    public int getId() {
        return id;
    }

    public int getTurnoEntrada() {
        return turnoEntrada;
    }
}