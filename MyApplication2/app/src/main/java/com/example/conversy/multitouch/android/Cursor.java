/*
 * Copyright (c) 2016 Stéphane Conversy, Nicolas Saporito - ENAC - All rights Reserved
 */

package com.example.conversy.multitouch.android;

import fr.liienac.statemachine.geometry.Point;

public class Cursor {
    public Point p;
    public int id;
    public int r, g, b;

    public Cursor(int id, Point p) {
        this.id = id;
        this.p = p;
    }
}
