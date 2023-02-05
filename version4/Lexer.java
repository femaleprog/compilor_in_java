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

class Lexer {
    private final HashMap<Integer, String> keyWords;

    // table des operateurs binaires
    private final String binOp[] = { "+", "-", "<<", ">>", "*", "&", "|", "/" };

    // table des comparateurs binaires
    private final String binComp[] = { "<", ">", "<=", ">=", "==", "!=" };

    // tables des identifiers
    private HashMap<Integer, String> identifiers ;
    private final char EOF='\u001a';
    private BufferedReader buffer;
    private char currentChar;
    private ArrayList<Token> tokens;


    public Lexer() {
        this.keyWords = new HashMap<>();
        this.identifiers = new HashMap<>();
    }

    public Lexer(String sourceFile)throws IOException {
        buffer = new BufferedReader((new FileReader(sourceFile)));
        currentChar = read();
        this.tokens = new ArrayList<Token>();
        this.keyWords = new HashMap<>();
        this.identifiers = new HashMap<>();
        keyWords.put(1, "else");
        keyWords.put(2, "then");
        keyWords.put(3, "for");
        keyWords.put(4, "while");
        keyWords.put(5, "void");
        keyWords.put(6, "int");
        keyWords.put(7, "extern");
        keyWords.put(8, "return");
        keyWords.put(9, "if");
        keyWords.put(10, "chaine");
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public HashMap<Integer, String> getKeyWords(){
        return keyWords;
    }


    public HashMap<Integer, String> getIdentifiers(){
        return identifiers;
    }



   
    public int getIndexKeyWord(String stringToFind, HashMap<Integer, String> map) {
        int pos = -1;
        for (Map.Entry m : map.entrySet()) {
            if (m.getValue().equals(stringToFind)) {
                pos = Integer.valueOf((int) m.getKey());
                break;
            }
        }
        return pos;
    }

    public int getIndexdentif(String stringToFind, HashMap<Integer, String> map) {
        int pos = map.size();
        for (Map.Entry m : map.entrySet()) {
            if (m.getValue().equals(stringToFind)) {
                pos = Integer.valueOf((int) m.getKey());
                break;
            }
        }
        return pos;
    }
    

    public int isKeyWord(String lexeme) {
        return getIndexKeyWord(lexeme, keyWords);
    }

    public int getIndexIdentifier(String stringToFind, ArrayList<String> identifArray) {
        int pos = -1;
        for (int i = 0; i < identifArray.size(); i++) {
            if (identifArray.get(i).equals(stringToFind)) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    // la fonction qui donne la valeur numerique d'une constante de type 'int'
    private int getNumber() throws IOException {
        StringBuilder number = new StringBuilder();
        while (Character.isDigit(currentChar)) {
            number.append(currentChar);
            currentChar=read();
        }
        return Integer.parseInt(number.toString());
    }

    /// La fonction qui donne la valeur de chaine de caractere saisie par 
    private String getWord() throws IOException{
        StringBuilder word = new StringBuilder();
        while (Character.isLetter(currentChar)) {
            word.append(currentChar);
            currentChar=read();
        }
        return word.toString();
    }

    //// la fonction qui donne l'index d'un string dans le tableau de synboles
    public int getIndex(String stringToFind, String[] stringArray){
        int post = -1;
        for (int i=0;i<stringArray.length;i++) {
            if (stringArray[i].equals(stringToFind)) {
                post = i;
                return post ;
            }
        }
        return post;
    }

    /// La fonction qui lit les caracteres
    public char read() throws IOException {
        int c = buffer.read();
        if (c != -1)
            return (char) c;
        return EOF;
    }

    /// La fonction qui genere les unites lexicales suivantes
    public void tokenize() throws IOException {
        while (currentChar != EOF) {

            // if we encounter a digit
            if (Character.isDigit(currentChar)) {
                int number = getNumber();
                tokens.add(new Token(TokenType.CONST_INT, String.valueOf(number)));
            }
            // if we encounter a character
            else if (Character.isLetter(currentChar)) {
                String word = getWord();
                int indexWord = isKeyWord(word);
                if (indexWord != -1) {
                    tokens.add(new Token(TokenType.KEY_WORD, String.valueOf(indexWord) ));
            }else {
                    int indexIdentif=getIndexdentif(word,identifiers);
                    identifiers.put(indexIdentif,word);
                    tokens.add(new Token(TokenType.IDENTIF, indexIdentif));
                }

            } else if (currentChar == ';') {
                tokens.add(new Token(TokenType.SEMICOLON,";" ));

                currentChar=read();
            }else if (currentChar == ',') {
                tokens.add(new Token(TokenType.COMMA,"," ));

                currentChar=read();
            }else if (currentChar == '+') {
                tokens.add(new Token(TokenType.BINARY_OPERATOR, getIndex("+",binOp)));

                currentChar=read();
            }
             else if (currentChar == '-') {
                tokens.add(new Token(TokenType.BINARY_OPERATOR, getIndex("-",binOp)));
                currentChar=read();
            } 
            else if (currentChar == '*') {
                tokens.add(new Token(TokenType.BINARY_OPERATOR, getIndex("*",binOp)));
                currentChar=read();
            } 
            else if (currentChar == '/') {
                tokens.add(new Token(TokenType.BINARY_OPERATOR, getIndex("/",binOp)));
                currentChar=read();
            } 
            else if (currentChar == '(') {
                tokens.add(new Token(TokenType.PARENTH_O, "("));
                currentChar=read();
            } 
            else if (currentChar == ')') {
                tokens.add(new Token(TokenType.PARENTH_F, ")"));
                currentChar=read();
            }
            else if (currentChar == '[') {
                tokens.add(new Token(TokenType.BRACKET_O, "["));
                currentChar=read();
            } 
            else if (currentChar == ']') {
                tokens.add(new Token(TokenType.BRACKET_F, "]"));
                currentChar=read();
            }
            else if (currentChar == '{') {
                tokens.add(new Token(TokenType.CBRACKET_O, "{"));
                currentChar=read();
            } 
            else if (currentChar == '}') {
                tokens.add(new Token(TokenType.CBRACKET_F, "}"));
                currentChar=read();
            }else if (currentChar == ',') {
                tokens.add(new Token(TokenType.COMMA, ","));
                currentChar=read();
            }else if (currentChar == ';') {
                tokens.add(new Token(TokenType.SEMICOLON, ";"));
                currentChar=read();
            }
            else if(currentChar=='<'){
                currentChar=read();
                if(currentChar=='<'){
                    tokens.add(new Token(TokenType.BINARY_OPERATOR,  getIndex("<<",binOp)));
                    currentChar=read();

                }else if(currentChar=='='){
                    tokens.add(new Token(TokenType.BINARY_COMPARATOR,  getIndex("<=",binComp)));
                    currentChar=read();
                }else{
                    tokens.add(new Token(TokenType.BINARY_COMPARATOR,  getIndex("<",binComp)));

                }
            }
            else if(currentChar=='>'){
                currentChar=read();
                if(currentChar=='>'){
                    tokens.add(new Token(TokenType.BINARY_OPERATOR,  getIndex(">>",binOp)));
                    currentChar=read();

                }else if(currentChar=='='){
                    tokens.add(new Token(TokenType.BINARY_COMPARATOR,  getIndex(">=",binComp)));
                    currentChar=read();;
                }else{
                    tokens.add(new Token(TokenType.BINARY_COMPARATOR,  getIndex(">",binComp)));
                }
            }
            else if(currentChar=='='){
                currentChar=read();;
                if(currentChar=='='){
                    tokens.add(new Token(TokenType.BINARY_OPERATOR,  getIndex("==",binComp)));
                    currentChar=read();
                }else{
                    tokens.add(new Token(TokenType.AFFECT,"="));
                }
            }

            else if(currentChar=='&'){
                currentChar=read();
                if(currentChar=='&'){
                    tokens.add(new Token(TokenType.DOUBLE_AND, "&&"));
                    currentChar=read();
                }else {
                    tokens.add(new Token(TokenType.BINARY_COMPARATOR, getIndex("&",binOp)));
                }
            }

            else if(currentChar=='|'){
                currentChar=read();
                if(currentChar=='|'){
                    tokens.add(new Token(TokenType.DOUBLE_OR, "||"));
                    currentChar=read();

                }else {
                    tokens.add(new Token(TokenType.BINARY_COMPARATOR, getIndex("|",binOp)));
                }
            }

            else if(currentChar=='!'){
                currentChar=read();
                if(currentChar=='='){
                    tokens.add(new Token(TokenType.BINARY_COMPARATOR, getIndex("!=",binOp)));
                    currentChar=read();

                }else {
                    tokens.add(new Token(TokenType.NEGATION, "!"));
                }
            }

            else if(currentChar=='"'){
                currentChar=read();
                String chain="";
                while(currentChar!='"'){
                    if(currentChar!='"')chain+=currentChar;
                    currentChar=read();

                }
                tokens.add(new Token(TokenType.CONST_STRING, chain));
                currentChar=read();

            }
            
             else {
                currentChar=read();
            }
        }
    }
    
}