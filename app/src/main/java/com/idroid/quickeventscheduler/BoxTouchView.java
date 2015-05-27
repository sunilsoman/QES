package com.idroid.quickeventscheduler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BoxTouchView extends View {

    //Initializing variables for four corners of the box.
    private Drawable topLeftCorner;
    private Drawable topRightCorner;
    private Drawable bottomLeftCorner;
    private Drawable bottomRightCorner;

	//boolean to check which corner is touched to move the box.
    private boolean bTopLeft = false;
    private boolean bTopRight = false;
    private boolean bBottomLeft = false;
    private boolean bBottomRight = false;

    // Starting positions of the box
    private float xTopLeft = 60;
    private float yTopLeft = 120;

    private float xTopRight = 240;
    private float yTopRight = 120;

    private float xBottomLeft = 60;
    private float yBottomLeft = 300;

    private float xBottomRight = 240;
    private float yBottomRight = 300;
	
	//current position of the touchevent
    private float positionX;
    private float positionY;

	//last touch position, it remembers where we started
    private float lastTouchPosX;
    private float lastTouchPosY;

	//four lines to connect four corners
    private Paint topLine;
    private Paint bottomLine;
    private Paint leftLine;
    private Paint rightLine;

    private int centerOfCorner;		//center of the corner

    private static final int INVALID_POINTER_ID = -1;			//a variable to declare pointer id to -1
    private int motionEventPointerId = INVALID_POINTER_ID;		//initialize motionEventPointerId to -1

    public BoxTouchView(Context context) {
        super(context);
        initialize(context);
    }

    public BoxTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public BoxTouchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {

        //Create the lines to connect four corners of the box.
        topLine = new Paint();
        topLine.setColor(Color.WHITE);
        topLine.setStrokeWidth(2);

        rightLine = new Paint();
        rightLine.setColor(Color.WHITE);
        rightLine.setStrokeWidth(2);

        bottomLine = new Paint();
        bottomLine.setColor(Color.WHITE);
        bottomLine.setStrokeWidth(2);

        leftLine = new Paint();
        leftLine.setColor(Color.WHITE);
        leftLine.setStrokeWidth(2);

		//set the positions of the four corners of the box.
        topLeftCorner = context.getResources().getDrawable(R.drawable.corner);

        centerOfCorner = topLeftCorner.getMinimumHeight() / 2;
		
        topLeftCorner.setBounds((int) xTopLeft, (int) yTopLeft,
                topLeftCorner.getIntrinsicWidth() + (int) xTopLeft,
                topLeftCorner.getIntrinsicHeight() + (int) yTopLeft);

        topRightCorner = context.getResources().getDrawable(R.drawable.corner);
        topRightCorner.setBounds((int) xTopRight, (int) yTopRight,
                topRightCorner.getIntrinsicWidth() + (int) xTopRight,
                topRightCorner.getIntrinsicHeight() + (int) yTopRight);

        bottomLeftCorner = context.getResources().getDrawable(R.drawable.corner);
        bottomLeftCorner.setBounds((int) xBottomLeft, (int) yBottomLeft,
                bottomLeftCorner.getIntrinsicWidth() + (int) xBottomLeft,
                bottomLeftCorner.getIntrinsicHeight() + (int) yBottomLeft);

        bottomRightCorner = context.getResources().getDrawable(R.drawable.corner);
        bottomRightCorner.setBounds((int) xBottomRight, (int) yBottomRight,
                bottomRightCorner.getIntrinsicWidth() + (int) xBottomRight,
                bottomRightCorner.getIntrinsicHeight() + (int) yBottomRight);

    }

    //This method is called when invalidate() is called so that it draws the box on canvas.
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        canvas.drawLine(xTopLeft + centerOfCorner, yTopLeft + centerOfCorner,
                xTopRight + centerOfCorner, yTopRight + centerOfCorner, topLine);
        canvas.drawLine(xBottomLeft + centerOfCorner, yBottomLeft + centerOfCorner,
                xBottomRight + centerOfCorner, yBottomRight + centerOfCorner, bottomLine);
        canvas.drawLine(xTopLeft + centerOfCorner, yTopLeft + centerOfCorner,
                xBottomLeft + centerOfCorner, yBottomLeft + centerOfCorner, leftLine);
        canvas.drawLine(xTopRight + centerOfCorner, yTopRight + centerOfCorner,
                xBottomRight + centerOfCorner, yBottomRight + centerOfCorner, rightLine);


        topLeftCorner.setBounds((int) xTopLeft, (int) yTopLeft,
                topLeftCorner.getIntrinsicWidth() + (int) xTopLeft,
                topLeftCorner.getIntrinsicHeight() + (int) yTopLeft);

        topRightCorner.setBounds((int) xTopRight, (int) yTopRight,
                topRightCorner.getIntrinsicWidth() + (int) xTopRight,
                topRightCorner.getIntrinsicHeight() + (int) yTopRight);

        bottomLeftCorner.setBounds((int) xBottomLeft, (int) yBottomLeft,
                bottomLeftCorner.getIntrinsicWidth() + (int) xBottomLeft,
                bottomLeftCorner.getIntrinsicHeight() + (int) yBottomLeft);

        bottomRightCorner.setBounds((int) xBottomRight, (int) yBottomRight,
                bottomRightCorner.getIntrinsicWidth() + (int) xBottomRight,
                bottomRightCorner.getIntrinsicHeight() + (int) yBottomRight);


        topLeftCorner.draw(canvas);
        topRightCorner.draw(canvas);
        bottomLeftCorner.draw(canvas);
        bottomRightCorner.draw(canvas);
        canvas.restore();
    }

	// Method to detect all the touch gestures
    public boolean onTouchEvent(MotionEvent event) {
        
		final int movement = event.getAction();			//get touchevent action

        switch (movement) {

            case MotionEvent.ACTION_DOWN: {
				
				//get x and y position of the touch event.
                final float x = event.getX();
                final float y = event.getY();
				
                touchDistanceFromCorners(x, y); //this method is described below

                lastTouchPosX = x;
                lastTouchPosY = y;
				
                motionEventPointerId = event.getPointerId(0);		//save poiterid to track movement
				
                break;
            }

            case MotionEvent.ACTION_MOVE: {

                final int eventPointerIndex = event.findPointerIndex(motionEventPointerId); //get pointer index of event started
				
                final float x = event.getX();
                final float y = event.getY();
                

                // Calculate the distance moved from previous touch position.
                final float changeInX = x - lastTouchPosX;
                final float changeInY = y - lastTouchPosY;


                // Move the object
                /*if (positionX >= 0 && positionX <= 800) {
                    positionX += changeInX;
                }
                if (positionY >= 0 && positionY <= 480) {
                    positionY += changeInY;
                }*/

                // all if conditions checks which corner is pressed and it also checks if the lines are not overlapping.
                if (bTopLeft && ((y + centerOfCorner * 2) < yBottomLeft) && ((x + centerOfCorner * 2) < xTopRight)) {
				
				//it saves the y value of the top right corner when top left corner is moved
                    if (changeInY != 0) {
                        yTopRight = y;
                    }
					//it save the x value of the bottom left corner when top left corner is moved
                    if (changeInX != 0) {
                        xBottomLeft = x;
                    }
					//it save the x and y values of the top left corner
                    xTopLeft = x;
                    yTopLeft = y;
                }
                if (bTopRight && ((y + centerOfCorner * 2) < yBottomRight) && (x > (xTopLeft + centerOfCorner * 2))) {
				
				//it save the y value of the top left corner when top right corner is moved 
                    if (changeInY != 0) {
                        yTopLeft = y;
                    }
					//it save the x value of the bottom right corner when top right corner is moved
                    if (changeInX != 0) {
                        xBottomRight = x;
                    }
					//it save the x and y values of the top right corner
                    xTopRight = x;
                    yTopRight = y;
                }
                if (bBottomLeft && (y > (yTopLeft + centerOfCorner * 2)) && ((x + centerOfCorner * 2) < xBottomRight)) {

				//it checks if the box is not going over the button.
                    if(y > (CameraActivity.screenHeight - CameraActivity.buttonHeight - 100)){
                        if (changeInX != 0) {
                            xTopLeft = x;
                        }
                        xBottomLeft = x;
                        yBottomRight = (CameraActivity.screenHeight - CameraActivity.buttonHeight - 100);
                        yBottomLeft = (CameraActivity.screenHeight - CameraActivity.buttonHeight - 100);
                    }
                    else {
                        if (changeInX != 0) {
                            xTopLeft = x;
                        }
                        if (changeInY != 0) {
                            yBottomRight = y;
                        }
                        xBottomLeft = x;
                        yBottomLeft = y;
                    }
                }
                if (bBottomRight && (y > (yTopLeft + centerOfCorner * 2)) && (x > (xBottomLeft + centerOfCorner * 2))) {
                    //Log.d("check y", "y:  " +  y + "screenHeight:  " + CameraActivity.screenHeight + "button:  " + CameraActivity.buttonHeight);
                    
					//it checks if the box is not going over the button
					if(y > (CameraActivity.screenHeight - CameraActivity.buttonHeight - 100) ){
                        if (changeInX != 0) {
                            xTopRight = x;
                        }
                        xBottomRight = x;
                        yBottomLeft = (CameraActivity.screenHeight - CameraActivity.buttonHeight - 100);
                        yBottomRight = (CameraActivity.screenHeight - CameraActivity.buttonHeight - 100);
                    }
                    else {
                        if (changeInX != 0) {
                            xTopRight = x;
                        }
                        if (changeInY != 0) {
                            yBottomLeft = y;
                        }
                        xBottomRight = x;
                        yBottomRight = y;
                    }
                }

                lastTouchPosX = x;
                lastTouchPosY = y;

                // call invalidate method to redraw 
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP: {
                
				//it will set all corners to false conditions
                bTopLeft = false;
                bTopRight = false;
                bBottomLeft = false;
                bBottomRight = false;

                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                motionEventPointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
			
                // get the index of the pointer of the last touch event
                final int eventPointerIndex = (movement & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = event.getPointerId(eventPointerIndex);
				
				// This is our active pointer going up. Choose a new active pointer and adjust accordingly.
                if (pointerId == motionEventPointerId) {
                    
                    final int newEventPointerIndex = eventPointerIndex == 0 ? 1 : 0;
                    lastTouchPosX = event.getX(newEventPointerIndex);
                    lastTouchPosY = event.getY(newEventPointerIndex);
                    motionEventPointerId = event.getPointerId(newEventPointerIndex);
                }
                break;
            }


        }
        return true;
    }

    //This method calculates distance from the corner when a touch event occurs. 
	//So at a time only one corner will be activated. 
    private void touchDistanceFromCorners(float x, float y) {

        double topLeftDistance = Math.sqrt(Math.pow((Math.abs((double) x - (double) xTopLeft)), 2)
                + Math.pow((Math.abs((double) y - (double) yTopLeft)), 2));

        double topRightDistance = Math.sqrt(Math.pow((Math.abs((double) x - (double) xTopRight)), 2)
                + Math.pow((Math.abs((double) y - (double) yTopRight)), 2));

        double bottomLeftDistance = Math.sqrt(Math.pow((Math.abs((double) x - (double) xBottomLeft)), 2)
                + Math.pow((Math.abs((double) y - (double) yBottomLeft)), 2));

        double bottomRightDistance = Math.sqrt(Math.pow((Math.abs((double) x - (double) xBottomRight)), 2)
                + Math.pow((Math.abs((double) y - (double) yBottomRight)), 2));

        if (topLeftDistance < 60) {
            bTopLeft = true;
            bTopRight = false;
            bBottomLeft = false;
            bBottomRight = false;
        } else if (topRightDistance < 60) {
            bTopLeft = false;
            bTopRight = true;
            bBottomLeft = false;
            bBottomRight = false;
        } else if (bottomLeftDistance < 60) {
            bTopLeft = false;
            bTopRight = false;
            bBottomLeft = true;
            bBottomRight = false;
        } else if (bottomRightDistance < 60) {
            bTopLeft = false;
            bTopRight = false;
            bBottomLeft = false;
            bBottomRight = true;
        }

    }
	
	//methods to get all x and y values.
    public float getXTopLeft() {
        return xTopLeft;
    }

    public float getYTopLeft() {
        return yTopLeft;
    }

    public float getXTopRight() {
        return xTopRight;
    }

    public float getYTopRight() {
        return yTopRight;
    }

    public float getXBottomLeft() {
        return xBottomLeft;
    }

    public float getYBottomLeft() {
        return yBottomLeft;
    }

    public float getYBottomRight() {
        return yBottomRight;
    }

    public float getXBottomRight() {
        return xBottomRight;
    }

    public void setInvalidate() {
        invalidate();

    }
}