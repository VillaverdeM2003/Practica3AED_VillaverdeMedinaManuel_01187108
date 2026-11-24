package org.example.practica3;

public class ColaCircular<T> {
    private T[] colaCir;
    private int inicio;
    private int fin;
    private int MAX;

    @SuppressWarnings("unchecked")
    public ColaCircular(int MAX) {
        this.MAX = MAX;
        this.colaCir = (T[]) new Object[MAX];
        this.inicio = 0;
        this.fin = 0;
    }

    public boolean insertaCircular(T dato) {
        if ((fin == MAX && inicio == 1) || ((fin + 1) == inicio)) {
            System.out.println("Desbordamiento en la cola circular");
            return false;
        } else {
            if (fin == MAX) {
                fin = 1;
            } else {
                fin = fin + 1;
            }

            colaCir[fin - 1] = dato;

            if (inicio == 0) {
                inicio = 1;
            }
            return true;
        }
    }

    public T eliminaCircular() {
        T dato = null;
        if (inicio == 0) {
            return null;
        } else {
            dato = colaCir[inicio - 1];
            colaCir[inicio - 1] = null;

            if (inicio == fin) {
                inicio = 0;
                fin = 0;
            } else {
                if (inicio == MAX) {
                    inicio = 1;
                } else {
                    inicio = inicio + 1;
                }
            }
        }
        return dato;
    }

    public boolean estaVacia() {
        return inicio == 0;
    }

    public int getTamanio() {
        if (inicio == 0) {
            return 0;
        }
        if (fin >= inicio) {
            return fin - inicio + 1;
        }
        return (MAX - inicio + 1) + fin;
    }

    public T verFrente() {
        if (estaVacia()) {
            return null;
        }
        return colaCir[inicio - 1];
    }
}