package com.plataformas;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.plataformas.modelos.IconoPuntos;
import com.plataformas.modelos.IconoVida;
import com.plataformas.modelos.Marcador;
import com.plataformas.modelos.Nivel;
import com.plataformas.modelos.controles.BotonBorrarPortales;
import com.plataformas.modelos.controles.BotonDisparar;
import com.plataformas.modelos.controles.BotonPortalAzul;
import com.plataformas.modelos.controles.BotonPortalNaranja;
import com.plataformas.modelos.controles.BotonReset;
import com.plataformas.modelos.controles.BotonSaltar;
import com.plataformas.modelos.controles.Pad;


public class GameView extends SurfaceView implements SurfaceHolder.Callback  {
    private Pad pad;
    boolean iniciado = false;
    Context context;
    GameLoop gameloop;
    private BotonDisparar botonDisparar;
    private BotonSaltar botonSaltar;
    private IconoVida[] iconosVida;
    private BotonPortalAzul portalAzul;
    private BotonPortalNaranja portalNaranja;
    private BotonBorrarPortales borrar;
    public static int pantallaAncho;
    public static int pantallaAlto;
    private BotonReset reset;
    public Marcador marcador;


    private Nivel nivel;
    public int numeroNivel = 0;
    private IconoPuntos iconoPuntos;

    public GameView(Context context) {
        super(context);
        iniciado = true;

        getHolder().addCallback(this);
        setFocusable(true);

        this.context = context;
        gameloop = new GameLoop(this);
        gameloop.setRunning(true);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // valor a Binario
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        // Indice del puntero
        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

        int pointerId  = event.getPointerId(pointerIndex);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                accion[pointerId] = ACTION_DOWN;
                x[pointerId] = event.getX(pointerIndex);
                y[pointerId] = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                accion[pointerId] = ACTION_UP;
                x[pointerId] = event.getX(pointerIndex);
                y[pointerId] = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                for(int i =0; i < pointerCount; i++){
                    pointerIndex = i;
                    pointerId  = event.getPointerId(pointerIndex);
                    accion[pointerId] = ACTION_MOVE;
                    x[pointerId] = event.getX(pointerIndex);
                    y[pointerId] = event.getY(pointerIndex);
                }
                break;
        }

