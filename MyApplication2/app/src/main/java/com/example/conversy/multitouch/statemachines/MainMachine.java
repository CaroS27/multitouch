package com.example.conversy.multitouch.statemachines;

import static java.lang.Math.abs;

import java.util.ArrayList;

import fr.liienac.statemachine.GraphicMachine;
import fr.liienac.statemachine.StateMachine;
import fr.liienac.statemachine.event.Move;
import fr.liienac.statemachine.event.Press;
import fr.liienac.statemachine.event.Release;
import fr.liienac.statemachine.geometry.Point;
import fr.liienac.statemachine.geometry.Vector;

public class MainMachine extends StateMachine {
    ArrayList<Integer> cursors = new ArrayList<Integer>();
    ArrayList<Point> points = new ArrayList<Point>();
    ArrayList<GraphicMachine> allMachines = null;


    public MainMachine(ArrayList<GraphicMachine> machines) {
        this.allMachines = machines;
    }

    public State start = new State() {
        Transition press = new Transition<Press>() {
            public boolean guard() {
                return evt.graphicItem == null;
            }

            public void action() {
                cursors.add(evt.cursorID);
                points.add(evt.p);
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
                System.out.println("final release--> " + cursors.size());
            }

            public State goTo() {
                return start;
            }
        };

        Transition release = new Transition<Release>() {
            public boolean guard() {
                return cursors.contains(evt.cursorID);
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
                return evt.graphicItem == null;
            }

            public void action() {
                cursors.add(evt.cursorID);
                points.add(evt.p);
            }

            public State goTo() { return RRR;}

        };

        Transition drag = new Transition<Move>() {
            public boolean guard() {
                return Point.distance(points.get(0), evt.p) > 50 && cursors.get(0) == evt.cursorID;
            }

            public void action() {
                for (int i=0; i<allMachines.size()-1; i++){
                    if (allMachines.get(i).getCurrentState() == allMachines.get(i).getFirstState()) {
                        allMachines.get(i).graphicItem.translateBy(new Vector(points.get(0), evt.p));
                    }
                }
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
                System.out.println("final release--> " + cursors.size());
            }

            public State goTo() {
                return start;
            }
        };

        Transition release = new Transition<Release>() {
            public boolean guard() {
                return cursors.contains(evt.cursorID);
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
                return evt.graphicItem == null;
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
                for (int i=0; i< allMachines.size() - 1; i++) {
                    if (allMachines.get(i).getCurrentState() == allMachines.get(i).getFirstState()) {
                        allMachines.get(i).graphicItem.translateBy(new Vector(points.get(0), evt.p));
                    }
                }
                points.set(0, evt.p);
            }
        };


    };

    public State RRR = new State() {
        Transition release = new Transition<Release>() {
            public boolean guard() {
                return (evt.graphicItem == null && cursors.contains(evt.cursorID));
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

                for (int i=0; i< allMachines.size() - 1; i++) {
                    if (allMachines.get(i).getCurrentState() == allMachines.get(i).getFirstState()) {
                        allMachines.get(i).graphicItem.scaleBy(vectorAfter.norm() / vectorBefore.norm(), center);
                        allMachines.get(i).graphicItem.rotateBy(vectorAfter, vectorBefore, center);
                    }
                }
            }

        };

    };

}