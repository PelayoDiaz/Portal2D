package com.plataformas.modelos.disparos;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;
import com.plataformas.modelos.Jugador;
import com.plataformas.modelos.Modelo;
import com.plataformas.modelos.Nivel;
import com.plataformas.modelos.enemigos.Enemigo;

/**
 * Created by jordansoy on 09/10/2017.
 */

public class DisparoJugador extends Disparo {

    public DisparoJugador(Context context, double xInicial, double yInicial, int orientacion) {
        super(context, xInicial, yInicial, orientacion);
        if (orientacion == Jugador.IZQUIERDA)
            velocidadX = velocidadX*-1;
    }
}