        try {
            procesarEventosTouch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    int NO_ACTION = 0;
    int ACTION_MOVE = 1;
    int ACTION_UP = 2;
    int ACTION_DOWN = 3;
    int accion[] = new int[6];
    float x[] = new float[6];
    float y[] = new float[6];

    public void procesarEventosTouch() throws Exception {
        boolean pulsacionPadMover = false;

        for(int i=0; i < 6; i++){
            if(accion[i] != NO_ACTION ) {

                if(accion[i] == ACTION_DOWN){
                    if(nivel.nivelPausado)
                        nivel.nivelPausado = false;
                }

                if (botonDisparar.estaPulsado(x[i], y[i])) {
                    if (accion[i] == ACTION_DOWN) {
                        nivel.botonDispararPulsado = true;
                        nivel.direccionDisparo = botonDisparar.getDireccion(x[i], y[i]);
                    }
                }

                if (botonSaltar.estaPulsado(x[i], y[i])) {
                    if (accion[i] == ACTION_DOWN) {
                        nivel.botonSaltarPulsado = true;
                    }
                }

                if (portalNaranja.estaPulsado(x[i], y[i])){
                    if (accion[i] == ACTION_DOWN) {
                        nivel.colorPortal = false;
                        portalNaranja.setImagen(true);
                        portalAzul.setImagen(false);
                    }
                }

                if (reset.estaPulsado(x[i], y[i])){
                    inicializar();
                }

                if (borrar.estaPulsado(x[i], y[i])){
                    if (accion[i] == ACTION_DOWN) {
                        nivel.borrarPortales = true;
                    }
                }

                if (portalAzul.estaPulsado(x[i], y[i])){
                    if (accion[i] == ACTION_DOWN) {
                        nivel.colorPortal = true;
                        portalNaranja.setImagen(false);
                        portalAzul.setImagen(true);
                    }
                }

                if (pad.estaPulsado(x[i], y[i])) {
                    float orientacion = pad.getOrientacionX(x[i]);

                    // Si almenosuna pulsacion está en el pad
                    if (accion[i] != ACTION_UP) {
                        pulsacionPadMover = true;
                        nivel.orientacionPad = orientacion;
                    }
                }
            }
        }
        if(!pulsacionPadMover) {
            nivel.orientacionPad = 0;
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v("Tecla","Tecla pulsada: "+keyCode);

        if( keyCode == 62) {
            nivel.botonDispararPulsado = true;
        }
        if( keyCode == 32) {
            nivel.orientacionPad = -0.5f;
        }
        if( keyCode == 29) {
            nivel.orientacionPad = 0.5f;
        }
        if( keyCode == 47) {
            nivel.orientacionPad = 0;
        }
        if( keyCode == 51) {
            nivel.botonSaltarPulsado = true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp (int keyCode, KeyEvent event) {
        if( keyCode == 32 || keyCode == 29) {
            nivel.orientacionPad = 0;
        }
        return super.onKeyDown(keyCode, event);
    }



    protected void inicializar() throws Exception {
        nivel = new Nivel(context,numeroNivel);
        pad = new Pad(context);
        portalAzul = new BotonPortalAzul(context);
        portalNaranja = new BotonPortalNaranja(context);
        borrar = new BotonBorrarPortales(context);
        botonDisparar = new BotonDisparar(context);
        botonSaltar = new BotonSaltar(context);
        reset = new BotonReset(context);
        iconosVida = new IconoVida[3];
        nivel.gameView = this;
        marcador = new Marcador(context, GameView.pantallaAncho*0.9,GameView.pantallaAlto*0.1);
        iconoPuntos = new IconoPuntos(context, GameView.pantallaAncho*0.87,GameView.pantallaAlto*0.08);

        iconosVida[0] = new IconoVida(context, GameView.pantallaAncho*0.05,
                GameView.pantallaAlto*0.1);
        iconosVida[1] = new IconoVida(context, GameView.pantallaAncho*0.09,
                GameView.pantallaAlto*0.1);
        iconosVida[2] = new IconoVida(context, GameView.pantallaAncho*0.13,
                GameView.pantallaAlto*0.1);
    }

    public void nivelCompleto() throws Exception {

        if (numeroNivel < 1){ // Número Máximo de Nivel
            numeroNivel++;
        } else {
            numeroNivel = 0;
        }
        inicializar();
    }


    public void actualizar(long tiempo) throws Exception {
        if (!nivel.nivelPausado)
            nivel.actualizar(tiempo);
    }

    protected void dibujar(Canvas canvas) {
        nivel.dibujar(canvas);
        marcador.dibujar(canvas);
        if (!nivel.nivelPausado) {
            pad.dibujar(canvas);
            portalAzul.dibujar(canvas);
            portalNaranja.dibujar(canvas);
            borrar.dibujar(canvas);
            botonDisparar.dibujar(canvas);
            botonSaltar.dibujar(canvas);
            reset.dibujar(canvas);
            iconoPuntos.dibujar(canvas);
        }
        for(int i=0; i < nivel.jugador.vidas; i++)
            iconosVida[i].dibujar(canvas);

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        pantallaAncho = width;
        pantallaAlto = height;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (iniciado) {
            iniciado = false;
            if (gameloop.isAlive()) {
                iniciado = true;
                gameloop = new GameLoop(this);
            }

            gameloop.setRunning(true);
            gameloop.start();
        } else {
            iniciado = true;
            gameloop = new GameLoop(this);
            gameloop.setRunning(true);
            gameloop.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        iniciado = false;

        boolean intentarDeNuevo = true;
        gameloop.setRunning(false);
        while (intentarDeNuevo) {
            try {
                gameloop.join();
                intentarDeNuevo = false;
            }
            catch (InterruptedException e) {
            }
        }
    }

    public Marcador getMarcador(){
        return marcador;
    }

}

