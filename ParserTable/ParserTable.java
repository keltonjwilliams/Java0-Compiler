package ParserTable;

import java.util.ArrayList;
import java.util.Scanner;

public class ParserTable {
    
    private TableItem[][] stateTable;

    private String[] stateTableCols;

    private int letterIndex;
    private int numberIndex;

    ArrayList<String[]> tokens = new ArrayList<String[]>();

    private final String[] reservedWords = {"CONST", "IF", "VAR", "THEN", "PROCEDURE", "WHILE", "CALL", "DO", "ODD", "CLASS", "GET", "PRINT"};

    public ParserTable() {
        stateTable = new TableItem[28][16];
        stateTableCols = new String[stateTable[0].length];

        stateTableCols[0] = "number"; stateTableCols[1] = "letter"; stateTableCols[2] = "*"; stateTableCols[3] = "/"; stateTableCols[4] = "="; stateTableCols[5] = "<"; stateTableCols[6] = ">"; stateTableCols[7] = " "; stateTableCols[8] = "+"; stateTableCols[9] = "-"; stateTableCols[10] = "{"; stateTableCols[11] = "}"; stateTableCols[12] = ","; stateTableCols[13] = ";"; stateTableCols[14] = "("; stateTableCols[15] = ")";

        stateTable[0][0] = new TableItem(3); stateTable[0][1] = new TableItem(5); stateTable[0][2] = new TableItem(2); stateTable[0][3] = new TableItem(7); stateTable[0][4] =  new TableItem(11); stateTable[0][5] = new TableItem(14); stateTable[0][6] = new TableItem(17); stateTable[0][7] = new TableItem(0); stateTable[0][8] = new TableItem(20); stateTable[0][9] = new TableItem(21); stateTable[0][10] = new TableItem(22); stateTable[0][11] = new TableItem(23); stateTable[0][12] = new TableItem(24); stateTable[0][13] = new TableItem(25); stateTable[0][14] = new TableItem(26); stateTable[0][15] = new TableItem(27);
        stateTable[1][0] = new TableItem("ERROR");
        stateTable[2][0] = new TableItem("*", "<mop>");
        stateTable[3][0] = new TableItem(3); stateTable[3][1] = new TableItem(3); stateTable[3][7] = new TableItem(4); stateTable[3][12] = new TableItem(4); stateTable[3][13] = new TableItem(4);
        stateTable[4][0] = new TableItem("<int>");
        stateTable[5][0] = new TableItem(5); stateTable[5][1] = new TableItem(5); stateTable[5][7] = new TableItem(6); stateTable[5][12] = new TableItem(6); stateTable[5][13] = new TableItem(6);
        stateTable[6][0] = new TableItem("<var>");
        stateTable[7][2] = new TableItem(8); stateTable[7][7] = new TableItem(10);
        stateTable[8][0] = new TableItem(8); stateTable[8][1] = new TableItem(8); stateTable[8][2] = new TableItem(9); stateTable[8][3] = new TableItem(8); stateTable[8][4] = new TableItem(8); stateTable[8][5] = new TableItem(8); stateTable[8][6] = new TableItem(8); stateTable[8][7] = new TableItem(8); stateTable[8][8] = new TableItem(8); stateTable[8][9] = new TableItem(8); stateTable[8][10] = new TableItem(8); stateTable[8][11] = new TableItem(8); stateTable[8][12] = new TableItem(8); stateTable[8][13] = new TableItem(8);
        stateTable[9][0] = new TableItem(8); stateTable[9][1] = new TableItem(8); stateTable[9][2] = new TableItem(8); stateTable[9][3] = new TableItem(0); stateTable[9][4] = new TableItem(8); stateTable[9][5] = new TableItem(8); stateTable[9][6] = new TableItem(8); stateTable[9][7] = new TableItem(8); stateTable[9][8] = new TableItem(8); stateTable[9][9] = new TableItem(8); stateTable[9][10] = new TableItem(8); stateTable[9][11] = new TableItem(8); stateTable[9][12] = new TableItem(8); stateTable[9][13] = new TableItem(8);
        stateTable[10][0] = new TableItem("/", "<mop>");
        stateTable[11][4] = new TableItem(13); stateTable[11][7] = new TableItem(12);
        stateTable[12][0] = new TableItem("=", "<assign>");
        stateTable[13][0] = new TableItem("==", "<relop>");
        stateTable[14][4] = new TableItem(16); stateTable[14][7] = new TableItem(15);
        stateTable[15][0] = new TableItem("<", "<relop>");
        stateTable[16][0] = new TableItem("<=", "<relop>");
        stateTable[17][4] = new TableItem(19); stateTable[17][7] = new TableItem(18);
        stateTable[18][0] = new TableItem(">", "<relop>");
        stateTable[19][0] = new TableItem(">=", "<relop>");
        stateTable[20][0] = new TableItem("+", "<addop>");
        stateTable[21][0] = new TableItem("-", "<addop>");
        stateTable[22][0] = new TableItem("{", "<LB>");
        stateTable[23][0] = new TableItem("}", "<RB>");
        stateTable[24][0] = new TableItem(",", "<comma>");
        stateTable[25][0] = new TableItem(";", "<semi>");
        stateTable[26][0] = new TableItem("(", "<LP>");
        stateTable[27][0] = new TableItem(")", "<RP>");

        for (int i = 0; i < stateTableCols.length; i++) {
            if (stateTableCols[i].equals("letter")) {
                letterIndex = i;
            } else if (stateTableCols[i].equals("number")) {
                numberIndex = i;
            }
        }
    }

