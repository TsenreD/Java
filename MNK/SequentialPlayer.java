package game;

public class SequentialPlayer implements Player {
    private int m, n;
    private String game;

    public SequentialPlayer(int m, int n, String game) {
        this.m = m;
        this.n = n;
        this.game = game;
    }

    @Override
    public Move makeMove(Position position) {
        try {
            switch (game) {
                case "mnk":
                    for (int r = 0; r <= m; r++) {
                        for (int c = 0; c <= n; c++) {
                            final Move move = new Move(r, c, position.getTurn());
                            if (position.isValid(move)) {
                                return move;
                            }
                        }
                    }

                case "hex":
                    for (int i = 0; i < 2 * m - 1; i++) {
                        for (int j = 0; j < (i < m ? i + 1 : 2 * m - 1 - i); j++) {
                            final Move move = new Move(i, j, position.getTurn());
                            if (position.isValid(move)) {
                                return move;
                            }
                        }
                    }
                default:
                    return new Move("Lose");
            }

        } catch (Exception e) {
            return new Move("Lose");
        }
    }

    @Override
    public String makeDecision(Position position) {
        return "decline";
    }
}
