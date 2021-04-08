public class GameLogic {

    /*General method for checking a valid line (row / column / diagonal)*/
    private static String checkLines(XOBoard gameBoard, int i, int endOfLine, int jIncrease, int jBound, int lengthToWin) {
        int size = gameBoard.getSize();

        for (int j = i; endOfLine - j >= jBound; j += jIncrease) {
            char current = gameBoard.getCellContent(j / size, j % size);

            if (current == '-') {
                continue;
            }

            int count = 1;
            char toCheck = gameBoard.getCellContent((j + jIncrease) / size, (j + jIncrease) % size);

            while (j < endOfLine && toCheck == current) {
                j += jIncrease;
                count++;
                if (count == lengthToWin) {
                    return "" + current;
                }
                toCheck = gameBoard.getCellContent((j + jIncrease) / size, (j + jIncrease) % size);
            }
        }

        return "None";
    }

    /*Checks if there is a row filled with the same character (X / O).*/
    public static String checkRows(XOBoard gameBoard, int lengthToWin) {
        int size = gameBoard.getSize();

        for (int i = 0; i < size * size; i += size) {
            int endOfLine = i + size - 1;
            String result = checkLines(gameBoard, i, endOfLine, 1, lengthToWin - 1, lengthToWin);
            if (!result.equals("None")) {
                return result;
            }
        }

        return "None";
    }

    /*Checks if there is a column filled with the same character (X / O).*/
    public static String checkCols(XOBoard gameBoard, int lengthToWin) {
        int size = gameBoard.getSize();

        for (int i = 0; i < size; i++) {
            int endOfLine = i + (size - 1) * size;
            String result = checkLines(gameBoard, i, endOfLine, size, (lengthToWin - 1) * size, lengthToWin);
            if (!result.equals("None")) {
                return result;
            }
        }

        return "None";
    }

    /*Checks if there is a left to right or right to left diagonal filled with the same character (X / O)*/
    public static String checkDiagonals(XOBoard gameBoard, int lengthToWin) {
        String result = checkLeftDiagonals(gameBoard, lengthToWin);

        if (!result.equals("None")) {
            return result;
        }

        return checkRightDiagonals(gameBoard, lengthToWin);
    }

    /*Checks if 1 of the left to right diagonals filled with the same character (X / O).*/
    private static String checkLeftDiagonals(XOBoard gameBoard, int lengthToWin) {
        int size = gameBoard.getSize();

        /*Main diagonal and above*/
        for (int i = 0; size - i >= lengthToWin; i++) {
            int endOfLine = size * (size - i) - 1;
            String result = checkLines(gameBoard, i, endOfLine, size + 1, (lengthToWin - 1) * (size + 1), lengthToWin);
            if (!result.equals("None")) {
                return result;
            }
        }

        /*Below main diagonal*/
        for (int i = size, k = 2; size * size - i >= lengthToWin * size; i += size, k++) {
            int endOfLine = size * size - k;
            String result = checkLines(gameBoard, i, endOfLine, size + 1, (lengthToWin - 1) * (size + 1), lengthToWin);
            if (!result.equals("None")) {
                return result;
            }
        }

        return "None";
    }

    /*Checks if 1 of the right to left diagonals filled with the same character (X / O).*/
    private static String checkRightDiagonals(XOBoard gameBoard, int lengthToWin) {
        int size = gameBoard.getSize();

        /*Main diagonal and above*/
        for (int i = size - 1, k = size; size * size - i > size * (lengthToWin - 1); i += size, k--) {
            int endOfLine = size * size - k;
            String result = checkLines(gameBoard, i, endOfLine, size - 1, (lengthToWin - 1) * (size - 1), lengthToWin);
            if (!result.equals("None")) {
                return result;
            }
        }

        /*Below main diagonal*/
        for (int i = size - 2; i >= lengthToWin - 1; i--) {
            int endOfLine = size * i;
            String result = checkLines(gameBoard, i, endOfLine, size - 1, (lengthToWin - 1) * (size - 1), lengthToWin);
            if (!result.equals("None")) {
                return result;
            }
        }

        return boardIsFull(gameBoard) ? "Draw" : "None";
    }

    /*Checks if the board is full*/
    public static boolean boardIsFull(XOBoard gameBoard) {
        int size = gameBoard.getSize();
        return gameBoard.getCountOccupiedCells() == size * size;
    }
}