package SymbolTable;

public class FSAItem {

    private int nextState;

    private boolean isFinal;

    public FSAItem() {
        isFinal = true;
    }

    public FSAItem(int next) {
        nextState = next;
        isFinal = false;
    }

    public int getNext() {
        return nextState;
    }

    public boolean isFinal() {
        return isFinal;
    }
}