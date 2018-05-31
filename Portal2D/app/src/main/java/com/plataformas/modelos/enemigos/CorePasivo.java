package com.plataformas.modelos.enemigos;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;

/**
 * Created by Pelayo Díaz Soto on 20/11/2017.
 */

public class CorePasivo extends Enemigo {


    public CorePasivo(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial);
        inicializar();
    }


    public void inicializar (){

        Sprite flotando = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.aniamcion_core2),
                ancho, altura,
                8, 6, true);
        sprites.put(CAMINANDO, flotando);

        Sprite muerte = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_muerte_core2),
                ancho, altura,
                8, 6, false);
        sprites.put(MUERTE, muerte);

        sprite = flotando;
    }
}
