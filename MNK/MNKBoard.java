package game;


public class MNKBoard extends BasicBoard  {

    public MNKBoard(int m, int n, int k) {
        super(m, n, k);
    }

    @Override
    protected int checkCol(Move move) {
        return n;
    }

    @Override
    protected int checkRow(Move move) {
        return m;
    }

    @Override
    public void printState() {
        final StringBuilder sb = new StringBuilder("  ");
        int[] pos = new int[n];
        for (int i = 0; i < n; i++) {
            sb.append(i + 1).append(" ");
            pos[i] = i > 0 ? pos[i - 1] + 1 + numLen(i + 1) : numLen(m + 1);
        }
        sb.append(System.lineSeparator());
        for (int r = 0; r < m; r++) {
            StringBuilder aligner = new StringBuilder().append(r + 1).append(" ");
            aligner.setLength(numLen(r + 1) + 1);
            for (int i = 0; i < n; i++) {
                while (aligner.length() < pos[i] - 1) {
                    aligner.append(" ");
                }
                aligner.append(CELL_TO_STRING.get(field[r][i])).append(" ");
            }
            aligner.append(System.lineSeparator());
            sb.append(aligner);
        }
        sb.setLength(sb.length() - System.lineSeparator().length());
        System.out.println(sb);;
    }
}
