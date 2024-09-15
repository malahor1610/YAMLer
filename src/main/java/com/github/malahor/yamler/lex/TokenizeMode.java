package com.github.malahor.yamler.lex;

public enum TokenizeMode {
    BASIC, FOLD, PIPE;

    public boolean isSpecial(){
        return this.equals(FOLD) || this.equals(PIPE);
    }
}
