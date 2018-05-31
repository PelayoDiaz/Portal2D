package com.plataformas.modelos.puntosControl;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;

/**
 * Created by Pelayo Díaz Soto on 14/12/2017.
 */

public class CajaExplosiva extends Control {

    private int posX;
    private int posY;
    private boolean exists;

    public CajaExplosiva(Context context, double x, double y, int posX, int posY) {
        super(context, x, y, 32, 32);
        this.posX=posX;
        this.posY=posY;
        this.exists = true;
        setImagen();
    }

    public void setImagen(){
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.box_explosive);
    }

    public void removeImagen(){
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.no_wall_box);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean exists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}
