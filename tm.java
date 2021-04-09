// Vincent Tran, 3/28/21, COT 4210, Program 3

import java.util.*;

public class tm {
    public static void main(String[] args) {

        int NumberOfTuringMachines;
        int NumberOfInputStrings;
        int MaximumNumberOfSteps;
        int NumberOfStates;
        int NumberOfRules;
        int i;
        int j;
        int k;
        Scanner scnr = new Scanner(System.in);

        NumberOfTuringMachines = scnr.nextInt();
        tm[] TuringMachine = new tm[NumberOfTuringMachines];
        String[][] StringToTest = new String[NumberOfTuringMachines][];

        for (i = 0; i < NumberOfTuringMachines; i++) {
            NumberOfStates = scnr.nextInt();
            NumberOfRules = scnr.nextInt();

            StoreRule[] MachineRule = new StoreRule[NumberOfRules];

            // Storing the rules per Turing Machine
            for (j = 0; j < NumberOfRules; j++)
                MachineRule[j] = new StoreRule(scnr);

            NumberOfInputStrings = scnr.nextInt();
            MaximumNumberOfSteps = scnr.nextInt();

            // Creating seperate Turing machines with respective strings.
            TuringMachine[i] = new tm();
            StringToTest[i] = new String[NumberOfInputStrings];

            System.out.println("\nMachine #" + (i + 1) + ":");
            for (k = 0; k < NumberOfInputStrings; k++) {

                StringToTest[i][k] = scnr.next();

                // Determines string in accept, reject, or halt state.
                int result = TuringMachine[i].AcceptanceCheck(StringToTest[i][k], MaximumNumberOfSteps, MachineRule);

                if (result == 1) {
                    System.out.println(StringToTest[i][k] + ": YES");
                } else if (result == 0) {
                    System.out.println(StringToTest[i][k] + ": NO");
                } else {
                    System.out.println(StringToTest[i][k] + ": DOES NOT HALT IN "
                            + TuringMachine[i].AcceptanceCheck(StringToTest[i][k], MaximumNumberOfSteps, MachineRule)
                            + " STEPS");
                }
            }
        }
        scnr.close();
    }

    // Stores the Rules into a list
    private StoreRule[] ListOfRules(int state, StoreRule[] MachineRule) {
        ArrayList<StoreRule> ListOfRules = new ArrayList<StoreRule>();

        // For every rule given, if the InputState equals initial state, add rule.
        for (StoreRule rule : MachineRule) {
            if (rule.getInputState() == state) {
                ListOfRules.add(rule);
            }
        }
        return ListOfRules.toArray(new StoreRule[0]);
    }

    // Adds each input string into the TM tape.
    private ArrayList<Character> Tape(String InputString) {
        ArrayList<Character> tape = new ArrayList<Character>();
        for (Character c : InputString.toCharArray()) {
            tape.add(c);
        }
        return tape;
    }

    // Returns whether string is in accept, reject, or does not halt state.
    private int AcceptanceCheck(String InputString, int MaximumNumberOfSteps, StoreRule[] machineRule) {
        ArrayList<Character> tape = Tape(InputString);
        int currentState = 0;
        int head = 0;
        int steps = 0;

        // Determines whether Turing Machine will halt
        while (steps < MaximumNumberOfSteps) {

            // Determines rules
            StoreRule[] currentRules = ListOfRules(currentState, machineRule);
            for (StoreRule rule : currentRules) {

                // Check for blank part of tape
                if (head >= tape.size()) {
                    tape.add('B');
                }
                if (tape.get(head) == rule.getCharToRead()) {

                    // Move to next state
                    currentState = rule.getOutputState();
                    tape.remove(head);
                    tape.add(head, rule.getCharToWrite());
                    head = head + rule.getDirection();

                    // Check if tape goes to the left
                    if (head < 0) {
                        head = 0;
                    }
                    steps++;

                    // Break out of loop if steps reaches max steps
                    break;
                }
            }

            // Determines Accept or Reject state
            if (currentState == 1) {
                return 1;
            } else if (currentState == 2) {
                return 0;
            }
        }
        return MaximumNumberOfSteps;

    }

    private static class StoreRule {

        private int InputState;
        private char CharacterToRead;
        private int OutputState;
        private char CharacterToWrite;
        private int LeftOrRight;

        StoreRule(Scanner scnr) {
            InputState = scnr.nextInt();
            CharacterToRead = scnr.next().charAt(0);
            OutputState = scnr.nextInt();
            CharacterToWrite = scnr.next().charAt(0);
            char tapeHeadDirection = scnr.next().charAt(0);

            if (tapeHeadDirection == 'L') {
                LeftOrRight = -1;
            } else {
                LeftOrRight = 1;
            }
        }

        int getInputState() {
            return InputState;
        }

        char getCharToRead() {
            return CharacterToRead;
        }

        int getOutputState() {
            return OutputState;
        }

        char getCharToWrite() {
            return CharacterToWrite;
        }

        int getDirection() {
            return LeftOrRight;
        }
    }
}