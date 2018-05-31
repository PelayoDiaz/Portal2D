package com.plataformas.modelos.puntosControl;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;
import com.plataformas.modelos.Modelo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Pelayo Díaz Soto on 13/12/2017.
 */

public class Interruptor extends Control{

    private List<CajaMuro> cajaMuros;
    private Modelo pressedBy;

    public Interruptor(Context context, double x, double y) {
        super(context, x, y, 30, 30);
        setImagen();
        this.cajaMuros = new LinkedList<CajaMuro>();
    }

    public void addMuro(CajaMuro muro){
        this.cajaMuros.add(muro);
    }

    public List<CajaMuro> getMuros(){
        return this.cajaMuros;
    }

    private void setImagen(){
        if (pressedBy==null)
            imagen = CargadorGraficos.cargarDrawable(context, R.drawable.switch_red);
        else
            imagen = CargadorGraficos.cargarDrawable(context, R.drawable.switch_red_pressed);
    }

    public Modelo getPressedBy() {
        return pressedBy;
    }

    public void setPressedBy(Modelo pressedBy) {
        this.pressedBy = pressedBy;
        setImagen();
    }
}
