package com.example.ex4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class JoystickView extends SurfaceView implements SurfaceHolder.Callback,View.OnTouchListener  {
    private float centerX;
    private float centerY;
    private float baseRadius;
    private float hatRadius;
    private JoystickListener joystickCallback;


    public JoystickView(Context context, AttributeSet att) {
        super(context, att);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    public JoystickView(Context context, AttributeSet att, int style) {
        super(context, att, style);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    public JoystickView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        setDim();
        drawJoystick(centerX,centerY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){

    }

    private void bound (MotionEvent motionEvent) {
        float displacement = (float) Math.sqrt
                (Math.pow(motionEvent.getX() - centerX, 2)
                        + Math.pow(motionEvent.getY() - centerY, 2));
        if (displacement < baseRadius) {
            drawJoystick(motionEvent.getX(), motionEvent.getY());
            joystickCallback.onJoystickMoved ((centerX - motionEvent.getX()) / baseRadius,
                            (motionEvent.getY() - centerY) / baseRadius, getId());
        } else {
            float ratio = baseRadius / displacement;
            float constrainedX = centerX + (motionEvent.getX() - centerX) * ratio;
            float constrainedY = centerY + (motionEvent.getY() - centerY) * ratio;
            drawJoystick(constrainedX, constrainedY);
            joystickCallback.onJoystickMoved((constrainedX - centerX) / baseRadius,
                    (centerY - constrainedY) / baseRadius, getId());

        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent){
        if(view.equals(this)){
            if(motionEvent.getAction() != motionEvent.ACTION_UP){
                drawJoystick(motionEvent.getX(), motionEvent.getY());
                //Checking for Out of Bounds
                bound(motionEvent);
            }}else {
            drawJoystick(centerX, centerY);
            joystickCallback.onJoystickMoved(0,0,getId());
        }
        return true;
    }

    private void setDim (){
        centerX = (float) getWidth() / 2;
        centerY = (float) getHeight() / 2;
        baseRadius =(float) Math.min(getWidth(), getHeight()) / 3;
        hatRadius = (float) Math.min(getWidth(), getHeight()) / 5;
    }

    private void drawJoystick(float x, float y) {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = this.getHolder().lockCanvas();
            Paint colors = new Paint();
            canvas.drawColor(Color.BLUE, PorterDuff.Mode.CLEAR);
            colors.setARGB(255, 50, 50, 50);
            canvas.drawCircle(centerX, centerY, baseRadius, colors);
            colors.setARGB(255, 0, 0, 255);
            canvas.drawCircle(x, y, hatRadius, colors);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public interface JoystickListener {

        void onJoystickMoved(float xPlumbus, float yPlumbus, int source);

    }

}
