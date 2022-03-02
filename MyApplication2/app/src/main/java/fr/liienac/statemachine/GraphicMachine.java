package fr.liienac.statemachine;

import fr.liienac.statemachine.graphic.GraphicItem;

public abstract class GraphicMachine extends StateMachine{

    public GraphicItem graphicItem;

    public GraphicMachine(GraphicItem graphicItem) {
        this.graphicItem = graphicItem;
    }

    public GraphicItem getGraphicItem() {
        return graphicItem;
    }
}
