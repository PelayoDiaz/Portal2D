package com.plataformas.modelos.coleccionables;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.plataformas.GameView;
import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;
import com.plataformas.modelos.Modelo;
import com.plataformas.modelos.Nivel;
import com.plataformas.modelos.puntosControl.Meta;

import java.util.HashMap;

/**
 * Created by Pelayo Díaz Soto on 26/11/2017.
 */

public class Coleccionable extends Modelo {

    public static final String BASICO = "Basico";
    public static final String GOTIT = "Atrapado";
    public int estado = ACTIVO;
    public static final int ACTIVO = 1;
    public static final int INACTIVO = 0;
    public static final int ELIMINAR = -1;

    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();

    public Coleccionable(Context context, double x, double y) {
        super(context, x, y, 32, 32);
        altura = 32;
        ancho = 32;
        inicializar();
    }

    public void inicializar() {
        Sprite basico = new Sprite(CargadorGraficos.cargarDrawable(
                context, R.drawable.animacion_recolectable),
                ancho, altura,
                10, 8, true);
        sprites.put(BASICO, basico);

        Sprite impactado = new Sprite(CargadorGraficos.cargarDrawable(
                context, R.drawable.recolectable_cogido),
                ancho, altura,
                15, 8, false);
        sprites.put(GOTIT, impactado);
        sprite = basico;
    }

    public void actualizar (long tiempo) {
        boolean finSprite = sprite.actualizar(tiempo);
        if (estado == INACTIVO && finSprite == true) {
            estado = ELIMINAR;
        }
        if (estado == INACTIVO) {
            sprite = sprites.get(GOTIT);
        }
    }

    public void atrapado() {
        estado = INACTIVO;
        sprite = sprites.get(GOTIT);
    }

    public void execute(GameView view, Meta meta){
        view.getMarcador().setPuntos(view.getMarcador().getPuntos()+1);
        if (view.getMarcador().getPuntos()==3){
            meta.setAbierta(true);
        }
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX , (int) y- Nivel.scrollEjeY);
    }
}
