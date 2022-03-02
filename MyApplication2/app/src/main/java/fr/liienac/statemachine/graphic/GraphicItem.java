/*
 * Copyright (c) 2016 Stéphane Conversy, Nicolas Saporito - ENAC - All rights Reserved
 */
package fr.liienac.statemachine.graphic;

import fr.liienac.statemachine.geometry.Point;
import fr.liienac.statemachine.geometry.Vector;

public interface GraphicItem {
    void setStyle(int r_, int g_, int b_);

    void translateBy(Vector v);

    void rotateBy(float dangle, Point center);

    // Rotate about center with an angle specified by two vectors.
    // Avoids the use of acos
    void rotateBy(Vector u, Vector v, Point center);

    void scaleBy(float ds, Point center);
}
