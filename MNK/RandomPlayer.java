package game;

import java.util.Random;

public class RandomPlayer implements Player {
    private final Random random = new Random();
    private int m, n;
    private String game;

    public RandomPlayer(int m, int n, String game) {
        this.m = m;
        this.n = n;
        this.game = game;
    }

    @Override
    public Move makeMove(Position position) {
        try {
            switch (game) {
                case "mnk":
                    Move move = new Move(
                            random.nextInt(m),
                            random.nextInt(n),
                            position.getTurn()
                    );
                    return position.isValid(move) ? move : new Move("Lose");
                case "hex":
                    int r = random.nextInt(2 * n - 1);
                    move = new Move(
                            r,
                            random.nextInt(r < n ? r + 1 : 2 * n - 1 - r),
                            position.getTurn()
                    );
                    return position.isValid(move) ? move : new Move("Lose");
                default:
                    throw new UnsupportedOperationException("This game is not supported");
            }
        } catch (Exception e) {
            return new Move("Lose");
        }
    }

    @Override
    public String makeDecision(Position position) {
        return random.nextInt(2) == 0 ? "accept" : "decline";
    }
}