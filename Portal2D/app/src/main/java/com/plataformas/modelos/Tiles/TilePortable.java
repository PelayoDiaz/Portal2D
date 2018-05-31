package com.plataformas.modelos.Tiles;

import android.graphics.drawable.Drawable;

/**
 * Created by Pelayo Díaz Soto on 11/12/2017.
 */

public class TilePortable extends Tile {

    public TilePortable(Drawable imagen, int tipoDeColision){
        super(imagen, tipoDeColision);
        super.portable = true;
    }
}
