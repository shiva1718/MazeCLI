package maze;

import java.io.Serializable;

public class Node implements Serializable {

    private int placeInRow;
    private int placeInColumn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return placeInRow == node.placeInRow &&
                placeInColumn == node.placeInColumn;
    }

    public int getPlaceInRow() {
        return placeInRow;
    }

    public void setPlaceInRow(int placeInRow) {
        this.placeInRow = placeInRow;
    }

    public int getPlaceInColumn() {
        return placeInColumn;
    }

    public void setPlaceInColumn(int placeInColumn) {
        this.placeInColumn = placeInColumn;
    }
}
