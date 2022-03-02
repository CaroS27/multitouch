package com.example.conversy.multitouch.statemachines;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import java.util.ArrayList;

import fr.liienac.statemachine.GraphicMachine;
import fr.liienac.statemachine.event.Move;
import fr.liienac.statemachine.event.Press;
import fr.liienac.statemachine.event.Release;
import fr.liienac.statemachine.geometry.Point;
import fr.liienac.statemachine.geometry.Vector;
import fr.liienac.statemachine.graphic.GraphicItem;

public class PressureMachine extends GraphicMachine {
    ArrayList<Integer> cursors = new ArrayList<Integer>();
    ArrayList<Point> points = new ArrayList<Point>();

    // Pressure needed to freeze
    double hapticPressure = 0.16;

    // Get the context of th main activity to access the vibrator
    Context myContext;


    public PressureMachine(GraphicItem graphicItem, Context context) {
        super(graphicItem);
        this.myContext = context;

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

            // When all the touches are released
            public boolean guard() {
                return cursors.contains(evt.cursorID) && cursors.size() == 1;
            }

            public void action() {
                // we have to remove all the points and cursors registered
                points.removeAll(points);
                cursors.removeAll(cursors);
                graphicItem.setStyle(0, 0, 0);
                System.out.println("final release--> " + cursors.size());
            }

            public State goTo() {
                return start;
            }
        };

        Transition release = new Transition<Release>() {
            // When release one touch
            public boolean guard() {
                return evt.graphicItem == graphicItem && cursors.contains(evt.cursorID);
            }

            @Override
            public void action() {
                // we free the cursor and the points selected
                points.remove(cursors.indexOf(evt.cursorID));
                cursors.remove(cursors.indexOf(evt.cursorID));
                System.out.println("release --> " + cursors.size());
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

            public State goTo() {
                return RRR;
            }
        };

        Transition drag = new Transition<Move>() {
            // the move event
            public boolean guard() {
                return Point.distance(points.get(0), evt.p) > 50 && evt.graphicItem == graphicItem && cursors.get(0) == evt.cursorID;
            }

            public void action() {
                graphicItem.translateBy(new Vector(points.get(0), evt.p));
                points.set(0, evt.p);
            }

            public State goTo() {
                return moving;
            }
        };
    };

    public State moving = new State() {

        // When all the touches are released
        Transition finalRelease = new Transition<Release>() {
            public boolean guard() {
                return cursors.contains(evt.cursorID) && cursors.size() == 1;
            }

            public void action() {
                cursors.removeAll(cursors);
                points.removeAll(points);
                graphicItem.setStyle(0, 0, 0);
                System.out.println("final release--> " + cursors.size());
            }

            public State goTo() {
                return start;
            }
        };

        // When release one touch
        Transition release = new Transition<Release>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem && cursors.contains(evt.cursorID);
            }

            @Override
            public void action() {
                points.remove(cursors.indexOf(evt.cursorID));
                cursors.remove(cursors.indexOf(evt.cursorID));
                System.out.println("release --> " + cursors.size());
            }

            public State goTo() {
                return touched;
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

            public State goTo() {
                return RRR;
            }
        };

        Transition drag = new Transition<Move>() {
            public boolean guard() {
                return cursors.get(0) == evt.cursorID && evt.pressure < hapticPressure;
            }

            public void action() {
                graphicItem.translateBy(new Vector(points.get(0), evt.p));
                points.set(0, evt.p);

            }
        };


        Transition freezing = new Transition<Move>() {
            public boolean guard() {
                return cursors.get(0) == evt.cursorID && evt.pressure >= 0.05;
            }

            public void action() {
                graphicItem.translateBy(new Vector(points.get(0), evt.p));
                points.set(0, evt.p);
                cursors.removeAll(cursors);
                points.removeAll(points);
                graphicItem.setStyle(0, 0, 255);

                // Simulates haptic feedbacks
                Vibrator v = (Vibrator) myContext.getSystemService(Context.VIBRATOR_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(50);
                }
            }

            public State goTo() {
                return Freeze;
            }
        };


    };


    public State RRR = new State() {
        // enables zoom and rotation at the same time
        Transition release = new Transition<Release>() {
            public boolean guard() {
                return (evt.graphicItem == graphicItem && cursors.contains(evt.cursorID));
            }

            public void action() {
                points.remove(cursors.indexOf(evt.cursorID));

                cursors.remove(cursors.indexOf(evt.cursorID));

            }

            public State goTo() {
                return touched;
            }
        };

        Transition rotate = new Transition<Move>() {
            public boolean guard() {
                // makes sure we are on a cursor that point the item
                return cursors.contains(evt.cursorID);

            }

            public void action() {
                Vector vectorBefore = new Vector(points.get(0), points.get(1));
                int movingPointId = cursors.indexOf(evt.cursorID);
                Point center;
                if (movingPointId == 0) {
                    center = points.get(1);
                    points.set(0, evt.p);
                } else {
                    center = points.get(0);
                    points.set(1, evt.p);
                }
                Vector vectorAfter = new Vector(points.get(0), points.get(1));
                graphicItem.scaleBy(vectorAfter.norm() / vectorBefore.norm(), center);
                graphicItem.rotateBy(vectorAfter, vectorBefore, center);



            }

        };

    };

    public State Freeze = new State() {

        // when an item is blocked and doesn't react to the whole canvas movements

        Transition release = new Transition<Release>(){

            public boolean guard() {
                return (cursors.contains(evt.cursorID));
            }

            public void action(){
                cursors.remove(cursors.indexOf(evt.cursorID));
                points.remove(cursors.indexOf(evt.cursorID));
            }

        };

        Transition press = new Transition<Press>(){

            // unfreeze the item

            public boolean guard() {
                return (evt.graphicItem == graphicItem);
            }

            public void action(){
                points.add(evt.p);
                cursors.add(evt.cursorID);
                graphicItem.setStyle(255, 0, 0);
            }

            public State goTo(){
                return touched;
            }

        };

    };

}
