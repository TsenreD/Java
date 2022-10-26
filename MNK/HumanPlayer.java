package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class HumanPlayer implements Player {
    private final Scanner in;
    private boolean draw;

    public HumanPlayer(Scanner in) {
        this.in = in;
        this.draw = false;
    }

    @Override
    public Move makeMove(Position position) {
        System.out.println();
        System.out.println("Current position");
        position.printState();
        System.out.println("Enter you move for " + position.getTurn());
        System.out.println("If you want to give up, type concede, if you want to offer a draw, type draw");
        while (true) {
            String line = in.nextLine();
            Scanner lineScanner = new Scanner(line);
            IntList moveParameters = new IntList();
            while (lineScanner.hasNextInt()) {
                moveParameters.add(lineScanner.nextInt());
            }
            if (moveParameters.size() == 0 && lineScanner.hasNext()) {
                String offer = lineScanner.next().toLowerCase();
                if (offer.equals("draw") || offer.equals("concede") && !lineScanner.hasNext()) {
                    if (offer.equals("draw")) {
                        if (draw) {
                            System.out.println("You cannot offer a draw again, please make a valid move");
                            continue;
                        }
                        draw = true;
                    }
                    return new Move(offer);
                }
            }
            if (moveParameters.size() != 2) {
                System.out.println("Incorrect input, please enter 2 integers, or draw, or concede)");
            } else if (!position.isValid(
                    new Move(moveParameters.get(0) - 1, moveParameters.get(1) - 1, position.getTurn()))
            ) {
                System.out.println("The move you are trying to make is invalid, please try again");
            } else {
                draw = false;
                return new Move(moveParameters.get(0) - 1, moveParameters.get(1) - 1, position.getTurn());
            }
        }
    }

    @Override
    public String makeDecision(Position position) {
        while (true) {
            String line = in.nextLine();
            Scanner lineScanner = new Scanner(line);
            List<String> word = new ArrayList<>();
            while (lineScanner.hasNext()) {
                word.add(lineScanner.next().toLowerCase());
            }
            if (word.size() != 1 || !(word.get(0).equals("accept")
                    || word.get(0).equals("decline"))) {
                System.out.println("Please enter either accept or decline");
                continue;
            }
            return word.get(0);
        }
    }
}
