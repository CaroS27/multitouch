/*
 * Copyright (c) 2016 Stéphane Conversy, Nicolas Saporito - ENAC - All rights Reserved
 */
package com.example.conversy.multitouch.statemachines;

import static java.lang.Math.abs;

import java.util.ArrayList;

import fr.liienac.statemachine.GraphicMachine;
import fr.liienac.statemachine.event.Press;
import fr.liienac.statemachine.event.Release;
import fr.liienac.statemachine.geometry.Point;
import fr.liienac.statemachine.graphic.GraphicItem;

public class SelectMachine extends GraphicMachine {
    ArrayList<Integer> cursors = new ArrayList<Integer>();
    ArrayList<Point> points = new ArrayList<Point>();


    public SelectMachine(GraphicItem graphicItem) {
        super(graphicItem);
    }

    public State start = new State() {
        Transition press = new Transition<Press>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem;
            }

            public void action() {
                graphicItem = (GraphicItem) evt.graphicItem;
                cursors.add(evt.cursorID);
                points.add(evt.p);
                graphicItem.setStyle(255, 0, 0);
            }

            public State goTo() {
                return touched;
            }
        };
    };


    public State touched = new State() {
        Transition finalRelease = new Transition<Release>() {
            public boolean guard() {
                return cursors.contains(evt.cursorID) && cursors.size() == 1;
            }

            public void action() {
                points.removeAll(points);
                cursors.removeAll(cursors);
                graphicItem.setStyle(0, 0, 0);
                System.out.println("final release--> "+ cursors.size());
            }

            public State goTo() {
                return start;
            }
        };

        Transition release = new Transition<Release>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem && cursors.contains(evt.cursorID);
            }

            @Override
            public void action() {
                points.remove(cursors.indexOf(evt.cursorID));
                cursors.remove(cursors.indexOf(evt.cursorID));
                System.out.println("release --> "+ cursors.size());
            }
        };

        Transition press = new Transition<Press>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem;
            }

            public void action() {
                cursors.add(evt.cursorID);
                points.add(evt.p);
            }

        };

    };

}