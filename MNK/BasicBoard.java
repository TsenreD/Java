package game;

import java.util.Arrays;
import java.util.Map;

public abstract class BasicBoard implements Board {
    protected static final Map<Cell, String> CELL_TO_STRING = Map.of(
            Cell.E, ".",
            Cell.X, "X",
            Cell.O, "0"
    );
    protected final Boards board;
    protected final Cell[][] field;
    protected Cell turn;
    protected int m, n, k;

    protected int moveCounter;

    public BasicBoard(int m, int n, int k) {
        field = new Cell[m][n];
        this.k = k;
        this.m = m;
        this.n = n;
        for (Cell[] row : field) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
        board = Boards.RECT;
    }

    public BasicBoard(int n, int k) {
        field = new Cell[2 * n - 1][];
        this.k = k;
        this.n = n;
        for (int i = 1; i <= field.length; i++) {
            Cell[] row = new Cell[i <= n ? i : 2 * n - i];
            Arrays.fill(row, Cell.E);
            field[i - 1] = row;
        }
        //Arrays.fill(row, Cell.E);
        turn = Cell.X;
        board = Boards.HEX;
    }

    public Cell getTurn() {
        return this.turn;
    }

    public Cell getCell(int row, int column) {
        return field[row - 1][column - 1];
    }

    @Override
    public Position getPosition() {
        return new BoardForShow(this);
    }

    @Override
    public GameResult makeMove(Move move) {
        moveCounter++;
        if (!isValid(move)) {
            return GameResult.LOOSE;
        }
        field[move.getRow()][move.getCol()] = move.getValue();
        if (checkWin(move.getRow(), move.getCol())) {
            return GameResult.WIN;
        }

        if (checkDraw()) {
            return GameResult.DRAW;
        }

        turn = turn == Cell.X ? Cell.O : Cell.X;
        return GameResult.UNKNOWN;
    }

    protected boolean checkWin(int row, int col) {
        int[][] checkPos;
        switch (board) {
            case RECT:
                int[] hor = new int[]{-1, 0, 1, 0};
                int[] vert = new int[]{0, 1, 0, -1};
                int[] mainDiag = new int[]{-1, -1, 1, 1};
                int[] sideDiag = new int[]{-1, 1, 1, -1};
                checkPos = new int[][]{hor, vert, mainDiag, sideDiag};
                break;
            case HEX:
                hor = new int[]{0, -1, 0, 1};
                if (row < n) {
                    mainDiag = new int[]{-1, -1, 1, 1};
                    sideDiag = new int[]{-1, 0, 1, 0};
                } else if (row == n) {
                    mainDiag = new int[]{-1, -1, 1, 0};
                    sideDiag = new int[]{-1, 0, 1, -1};
                } else {
                    mainDiag = new int[]{1, 0, -1, 0};
                    sideDiag = new int[]{-1, 1, 1, -1};
                }
                checkPos = new int[][]{hor, mainDiag, sideDiag};
                break;
            default:
                throw new UnsupportedOperationException("This board is not supported");
        }
        for (int[] type : checkPos) {
            int counter = 1 + lineChecker(row, col, type[0], type[1])
                    + lineChecker(row, col, type[2], type[3]);
            if (counter >= k) {
                return true;
            }
        }
        return false;
    }

    protected int lineChecker(int row, int col, int addRow, int addCol) {
        int counter = 0;
        while (0 <= col + addCol && col + addCol < checkCol(new Move(row + addRow, col + addCol, Cell.E))
                && 0 <= row + addRow && row + addRow < checkRow(new Move(row + addRow, col + addCol, Cell.E))
                && field[row][col] == field[row + addRow][col + addCol]) {
            counter++;
            row += addRow;
            col += addCol;
            if (counter == k) {
                break;
            }
        }
        return counter;
    }

    public boolean isValid(Move move) {
        return 0 <= move.getRow() && move.getRow() < checkRow(move)
                && 0 <= move.getCol() && move.getCol() < checkCol(move)
                && field[move.getRow()][move.getCol()] == Cell.E
                && turn == move.getValue();
    }

    public int numLen(int n) {
        int c = 0;
        while (n > 0) {
            n /= 10;
            c++;
        }
        return c;
    }


    protected boolean checkDraw() {
        return moveCounter == m * n;
    }

    protected abstract int checkCol(Move move);

    protected abstract int checkRow(Move move);
}
