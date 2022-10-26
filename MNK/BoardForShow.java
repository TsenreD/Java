package game;

public class BoardForShow implements Position {
    private final Board board;

    public BoardForShow(Board board) {
        this.board = board;
    }

    @Override
    public Cell getTurn() {
        return board.getTurn();
    }

    @Override
    public boolean isValid(Move move) {
        return board.isValid(move);
    }

    @Override
    public Cell getCell(int row, int column) {
        return board.getCell(row, column);
    }

    @Override
    public void printState() {
        board.printState();
    }

    @Override
    public String toString() {
        return board.toString();
    }
}
