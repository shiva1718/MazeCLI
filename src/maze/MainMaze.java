package maze;

import java.io.*;
import java.util.Scanner;

public class MainMaze {

    static MazeRunner mazeRunner;

    public static void main(String[] args) {
        mazeRunner = new MazeRunner();
        boolean isRunning = true;
        Scanner kb = new Scanner(System.in);
        boolean isMazeCreated = false;
        while (!isMazeCreated) {
            System.out.println("" +
                    "=== Menu ===\n" +
                    "1. Generate a new maze\n" +
                    "2. Load a maze\n" +
                    "0. Exit");
            int choice = Integer.parseInt(kb.nextLine());
            switch (choice) {
                case 1 -> {
                    generateMaze(kb);
                    isMazeCreated = true;
                }
                case 2 -> {
                    loadMaze(kb);
                    isMazeCreated = true;
                }
                case 0 -> {
                    isMazeCreated = true;
                    isRunning = false;
                }
                default -> System.out.println("Incorrect option. Please try again");
            }
        }
        while (isRunning) {
            System.out.println("" +
                    "=== Menu ===\n" +
                    "1. Generate a new maze\n" +
                    "2. Load a maze\n" +
                    "3. Save the maze\n" +
                    "4. Display the maze\n" +
                    "5. Find the escape\n" +
                    "0. Exit");
            int choice = Integer.parseInt(kb.nextLine());
            switch (choice) {
                case 1 -> generateMaze(kb);
                case 2 -> loadMaze(kb);
                case 3 -> saveMaze(kb);
                case 4 -> displayMaze();
                case 5 -> {
                    PathFinder pathFinder = new PathFinder(mazeRunner);
                    pathFinder.findPath();
                    pathFinder.printWithCorrectRoute();
                }
                case 0 -> isRunning = false;
                default -> System.out.println("Incorrect option. Please try again");
            }
        }

    }

    public static void generateMaze(Scanner kb) {
        mazeRunner.getSizeOfMaze(kb);
//        long start = System.nanoTime();
        mazeRunner.createGraphWithRandomEdges();
//        mazeRunner.printListOfEdges();
        mazeRunner.createMinimumSpanningTree();
//        mazeRunner.printMinimumSpanningTree();
        mazeRunner.createMaze();
        mazeRunner.printMaze();
//        long end = System.nanoTime();
//        long elapsedNano = end - start;

    }

    public static void loadMaze(Scanner kb) {
        String filename = kb.nextLine();
        File file = new File(filename);
        if (file.exists()) {
            try {
                mazeRunner = (MazeRunner) deserialize(filename);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("Cannot load the maze. It has an invalid format");
            }
        } else {
            System.out.printf("The file %s does not exist\n", filename);
        }
    }

    public static void saveMaze(Scanner kb) {
        String filename = kb.nextLine();
        File file = new File(filename);
        boolean isCreated = false;
        if (!file.exists()) {
            try {
                isCreated = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (file.exists()) {
            try {
                serialize(mazeRunner, filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Something wrong happened");
            System.out.println("isCreated = " + isCreated);
            System.out.println("filename = " + filename);
        }
    }

    public static void displayMaze() {
        mazeRunner.printMaze();
    }

    /**
     * Serialize the given object to the file
     */
    public static void serialize(Object obj, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
    }

    /**
     * Deserialize to an object from the file
     */
    public static Object deserialize(String fileName) throws IOException, ClassNotFoundException {

        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }
}
