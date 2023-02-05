import java.lang.ProcessBuilder.Redirect.Type;
import java.util.*;
import java.util.Stack;
import java.io.*;


enum TokenType {

    BINARY_OPERATOR,
    BINARY_COMPARATOR,
    DOUBLE_AND,
    DOUBLE_OR,
    NEGATION,
    BRACKET_O,
    BRACKET_F,
    PARENTH_O,
    PARENTH_F,
    COLON,
    SEMICOLON,
    AFFECT,
    CBRACKET_O,
    CBRACKET_F,
    KEY_WORD,
    CONST_INT,
    CONST_STRING,
    IDENTIF,
    NUMBER,
    COMMA,
}

class Token {
    private TokenType type;
    private Object value;
    private int line;
    private int column;

    public Token(TokenType type, Object value, int line, int column) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;

    }

    public TokenType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    boolean isThisKey(String st) {
        if(this.type == TokenType.KEY_WORD){
            if((this.getValue()).equals(st)){
                return true;
            }
        }
        return false;
    }
}
