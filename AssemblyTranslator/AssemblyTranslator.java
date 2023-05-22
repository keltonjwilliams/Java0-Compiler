package AssemblyTranslator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class AssemblyTranslator {

    ArrayList<String[]> symtab = new ArrayList<>();
    ArrayList<String[]> quads = new ArrayList<>();    

    private final String beginning = 
    "\tsys_exit	equ	1\n" +
    "\tsys_read	equ	3\n" +
    "\tsys_write   equ	4\n" +
    "\tstdin		equ	0\n" +
    "\tstdout		equ	1\n" +
    "\tstdout		equ	1\n" +
    "\tstderr		equ	3\n\n";

    private String ds;
    private String cs;

    private final String EOF = 
    "\tmov eax,sys_exit\n" +
    "\txor ebx,ebx\n" +
    "\tint 80h\n\n";

    private final String intToString =
    "ConvertIntegerToString:\n" +
    "\tmov ebx, ResultValue + 4   ;Store the integer as a five\n" +
    "\t\t\t\t\t;digit char string at Result for printing\n\n" +
    "ConvertLoop:\n" +
    "\tsub dx,dx  ; repeatedly divide dx:ax by 10 to obtain last digit of number\n" +
    "\tmov cx,10  ; as the remainder in the DX register.  Quotient in AX.\n" +
    "\tdiv cx\n" +
    "\tadd dl,'0' ; Add '0' to dl to convert from binary to character.\n" +
    "\tmov [ebx], dl\n" +
    "\tdec ebx\n" +
    "\tcmp ebx,ResultValue\n" +
    "\tjge ConvertLoop\n\n" +
    "\tret\n\n";

    private final String getInt = 
    "GetAnInteger:\t;Get an integer as a string\n"+
    "\t;get response\n" +
    "\tmov eax,3\t;read\n" +
    "\tmov ebx,2	;device\n" +
    "\tmov ecx,num	;buffer address\n" +
    "\tmov edx,6	;max characters\n" +
    "\tint 0x80\n" +
    "\t;print number\n" +
    "\tmov edx,eax 	; eax contains the number of character read including <lf>\n" +
    "\tmov eax, 4\n" +
    "\tmov ebx, 1\n" +
    "\tmov ecx, num\n" + 
    "\tint 80\n\n" +
    "ConvertStringToInteger:\n" +
    "\tmov ax,0	;hold integer\n" +
    "\tmov [ReadInt],ax ;initialize 16 bit number to zero\n" +
    "\tmov ecx,num	;pt - 1st or next digit of number as a string \n" +
    "\tmov bx,0\n" +
    "\tmov bl, byte [ecx] ;contains first or next digit\n" +
    "Next:\tsub bl,'0'	;convert character to number\n" +
    "\tmov ax,[ReadInt]\n" +
    "\tmov dx,10\n" +
    "\tmul dx		;eax = eax * 10\n" +
    "\tadd ax,bx\n" +
    "\tmov [ReadInt], ax\n\n" +
    "\tmov bx,0\n" +
    "\tadd ecx,1 	;pt = pt + 1\n" +
    "\tmov bl, byte[ecx]\n" +
    "\tcmp bl,0xA	;is it a <lf>\n" +
    "\tjne Next	; get next digit \n" +
    "\tret\n\n";

    private final String printStr = 
    "PrintString:\n" +
    "\tpush\tax\n" +
    "\tpush\tdx\n" +
    "; subpgm:\n" +
    "\tmov\teax, 4\n" +
    "\tmov\tebx, 1\n" +
    "\tmov\tecx, userMsg\n" +
    "\tmov\tedx, lenUserMsg\n" +
    "\tint\t80h\n" +
    "\tpop\tdx\n" +
    "\tpop\tax\n" +
    "\tret\n\n";

    private final int OFFSET = 10;

    public void getInFiles(Scanner symScan, Scanner qScan) {
        symScan.nextLine(); // Throw away first line;
        while (symScan.hasNextLine()) {
            // numSTRows++;
            // symScan.nextLine();
            String line = symScan.nextLine();
            // System.out.println(line);
            symtab.add(new String[] {
                line.substring(0, OFFSET).trim(),
                line.substring(OFFSET, 2*OFFSET).trim(),
                line.substring(2*OFFSET, 3*OFFSET).trim(),
                line.substring(3*OFFSET, 4*OFFSET).trim(),
                line.substring(4*OFFSET).trim()
            });

        }

        while (qScan.hasNextLine()) {
            String[] quad = qScan.nextLine().split(", ");
            quads.add(quad);
            if (!quad[3].equals("-")) {
                symtab.add(new String[] {quad[3],
                    "","",
                    String.valueOf(Integer.parseInt(symtab.get(symtab.size()-1)[2]+2)),
                    "DS" });
            }
        }
    }

    public void writeASM() throws IOException {
        FileWriter writer = new FileWriter("./out.asm");
        
        writer.write(beginning);

        generateDS();
        generateCS();

        writer.write(ds);
        writer.write(cs);

        writer.write(EOF);

        // Int to string routine from given example .asm code on blackboard
        writer.write(intToString);

        writer.write(getInt);

        writer.write(printStr);

        writer.write("\t\n");
        writer.close();
    }

    private void generateDS() {
        ds = new String();
        ds += "section .data\n";
        

        // ds += "\t\n";
        // System.out.println(symtab.size());
        for (String[] row : symtab) {
            if (row[4].equals("DS") && !row[2].equals("")) {
                if (row[1].equals("$NumLit")) {
                    ds += "\tLit" + row[0] + "\tdw\t" + row[2] + "\n";
                } else {
                    ds += "\t" + row[0] + "\tdw\t" + row[2] + "\n";
                }
            }
        }
        ds += "\tuserMsg\tdb\t\'Enter an integer (less than 32,765): \'\n";
        ds += "\tlenUserMsg\tequ\t$-userMsg\n";
        ds += "\tdisplayMsg\tdb\t\'test\'\n";
        ds += "\tlenDisplayMsg\tequ\t$-displayMsg\n";
        ds += "\tResult\tdb\t'Ans = '\n";
        ds += "\tResultValue\tdb\t\'aaaaa\'\n";
        ds += "\tResultEnd\tequ\t$-ResultValue\n";
        ds += "\tnewline\tdb\t0xA\n\n";
        
        ds += "\nsection .bss\n";
        for (String[] row : symtab) {
            if (row[4].equals("DS") && row[2].equals("")) {
                ds += "\t" + row[0] + "\tresw\t1" + row[2] + "\n";
            }
        }
        ds += "\n";
    }

    private void generateCS() {
        cs = new String();
        cs += "section .text\n";
        cs += "\tglobal\t_start\n";
        cs += "_start:\n";
        for (String[] row : symtab) {
            if (row[4].equals("CS")) {
                cs += "/t" + row[1] + ":\t"; break;
            }
        }
        //cs += "\tmov ax, @data\n";
        cs += "\tmov ds, ax\n";
        
        // int numEqualOps = 0;
        for (int i = 0; i < quads.size(); i++) {
            String[] quad = quads.get(i);
            try {
                Integer.parseInt(quad[1]);
                quad[1] = "Lit" + Integer.parseInt(quad[1]);
            } catch (Exception e) {
                // TODO: handle exception
            }
            try {
                Integer.parseInt(quad[2]);
                quad[2] = "Lit" + Integer.parseInt(quad[2]);
            } catch (Exception e) {
                // TODO: handle exception
            }

            switch (quad[0]) {
                case "+": 
                    cs += "\tmov ax,[" + quad[1] + "]\n";
                    cs += "\tadd ax,[" + quad[2] + "]\n";
                    cs += "\tmov [" + quad[3] + "], ax\n\n";
                    break;
                case "-": 
                    cs += "\tmov ax,[" + quad[1] + "]\n";
                    cs += "\tsub ax,[" + quad[2] + "]\n";
                    cs += "\tmov [" + quad[3] + "], ax\n\n";
                    break;
                case "*": 
                    cs += "\tmov ax,[" + quad[1] + "]\n";
                    cs += "\tmov cx,[" + quad[2] + "]\n";
                    cs += "\tmul cx\n";
                    cs += "\tmov [" + quad[3] + "], ax\n\n";
                    break;
                case "/": 
                    cs += "\tmov ax,[" + quad[1] + "]\n";
                    cs += "\tmov cx,[" + quad[2] + "]\n";
                    cs += "\tmul cx\n";
                    cs += "\tmov [" + quad[3] + "], ax\n\n";
                    break;
                case "=": 
                    cs += "\tmov ax,[" + quad[2] + "]\n";
                    cs += "\tmov [" + quad[1] + "], ax\n\n";
                    break;
                case "==": 
                    cs += "\tmov ax,[" + quad[1] + "]\n";
                    cs += "\tcmp ax,[" + quad[2] + "]\n";
                    break;
                case "!=": 
                    cs += "\tmov ax,[" + quad[1] + "]\n";
                    cs += "\tcmp ax,[" + quad[2] + "]\n";
                    break;
                case ">": 
                    cs += "\tmov ax,[" + quad[1] + "]\n";
                    cs += "\tcmp ax,[" + quad[2] + "]\n";
                    break;
                case ">=": 
                    cs += "\tmov ax,[" + quad[1] + "]\n";
                    cs += "\tcmp ax,[" + quad[2] + "]\n";
                    break;
                case "<": 
                    cs += "\tmov ax,[" + quad[1] + "]\n";
                    cs += "\tcmp ax,[" + quad[2] + "]\n";
                    break;
                case "<=": 
                    cs += "\tmov ax,[" + quad[1] + "]\n";
                    cs += "\tcmp ax,[" + quad[2] + "]\n";
                    break;
                case "THEN":
                    cs += "\tJ" + quad[2] + " " + quad[1] + "\n\n";
                    break;
                case "L":
                    cs += quad[1] + ":\tnop\n";
                    break;
                case "GET":
                    cs += "\tmov bx, OFFSET UserMsg\n";
                    cs += "\tcall PrintString\n";
                    cs += "\tcall GetAnInteger\n";
                    cs += "\tmov ax, [ReadInt]\n";
                    cs += "\tmov [" + quad[1] + "], ax\n\n";
                    break;
                case "PRINT":
                    cs += "\tmov ax, [" + quad[1] + "]\n";
                    cs += "\tcall convertIntegerToString\n";
                    cs += "\tmov bx,OFFSET ResultValue\n";
                    cs += "\tcall PrintString\n\n";
                    break;
            }
        }
    }
}
