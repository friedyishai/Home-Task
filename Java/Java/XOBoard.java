public class XOBoard {
    private char[][] board;
    private int size;
    private final static int MIN_SIZE = 3; //define minimum size of board.
    private final static int MIN_LENGTH_TO_WIN = 3; //define minimum sequence to win.
    private int countOccupiedCells;

    /*Constructor*/
    public XOBoard(int size) {
        this.size = size;
        board = new char[size][size];
        countOccupiedCells = 0;
        buildBoard();
    }

    /*Default Constructor */
    public XOBoard() {
        this(MIN_SIZE);
    }

    /*Initializes new board*/
    private void buildBoard() {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                board[i][j] = '-';// '-' character symbolizes an empty position in the board.
            }
        }
    }

    /*Add X or O to position [row, col] if that position is not full*/
    public boolean put(char toPut, int row, int col) {
        if(board[row][col] != '-') {// occupied position.
            System.out.println("\nThe position you selected is full, please try again.");
            return false;
        } else {
            board[row][col] = toPut;
            countOccupiedCells++;
            return true;
        }
    }

    /*Game bord print to console.*/
    public void display() {
        String boardToString = "";
        /* Arithmetic sequence: a(1) = 7, d = 2, a(n - 1) = a(n - 2)*d + a1 */
        int d = 2, a1 = 7;
        int border = (size - 2) * d + a1;

        /*top border*/
        for(int i = 0; i < border; i++) {
            boardToString += "#";
        }

        boardToString += "\n";

        for(int i = 0; i < size; i++) {
            boardToString += "# ";//left border
            for (int j = 0; j < size; j++) { //board cells
                boardToString += board[i][j] + " ";
            }
            boardToString += "#\n";//right border
        }

        /*bottom border*/
        for(int i = 0; i < border; i++) {
            boardToString += "#";
        }

        System.out.println(boardToString);
    }

    /*Current status of the game (X / O / None / Draw)*/
    public String status(int lengthToWin) {
        String result = GameLogic.checkRows(this, lengthToWin);

        if(!result.equals("None")) {// -> result = "X" or result = "O".
            return result;
        }

        result = GameLogic.checkCols(this, lengthToWin);

        if(!result.equals("None")) {
            return result;
        }

        result = GameLogic.checkDiagonals(this, lengthToWin);

        return result;
    }

    /*Get size of board*/
    public int getSize() {
        return size;
    }

    /*Get the minimum size at which a board can be created*/
    public int getMinSize() {
        return MIN_SIZE;
    }

    /*Get the minimum size at which a board can be created*/
    public int getMinLengthToWin() {
        return MIN_LENGTH_TO_WIN;
    }

    /*Get the content of the cell in position [row, col]: (- / x / o)*/
    public char getCellContent(int row, int col) {
        if(row > size || col > size || row < 0 || col < 0)
            return '\0';

        return board[row][col];
    }

    /*Get the number of full cells*/
    public int getCountOccupiedCells() {
        return countOccupiedCells;
    }

    /*Checks if this row/col is in the board bounds*/
    public boolean checkBounds(int toCheck) {
        if (toCheck > size || toCheck < 1) {
            System.out.println("\nOut of bounds, try again.");
            return false;
        }
        return true;
    }
}
