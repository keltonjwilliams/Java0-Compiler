package QuadGeneration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class QuadPDA {

    private ArrayList<String[]> quads = new ArrayList<>();

    String[] precRowCol;
    String[][] precidenceTable;

    String[][] tokens;

    public QuadPDA() {
        precRowCol = new String[20];

        precRowCol[0] = "="; precRowCol[1] = "+"; precRowCol[2] = "-"; precRowCol[3] = "("; precRowCol[4] = ")"; precRowCol[5] = "*"; precRowCol[6] = "/"; precRowCol[7] = "IF"; precRowCol[8] = "THEN"; precRowCol[9] = "ODD";  precRowCol[10] = "=="; precRowCol[11] = "!="; precRowCol[12] = ">"; precRowCol[13] = "<"; precRowCol[14] = ">="; precRowCol[15] = "<="; precRowCol[16] = "WHILE"; precRowCol[17] = "DO"; precRowCol[18] = "{";  precRowCol[19] = "}"; 
        
        precidenceTable = new String[20][20];

        // = (row 0)
        precidenceTable[0][1] = "<"; precidenceTable[0][2] = "<"; precidenceTable[0][3]= "<"; precidenceTable[0][5] = "<"; precidenceTable[0][6] = "<";
        // + (row 1)
        precidenceTable[1][1] = ">"; precidenceTable[1][2] = ">"; precidenceTable[1][3]= "<"; precidenceTable[1][4] = ">"; precidenceTable[1][5] = "<"; precidenceTable[1][6] = "<"; precidenceTable[1][8] = ">"; precidenceTable[1][10] = ">"; precidenceTable[1][11] = ">"; precidenceTable[1][12] = ">"; precidenceTable[1][13] = ">"; precidenceTable[1][14] = ">"; precidenceTable[1][15] = ">";
        // - (row 2)
        precidenceTable[2][1] = ">"; precidenceTable[2][2] = ">"; precidenceTable[2][3]= "<"; precidenceTable[2][4] = ">"; precidenceTable[2][5] = "<"; precidenceTable[2][6] = "<"; precidenceTable[2][8] = ">"; precidenceTable[2][10] = ">"; precidenceTable[2][11] = ">"; precidenceTable[2][12] = ">"; precidenceTable[2][13] = ">"; precidenceTable[2][14] = ">"; precidenceTable[2][15] = ">";
        // ( (row 3)
        precidenceTable[3][1] = "<"; precidenceTable[3][2] = "<"; precidenceTable[3][3]= "<"; precidenceTable[3][4] = "="; precidenceTable[3][5] = "<"; precidenceTable[3][6] = "<";
        // ) (row 4)
        precidenceTable[4][1] = ">"; precidenceTable[4][2] = ">"; precidenceTable[4][4] = ">"; precidenceTable[4][5] = ">"; precidenceTable[4][6] = ">";
        // * (row 5)
        precidenceTable[5][1] = ">"; precidenceTable[5][2] = ">"; precidenceTable[5][3] = "<"; precidenceTable[5][4] = ">"; precidenceTable[5][5] = ">"; precidenceTable[5][6] = ">"; precidenceTable[5][8] = ">"; precidenceTable[5][10] = ">"; precidenceTable[5][11] = ">"; precidenceTable[5][12] = ">"; precidenceTable[5][13] = ">"; precidenceTable[5][14] = ">"; precidenceTable[5][15] = ">";
        // / (row 6)
        precidenceTable[6][1] = ">"; precidenceTable[6][2] = ">"; precidenceTable[6][3] = "<"; precidenceTable[6][4] = ">"; precidenceTable[6][5] = ">"; precidenceTable[6][6] = ">"; precidenceTable[6][8] = ">"; precidenceTable[6][10] = ">"; precidenceTable[6][11] = ">"; precidenceTable[6][12] = ">"; precidenceTable[6][13] = ">"; precidenceTable[6][14] = ">"; precidenceTable[6][15] = ">";
        // IF (row 7)
        precidenceTable[7][1] = "<"; precidenceTable[7][2] = "<"; precidenceTable[7][3]= "<"; precidenceTable[7][5] = "<"; precidenceTable[7][6] = "<"; precidenceTable[7][8] = "="; precidenceTable[7][9] = "<"; precidenceTable[7][10] = "<"; precidenceTable[7][11] = "<"; precidenceTable[7][12] = "<"; precidenceTable[7][13] = "<"; precidenceTable[7][14] = "<"; precidenceTable[7][15] = "<";
        // THEN (row 8)
        precidenceTable[8][0] = "<"; precidenceTable[8][7] = "<"; precidenceTable[8][16] = "<"; precidenceTable[8][18] = "<";
        // ODD (row 9)
        precidenceTable[9][1] = "<"; precidenceTable[9][2] = "<"; precidenceTable[9][3]= "<"; precidenceTable[9][5] = "<"; precidenceTable[9][6] = "<"; precidenceTable[9][8] = "<";
        // == (row 10)
        precidenceTable[10][1] = "<"; precidenceTable[10][2] = "<"; precidenceTable[10][3]= "<"; precidenceTable[10][5] = "<"; precidenceTable[10][6] = "<"; precidenceTable[10][8] = ">";
        // != (row 11)
        precidenceTable[11][1] = "<"; precidenceTable[11][2] = "<"; precidenceTable[11][3]= "<"; precidenceTable[11][5] = "<"; precidenceTable[11][6] = "<"; precidenceTable[11][8] = ">";
        // > (row 12)
        precidenceTable[12][1] = "<"; precidenceTable[12][2] = "<"; precidenceTable[12][3]= "<"; precidenceTable[12][5] = "<"; precidenceTable[12][6] = "<"; precidenceTable[12][8] = ">";
        // < (row 13)
        precidenceTable[13][1] = "<"; precidenceTable[13][2] = "<"; precidenceTable[13][3]= "<"; precidenceTable[13][5] = "<"; precidenceTable[13][6] = "<"; precidenceTable[13][8] = ">";
        // >= (row 14)
        precidenceTable[14][1] = "<"; precidenceTable[14][2] = "<"; precidenceTable[14][3]= "<"; precidenceTable[14][5] = "<"; precidenceTable[14][6] = "<"; precidenceTable[14][8] = ">";
        // <= (row 15)
        precidenceTable[15][1] = "<"; precidenceTable[15][2] = "<"; precidenceTable[15][3]= "<"; precidenceTable[15][5] = "<"; precidenceTable[15][6] = "<"; precidenceTable[15][8] = ">";
        // WHILE (row 16)
        precidenceTable[16][1] = "<"; precidenceTable[16][2] = "<"; precidenceTable[16][3]= "<"; precidenceTable[16][5] = "<"; precidenceTable[16][6] = "<"; precidenceTable[16][9] = "<"; precidenceTable[16][10] = "<"; precidenceTable[16][11] = "<"; precidenceTable[16][12] = "<"; precidenceTable[16][13] = "<"; precidenceTable[16][14] = "<"; precidenceTable[16][16] = "="; 
        // DO (row 17)
        precidenceTable[16][0] = "<"; precidenceTable[16][7] = "<"; precidenceTable[16][16] = "<"; precidenceTable[16][18] = "<";
        // { (row 18)

        // } (row 19)
    }

    private final int OFFSET = 10;
    
    public void parseFile(File file) throws FileNotFoundException {
        // ArrayList<String> listT = new ArrayList<String>();
        Scanner scan = new Scanner(file);
        scan.nextLine();  // Throw away first line
        int numRows = 0;
        while (scan.hasNext()) {
            numRows++;
            scan.nextLine();
        }
        scan.close();
        scan = new Scanner(file);

        tokens = new String[numRows][2];
        int row = 0;

        scan.nextLine();  // Throw away first line
        while (scan.hasNext()) {
            String line = scan.nextLine();

            tokens[row][0] = line.substring(0, OFFSET).trim();
            tokens[row][1] = line.substring(OFFSET).trim();
            row++;
        }
        scan.close();
    }

    private int tempNo = 1;
    private int index = 0;

    public void generateQuads() {
        Stack<String> stack = new Stack<>();
        String lastSymbol = null;

        while (index < tokens.length) {
            if (isReservedWord(tokens[index][0])) {
                evaluateReservedWords(stack);
            }
            processToken(stack,lastSymbol);
            index++;
        }
        index = 0;
    }

    public void printQuads() {
        for (String quad[] : quads) {
            System.out.println(quad[0] + ", " + quad[1] + ", " + quad[2] + ", " + quad[3]);
        }
    }

    public void writeToFile(File f) throws IOException {
        FileWriter writer = new FileWriter(f);

        for (String[] quad : quads) {
            writer.write(quad[0] + ", " + quad[1] + ", " + quad[2] + ", " + quad[3]);
            writer.write("\n");
        }
        
        writer.close();
    }

    public String getQuads() {
        String r = "";
        for (String quad[] : quads) {
            r += quad[0] + ", " + quad[1] + ", " + quad[2] + ", " + quad[3] + "\n";
        }
        return r;
    }

    private void processToken(Stack<String> stack, String last) {
        if (tokens[index][0].equals(";")) {
            // End of line
            evaluateStack(stack);
            if (stack.size() == 1) {
                stack.pop();
            }
            last = null;
        } else if (tokens[index][0].equals("(") || tokens[index][0].equals("{")){
            stack.push(evaluateSubStack());
        } else if (!isTerminalSymbol(tokens[index][0])) {
            stack.push(tokens[index][0]);
        } else {
            // stack.push(token);
            if (last != null && takesPrecedence(last, tokens[index][0])) {
                String q2 = stack.pop();
                String q0 = stack.pop();
                String q1 = stack.pop();
                String q3 = "-";
                if (!q0.equals("=")) {
                    q3 = "T" + tempNo;
                    stack.push("T" + tempNo);
                    tempNo++;
                }
                quads.add(new String[] {
                    q0, q1, q2, q3
                });
                stack.push(tokens[index][0]);
            } else {
                stack.push(tokens[index][0]);
                last = tokens[index][0];
            }
        }

        // Sets last
        if (isTerminalSymbol(tokens[index][0])) {
            last = tokens[index][0];
        }
    }

    // Creates a sub-stack to evaluate statements inside perenthesis or brackets
    private String evaluateSubStack() {
        Stack<String> subStack = new Stack<String>();
        String lastSymbol = null;

        index++;
        while (!tokens[index][0].equals(")") && !tokens[index][0].equals("}")) {
            processToken(subStack, lastSymbol);

            index++;
        }

        String var = evaluateStack(subStack);
        if (var != null) {
            return var;
        }
        return null;//subStack.pop();
    }

    // Pops stack into quads, returning a single item or nothing
    private String evaluateStack(Stack<String> s) {
        while (!s.empty()) {
            if (s.size() == 1) {
                return s.pop();
            }

            if (s.peek().equals("{") || s.peek().equals("}")) {
                s.pop();
            }

            if (s.peek().equals("THEN")) {
                s.pop(); // THROW AWAY "THEN"
                s.pop(); // THROW AWAY "IF"
                quads.add(new String[] {"L", "L" + labelNo, "-", "-"});
                labelNo++;
            } else {
                String q2 = s.pop();
                String q0 = s.pop();
                String q1 = s.pop();
                String q3 = "-";
                if (!q0.equals("=")) {
                    q3 = "T" + tempNo;
                    s.push("T" + tempNo);
                    tempNo++;
                }
                quads.add(new String[] {
                    q0, q1, q2, q3
                });
            }
        }
        // Returns null if stack is empty
        return null;
    }

    private int labelNo = 1;

    private void evaluateReservedWords(Stack<String> s) {
        if (isOnSymTab(index)) {
            while (!tokens[index][0].equals(";")) {
                // Skip tokens
                index++;
            }
            return; // Go back to generateQuads
        }

        if (tokens[index][0].equals("PRINT") || tokens[index][0].equals("GET")) {
            quads.add(new String[] {tokens[index][0], tokens[index+1][0], "-", "-"});
            index += 2;
            return; // Go back to generateQuads
        }

        if (tokens[index][0].equals("IF") || tokens[index][0].equals("WHILE")) {
            String last = null;
            quads.add(new String[] {tokens[index][0], "-", "-", "-"});
            String relop = "";
            index++;
            while (!tokens[index][0].equals("THEN") && !tokens[index][0].equals("DO")) {
                processToken(s, last);
                if (isRelOp(index)) {
                    relop = tokens[index][0];
                }
                index++;
            }
            
            if (tokens[index][0].equals("{") || tokens[index][0].equals("IF") || tokens[index][0].equals("WHILE")) {
                index++;
            } else {
                String q2 = s.pop();
                String q0 = s.pop();
                String q1 = s.pop();
                quads.add(new String[] {q0, q1, q2, "-"});
                evaluateStack(s);
            }

            
            quads.add(new String[] {tokens[index][0], "L" + labelNo, getBooleanLabel(relop), "-"});

        }

    }

    // Returns true if left >= right, ese returns false
    private boolean takesPrecedence(String left, String right) {
        if (left.equals(null)) {
            return true;
        }

        int row = -1;
        int col = -1;

        for (int i = 0; i < precRowCol.length; i++) {
            if (precRowCol[i].equals(left)) {
                row = i;
            }
            if (precRowCol[i].equals(right)) {
                col = i;
            }
            if (row != -1 && col != -1) {
                break;
            }
        }

        if (precidenceTable[row][col].equals(">") || precidenceTable[row][col].equals("=")) {
            return true;
        }
        return false;
    }

    // Tests if a given token is terminal or non-terminal
    private boolean isTerminalSymbol(String token) {
        for (String terminal : precRowCol) {
            if (terminal.equals(token)) {
                return true;
            }
        }
        return false;
    }

    private final String[] reservedWords = {"CONST", "IF", "VAR", "THEN", "PROCEDURE", "WHILE", "CALL", "DO", "ODD", "CLASS", "PRINT", "GET"};

    private boolean isReservedWord(String token) {
        for (String word : reservedWords) {
            if (token.equals(word)) {
                return true;
            }
        }
        return false;
    }

    // Returns true if the line is already on symbol table
    private boolean isOnSymTab(int i) {
        String t = tokens[i][0];
        if (t.equals("CLASS") || t.equals("VAR") || t.equals("CONST")) {
            return true;
        }
        return false;
    }

    private boolean isRelOp(int i) {
        if (tokens[i][1].equals("<relop>")) {
            return true;
        }
        return false;
    }

    // Gets opposite of boolean label (returns LE for > and GT for <=)
    private String getBooleanLabel(String op) {
        switch (op) {
            case ">":
                return "LE";
            case ">=":
                return "L";
            case "<":
                return "GE";
            case "<=":
                return "G";
            case "==":
                return "NE";
            case "!=":
                return "E";
        }
        return null; // Returns null if op is not a boolean operator
    }
}