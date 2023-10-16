package maze;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Queue;

public class PathFinder {
    private MazeRunner mazeRunner;
    private int[][] mazeWithFoundPath;
    private Point startPoint;
    private Point endPoint;
    private Queue<String> path;
    private String correctPath;

    public PathFinder(MazeRunner mazeRunner) {
        this.mazeRunner = mazeRunner;
        path = new ArrayDeque<>();
        mazeWithFoundPath = new int[mazeRunner.getMaze().length][mazeRunner.getMaze()[0].length];
    }

    public void findPath() {
        findStartPoint();
        findEndPoint();
        path.offer("");
        String[] allDirections = {"U", "D", "L", "R"};
        String currentPath = "";
        while (!isEndPoint(currentPath)) {
            currentPath = path.poll();
            for (String direction : allDirections) {
//                String toPut = currentPath.concat(direction);
                String toPut = currentPath + direction;
                if (isValid(toPut)) {
                    path.offer(toPut);
                }
            }
//            System.out.println("currentPath = " + currentPath);
//            System.out.println("path = " + path);
        }
        System.out.println(currentPath);
        correctPath = currentPath;
        updateWithEscapeRoute();
    }

    private void updateWithEscapeRoute() {
        Point currentPoint = new Point(startPoint);
        for (int i = 0; i < mazeRunner.getMaze().length; i++) {
            for (int j = 0; j < mazeRunner.getMaze()[i].length; j++) {
                mazeWithFoundPath[i][j] = mazeRunner.getMaze()[i][j];
            }
        }
//        mazeWithFoundPath = Arrays.copyOf(mazeRunner.getMaze(), mazeRunner.getMaze().length);
        mazeWithFoundPath[currentPoint.x][currentPoint.y] = 2;
        for (char c : correctPath.toCharArray()) {
            moveOneStep(currentPoint, c);
            mazeWithFoundPath[currentPoint.x][currentPoint.y] = 2;
        }
    }

    private void moveOneStep(Point currentPoint, char c) {
        switch (c) {
            case 'U':
                currentPoint.translate(-1, 0);
                break;
            case 'D':
                currentPoint.translate(1, 0);
                break;
            case 'L':
                currentPoint.translate(0, -1);
                break;
            case 'R':
                currentPoint.translate(0, 1);
                break;
            case ' ':
                break;
            default:
                System.out.println("This shouldn't happen: " + c);
        }
    }

    public void printWithCorrectRoute() {
        mazeRunner.printMaze();
        System.out.println("\n");
        for (int[] row : mazeWithFoundPath) {
            for (int tile : row) {
                switch (tile) {
                    case 2 -> System.out.print("//");
                    case 1 -> System.out.print("██");
                    case 0 -> System.out.print("  ");
                    default -> System.out.println("Wow, this shouldn't happen!");
                }
            }
            System.out.println();
        }
    }

    private boolean isValid(String currentPath) {
        Point currentPoint = new Point(startPoint.x, startPoint.y);
        char preChar = ' ';
        for (char c : currentPath.toCharArray()) {
            moveOneStep(currentPoint, c);
            if (currentPoint.x < 0 || currentPoint.x >= mazeRunner.getMaze().length ||
                    currentPoint.y < 0 || currentPoint.y >= mazeRunner.getMaze()[0].length ||
                    mazeRunner.getMaze()[currentPoint.x][currentPoint.y] == 1) {
                return false;
            }
            switch (c) {
                case 'U':
                    if (preChar == 'D') {
                        return false;
                    }
                    break;
                case 'D':
                    if (preChar == 'U') {
                        return false;
                    }
                    break;
                case 'L':
                    if (preChar == 'R') {
                        return false;
                    }
                    break;
                case 'R':
                    if (preChar == 'L') {
                        return false;
                    }
                    break;
                default:
                    System.out.println("This should never happen!");
            }
            preChar = c;
        }
        return true;
    }

    private boolean isEndPoint(String currentPath) {
        Point currentPoint = new Point(startPoint.x, startPoint.y);
        for (char c : currentPath.toCharArray()) {
            switch (c) {
                case 'U':
                    currentPoint.translate(-1, 0);
                    break;
                case 'D':
                    currentPoint.translate(1, 0);
                    break;
                case 'L':
                    currentPoint.translate(0, -1);
                    break;
                case 'R':
                    currentPoint.translate(0, 1);
                    break;
                case ' ':
                    return false;
                default:
                    System.out.println("This shouldn't happen: " + c);
            }
        }
        return currentPoint.equals(endPoint);
    }

    private void findStartPoint() {
        for (int i = 0; i < mazeRunner.getMaze()[0].length; i++) {
            if (mazeRunner.getMaze()[0][i] == 0) {
                startPoint = new Point(0, i);
                return;
            }
        }
        for (int i = 0; i < mazeRunner.getMaze().length; i++) {
            if (mazeRunner.getMaze()[i][0] == 0) {
                startPoint = new Point(i, 0);
                return;
            }
        }

        for (int i = 0; i < mazeRunner.getMaze()[0].length; i++) {
            if (mazeRunner.getMaze()[mazeRunner.getMaze().length - 1][i] == 0) {
                startPoint = new Point(mazeRunner.getMaze().length - 1, i);
                return;
            }
        }

        for (int i = 0; i < mazeRunner.getMaze().length; i++) {
            if (mazeRunner.getMaze()[i][mazeRunner.getMaze()[0].length - 1] == 0) {
                startPoint = new Point(i, mazeRunner.getMaze()[0].length - 1);
                return;
            }
        }
        startPoint = null;
    }

    private void findEndPoint() {
        for (int i = 0; i < mazeRunner.getMaze()[0].length; i++) {
            if (mazeRunner.getMaze()[0][i] == 0 && !new Point(0, i).equals(startPoint)) {
                endPoint = new Point(0, i);
                return;
            }
        }
        for (int i = 0; i < mazeRunner.getMaze().length; i++) {
            if (mazeRunner.getMaze()[i][0] == 0 && !new Point(i, 0).equals(startPoint)) {
                endPoint = new Point(i, 0);
                return;
            }
        }

        for (int i = 0; i < mazeRunner.getMaze()[0].length; i++) {
            if (mazeRunner.getMaze()[mazeRunner.getMaze().length - 1][i] == 0 && !new Point(mazeRunner.getMaze().length - 1, i).equals(startPoint)) {
                endPoint = new Point(mazeRunner.getMaze().length - 1, i);
                return;
            }
        }

        for (int i = 0; i < mazeRunner.getMaze().length; i++) {
            if (mazeRunner.getMaze()[i][mazeRunner.getMaze()[0].length - 1] == 0 &&
                    !new Point(i, mazeRunner.getMaze()[0].length - 1).equals(startPoint)) {
                endPoint = new Point(i, mazeRunner.getMaze()[0].length - 1);
                return;
            }
        }
        endPoint = null;
    }
}
