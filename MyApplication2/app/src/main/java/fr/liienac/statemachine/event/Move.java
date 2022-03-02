/*
 * Copyright (c) 2016 Stéphane Conversy, Nicolas Saporito - ENAC - All rights Reserved
 */
package fr.liienac.statemachine.event;

import fr.liienac.statemachine.geometry.Point;

public class Move<Item> extends PositionalEvent<Item> {

	// Add attribute pressure to be accessed in state machines
	public float pressure;

	public Move(int cursorid_, Point p_, Item s_, float angRad, float pressure) { super(cursorid_, p_, s_, angRad); this.pressure = pressure;}
}