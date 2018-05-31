package com.plataformas.modelos.Tiles;

import android.graphics.drawable.Drawable;

/**
 * Created by jordansoy on 09/10/2017.
 */

public class Tile {
    public static final int PASABLE = 0;
    public static final int SOLIDO = 1;

    public int tipoDeColision; // PASABLE o SOLIDO
    public boolean portable;

    public static int ancho = 40;
    public static int altura = 32;

    public Drawable imagen;

    public Tile(Drawable imagen, int tipoDeColision)
    {
        this.imagen = imagen ;
        this.tipoDeColision = tipoDeColision;
        this.portable = false;
    }

    public Tile(Drawable imagen, int tipoDeColision, boolean portable) {
        this(imagen, tipoDeColision);
        this.portable = portable;
    }

    public boolean isPortable(){
        return this.portable;
    }

    public Tile(){}

}
