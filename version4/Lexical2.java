import java.util.* ;

class Lexical2 {
    public static void main(String[] args) {
        MyString input = new MyString("chaine test= \" gcuchuj\" ; int main() { int a = 5; if(a > 2) { return a; } else { return 0; } }");
        
        Lexer lexer = new Lexer(input);
        lexer.tokenize();
        ArrayList<Token> tokens = lexer.getTokens();
        for (Token token : tokens) {
            System.out.println("Type: " + token.getType() + " Value: " + token.getValue());
        }
    }
}


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
}


    class MyKey {
    private int id;
    private int position ;
    public MyKey(int id) {
        this.id = id;
    }
    public int hashCode() {
        return id;
    }

    public int getPosition(){
        return id;
    }
}
    class MyString {
    private String str;

    public MyString(String str) {
        this.str = str;
    }

    public String getValue(){
        return str;
    }

    public boolean equals( MyString string2 ) {
       return this.str == string2.getValue() ;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash * 31) + str.charAt(i);
        }
        System.out.println(hash);
        return hash;
    }
}

class Lexer{
    private final HashMap<MyKey,MyString> keyWords;
 
    //table des operateurs binaires
    private final String binOp[]= {"+","-","<<",">>","*","&","|","/"};
 
    //table des comparateurs binaires
    private final String binComp[]= {"<",">","<=",">=","==","!="};

    //tables des identifiers
    private ArrayList<String> identifiers = new ArrayList<String>();


    private String input;
    private ArrayList<Token> tokens;


    public Lexer(){this.keyWords=new HashMap<MyKey, MyString>();}

    public Lexer(MyString input0) {
        String input = input0.getValue();
        this.input = input;
        this.tokens = new ArrayList<Token>(); //identifiers
        this.keyWords=new HashMap<MyKey, MyString>();

        MyString string1 = new MyString("else");
        MyKey id1 = new MyKey(1);

       MyString string2 = new MyString("then");
       MyKey id2 = new MyKey(2);

       MyString string3 = new MyString("for");
       MyKey id3 = new MyKey(3);

       MyString string4 = new MyString("while");
       MyKey id4 = new MyKey(4);

       MyString string5 = new MyString("void");
       MyKey id5 = new MyKey(5);

       MyString string6 = new MyString("int");
       MyKey id6 = new MyKey(6);

       MyString string7 = new MyString("extern");
       MyKey id7 = new MyKey(7);

       MyString string8 = new MyString("return");
       MyKey id8 = new MyKey(8);

       MyString string9 = new MyString("if");
       MyKey id9 = new MyKey(9);

       MyString string10 = new MyString("chaine");
       MyKey id10 = new MyKey(10);
        keyWords.put(id1, string1);
        keyWords.put(id2, string2);
        keyWords.put(id3, string3);
        keyWords.put(id4, string4);
        keyWords.put(id5, string5);
        keyWords.put(id6, string6);
        keyWords.put(id7, string7);
        keyWords.put(id8, string8);
        keyWords.put(id9, string9);
        keyWords.put(id10, string10);
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }
     
    // looks for a keyword and return its key
    public int getIndexKeyWord(String stringToFind, HashMap<MyKey,MyString> map){
       int pos = -1 ;
       MyKey result = null;
       for (Map.Entry<MyKey, MyString> entry : map.entrySet()) {

        // entry.getValue() is a MyString
        System.out.println(entry.getValue().getValue());
       if (entry.getValue().getValue().equals(stringToFind)) {
        System.out.println(entry.getKey());
        pos = entry.getKey().getPosition();
        break;
      }
    }
    System.out.println(pos);
    return pos;
    }



    public int isKeyWord(String lexeme) {
        return getIndexKeyWord(lexeme,keyWords);
    }

