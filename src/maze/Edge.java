package maze;

import java.io.Serializable;

public class Edge implements Serializable {

    private Node firstNode;
    private Node secondNode;

    private int weight;

    // I did not want to check both nodes every time, so I created a function in Edge to do this
    public boolean containsNode(Node node){

        return node.equals(firstNode) || node.equals(secondNode);

    }

    // Two edges in a undirected graph are equal, if they contain the same nodes
    // the order don't matter. to spare me the eternal comparison, i overwrote the equal method.
    // the weight does not matter in this case
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge edge = (Edge) o;
        return this.containsNode(edge.firstNode) &&
                this.containsNode(edge.secondNode);
    }

    public Node getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(Node firstNode) {
        this.firstNode = firstNode;
    }

    public Node getSecondNode() {
        return secondNode;
    }

    public void setSecondNode(Node secondNode) {
        this.secondNode = secondNode;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
