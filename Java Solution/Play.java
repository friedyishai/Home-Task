import java.util.Scanner;

public class Play {
    private final static Scanner scan = new Scanner(System.in);
    private final static int INVALID = -1;

    public static void main(String[]args) {
        play();
        System.exit(0);
    }

    /*game play according to game logic.*/
    private static void play() {
        XOBoard board = new XOBoard(); //create default board object.
        int size, minSize, minLengthToWin,lengthToWin, turn;
        boolean playing = true, win;

        while (playing) {
            size = lengthToWin = turn = 0;
            minSize = board.getMinSize();
            minLengthToWin = board.getMinLengthToWin();
            win = false;

            System.out.println("\nWelcome!");

            while (size < minSize) {
                size = getSizeFromUser(minSize);
            }

            while (lengthToWin < minSize) {
                lengthToWin = getWinLengthFromUser(size, minLengthToWin);
            }

            board = new XOBoard(size); //create the real board with valid size

            while (!GameLogic.boardIsFull(board) && !win) {
                int player = (turn % 2 == 0) ? 1 : 2;
                boolean outOfBounds = true;

                while (outOfBounds) {
                    System.out.println("\nPlayer " + player + " turn.");
                    board.display();

                    int[] position = {INVALID, INVALID};
                    getPositionFromUser(board, position);

                    if (position[0] != INVALID) {
                        char toPut = (player == 1) ? 'X' : 'O';
                        boolean validPut = board.put(toPut, position[0], position[1]);

                        if (validPut) {
                            outOfBounds = false;
                        }
                    }
                }
                String s = board.status(lengthToWin);
                win = checkStatus(s);
                turn++;
            }

            board.display();

            boolean wrongChoice = true;

            while (wrongChoice) {
                 boolean[] next = {true, true};
                 nextStep(next);
                 playing = next[0];
                 wrongChoice = next[1];
            }
        }
    }

    /*Get size of board from the user*/
    private static int getSizeFromUser(int minSize) {
        int size;

        System.out.println("\nTo start new game enter board size:");
        size = scan.nextInt();

        if (size >= minSize) {
            return size;
        } else {
            System.out.println("\nThe minimum size is " + minSize + ", try again.");
            return INVALID;
        }
    }

    /*Get length of win sequence from the user*/
    private static int getWinLengthFromUser(int size, int minLengthToWin) {
        int lengthToWin;

        System.out.println("\nEnter the sequence length required to win:");
        lengthToWin = scan.nextInt();

        if (lengthToWin <= size && lengthToWin >= minLengthToWin) {
            return lengthToWin;
        } else if(lengthToWin > size) {
            System.out.println("\nThe maximum length is " + size + ", try again.");
        } else {
            System.out.println("\nThe minimum length is " + minLengthToWin + ", try again.");
        }
        return INVALID;
    }

    /*Get user's choice for next step (exit / new game)*/
    private static void nextStep(boolean[] next) {
        System.out.println("\nStart a new game? (y/n):");
        String choice = scan.next();

        if (choice.equals("n")) {
            next[0] = false;
            System.out.println("\nGoodbye!");
            next[1] = false;
        } else if(choice.equals("y")) {
            next[1] = false;
        } else {
            System.out.println("\nWrong choice, try again.");
        }
    }

    /*get position from current player*/
    private static void getPositionFromUser(XOBoard board, int[] position) {
        System.out.println("\nEnter row number (from 1 to " + board.getSize() + ")");
        int row = scan.nextInt();

        if(!board.checkBounds(row)) {
            return;
        }

        System.out.println("\nEnter column number (from 1 to " + board.getSize() + ")");
        int col = scan.nextInt();

        if(!board.checkBounds(col)) {
            return;
        }

        position[0] = row - 1;
        position[1] = col - 1;
    }

    /*checks current status*/
    private static boolean checkStatus(String s) {
        String winner;

        switch (s) {
            case "X" -> {
                winner = "Player 1";
                System.out.println("\nThe winner is " + winner + "!");
                return true;
            }
            case "O" -> {
                winner = "Player 2";
                System.out.println("\nThe winner is " + winner + "!");
                return true;
            }
            case "Draw" -> System.out.println("\nThe game ended in a Draw.");
        }

        return false;
    }
}
