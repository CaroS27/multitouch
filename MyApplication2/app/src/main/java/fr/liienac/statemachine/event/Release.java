/*
 * Copyright (c) 2016 Stéphane Conversy, Nicolas Saporito - ENAC - All rights Reserved
 */
package fr.liienac.statemachine.event;

import fr.liienac.statemachine.geometry.Point;

public class Release<Item> extends PositionalEvent {
	public Release(int cursorid_, Point p_, Item s_, float angRad) { super(cursorid_, p_, s_, angRad); }
}