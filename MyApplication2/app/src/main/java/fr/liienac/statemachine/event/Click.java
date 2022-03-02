/*
 * Copyright (c) 2016 Stéphane Conversy, Nicolas Saporito - ENAC - All rights Reserved
 */
package fr.liienac.statemachine.event;

import fr.liienac.statemachine.geometry.Point;

public class Click<Item> extends PositionalEvent<Item> {
    public int num;
	public Click(int cursorid_, Point p_, Item s_, float angRad, int num_) { super(cursorid_, p_, s_, angRad); num=num_; }
}