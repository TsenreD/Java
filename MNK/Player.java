package game;

public interface Player {
    Move makeMove(Position position);

    String makeDecision(Position position);
}
