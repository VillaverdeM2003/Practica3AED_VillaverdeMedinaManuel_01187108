package org.example.practica3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimuladorEngine {
    public enum Fase {
        ESPERANDO_ROLL, ESPERANDO_MOVE
    }

    private final List<Estacion> estaciones;
    private final int totalDadosDisponibles;
    private int throughputTotal;
    private int rondaActual;
    private double leadTimeAcumulado;
    private final Random rand;
    private Fase faseActual;

    private final List<Integer> historialThroughput;
    private final List<Integer> historialWip;
    private final List<Double> historialLeadTime;

    public SimuladorEngine(int numEstaciones, int piezasIniciales) {
        this.estaciones = new ArrayList<>();
        this.totalDadosDisponibles = numEstaciones;
        this.rand = new Random();

        this.historialThroughput = new ArrayList<>();
        this.historialWip = new ArrayList<>();
        this.historialLeadTime = new ArrayList<>();

        for (int i = 1; i <= numEstaciones; i++) {
            estaciones.add(new Estacion("Estación " + i, 300));
        }

        resetSimulacion(piezasIniciales);
    }

    public void resetSimulacion(int piezasIniciales) {
        this.throughputTotal = 0;
        this.rondaActual = 0;
        this.leadTimeAcumulado = 0;
        this.faseActual = Fase.ESPERANDO_ROLL;

        historialThroughput.clear();
        historialWip.clear();
        historialLeadTime.clear();


        for (Estacion e : estaciones) {
            while (!e.getColaEntrada().estaVacia()) {
                e.getColaEntrada().eliminaCircular();
            }
            e.getResultadosDados().clear();
            e.getResultadosDados().add(1);
            e.setUltimasPiezasProcesadas(0);


            for (int p = 0; p < 5; p++) {
                e.getColaEntrada().insertaCircular(new Pieza(0));
            }
        }

        guardarEstadoHistorial();
    }


    public void ejecutarRoll() {
        if (faseActual != Fase.ESPERANDO_ROLL) return;

        for (Estacion e : estaciones) {
            e.lanzarDados(rand);
        }
        faseActual = Fase.ESPERANDO_MOVE;
    }


    public void ejecutarMove() {
        if (faseActual != Fase.ESPERANDO_MOVE) return;

        rondaActual++;


        while (estaciones.get(0).getColaEntrada().getTamanio() < 10) {
            estaciones.get(0).getColaEntrada().insertaCircular(new Pieza(rondaActual));
        }


        for (int i = estaciones.size() - 1; i >= 0; i--) {
            Estacion estacionActual = estaciones.get(i);
            int capacidadProcesamiento = 0;
            for (int val : estacionActual.getResultadosDados()) {
                capacidadProcesamiento += val;
            }

            int piezasDisponibles = estacionActual.getColaEntrada().getTamanio();
            int piezasAMover = Math.min(capacidadProcesamiento, piezasDisponibles);

            for (int p = 0; p < piezasAMover; p++) {
                Pieza pieza = estacionActual.getColaEntrada().eliminaCircular();
                if (pieza != null) {
                    if (i == estaciones.size() - 1) {
                        throughputTotal++;
                        leadTimeAcumulado += (rondaActual - pieza.getTurnoEntrada());
                    } else {
                        estaciones.get(i + 1).getColaEntrada().insertaCircular(pieza);
                    }
                }
            }
            estacionActual.setUltimasPiezasProcesadas(piezasAMover);
        }

        guardarEstadoHistorial();
        faseActual = Fase.ESPERANDO_ROLL;
    }

    private void guardarEstadoHistorial() {
        historialThroughput.add(throughputTotal);
        historialWip.add(getWipTotal());
        historialLeadTime.add(getLeadTimePromedio());
    }

    public int getDadosDisponiblesEnReserva() {
        int usados = 0;
        for (Estacion e : estaciones) {
            usados += e.getDadosAsignados();
        }
        return totalDadosDisponibles - usados;
    }

    public boolean validarAsignacionDados() {
        return getDadosDisponiblesEnReserva() >= 0;
    }

    public int getWipTotal() {
        int wip = 0;
        for (Estacion e : estaciones) {
            wip += e.getColaEntrada().getTamanio();
        }
        return wip;
    }

    public double getLeadTimePromedio() {
        return throughputTotal == 0 ? 0 : leadTimeAcumulado / throughputTotal;
    }

    public List<Estacion> getEstaciones() {
        return estaciones;
    }
    public int getTotalDadosDisponibles() {
        return totalDadosDisponibles;
    }
    public int getThroughputTotal() {
        return throughputTotal;
    }
    public int getRondaActual() {
        return rondaActual;
    }
    public Fase getFaseActual()
    {
        return faseActual;
    }
    public List<Integer> getHistorialThroughput()
    {
        return historialThroughput;
    }
    public List<Integer> getHistorialWip()
    {
        return historialWip;
    }
    public List<Double> getHistorialLeadTime()
    {
        return historialLeadTime;
    }
}