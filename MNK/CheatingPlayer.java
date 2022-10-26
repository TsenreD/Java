//package game;
//
//public class CheatingPlayer implements Player {
//    @Override
//    public Move makeMove(Position position) {
//        final  Board board = (MNKBoard) position;
//        Move first = null;
//        for (int r = 0; r < 3; r++) {
//            for (int c = 0; c < 3; c++) {
//                final Move move = new Move(r, c, Cell.O);
//                if (position.isValid(move)) {
//                    if (first == null) {
//                        first = move;
//                    } else {
//                        board.makeMove(move);
//                    }
//                }
//            }
//        }
//        return first;
//    }
//
//    @Override
//    public String makeDecision(Position position) {
//        return null;
//    }
//}
