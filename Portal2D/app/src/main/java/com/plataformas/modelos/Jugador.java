package com.plataformas.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;

import java.util.HashMap;

/**
 * Created by jordansoy on 09/10/2017.
 */

public class Jugador extends Modelo {
    public static final String PARADO_DERECHA = "animacion_prota_quieto_derecha";
    public static final String PARADO_IZQUIERDA = "animacion_prota_quieto_izquierda";
    public static final String CAMINANDO_DERECHA = "animacion_prota_corre_derecha";
    public static final String CAMINANDO_IZQUIERDA = "animacion_prota_corre_izquierda";
    public static final String SALTANDO_DERECHA = "animacion_prota_salto_derecha";
    public static final String SALTANDO_IZQUIERDA = "animacion_prota_salto_izquierda";
    public static final String DISPARANDO_DERECHA = "animacion_prota_ataque_derecha";
    public static final String DISPARANDO_IZQUIERDA = "animacion_prota_ataque_izquierda";
    public static final String GOLPEADO_DERECHA = "animacion_prota_golpe_derecha";
    public static final String GOLPEADO_IZQUIERDA = "animacion_prota_golpe_izquierda";

    public int orientacion;
    public static final int DERECHA = 1;
    public static final int IZQUIERDA = -1;


    public boolean saltoPendiente; // tiene que saltar
    public boolean enElAire; // está en el aire

    double velocidadX;
    float velocidadY; // actual
    float velcidadSalto = -14; // velocidad que le da el salto
    public boolean disparando;
    public boolean golpeado = false;
    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();

    double xInicial;
    double yInicial;

    public int vidas;

    // ACTUAL
    public double msInmunidad = 0;


    public Jugador(Context context, double xInicial, double yInicial) {
        super(context, 0, 0, 40, 40);
        vidas = 3;
        // guardamos la posición inicial porque más tarde vamos a reiniciarlo
        this.xInicial = xInicial;
        this.yInicial = yInicial - altura/2;

        this.x =  this.xInicial;
        this.y =  this.yInicial;

        inicializar();
    }

    public void inicializar (){
        Sprite disparandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_prota_ataque_derecha),
                ancho, altura,
                8, 4, false);
        sprites.put(DISPARANDO_DERECHA, disparandoDerecha);

        Sprite disparandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_prota_ataque_izquierda),
                ancho, altura,
                8, 4, false);
        sprites.put(DISPARANDO_IZQUIERDA, disparandoIzquierda);


        Sprite saltandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_prota_salto_derecha),
                ancho, altura,
                8, 4, true);
        sprites.put(SALTANDO_DERECHA, saltandoDerecha);

        Sprite saltandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_prota_salto_izquierda),
                ancho, altura,
                8, 4, true);
        sprites.put(SALTANDO_IZQUIERDA, saltandoIzquierda);

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_prota_corre_derecha),
                ancho, altura,
                8, 4, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_prota_corre_izquierda),
                ancho, altura,
                8, 4, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);

        Sprite paradoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_prota_quieto_derecha),
                ancho, altura,
                8, 4, true);
        sprites.put(PARADO_DERECHA, paradoDerecha);

        Sprite paradoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_prota_quieto_izquierda),
                ancho, altura,
                8, 4, true);
        sprites.put(PARADO_IZQUIERDA, paradoIzquierda);

        Sprite golpeadoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_prota_golpe_derecha),
                ancho, altura,
                8, 4, false);
        sprites.put(GOLPEADO_DERECHA, golpeadoDerecha);

        Sprite golpeadoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_prota_golpe_izquierda),
                ancho, altura,
                8, 4, false);
        sprites.put(GOLPEADO_IZQUIERDA, golpeadoIzquierda);

// animación actual
        sprite = paradoDerecha;
    }

    public void procesarOrdenes (float orientacionPad, boolean saltar, boolean disparar) {
        if (saltar){
            if (!enElAire) {
                saltoPendiente = true;
            }
        }

        if (disparar){
            disparando = true;
            // preparar los sprites, no son bucles hay que reiniciarlos
            sprites.get(DISPARANDO_DERECHA).setFrameActual(0);
            sprites.get(DISPARANDO_IZQUIERDA).setFrameActual(0);
        }


        if (orientacionPad > 0) {
            velocidadX = -5;
            orientacion = IZQUIERDA;
        } else if (orientacionPad < 0 ){
            velocidadX = 5;
            orientacion = DERECHA;
        } else {
            velocidadX = 0;
        }
    }


    public void actualizar (long tiempo) {
        if(msInmunidad > 0){
            msInmunidad -= tiempo;
        }

        boolean finSprite = sprite.actualizar(tiempo);

        // Deja de estar golpeado, cuando lo estaba y se acaba el sprite
        if (golpeado && finSprite){
            golpeado = false;
        }

        if(disparando && finSprite){
            disparando = false;
        }


        // saltar
        if (saltoPendiente){
            saltoPendiente = false;
            enElAire = true;
            velocidadY = velcidadSalto;
        }

        if (velocidadX > 0){
            sprite = sprites.get(CAMINANDO_DERECHA);
        }
        if (velocidadX < 0 ){
            sprite = sprites.get(CAMINANDO_IZQUIERDA);
        }
        if (velocidadX == 0 ){
            if (orientacion == DERECHA){
                sprite = sprites.get(PARADO_DERECHA);
            } else if (orientacion == IZQUIERDA) {
                sprite = sprites.get(PARADO_IZQUIERDA);
            }
        }


        if (enElAire && orientacion == IZQUIERDA ){
            sprite = sprites.get(SALTANDO_IZQUIERDA);

        } else if (enElAire && orientacion == DERECHA ){
            sprite = sprites.get(SALTANDO_DERECHA);
        }

        if (disparando){
            if (orientacion == DERECHA){
                sprite = sprites.get(DISPARANDO_DERECHA);
            } else if (orientacion == IZQUIERDA) {
                sprite = sprites.get(DISPARANDO_IZQUIERDA);
            }
        }

        if (golpeado){
            if (orientacion == DERECHA){
                sprite = sprites.get(GOLPEADO_DERECHA);
            } else if (orientacion == IZQUIERDA) {
                sprite = sprites.get(GOLPEADO_IZQUIERDA);
            }
        }


    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX , (int) y - Nivel.scrollEjeY,msInmunidad > 0);
    }

    public void restablecerPosicionInicial(){
        vidas = 3;
        golpeado = false;
        msInmunidad = 0;

        this.x = xInicial;
        this.y = yInicial;
        orientacion = IZQUIERDA;
    }

    public void guardarPosicion(double x, double y){
        xInicial = x;
        yInicial = y;
    }

    public int golpeado(){
        if (msInmunidad <= 0) {
            if (vidas > 0) {
                vidas--;
                msInmunidad = 3000;
                golpeado = true;
                // Reiniciar animaciones que no son bucle
                sprites.get(GOLPEADO_IZQUIERDA).setFrameActual(0);
                sprites.get(GOLPEADO_DERECHA).setFrameActual(0);
            }
        }
        return vidas;
    }

    public void teletransportar(double x, double y, int direccion){
        this.x = x+(35*direccion);
        this.y = y-velocidadY;
    }

}
