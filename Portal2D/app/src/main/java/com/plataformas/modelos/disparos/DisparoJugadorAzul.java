package com.plataformas.modelos.disparos;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;

/**
 * Created by Pelayo Díaz Soto on 12/12/2017.
 */

public class DisparoJugadorAzul extends DisparoJugador{

    public DisparoJugadorAzul(Context context, double xInicial, double yInicial, int orientacion) {
        super(context, xInicial, yInicial, orientacion);
        this.inicializar();
    }

    public void inicializar (){
        sprite= new Sprite(
                CargadorGraficos.cargarDrawable(context,
                        R.drawable.animacion_disparo4),
                ancho, altura,
                15, 4, true);
    }
}
