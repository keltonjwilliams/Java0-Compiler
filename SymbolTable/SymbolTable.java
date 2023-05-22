package SymbolTable;

import java.util.ArrayList;
import java.util.Scanner;

public class SymbolTable {

    private FSAItem[][] FSA;
    private String[] FSACols;

    private ArrayList<String[]> symbolTable = new ArrayList<>();

    public SymbolTable() {
        FSA = new FSAItem[12][12];

        FSA[0][0] = new FSAItem(1);
        FSA[1][1] = new FSAItem(2);
        FSA[2][2] = new FSAItem(3);
        FSA[3][1] = new FSAItem(10); FSA[3][3] = new FSAItem(4); FSA[3][8] = new FSAItem(8); FSA[3][9] = new FSAItem(10);
        FSA[4][1] = new FSAItem(5);
        FSA[5][4] = new FSAItem(6);
        FSA[6][5] = new FSAItem(7);
        FSA[7][6] = new FSAItem(3); FSA[7][7] = new FSAItem(4);
        FSA[8][1] = new FSAItem(9);
        FSA[9][6] = new FSAItem(3); FSA[9][7] = new FSAItem(8);
        FSA[10][1] = new FSAItem(10); FSA[10][4] = new FSAItem(10); FSA[10][5] = new FSAItem(11); FSA[10][8] = new FSAItem(10); FSA[10][9] = new FSAItem(10); FSA[10][11] = new FSAItem(12);
        FSA[11][1] = new FSAItem(10); FSA[11][4] = new FSAItem(10); FSA[11][6] = new FSAItem(10); FSA[11][9] = new FSAItem(10); FSA[11][10] = new FSAItem(10); FSA[11][11] = new FSAItem(12);

        FSACols = new String[FSA[0].length];
        FSACols[0] = "CLASS";
        FSACols[1] = "<var>";
        FSACols[2] = "{";
        FSACols[3] = "CONST";
        FSACols[4] = "=";
        FSACols[5] = "<int>";
        FSACols[6] = ";";
        FSACols[7] = ",";
        FSACols[8] = "VAR";
        FSACols[9] = "reserved";
        FSACols[10] = "any";
        FSACols[11] = "EOF";
    }

    public void parseSymbols(Scanner scan) {

        scan.nextLine(); // Throw away first line

        int currentState = 0;
        String varType = "";
        // String previousClass = "";
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String token = line.substring(0, 10).trim();
            String classification = line.substring(10);

            // System.out.println(token + " " + currentState);
            // System.out.println(currentState);
            for (int i = 0; i < FSACols.length; i++) {
                if (token.equals(FSACols[i]) || classification.equals(FSACols[i])) {
                    if (FSA[currentState][i] != null) {
                        currentState = FSA[currentState][i].getNext();

                        if (classification.charAt(0) == '$') {
                            varType = classification;
                        }

                        boolean isOnSymTab = false;
                        for (String[] row : symbolTable) {
                            if (row[0].equals(token)) {
                                isOnSymTab = true;
                                break;
                            }
                        }

                        if ((classification.equals("<var>") || classification.equals("<int>")) && !isOnSymTab) {
                            // System.out.println("Inserting " + token);
                            insert(token, classification, varType);
                        }
                    }
                    // ANY TOKEN VALID AT STATE 10
                    if (FSA[currentState][10] != null) {
                        // System.out.println("TEST YEAH");
                        currentState = FSA[currentState][10].getNext();
                        break;
                    }
                    break;
                }
                
            }
        }

        // EOF
        currentState = FSA[currentState][11].getNext();

        scan.close();
    }

    private final int OFFSET = 10;

    public void printSymbolTable() {
        System.out.println("Tokens    Class     Value     Address   Segment");
        for (String[] line : symbolTable) {
            System.out.print(line[0] + " ".repeat(OFFSET-line[0].length()));
            System.out.print(line[1] + " ".repeat(OFFSET-line[1].length()));
            System.out.print(line[2] + " ".repeat(OFFSET-line[2].length()));
            System.out.print(line[3] + " ".repeat(OFFSET-line[3].length()));
            System.out.println(line[4]);
        }
    }

    public String getSymbolTable() {
        String symtab = "";
        for (String[] row : symbolTable) {
            symtab += row[0] + " ".repeat(OFFSET-row[0].length());
            symtab += row[1] + " ".repeat(OFFSET-row[1].length());
            symtab += row[2] + " ".repeat(OFFSET-row[2].length());
            symtab += row[3] + " ".repeat(OFFSET-row[3].length());
            symtab += row[4] + "\n";
        }
        return symtab;
    }

    // private final String[] reservedWords = {"CONST", "IF", "VAR", "THEN", "PROCEDURE", "WHILE", "CALL", "DO", "ODD", "CLASS"};
    // private final String[] singleCharDelimiters = {"=", ",", ";", "+", "-", "*", "/", "(", ")", "<", ">", "{", "}"};
    // private final String[] doubleCharDelimiters = {"==", ">=", "<=", "!=", "/*", "*/"};

    private int nextAddress = 0;

    private void insert(String token, String clss, String type) {
        if (clss.equals("<int>")) {
            if (symbolTable.size() > 1 && (symbolTable.get(symbolTable.size()-1)[1].equals("<const>"))) {
                    String[] last = symbolTable.remove(symbolTable.size()-1);
                    last[2] = token;
                    symbolTable.add(last);
            } else {
                symbolTable.add(new String[] {token, "$NumLit", token, String.valueOf(nextAddress), "DS"});
                nextAddress += 2;
            }
        } else if (type.equals("$CLASS")) {
            symbolTable.add(new String[] {token, "<PgmName>", "", String.valueOf(nextAddress), "CS"});
        } 
        else if (type.equals("$CONST")) {
            symbolTable.add(new String[] {token, "<const>", "", String.valueOf(nextAddress), "DS"});
            nextAddress += 2;
        } else {
            symbolTable.add(new String[] {token, clss, "", String.valueOf(nextAddress), "DS"});
            nextAddress += 2;
        }
    }
}
