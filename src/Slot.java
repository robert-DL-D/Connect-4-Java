class Slot {

    private final int row;
    private final int column;
    private int playerPiece;

    Slot(int row, int column) {
        this.row = row;
        this.column = column;
    }

    int getPlayerPiece() {
        return playerPiece;
    }

    void setPlayerPiece(int playerPiece) {
        this.playerPiece = playerPiece;
    }

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }
}
