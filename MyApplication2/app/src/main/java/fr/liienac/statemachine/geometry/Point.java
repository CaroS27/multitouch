/*
 * Copyright (c) 2016 Stéphane Conversy, Nicolas Saporito - ENAC - All rights Reserved
 */
package fr.liienac.statemachine.geometry;

public class Point {
	public float x,y;
	private static final double THRESHOLD = 0.00001;
	
	public Point(float x_, float y_) {x=x_;y=y_;}

	public boolean equals(Point p) {
		return Math.abs(x - p.x) < THRESHOLD
				&& Math.abs(y - p.y) < THRESHOLD;
	}

	static public Vector minus(Point p1, Point p2) {
		float dx = (p1.x - p2.x);
		float dy = (p1.y - p2.y);
		return new Vector(dx,dy);
	}

	static public Point middle(Point p1, Point p2) {
		float x = (p1.x + p2.x)/2;
		float y = (p1.y + p2.y)/2;
		return new Point(x,y);
	}

	static public float distanceSq(Point p1, Point p2) {
        return new Vector(p1,p2).normSq();
	}

	static public float distance(Point p1, Point p2) {
		return (float)Math.sqrt(distanceSq(p1,p2));
	}

}
