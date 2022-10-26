package game;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please choose the game: Type MNK or Hex");
        List<String> type = new ArrayList<>();
        boolean playing = true;
        final Map<String, Integer> gameParameters = Map.of("mnk", 3, "hex", 2);
        while (playing) {
            try {
                String line = sc.nextLine();
                Scanner lineSc = new Scanner(line);
                type = new ArrayList<>();
                while (lineSc.hasNext()) {
                    type.add(lineSc.next().toLowerCase());
                }
                lineSc.close();
                if (type.size() != 1 || !gameParameters.containsKey(type.get(0))) {
                    System.out.println("Incorrect input, please try again");
                    continue;
                }
                break;
            } catch (NoSuchElementException e) {
                System.out.println("Game over");
                playing = false;
            }
        }
        IntList parameters = new IntList();
        while (playing) {
            System.out.println("To choose the game parameters, enter " + gameParameters.get(type.get(0)) + " positive integers");
            try {
                String line = sc.nextLine();
                Scanner lineSc = new Scanner(line);
                parameters = new IntList();
                while (lineSc.hasNextInt()) {
                    parameters.add(lineSc.nextInt());
                }
                if (lineSc.hasNext() || parameters.size() != gameParameters.get(type.get(0)) || parameters.containsNonPositive()) {
                    System.out.println("Incorrect input, please try again");
                    lineSc.close();
                    continue;
                }
                lineSc.close();
                break;
            } catch (NoSuchElementException e) {
                System.out.println("Game over");
                playing = false;
            }
        }
        if (playing) {
            Board board;
            int m, n, k;
            switch (type.get(0)) {
                case "mnk" -> {
                    m = parameters.get(0);
                    n = parameters.get(1);
                    k = parameters.get(2);
                    board = new MNKBoard(m, n, k);
                }
                case "hex" -> {
                    m = parameters.get(0);
                    n = m;
                    k = parameters.get(1);
                    board = new HexBoard(n, k);
                }
                default -> throw new UnsupportedOperationException("This game is not supported");
            }
            final int result = new TwoPlayerGame(
                    board,
                    new HumanPlayer(sc),
                    new HumanPlayer(sc)
            ).play(true);
            switch (result) {
                case 1 -> System.out.println("First player won");
                case 2 -> System.out.println("Second player won");
                case 0 -> System.out.println("Draw");
                default -> throw new AssertionError("Unknown result " + result);
            }
            sc.close();
        }
    }
}