    public int getIndexIdentifier(String stringToFind, ArrayList<String> identifArray){
        int pos = -1;
        for (int i=0 ; i < identifArray.size() ; i++) {
            if (identifArray.get(i).equals(stringToFind)) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    private int getNumber(int index) {
        StringBuilder number = new StringBuilder();
        while ( index < input.length() && Character.isDigit(input.charAt(index))) {
            number.append(input.charAt(index));
            index++;
        }
        return Integer.parseInt(number.toString());
    }

    private String getWord(int index) {
        StringBuilder word = new StringBuilder();
        while (index < input.length() && Character.isLetter(input.charAt(index))) {
            word.append(input.charAt(index));
            index++;
        }
        return word.toString();
    }


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






    public void tokenize() {
        int index = 0;
        while ( index < input.length() ) {
            char currentChar = input.charAt(index);

            //if we encounter a digit
            if ( Character.isDigit(currentChar) ) {
                int number = getNumber(index);
                tokens.add(new Token(TokenType.CONST_INT, String.valueOf(number)));
                index += Integer.toString(number).length();
            } 
            // if we encounter a character
            else if (Character.isLetter(currentChar)) {
                String word = getWord(index);
                int indexWord=isKeyWord(word);
                if(indexWord!=-1){
                    //System.out.println(indexWord);
                    tokens.add(new Token(TokenType.KEY_WORD, String.valueOf(indexWord) ));
                }else{
                    int indexIdentif=getIndexIdentifier(word, identifiers);
                    if(indexIdentif==-1){
                        identifiers.add(word);
                        tokens.add(new Token(TokenType.IDENTIF,identifiers.size()-1 ));
                    }else{
                        tokens.add(new Token(TokenType.IDENTIF,indexIdentif ));
                    }
                }
                index += word.length();
            }
             else if (currentChar == '+') {
                tokens.add(new Token(TokenType.BINARY_OPERATOR, getIndex("+",binOp)));

                index++;
            }
             else if (currentChar == '-') {
                tokens.add(new Token(TokenType.BINARY_OPERATOR, getIndex("-",binOp)));
                index++;
            } 
            else if (currentChar == '*') {
                tokens.add(new Token(TokenType.BINARY_OPERATOR, getIndex("*",binOp)));
                index++;
            } 
            else if (currentChar == '/') {
                tokens.add(new Token(TokenType.BINARY_OPERATOR, getIndex("/",binOp)));
                index++;
            } 
            else if (currentChar == '(') {
                tokens.add(new Token(TokenType.PARENTH_O, "("));
                index++;
            } 
            else if (currentChar == ')') {
                tokens.add(new Token(TokenType.PARENTH_F, ")"));
                index++;
            }
            else if (currentChar == '[') {
                tokens.add(new Token(TokenType.BRACKET_O, "["));
                index++;
            } 
            else if (currentChar == ']') {
                tokens.add(new Token(TokenType.BRACKET_F, "]"));
                index++;
            }
            else if (currentChar == '{') {
                tokens.add(new Token(TokenType.CBRACKET_O, "{"));
                index++;
            } 
            else if (currentChar == '}') {
                tokens.add(new Token(TokenType.CBRACKET_F, "}"));
                index++;
            }
            else if(currentChar=='<'){
                index++;
                currentChar = input.charAt(index);
                if(currentChar=='<'){
                    tokens.add(new Token(TokenType.BINARY_OPERATOR,  getIndex("<<",binOp)));
                    index++;

                }else if(currentChar=='='){
                    tokens.add(new Token(TokenType.BINARY_COMPARATOR,  getIndex("<=",binComp)));
                    index++;
                }else{
                    tokens.add(new Token(TokenType.BINARY_COMPARATOR,  getIndex("<",binComp)));

                }
            }
            else if(currentChar=='>'){
                index++;
                currentChar = input.charAt(index);
                if(currentChar=='>'){
                    tokens.add(new Token(TokenType.BINARY_OPERATOR,  getIndex(">>",binOp)));
                    index++;

                }else if(currentChar=='='){
                    tokens.add(new Token(TokenType.BINARY_COMPARATOR,  getIndex(">=",binComp)));
                    index++;
                }else{
                    tokens.add(new Token(TokenType.BINARY_COMPARATOR,  getIndex(">",binComp)));
                }
            }
            else if(currentChar=='='){
                index++;
                currentChar = input.charAt(index);
                if(currentChar=='='){
                    tokens.add(new Token(TokenType.BINARY_OPERATOR,  getIndex("==",binComp)));
                    index++;
                }else{
                    tokens.add(new Token(TokenType.AFFECT,"="));
                }
            }

            else if(currentChar=='&'){
                index++;
                currentChar = input.charAt(index);
                if(currentChar=='&'){
                    tokens.add(new Token(TokenType.DOUBLE_AND, "&&"));
                    index++;

                }else {
                    tokens.add(new Token(TokenType.BINARY_COMPARATOR, getIndex("&",binOp)));
                }
            }

            else if(currentChar=='|'){
                index++;
                currentChar = input.charAt(index);
                if(currentChar=='|'){
                    tokens.add(new Token(TokenType.DOUBLE_OR, "||"));
                    index++;

                }else {
                    tokens.add(new Token(TokenType.BINARY_COMPARATOR, getIndex("|",binOp)));
                }
            }

            else if(currentChar=='!'){
                index++;
                currentChar = input.charAt(index);
                if(currentChar=='='){
                    tokens.add(new Token(TokenType.BINARY_COMPARATOR, getIndex("!=",binOp)));
                    index++;

                }else {
                    tokens.add(new Token(TokenType.NEGATION, "!"));
                }
            }

            else if(currentChar=='"'){
                index++;
                currentChar = input.charAt(index);
                String chain="";
                while(currentChar!='"'){
                    currentChar = input.charAt(index);
                    if(currentChar!='"')chain+=currentChar;
                    index++;

                }
                tokens.add(new Token(TokenType.CONST_STRING, chain));
                index++;

            }
            
             else {
                index++;
            }
        }
    }


    
}



