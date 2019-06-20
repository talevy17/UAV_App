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

public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private float centerX;
    private float centerY;
    private float baseRadius;
    private float hatRadius;
    private JoystickListener joystickCallback;


    /**
     * CTOR based on context and attribute.
     * @param context
     * @param att
     */
    public JoystickView(Context context, AttributeSet att) {
        super(context, att);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    /**
     * CTOR, based on context, attribute and style.
     * @param context
     * @param att
     * @param style
     */
    public JoystickView(Context context, AttributeSet att, int style) {
        super(context, att, style);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    /**
     * CTOR based on context only.
     * @param context
     */
    public JoystickView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    /**
     * sets the dimensions of the circle and draws the centered joystick on creation.
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setDim();
        drawJoystick(centerX, centerY);
    }

    /**
     * sets the dimensions of the joystick and draws it on change.
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //setDim();
        //drawJoystick(centerX, centerY);
    }

    /**
     * disconnects from the server when the joystick activity shuts down the joystick.
     * acts as a destructor.
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Client.getInstance().disconnect();
    }


    /**
     * Configures the parsing of the values based on the movement of the joystick,
     * and the draw logic, also pokes the onJoystickMoved method of the activity.
     * @param view
     * @param motionEvent
     * @return true.
     */
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.equals(this)) {
            if (motionEvent.getAction() != motionEvent.ACTION_UP) {
                //Checking for Out of Bounds
                float displacement = (float) Math.sqrt
                        (Math.pow(motionEvent.getX() - centerX, 2)
                                + Math.pow(motionEvent.getY() - centerY, 2));
                if (displacement < baseRadius) {
                    drawJoystick(motionEvent.getX(), motionEvent.getY());
                    joystickCallback.onJoystickMoved((motionEvent.getX() - centerX) / baseRadius,
                            (motionEvent.getY() - centerY) / baseRadius);
                } else {
                    float ratio = baseRadius / displacement;
                    float constrainedX = centerX + (motionEvent.getX() - centerX) * ratio;
                    float constrainedY = centerY + (motionEvent.getY() - centerY) * ratio;
                    drawJoystick(constrainedX, constrainedY);
                    joystickCallback.onJoystickMoved((constrainedX - centerX) / baseRadius,
                            (constrainedY - centerY) / baseRadius);

                }
            } else {
                drawJoystick(centerX, centerY);
                joystickCallback.onJoystickMoved(0, 0);
            }
        }
        return true;
    }

    /**
     * sets the dimensions of the joystick's circle.
     */
    private void setDim() {
        centerX = (float) getWidth() / 2;
        centerY = (float) getHeight() / 2;
        baseRadius = (float) Math.min(getWidth(), getHeight()) / 3;
        hatRadius = (float) Math.min(getWidth(), getHeight()) / 5;
    }

    /**
     * draws the joystick, based upon the movement.
     * @param x
     * @param y
     */
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

    /**
     * Interface configures the method to be activated by the activity that acts upon the
     * joystick movements.
     */
    public interface JoystickListener {
        void onJoystickMoved(float xPlumbus, float yPlumbus);
    }
}
