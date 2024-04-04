import java.util.*;

public class EightPuzzleSolver {

    private static final int[][] GOAL_STATE = {{7, 8, 1}, {6, 0, 2}, {5, 4, 3}};
    private static final int[][] MOVES = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    private static int numMove = 0;
    private static int numState = 1;


    public static void main(String[] args) {
        int[][] initialBoard = {{6, 7, 1}, {8, 2, 0}, {5, 4, 3}}; // Initial state of the puzzle
        Node solution = depthFirstSearch(initialBoard);
        if (solution != null) {
            System.out.println("Input:");
            printBoard(initialBoard);
            System.out.println("\n\nOutput:");
            printSolutionPath(solution);
            System.out.println("\nNumber of moves = " + numMove);
            System.out.println("Number of state enqueued = " + numState);
        } else {
            System.out.println("No solution found!");
        }
    }


    static Node depthFirstSearch(int[][] initialBoard) {

        Set<String> visitedStates = new HashSet<>();
        Stack<Node> stack = new Stack<>();
        Node initialNode = new Node(initialBoard, null, 0);
        stack.push(initialNode);

        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();
            visitedStates.add(Arrays.deepToString(currentNode.board));

            if (Arrays.deepEquals(currentNode.board, GOAL_STATE)) {
                return currentNode; // Solution found
            }

            if (currentNode.depth < 10) {
                List<Node> childNodes = generateChildNodes(currentNode);
                for (Node child : childNodes) {
                    if (!visitedStates.contains(Arrays.deepToString(child.board))) {
                        stack.push(child);
                        numState++;
                    }
                }
            }
        }

        return null; // No solution found
    }

    static List<Node> generateChildNodes(Node parent) {
        List<Node> childNodes = new ArrayList<>();
        int[][] parentBoard = parent.board;
        int emptyTileRow = -1, emptyTileCol = -1;

        // Find the position of the empty tile
        outerloop:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (parentBoard[i][j] == 0) {
                    emptyTileRow = i;
                    emptyTileCol = j;
                    break outerloop;
                }
            }
        }

        // Generate child nodes by moving the empty tile in all possible directions
        for (int[] move : MOVES) {
            int newRow = emptyTileRow + move[0];
            int newCol = emptyTileCol + move[1];
            if (isValidPosition(newRow, newCol)) {
                int[][] newBoard = copyBoard(parentBoard);
                // Swap empty tile with the tile at (newRow, newCol)
                newBoard[emptyTileRow][emptyTileCol] = newBoard[newRow][newCol];
                newBoard[newRow][newCol] = 0;
                childNodes.add(new Node(newBoard, parent, parent.depth + 1));
            }
        }

        return childNodes;
    }

    static boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3;
    }

    static int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, 3);
        }
        return newBoard;
    }

    static void printSolutionPath(Node solution) {
        List<Node> path = new ArrayList<>();
        Node current = solution;
        while (current != null) {
            path.add(current);
            current = current.parent;
            numMove = path.size() - 1;
        }
        Collections.reverse(path);
        for (Node node : path) {
            printBoard(node.board);
            System.out.println();
        }
    }

    static void printBoard(int[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    static class Node {
        int[][] board;
        Node parent;
        int depth;

        Node(int[][] board, Node parent, int depth) {
            this.board = board;
            this.parent = parent;
            this.depth = depth;
        }
    }
}
