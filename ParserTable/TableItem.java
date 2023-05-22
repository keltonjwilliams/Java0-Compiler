package ParserTable;

public class TableItem {

    private String symbol;
    private String classification;

    private int nextState;

    private final boolean hasSymbol;
    private final boolean isFinal;

    public TableItem(int next) {
        nextState = next;

        hasSymbol = false;
        isFinal = false;
    }

    public TableItem(String clss) {
        classification = clss;

        hasSymbol = false;
        isFinal = true;
    }

    public TableItem(String sym, String clss) {
        symbol = sym;
        classification = clss;

        hasSymbol = true;
        isFinal = true;
    }

    public String getSymbol() {
        // if (!isFinal) {
        //     throw new FinalStateException();
        // }
        return symbol;
    }

    public String getClassification() {
        // if (!isFinal) {
        //     throw new FinalStateException();
        // }
        return classification;
    }

    public String[] getItem() {
        String[] ret = new String[] {
            symbol, classification
        };

        return ret;
    }

    public boolean hasSymbol() {
        return hasSymbol;
    }

    public int getNext() {
        // if (isFinal) {
        //     throw new FinalStateException();
        // }
        return nextState;
    }

    public boolean isFinal() {
        return isFinal;
    }

    
}