package game;

public class Move {
    private final int row;
    private final int col;
    private final Cell value;
    private final String offer;

    public Move(int row, int col, Cell value) {
        this.row = row;
        this.col = col;
        this.value = value;
        this.offer = "";
    }

    public Move(String offer) {
        this.offer = offer;
        row = -1;
        col = -1;
        value = Cell.E;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Cell getValue() {
        return value;
    }

    public String getOffer() {
        return offer;
    }

    public void printState() {
        System.out.println(offer.isEmpty() ? String.format("Move(%s, %d, %d)", value, row + 1, col + 1) : offer);
    }

}
