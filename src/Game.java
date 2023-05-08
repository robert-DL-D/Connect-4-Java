import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

class Game {
    private static final int COLUMN = 7;
    private static final int ROW = 6;
    private final LinkedList<LinkedList<Slot>> rows = new LinkedList<>();
    private final LinkedList<Slot> row1 = new LinkedList<>();
    private final LinkedList<Slot> row2 = new LinkedList<>();
    private final LinkedList<Slot> row3 = new LinkedList<>();
    private final LinkedList<Slot> row4 = new LinkedList<>();
    private final LinkedList<Slot> row5 = new LinkedList<>();
    private final LinkedList<Slot> row6 = new LinkedList<>();
    private final Scanner myObj = new Scanner(System.in);
    private boolean player1Turn = true;
    private boolean gameOver;
    private Slot lastSlotted;

    Game() {
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
        rows.add(row5);
        rows.add(row6);

        for (LinkedList<Slot> row : rows) {
            for (int i = 0; i < COLUMN; i++) {
                row.add(new Slot(rows.indexOf(row) + 1, i + 1));
            }
        }

        test();
        printGame();
        turn();
    }

    private void turn() {
        do {
            System.out.println(player1Turn ? "Player 1 Select a column" : "Player 2 Select a column");
            String s = myObj.nextLine();

            while (Integer.parseInt(s) > 6 || Integer.parseInt(s) < 0) {
                System.out.println("Invalid value");
                s = myObj.nextLine();
            }

            if ("Exit".equalsIgnoreCase(s)) {
                gameOver = true;
            } else {
                dropPiece(s);
                checkConnect4();
                printGame();

                if (gameOver) {
                    System.out.println("Player " + lastSlotted.getPlayerPiece() + " won!\n");
                    gameOver = false;
                    player1Turn = lastSlotted.getPlayerPiece() == 1;

                    System.out.println("Enter 'Again' to play again, or 'Exit' to close the program");
                    s = myObj.nextLine();

                    while (!"again".equalsIgnoreCase(s) && !"exit".equalsIgnoreCase(s)) {
                        System.out.println("Invalid command");
                        System.out.println("Enter 'Again' to play again, or 'Exit' to close the program");
                        s = myObj.nextLine();
                    }

                    if (s.equalsIgnoreCase("again")) {
                        for (List<Slot> row : rows) {
                            for (int i = 0; i < COLUMN; i++) {
                                row.get(i).setPlayerPiece(0);
                            }
                        }
                        printGame();
                    } else if ("exit".equalsIgnoreCase(s)) {
                        System.exit(0);
                    }
                }
            }
        } while (!gameOver);
    }

    private void dropPiece(String s) {
        int column = Integer.parseInt(s);

        for (LinkedList<Slot> row : rows) {
            Slot slot = row.get(column);

            // Finds the first slot in the column that's unfilled
            if (slot.getPlayerPiece() == 0) {
                if (player1Turn) {
                    slot.setPlayerPiece(1);
                    player1Turn = false;
                } else {
                    slot.setPlayerPiece(2);
                    player1Turn = true;
                }

                lastSlotted = slot;
                break;
            }
        }
    }

    private void checkConnect4() {
        findHorizontalConnectStartingPiece();
        findVerticalStartingPiece();
        checkForwardDiagonalConnect4();
        checkBackwardDiagonalConnect4();
    }

    private void findHorizontalConnectStartingPiece() {
        List<Slot> row = rows.get(lastSlotted.getRow() - 1);
        int pieceColumn = lastSlotted.getColumn();

        if (pieceColumn <= 4) {
            for (int i = 0; i < pieceColumn; i++) {
                checkHorizontalConnect4(i, row);
            }
        } else {
            for (int i = 3; i >= pieceColumn - 4; i--) {
                checkHorizontalConnect4(i, row);
            }
        }
    }

    private void checkHorizontalConnect4(int j, List<Slot> row) {
        int connectCount = 0;
        int player = lastSlotted.getPlayerPiece();

        for (int i = j; i < j + 4; i++) {
            if (player == row.get(i).getPlayerPiece()) {
                connectCount++;
            } else {
                break;
            }

            if (connectCount == 4) {
                gameOver = true;
            }
        }
    }

    private void findVerticalStartingPiece() {
        int pieceColumn = lastSlotted.getColumn() - 1; // -1 for index reference
        int pieceRow = lastSlotted.getRow();

        if (pieceRow <= 4) {
            for (int i = 0; i < pieceRow; i++) {
                checkVerticalConnect4(i, pieceColumn);
            }
        } else {
            for (int i = 2; i >= pieceRow - 4; i--) {
                checkVerticalConnect4(i, pieceColumn);
            }
        }
    }

