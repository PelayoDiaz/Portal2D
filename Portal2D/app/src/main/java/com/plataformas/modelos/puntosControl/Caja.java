package com.plataformas.modelos.puntosControl;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;

/**
 * Created by Pelayo Díaz Soto on 12/12/2017.
 */

public class Caja extends Control{

    public int velocidadY=15;

    public Caja(Context context, double x, double y) {
        super(context, x, y, 40, 40);
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.box);
    }

    public void mover(double velocidadX){
        this.x = this.x+velocidadX;
    }

    public void teletransportar(double x, double y, int direccionSalida) {
        this.x = x+(45*direccionSalida);
        this.y = y-velocidadY;
    }

}
