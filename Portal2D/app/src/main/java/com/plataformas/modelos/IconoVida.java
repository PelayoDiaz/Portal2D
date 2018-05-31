package com.plataformas.modelos;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;

/**
 * Created by jordansoy on 10/10/2017.
 */

public class IconoVida extends Modelo {

    public IconoVida(Context context, double x, double y) {
        super(context, x, y, 20,20);
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.life);
    }
}

