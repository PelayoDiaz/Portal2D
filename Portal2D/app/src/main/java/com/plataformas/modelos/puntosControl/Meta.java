package com.plataformas.modelos.puntosControl;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.modelos.Modelo;
import com.plataformas.modelos.Nivel;

/**
 * Created by UO251000 on 02/11/2017.
 */

public class Meta extends Control {

    private boolean abierta;

    public Meta(Context context, double x, double y) {
        super(context, x, y, 110,78);
        this.abierta = false;
        setImagen();
    }

    private void setImagen(){
        if (abierta)
            imagen = CargadorGraficos.cargarDrawable(context, R.drawable.meta_abierta);
        else
            imagen = CargadorGraficos.cargarDrawable(context, R.drawable.meta_cerrada);
    }

    public boolean isAbierta(){
        return abierta;
    }

    public void setAbierta(boolean abierta) {
        this.abierta = abierta;
        setImagen();
    }
}

