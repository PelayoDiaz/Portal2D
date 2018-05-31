package com.plataformas.modelos.puntosControl;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;

/**
 * Created by Pelayo Díaz Soto on 12/12/2017.
 */

public class CajaMuro extends Control {

    private int enlace;
    private int posX;
    private int posY;

    public CajaMuro(Context context, double x, double y, int posX, int posY, int enlace) {
        super(context, x, y, 32, 32);
        setImagen();
        this.enlace=enlace;
        this.posX=posX;
        this.posY=posY;
    }

    public void setImagen(){
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.wall_box);
    }

    public void removeImagen(){
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.no_wall_box);
    }
    public int getEnlace() {
        return enlace;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
