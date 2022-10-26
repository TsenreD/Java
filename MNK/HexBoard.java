package game;

public class HexBoard extends BasicBoard implements Board {

    public HexBoard(int n, int k) {
        super(n, k);
    }

    @Override
    protected int checkCol(Move move) {
        return move.getRow() < n ? move.getRow() + 1 : 2 * n - 1 - move.getRow();
    }

    @Override
    protected int checkRow(Move move) {
        return 2 * n - 1;
    }

    @Override
    public void printState() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2 * n - 1; i++) {
            int cells = i < n ? i + 1 : 2 * n - 1 - i;
            for (int k = 0; k < n - cells + numLen(2 * n - 1) - numLen(i + 1); k++) {
                sb.append(" ");
            }
            sb.append(i + 1).append(" ");
            for (int j = 0; j < cells; j++) {
                sb.append(CELL_TO_STRING.get(field[i][j])).append(" ");
            }
            sb.append(i  < n ? i + 2 : 2 * n - i);
            sb.append(System.lineSeparator());
        }
        System.out.println(sb);
    }
}
