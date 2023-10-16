package maze;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MazeRunner implements Serializable {

    // size of the maze
    private int rows;
    private int columns;

    // size of the graph
    private int graphRows;
    private int graphColumns;

    // my first maze. Initialized for nostalgic reasons only ;)
    // could be not initialized, too
    private int[][] maze = {
            {1, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 0, 1, 0, 1},
            {1, 0, 0, 0, 1, 1, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    // my list of edges
    ArrayList<Edge> listOfEdges = new ArrayList<>();

    // my minimum spanning tree
    ArrayList<Edge> minimumSpanningTree = new ArrayList<>();

    /* my minimum spanning tree contains edges containing two nodes
    / There are two methods to check, if an edge will cause a circle in my tree
    / 1st: go through all edges and check, if the edge is the tree
    / 2nd: (and in my opinion simpler method) search if both nodes of this edge are already covered
    /       in this,
    / so I create parallel to the spanningTree a node list and use it :) */
    ArrayList<Node> nodesInMinimumSpanningTree = new ArrayList<>();

    void createMaze() {

        // crate the base maze
        // set all numbers on the border to 1

        // I don't have to catch mazes with size < 3, because it will never
        // get to the part where it starts to look for edges

        // create maze
        maze = new int[rows][columns];

        // now fill the maze
        // go through all rows
        for (int i = 0; i < rows; i++) {

            // go through all columns
            for (int j = 0; j < columns; j++) {

                // set the starting node always to 0
                if ((i == 1) && (j == 1)) {

                    maze[i][j] = 0;

                } else {

                    // if you are on the border, just set 1
                    if ((i == 0) || (j == 0) || (i == (rows - 1) || (j == (columns - 1)))) {

                        maze[i][j] = 1;

                    } else {

                        // if both coordinates are uneven, it's always a free field
                        // == vertices of our graph
                        if (((i % 2) == 1) && ((j % 2) == 1)) {

                            maze[i][j] = 0;

                        } else {

                            // if both coordinates are even, it's always a wall
                            if (((i % 2) == 0) && ((j % 2) == 0)) {

                                maze[i][j] = 1;

                            } else {

                                // now check, if our spanning tree has an edge here
                                // for this we create the supposed edge and check if it is in our list
                                // because we checked it. i and j have to be:
                                // one (and only one) even and one (and only one) uneven

                                Edge thisEdge = new Edge();
                                Node firstNode = new Node();
                                Node secondNode = new Node();

                                // if 'i' is uneven, we search for a horizontal edge
                                // this means row stays the same
                                if ((i % 2 == 1)) {

                                    firstNode.setPlaceInRow((i - 1) / 2);
                                    firstNode.setPlaceInColumn((j / 2) - 1);
                                    secondNode.setPlaceInRow((i - 1) / 2);
                                    secondNode.setPlaceInColumn((j / 2));


                                } else {

                                    // if 'i' is even, we search for a vertical edge
                                    // this means the column is the same for both

                                    firstNode.setPlaceInRow((i / 2) - 1);
                                    firstNode.setPlaceInColumn((j - 1) / 2);
                                    secondNode.setPlaceInRow((i / 2));
                                    secondNode.setPlaceInColumn((j - 1) / 2);

                                }

                                thisEdge.setFirstNode(firstNode);
                                thisEdge.setSecondNode(secondNode);
                                //weight is not relevant

                                // if we can find this edge in our tree, it's a walking path (means 0)
                                // otherwise it's a wall
                                boolean found = false;

                                for (Edge minimalSpanningTreeEdge : minimumSpanningTree) {

                                    if (minimalSpanningTreeEdge.equals(thisEdge)) {

                                        found = true;
                                        break;

                                    }

                                }

                                if (found) {

                                    maze[i][j] = 0;

                                } else {

                                    maze[i][j] = 1;

                                }

                            }


                        }


                    }

                }

            }


        }

        // now create a random entrance
        Random random = new Random();

        boolean createdEntrance = false;
        while (!createdEntrance) {

            int entrance = random.nextInt();

            entrance = Math.abs(entrance % rows);
            if (!((entrance == 0) || (entrance == (rows - 1)))) {
                if (maze[entrance][1] == 0) {

                    maze[entrance][0] = 0;
                    createdEntrance = true;


                }


            }

        }

        boolean createdExit = false;
        while (!createdExit) {

            int exit = random.nextInt();

            exit = Math.abs(exit % rows);

            if (!((exit == 0) || (exit == (rows - 1)))) {
                int lastColumnOfMaze = columns - 2;
                if ((columns % 2) == 0) {
                    lastColumnOfMaze = columns - 3;
                }

                if (maze[exit][lastColumnOfMaze] == 0) {

                    maze[exit][columns - 1] = 0;
                    if ((columns % 2) == 0) {

                        maze[exit][columns - 2] = 0;

                    }

                    createdExit = true;

                }


            }

        }

    }

    void kruskalMinimumSpanningTree() {

    }

    void createMinimumSpanningTree() {

        // you can only create a spanning tree, starting a size >= 5 on at least one side
        // this means our graph has to have at least one size >= 2
        if ((graphRows > 1) || (graphColumns > 1)) {

            // our first node is the 0 0- node
            Node startingNode = new Node();
            startingNode.setPlaceInRow(0);
            startingNode.setPlaceInColumn(0);

            Edge nextMinimumWeight = null;

            // ad the first edge
            // go through all edges
            for (Edge edge : listOfEdges) {
                // is this an edge of the starting node?
                if (edge.containsNode(startingNode)) {
                    // is this the first edge with the starting node?
                    if (nextMinimumWeight == null) {
                        // take the first, so I can compare it later
                        nextMinimumWeight = edge;
                    } else {
                        // if another edge is smaller, take the smaller one
                        if (edge.getWeight() < nextMinimumWeight.getWeight()) {
                            nextMinimumWeight = edge;
                        }
                    }
                }
            }

            // we have found our first edge! YAY \^.^/
            minimumSpanningTree.add(nextMinimumWeight);
            nodesInMinimumSpanningTree.add(nextMinimumWeight.getFirstNode());
            nodesInMinimumSpanningTree.add(nextMinimumWeight.getSecondNode());

            // reset our minimum weight edge
            nextMinimumWeight = null;

            // number of edges in a minimum spanning tree is always vertices - 1
            final int numberOfEdgesInMinimumSpanningTree = (graphRows * graphColumns) - 1;

            // now get a list of all edges
            while (minimumSpanningTree.size() < numberOfEdgesInMinimumSpanningTree) {

                // look after every edge in the graph

                for (Edge graphEdge : listOfEdges) {
                    //find the edge with min weight that does not form a circle

                    // the edge is only interesting, if it would not:
                    // generate a circle or is already part
                    // of our spanning tree
                    if (!createsCircleInSpanningTree(graphEdge)) {

                        // is this edge connected with our current spanning tree?
                        for (Node nodeInMinimumSpanningTree : nodesInMinimumSpanningTree) {

                            if (graphEdge.containsNode(nodeInMinimumSpanningTree)) {

                                // if this is the first edge, it's our current, minimal weight
                                if (nextMinimumWeight == null) {

                                    nextMinimumWeight = graphEdge;

                                } else {

                                    // is the weight of this edge smaller, than the others?
                                    if (graphEdge.getWeight() < nextMinimumWeight.getWeight()) {

                                        nextMinimumWeight = graphEdge;

                                    }

                                }

                            }

                        }

                    }

                }

                if (nextMinimumWeight == null) {
                    System.out.println("Something wrong happened!");
                    throw new NullPointerException("nextMinimumWeight is Null, can't add null edges to Minimum Spanning tree!");
                }

                // we found our next edge! :)
                minimumSpanningTree.add(nextMinimumWeight);

                // and for convenience the nodes to our nodes list
                nodesInMinimumSpanningTree.add(nextMinimumWeight.getFirstNode());
                nodesInMinimumSpanningTree.add(nextMinimumWeight.getSecondNode());

                // reset the minimum weight
                nextMinimumWeight = null;

            }

        }

    }

    boolean createsCircleInSpanningTree(Edge edge) {

//        boolean foundFirstNode = false;
//        boolean foundSecondNode = false;

//        for (Node spanningTreeNode : nodesInMinimumSpanningTree) {
//
//            if (spanningTreeNode.equals(edge.getFirstNode())) {
//
//                foundFirstNode = true;
//
//            }
//
//            if (spanningTreeNode.equals(edge.getSecondNode())) {
//
//                foundSecondNode = true;
//
//            }
//
//        }
//
//        return foundFirstNode && foundSecondNode;

        return nodesInMinimumSpanningTree.containsAll(List.of(edge.getFirstNode(), edge.getSecondNode()));


    }

    void createGraphWithRandomEdges() {

        // my vertices are the uneven fields of the maze
        this.graphRows = ((rows % 2) == 0) ? ((rows - 1) / 2) : (rows / 2);
        this.graphColumns = ((columns % 2) == 0) ? ((columns - 1) / 2) : (columns / 2);

        // random generator (for debugging i would recommend to use a seed)
        Random random = new Random();

        // fill the List with all possible edges
        for (int i = 0; i < graphRows; i++) {

            for (int j = 0; j < graphColumns; j++) {

                // Node of the current place in the Graph
                Node thisNode = new Node();
                thisNode.setPlaceInRow(i);
                thisNode.setPlaceInColumn(j);

                // if you are not in the last row, there is a node under this one
                if (i < (graphRows - 1)) {

                    Node nodeUnderThisNode = new Node();
                    nodeUnderThisNode.setPlaceInRow(i + 1);
                    nodeUnderThisNode.setPlaceInColumn(j);

                    Edge edgeFromThisNodeToTheNodeUnderIt = new Edge();
                    edgeFromThisNodeToTheNodeUnderIt.setFirstNode(thisNode);
                    edgeFromThisNodeToTheNodeUnderIt.setSecondNode(nodeUnderThisNode);
                    edgeFromThisNodeToTheNodeUnderIt.setWeight(random.nextInt());

                    listOfEdges.add(edgeFromThisNodeToTheNodeUnderIt);

                }

                // if you are not in the last column, there is a node next to this one
                if (j < (graphColumns - 1)) {

                    Node nodeNextThisNode = new Node();
                    nodeNextThisNode.setPlaceInRow(i);
                    nodeNextThisNode.setPlaceInColumn(j + 1);

                    Edge edgeFromThisNodeToTheNextNode = new Edge();
                    edgeFromThisNodeToTheNextNode.setFirstNode(thisNode);
                    edgeFromThisNodeToTheNextNode.setSecondNode(nodeNextThisNode);
                    edgeFromThisNodeToTheNextNode.setWeight(random.nextInt());

                    listOfEdges.add(edgeFromThisNodeToTheNextNode);

                }

            }

        }

    }

    void getSizeOfMaze(Scanner scanner) {

        System.out.println("Please, enter the size of a maze");

        int inputRows = Integer.parseInt(scanner.nextLine());
        int inputColumns = inputRows;

        while ((inputRows < 3) || (inputColumns < 3)) {

            if (inputRows < 3) {

                System.out.println("To few rows for a maze.");
                System.out.println("Please insert a correct number (> 3)");

                inputRows = scanner.nextInt();


            } else {

                System.out.println("To few columns for a maze.");
                System.out.println("Please insert a correct number (> 3)");

                inputColumns = scanner.nextInt();

            }

        }

        this.rows = inputRows;
        this.columns = inputColumns;

    }

    // for debugging
    void printMinimumSpanningTree() {

        System.out.println();
        System.out.println("Minimum spanning tree:");
        for (Edge edge : minimumSpanningTree) {

            /*System.out.printf("  {%d, %d} - {%d, %d}: %d", edge.getFirstNode().getPlaceInRow(), edge.getFirstNode().getPlaceInColumn(),
                    edge.getSecondNode().getPlaceInRow(), edge.getSecondNode().getPlaceInColumn(), edge.getWeight());*/

            System.out.println("   {" + edge.getFirstNode().getPlaceInRow() + ", " + edge.getFirstNode().getPlaceInColumn() + "} - " +
                    "{" + edge.getSecondNode().getPlaceInRow() + ", " + edge.getSecondNode().getPlaceInColumn() + "}: " + edge.getWeight());

        }

    }

    // for debugging
    void printListOfEdges() {

        System.out.println();
        System.out.println("List of Edges between vertices");
        for (Edge edge : listOfEdges) {

            System.out.println("   {" + edge.getFirstNode().getPlaceInRow() + ", " + edge.getFirstNode().getPlaceInColumn() + "} - " +
                    "{" + edge.getSecondNode().getPlaceInRow() + ", " + edge.getSecondNode().getPlaceInColumn() + "}: " + edge.getWeight());

        }

    }

    void printMaze() {

        for (int[] row : maze) {

            for (int tile : row) {

                switch (tile) {
//                    case 1 -> System.out.print("||");
                    case 1 -> System.out.print("██");
//                    case 1 -> System.out.print((char) 0x2588 + "" + (char) 0x2588);
                    case 0 -> System.out.print("  ");
                    default -> System.out.println("Wow, this shouldn't happen! " + tile);
                }
            }

            System.out.println();

        }

    }

    public int[][] getMaze() {
        return maze;
    }
}
