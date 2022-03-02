/*
 * Copyright (c) 2016 Stéphane Conversy, Nicolas Saporito - ENAC - All rights Reserved
 */
package com.example.conversy.multitouch.android;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import fr.liienac.statemachine.geometry.Point;
import fr.liienac.statemachine.geometry.Vector;
import fr.liienac.statemachine.graphic.GraphicItem;
import fr.liienac.statemachine.graphic.Style;


public class GraphicItemAndroid implements GraphicItem {
	public float x, y, width, height, xText, yText;
    public Style style;

	public GraphicItemAndroid(float x_, float y_, float w_, float h_) {
		x=x_; y=y_; width=w_; height=h_;
        xText=x_; yText=y_;
        style = new Style(0,0,0);
	}

	public float getX(){
	    return x;
    }

    public float getY(){
        return y;
    }

 	public Matrix transform = new Matrix();

    @Override
    public void setStyle(int r_, int g_, int b_) {
        style.r = r_;
        style.g = g_;
        style.b = b_;
    }

    @Override
    public void translateBy(Vector v) {
        // Compute transform matrix for shape
		transform.postTranslate(v.dx, v.dy);

        // Compute coordinates for text
        xText += v.dx;
        yText += v.dy;
	}

	@Override
    public void rotateBy(float dangle, Point center) {
        // Compute transform matrix for shape
		Matrix t = transform;
		t.postTranslate(-center.x, -center.y);
		t.postRotate((float)(dangle*180/Math.PI));
		t.postTranslate(center.x, center.y);

        // Compute coordinates for text
        float cosa = (float)Math.cos(dangle);
        float sina = (float)Math.sin(dangle);
        float xTextTemp = xText - center.x;
        float yTextTemp = yText - center.y;
        xText = xTextTemp * cosa + yTextTemp * sina;
        yText = - xTextTemp * sina + yTextTemp * cosa;
        xText += center.x;
        yText += center.y;
	}

	// rotate about center with an angle specified by two vectors
	// avoids the use of acos
    @Override
    public void rotateBy(Vector u, Vector v, Point center) {
        // Compute transform matrix for shape
        Matrix t = transform;
        float uvnorm = u.norm() * v.norm();
        float cosa = Vector.scalarProduct(u, v) / uvnorm;
        float sina = Vector.crossProduct(u, v) / uvnorm;
        Matrix rot = new Matrix();
        rot.setSinCos(-sina, cosa);
        t.postTranslate(-center.x, -center.y);
        t.postConcat(rot);
        t.postTranslate(center.x, center.y);

        // Compute coordinates for text
        float xTextTemp = xText - center.x;
        float yTextTemp = yText - center.y;
        xText = xTextTemp * cosa + yTextTemp * sina;
        yText = - xTextTemp * sina + yTextTemp * cosa;
        xText += center.x;
        yText += center.y;
    }

	@Override
    public void scaleBy(float ds, Point center) {
        // Compute transform matrix for shape
		Matrix t = transform;
		t.postTranslate(-center.x, -center.y);
		t.postScale(ds, ds);
		t.postTranslate(center.x, center.y);

        // Compute coordinates for text
        xText = (xText - center.x) * ds + center.x;
        yText = (yText - center.y) * ds + center.y;
	}
	
	private void applyTransform(Canvas canvas) {
		canvas.concat(transform);		
	}
	
	public void draw(Canvas canvas, Paint paint) {
        // Draw shape
        canvas.save();
		applyTransform(canvas);
        paint.setARGB(255, style.r, style.g, style.b);
		// simple rectangle...
		// canvas.drawRect(x, y, x+width, y+height, paint);
		// ...or grid to see that scale and rotation are focused
		int LoD=30; // Level of Details
		float wLoD = width/(float)LoD;
		float hLoD = height/(float)LoD;
		canvas.translate(x, y);
		for (int i=0; i<=LoD; ++i) {
			canvas.drawLine(wLoD*i, 0, wLoD*i, height, paint);
			canvas.drawLine(0, hLoD*i, width, hLoD * i, paint);
        }
        canvas.restore();

        // Draw text (don't resize or rotate, just relocate using the coordinates computed during each transform)
		paint.setTextSize(30);
        paint.setARGB(255, 255, 125, 125);
        canvas.drawText("Blabla", xText, yText, paint);
	}

    public void drawPickingView(ColorPicking colorPicking, Paint paint) {
        colorPicking.canvas.save();
        applyTransform(colorPicking.canvas);
        colorPicking.newColor(this, paint);
        colorPicking.canvas.drawRect(x, y, x+width, y+height, paint);
        colorPicking.canvas.restore();
    }
	
}
