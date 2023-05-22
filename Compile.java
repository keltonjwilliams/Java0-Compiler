import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import AssemblyTranslator.AssemblyTranslator;
import ParserTable.ParserTable;
import QuadGeneration.QuadPDA;
import SymbolTable.SymbolTable;

public class Compile {
    
    public static void main(String[] args) {
        new Compile();
    }

    public Compile() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the a file name (without the \".txt\"):");
        String inputFileName = input.nextLine();
        input.close();

        File inFile = new File("./InputFiles/" + inputFileName + ".txt");
        ParserTable pt = new ParserTable();
        try {
            Scanner scan = new Scanner(inFile);
           
            pt.parseFile(scan);
            scan.close();
            System.out.println("STEP 1 DONE");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("ERROR STEP 1");
        }

        try {
            FileWriter writer = new FileWriter("./TextFiles/tokens.txt");
            writer.write(pt.getTokens());
            writer.close();
            System.out.println("STEP 2 DONE");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR STEP 2");
        }

        SymbolTable st = new SymbolTable();
        File tokenFile = new File("./TextFiles/tokens.txt");
        try {
            Scanner scan = new Scanner(tokenFile);
            st.parseSymbols(scan);
            System.out.println("STEP 3 DONE");
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("ERROR STEP 3");
        }

        try {
            FileWriter writer = new FileWriter("./TextFiles/symtab.txt");
            writer.write(st.getSymbolTable());
            writer.close();
            System.out.println("STEP 4 DONE");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR STEP 4");
        }

        QuadPDA q = new QuadPDA();
        try {
            q.parseFile(tokenFile);
            q.generateQuads();
            System.out.println("STEP 5 DONE");
        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
            System.out.println("ERROR STEP 5");
        }

        try {
            // String path = "./TextFiles/" + inputFileName;
            // File qFile = new File(path);
            // q.writeToFile(tokenFile);
            q.writeToFile(new File("./TextFiles/quads.txt"));
            // System.out.println("PRINTING");
            // System.out.println(q.getQuads());
            q.printQuads();
            System.out.println("STEP 6 DONE");
        } catch (IOException e) {
            
            e.printStackTrace();
            System.out.println("ERROR STEP 6");
        }

        try {
            AssemblyTranslator at = new AssemblyTranslator();
            File file1 = new File("./TextFiles/symtab.txt");
            File file2 = new File("./TextFiles/quads.txt");
            Scanner s1 = new Scanner(file1);
            Scanner s2 = new Scanner(file2);
            at.getInFiles(s1, s2);
            at.writeASM();
            System.out.println("STEP 7 DONE");
            s1.close();
            s2.close();
        } catch (IOException e) {
            
            e.printStackTrace();
            System.out.println("ERROR STEP 7");
        }

    }
}
