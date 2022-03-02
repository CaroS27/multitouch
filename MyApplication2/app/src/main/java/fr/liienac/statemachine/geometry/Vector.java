/*
 * Copyright (c) 2016 Stéphane Conversy, Nicolas Saporito - ENAC - All rights Reserved
 */
package fr.liienac.statemachine.geometry;

public class Vector {
    public float dx, dy;
    private static final double THRESHOLD = 0.00001;

    public Vector(float dx_, float dy_) { dx=dx_; dy=dy_; }
    public Vector(Point p1, Point p2) { dx=p2.x-p1.x; dy=p2.y-p1.y; }

    public boolean equals(Vector v) {
        return Math.abs(dx - v.dx) < THRESHOLD
                && Math.abs(dy - v.dy) < THRESHOLD;
    }

    static public Vector plus(Vector v1, Vector v2) {
        float dx = (v1.dx + v2.dx);
        float dy = (v1.dy + v2.dy);
        return new Vector(dx,dy);
    }

    static public Vector minus(Vector v1, Vector v2) {
        float dx = (v1.dx - v2.dx);
        float dy = (v1.dy - v2.dy);
        return new Vector(dx,dy);
    }

    static public Vector div(Vector v, float d) {
        float dx = v.dx / d;
        float dy = v.dy / d;
        return new Vector(dx, dy);
    }

    public float normSq() {
        return dx*dx + dy*dy;
    }

    public float norm() {
        return (float)Math.sqrt(normSq());
    }

    static public float scalarProduct(Vector u, Vector v) {
        return u.dx*v.dx + u.dy*v.dy;
    }

    static public float crossProduct(Vector u, Vector v) {
        return u.dx*v.dy - u.dy*v.dx;
    }
}
