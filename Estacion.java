package org.example.practica3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Estacion {
    private String nombre;
    private ColaCircular<Pieza> colaEntrada;
    private int dadosAsignados;
    private List<Integer> resultadosDados;
    private int ultimasPiezasProcesadas;

    public Estacion(String nombre, int capacidadCola) {
        this.nombre = nombre;
        this.colaEntrada = new ColaCircular<>(capacidadCola);
        this.dadosAsignados = 1;
        this.resultadosDados = new ArrayList<>();
        this.resultadosDados.add(1);
        this.ultimasPiezasProcesadas = 0;
    }

    public void agregarDado() {
        this.dadosAsignados++;
    }

    public void quitarDado() {
        if (this.dadosAsignados > 0) {
            this.dadosAsignados--;
        }
    }

    public void lanzarDados(Random rand) {
        resultadosDados.clear();
        if (dadosAsignados == 0) {
            resultadosDados.add(0);
        } else {
            for (int i = 0; i < dadosAsignados; i++) {
                resultadosDados.add(rand.nextInt(6) + 1);
            }
        }
    }

    public String getNombre() {
        return nombre;
    }

    public ColaCircular<Pieza> getColaEntrada() {
        return colaEntrada;
    }

    public int getDadosAsignados() {
        return dadosAsignados;
    }

    public List<Integer> getResultadosDados() {
        return resultadosDados;
    }

    public int getUltimasPiezasProcesadas() {
        return ultimasPiezasProcesadas;
    }

    public void setUltimasPiezasProcesadas(int ultimasPiezasProcesadas) {
        this.ultimasPiezasProcesadas = ultimasPiezasProcesadas;
    }
}