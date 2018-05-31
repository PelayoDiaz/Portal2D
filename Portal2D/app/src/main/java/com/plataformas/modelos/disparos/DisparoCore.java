package com.plataformas.modelos.disparos;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;
import com.plataformas.modelos.Jugador;
import com.plataformas.modelos.enemigos.Enemigo;

/**
 * Created by Pelayo Díaz Soto on 21/11/2017.
 */

public class DisparoCore extends Disparo {

    private Enemigo origen;

    public DisparoCore(Context context, double xInicial,
                       double yInicial, int orientacion, Enemigo origen) {
        super(context, xInicial, yInicial, orientacion);

        if (orientacion == Jugador.IZQUIERDA)
            velocidadX = velocidadX*-1;
        this.origen = origen;
        inicializar();
    }

    public void inicializar (){
        sprite= new Sprite(
                CargadorGraficos.cargarDrawable(context,
                        R.drawable.disparo_enemigo),
                ancho, altura,
                12, 6, true);
    }

    public Enemigo getOrigen() {
        return origen;
    }

    public void setOrigen(Enemigo origen) {
        this.origen = origen;
    }
}
