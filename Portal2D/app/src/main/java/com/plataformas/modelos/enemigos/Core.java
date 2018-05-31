package com.plataformas.modelos.enemigos;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;
import com.plataformas.modelos.Jugador;
import com.plataformas.modelos.disparos.Disparo;
import com.plataformas.modelos.disparos.DisparoCore;

/**
 * Created by Pelayo Díaz Soto on 20/11/2017.
 */

public class Core extends Enemigo {

    protected int cadenciaDisparo = 2000;
    protected long milisegundosDisparo = 0;

    public int orientacion;
    public boolean disparando = false;
    public static final int DERECHA = 1;
    public static final int IZQUIERDA = -1;

    public Core(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial);
        velocidadX = 3;
        inicializar();
    }

    public void inicializar() {

        Sprite flotando = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_movimiento_derecha),
                ancho, altura,
                8, 6, true);
        sprites.put(CAMINANDO, flotando);

        Sprite muerte = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_muerte),
                ancho, altura,
                8, 6, false);
        sprites.put(MUERTE, muerte);

        Sprite disparando = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_muerte),
                ancho, altura,
                8, 6, false);
        sprites.put(DISPARANDO, disparando);


        sprite = flotando;
    }

    public Disparo disparar(long milisegundos, double posicionJugador) {
        if (milisegundos - milisegundosDisparo > cadenciaDisparo
                + Math.random() * cadenciaDisparo && estado == ACTIVO) {
            disparando = true;
            milisegundosDisparo = milisegundos;
            setOrientacion();
            return new DisparoCore(context, x, y, getOrientacion(posicionJugador), this);
        }
        return null;
    }

    private int getOrientacion(double posicionJugador){
        if (posicionJugador<x)
            return Jugador.IZQUIERDA;
        else
            return Jugador.DERECHA;
    }

    private void setOrientacion() {
        if (velocidadX < 0)
            orientacion = IZQUIERDA;
        else
            orientacion = DERECHA;
    }

}
