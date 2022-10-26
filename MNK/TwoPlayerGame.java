package game;

public class TwoPlayerGame {
    private final Board board;
    private final Player[] players;


    public TwoPlayerGame(Board board, Player player1, Player player2) {
        this.board = board;
        this.players = new Player[]{player1, player2};
    }

    public int play(boolean log) {
        while (true) {
            final int result1 = makeMove(players[0], 1, log);
            if (result1 != -1) {
                return result1;
            }
            final int result2 = makeMove(players[1], 2, log);
            if (result2 != -1) {
                return result2;
            }
        }
    }

    private int makeMove(Player player, int no, boolean log) {
        GameResult result;
        try {
            Move move = player.makeMove(board.getPosition());
            switch (move.getOffer()) {
                case "concede" -> result = GameResult.LOOSE;
                case "draw" -> {
                    System.out.println("The other player is offering a draw. Will you accept? (Type accept or decline)");
                    switch (players[2 - no].makeDecision(board.getPosition())) {
                        case "accept" -> result = GameResult.DRAW;
                        case "decline" -> {
                            System.out.println("The other player has declined a draw");
                            return makeMove(player, no, log);
                        }
                        default -> throw new UnsupportedOperationException("Unacceptable answer got through");
                    }
                }
                default -> result = board.makeMove(move);
            }
            if (log) {
                System.out.println();
                System.out.println("Player: " + no);
                move.printState();
                board.printState();
                System.out.println("Result: " + result);
            }
        } catch (Exception e) {
            System.out.println(" Exception occurred, you have lost");
            result = GameResult.LOOSE;
        }

        return switch (result) {
            case WIN -> no;
            case LOOSE -> 3 - no;
            case DRAW -> 0;
            case UNKNOWN -> -1;
        };
    }
}
