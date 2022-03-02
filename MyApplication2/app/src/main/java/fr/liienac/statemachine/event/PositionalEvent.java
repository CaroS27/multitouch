/*
 * Copyright (c) 2016 Stéphane Conversy, Nicolas Saporito - ENAC - All rights Reserved
 */
package fr.liienac.statemachine.event;

import fr.liienac.statemachine.geometry.Point;

public class PositionalEvent<Item> extends Event {
	public int cursorID;
	public Point p;
	public Item graphicItem;
	public float orientation;
	PositionalEvent(int cursorid_, Point p_, Item s_) { cursorID=cursorid_; p=p_; graphicItem =s_; orientation=0;}
	PositionalEvent(int cursorid_, Point p_, Item s_, float o_) { cursorID=cursorid_; p=p_; graphicItem =s_; orientation=o_;}
}