    public void parseFile(Scanner scan) {
        while (scan.hasNextLine()) {
            parseChars(scan.nextLine().toCharArray());
        }
    }

    public void parseChars(char[] chars) {
        String token = "";
        int currentState = 0;

        // Parses one character at a time
        for (char c : chars) {
            boolean isSymbol = false;
            for (int i = 0; i < stateTableCols.length; i++) {
                if (stateTableCols[i].equals(String.valueOf(c))) {
                    isSymbol = true;
                    currentState = stateTable[currentState][i].getNext();
                    break;
                }
            }
            
            if (!isSymbol) {
                try {
                    Integer.parseInt(String.valueOf(c));
                    currentState = stateTable[currentState][numberIndex].getNext();
                } catch (Exception e) {
                    currentState = stateTable[currentState][letterIndex].getNext();
                }
            }

            token += c;

            // Checks if currentState is final
            if (stateTable[currentState][0] != null && stateTable[currentState][0].isFinal()) {

                if (stateTable[currentState][0].hasSymbol()) {
                    tokens.add(stateTable[currentState][0].getItem());
                } else {
                    token = token.strip();

                    boolean addLast = false;

                    if (token.charAt(token.length()-1) == c && token.length() > 1) {
                        addLast = true;
                        token = token.substring(0, token.length()-1);
                    }

                    if (stateTable[currentState][0].getClassification().equals("<int>")) {
                        tokens.add(new String[] {token, "<int>"});
                    } else {
                        // Check if token is a reservedWord, else assume token is var
                        boolean reserved = false;
                        for (String word : reservedWords) {
                            if (word.equals(token)) {
                                tokens.add(new String[] {token, "$" + word});
                                reserved = true;
                                break;
                            }
                        }
                        if (!reserved) {
                            tokens.add(new String[] {token, "<var>"});
                        }
                        
                    }

                    if (addLast) {
                        for (int i = 0; i < stateTableCols.length; i++) {
                            if (stateTableCols[i].equals(String.valueOf(c))) {
                                currentState = stateTable[0][i].getNext();
                                break;
                            }
                        }
                        if (stateTable[currentState][0].isFinal()) {
                            tokens.add(stateTable[currentState][0].getItem());
                        }
                    }
                }


                currentState = 0;
                token = "";
            }
        }
    }

    private final int OFFSET = 10;

    public void printTokens() {
        System.out.println("Token" + " ".repeat(OFFSET-"Token".length()) + "Classification");
        for (String[] token : tokens) {
            System.out.println(token[0] + " ".repeat(OFFSET-token[0].length()) + token[1]);
        }
    }

    public String getTokens() {
        String r = "Token" + " ".repeat(OFFSET-"Token".length()) + "Classification\n";
        for (String[] token : tokens) {
            r += token[0] + " ".repeat(OFFSET-token[0].length()) + token[1] + "\n";
        }
        return r;
    }
}
