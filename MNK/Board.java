package game;

public interface Board {

    Position getPosition();

    boolean isValid(Move move);

    GameResult makeMove(Move move);

    Cell getCell(int row, int column);

    Cell getTurn();

    void printState();
}
