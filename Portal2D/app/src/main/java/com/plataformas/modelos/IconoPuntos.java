package com.plataformas.modelos;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;

/**
 * Created by Pelayo Díaz Soto on 14/12/2017.
 */

public class IconoPuntos extends Modelo {

    public IconoPuntos(Context context, double x, double y) {
        super(context, x, y, 26, 26);
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.icono_puntos);
    }
}