    private void checkVerticalConnect4(int j, int pieceColumn) {
        int connectCount = 0;
        int player = lastSlotted.getPlayerPiece();

        for (int i = j; i < j + 4; i++) {
            if (player == rows.get(i).get(pieceColumn).getPlayerPiece()) {
                connectCount++;
            } else {
                break;
            }
            if (connectCount == 4) {
                gameOver = true;
            }
        }
    }

    private void checkForwardDiagonalConnect4() {

        int connectCount;
        int player = lastSlotted.getPlayerPiece();

        // Loops for checking connect 4
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 4; k++) {
                connectCount = 0;
                for (int i = 0; i < 4; i++) {
                    if (player == rows.get(j + i).get(k + i).getPlayerPiece()) {
                        connectCount++;

                    } else {
                        break;
                    }
                    if (connectCount == 4) {
                        gameOver = true;
                    }
                }
            }
        }
    }

    private void checkBackwardDiagonalConnect4() {
        int connectCount;
        int player = lastSlotted.getPlayerPiece();

        // Loops for checking connect 4
        for (int j = 0; j < 3; j++) {
            for (int k = 3; k < 7; k++) {
                connectCount = 0;
                for (int i = 0; i < 4; i++) {
                    if (player == rows.get(j + i).get(k - i).getPlayerPiece()) {
                        connectCount++;
                    } else {
                        break;
                    }
                    if (connectCount == 4) {
                        gameOver = true;
                    }
                }
            }
        }
    }

    private void printGame() {
        for (int i = rows.size() - 1; i >= 0; i--) {
            System.out.print(i + 1 + ": ");
            for (Slot slot : rows.get(i)) {
                System.out.print(slot.getPlayerPiece() + " ");
            }
            System.out.println();
        }

        System.out.println("----------------");
        System.out.println("C: 0 1 2 3 4 5 6");
    }

    private void test() {
        ThreadLocalRandom tLR = ThreadLocalRandom.current();

        // auto horizontal placement
        for (int i = 0; i < 3; i++) {
            row1.get(i).setPlayerPiece(1);
        }

        /*// auto vertical placement
        for (int i = 0; i < row1.size(); i++) {
            if(i % 2 == 1){
                row1.get(i).setPlayerPiece(1);
            } else {
                row1.get(i).setPlayerPiece(2);
            }
        }
        for (int i = 0; i < row2.size(); i++) {
            if(i % 2 == 1){
                row2.get(i).setPlayerPiece(1);
            } else {
                row2.get(i).setPlayerPiece(2);
            }
        }
        for (int i = 0; i < row1.size(); i++) {
            if(i % 2 == 0){
                row3.get(i).setPlayerPiece(1);
            } else {
                row3.get(i).setPlayerPiece(2);
            }
        }
        for (int i = 0; i < row2.size(); i++) {
            if(i % 2 == 0){
                row4.get(i).setPlayerPiece(1);
            } else {
                row4.get(i).setPlayerPiece(2);
            }
        } for (int i = 0; i < row2.size(); i++) {
            if(i % 2 == 0){
                row5.get(i).setPlayerPiece(1);
            } else {
                row5.get(i).setPlayerPiece(2);
            }
        }*/
        /*int column = tLR.nextInt(0, 7);
        LinkedList<Slot> row;
        for (int i = 0; i < 3; i++) {
            do {
                row = rows.get(tLR.nextInt(0, 6));
            } while (row.get(column).getPlayerPiece() == 1);
            row.get(column).setPlayerPiece(1);
        }*/

        // auto diagonal placement
        /*for (int i = 1; i < row1.size(); i++) {
            if (i % 2 == 0) {
                row1.get(i).setPlayerPiece(1);
            } else {
                row1.get(i).setPlayerPiece(2);
            }
        }
        for (int i = 1; i < row2.size(); i++) {
            if (i % 2 == 1) {
                row2.get(i).setPlayerPiece(1);
            } else {
                row2.get(i).setPlayerPiece(2);
            }
        }
        for (int i = 2; i < row1.size(); i++) {
            if (i % 2 == 0) {
                row3.get(i).setPlayerPiece(1);
            } else {
                row3.get(i).setPlayerPiece(2);
            }
        }*/
        /*for (int i = 3; i < 5; i++) {
            if (i % 2 == 1) {
                row4.get(i).setPlayerPiece(1);
            } else {
                row4.get(i).setPlayerPiece(2);
            }
        }*/
        /*for (int i = 0; i < row2.size(); i++) {
            if (i % 2 == 0) {
                row5.get(i).setPlayerPiece(1);
            } else {
                row5.get(i).setPlayerPiece(2);
            }
        }*/

    }

}
