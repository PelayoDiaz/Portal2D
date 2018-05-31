package com.plataformas.modelos.puntosControl;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.modelos.Modelo;
import com.plataformas.modelos.Nivel;

/**
 * Created by Pelayo Díaz Soto on 26/11/2017.
 */

public class PuntoSalvado extends Control {

    public PuntoSalvado(Context context, double x, double y) {
        super(context, x, y, 40, 46);
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.bandera);
    }
}
