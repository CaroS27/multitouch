package com.example.conversy.multitouch.statemachines;

import static java.lang.Math.abs;

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

public class RRRMachine extends GraphicMachine {
    ArrayList<Integer> cursors = new ArrayList<Integer>();
    ArrayList<Point> points = new ArrayList<Point>();
    public Vibrator vib;
    public VibrationEffect vibEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK);


    public RRRMachine(GraphicItem graphicItem, Vibrator vib) {
        super(graphicItem);
        this.vib = vib;

    }

    public State start = new State() {
        Transition press = new Transition<Press>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem;
            }

            public void action() {
                //vib.vibrate(vibEffect);
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
                System.out.println("final release--> " + cursors.size());
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
                return cursors.get(0) == evt.cursorID;
            }

            public void action() {
                graphicItem.translateBy(new Vector(points.get(0), evt.p));
                points.set(0, evt.p);
            }
        };


    };

//    public State resized = new State() {
//        Transition release = new Transition<Release>() {
//            public boolean guard() { return cursor.contains(evt.cursorID); }
//            public void action() {
//                point.remove(cursor.indexOf(evt.cursorID));
//
//                cursor.remove(evt.cursorID);
//
//            }
//            public State goTo() { return touched; }
//        };
//
//        Transition resize = new Transition<Move>() {
//            public boolean guard() { return cursor.contains(evt.cursorID); }
//
//            public void action() {
//                Vector vectorBefore = new Vector(point.get(0), point.get(1));
//                //float before = Point.distance(point.get(0), point.get(1));
//                int movingPointId = cursor.indexOf(evt.cursorID);
//                Point center;
//                if (movingPointId == 0) {
//                    center = point.get(1);
//                    point.set(0, evt.p);
//                } else {
//                    center = point.get(0);
//                    point.set(1, evt.p);
//                }
//                //float after = Point.distance(point.get(0), point.get(1));
//                Vector vectorAfter = new Vector(point.get(0), point.get(1));
//                //float angle = Vector.scalarProduct(vectorBefore,vectorAfter) / (vectorBefore.norm() * vectorAfter.norm());
//                //System.out.println(after/before);
//                graphicItem.scaleBy(vectorAfter.norm() / vectorBefore.norm(), center);
//                graphicItem.rotateBy(vectorAfter, vectorBefore, center);
//            }
//        };
//
//    };

//    public State waitigToDecide = new State() {
//        Transition release = new Transition<Release>() {
//            public boolean guard() {
//                return cursor.contains(evt.cursorID);
//            }
//
//            public void action() {
//                point.remove(cursor.indexOf(evt.cursorID));
//
//                cursor.remove(evt.cursorID);
//
//            }
//
//            public State goTo() {
//                return touched;
//            }
//        };
//
//        Transition rotate = new Transition<Move>() {
//            public boolean guard() {
//
//                Vector vectorBefore = new Vector(point.get(0), point.get(1));
//                //float before = Point.distance(point.get(0), point.get(1));
//                int movingPointId = cursor.indexOf(evt.cursorID);
//                Point center;
//                if (movingPointId == 0) {
//                    center = point.get(1);
//                    point.set(0, evt.p);
//                } else {
//                    center = point.get(0);
//                    point.set(1, evt.p);
//                }
//                //float after = Point.distance(point.get(0), point.get(1));
//                Vector vectorAfter = new Vector(point.get(0), point.get(1));
//
//                float angle = Vector.scalarProduct(vectorBefore,vectorAfter) / (vectorBefore.norm() * vectorAfter.norm());
//                boolean doRotation = angle > 2;
//
//                return cursor.contains(evt.cursorID) && doRotation;
//
//
//            }
//
//            public State goTo() {
//                return rotated;
//            }
//        };
//
//        Transition resize = new Transition<Move>() {
//            public boolean guard() {
//                return cursor.contains(evt.cursorID);
//            }
//
//            public void action() {
//            }
//
//            public State goTo() {
//                return resized;
//            }
//        };
//    };

    public State RRR = new State() {
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

                return cursors.contains(evt.cursorID);


            }

            public void action() {
                Vector vectorBefore = new Vector(points.get(0), points.get(1));
                //float before = Point.distance(point.get(0), point.get(1));
                int movingPointId = cursors.indexOf(evt.cursorID);
                Point center;
                if (movingPointId == 0) {
                    center = points.get(1);
                    points.set(0, evt.p);
                } else {
                    center = points.get(0);
                    points.set(1, evt.p);
                }
                //float after = Point.distance(point.get(0), point.get(1));
                Vector vectorAfter = new Vector(points.get(0), points.get(1));
                //float angle = Vector.scalarProduct(vectorBefore,vectorAfter) / (vectorBefore.norm() * vectorAfter.norm());
                //System.out.println(after/before);
                graphicItem.scaleBy(vectorAfter.norm() / vectorBefore.norm(), center);
                graphicItem.rotateBy(vectorAfter, vectorBefore, center);
            }

        };

    };

}
