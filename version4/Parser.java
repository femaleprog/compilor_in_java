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

    class Parser {

        private final HashMap<Integer, String> keyWords;
        private final HashMap<Integer, String> identifiers;
        public final HashMap<Integer, Variable> variables = new HashMap<>();
        private final HashMap<Integer, Function> functions = new HashMap<>();
        public final HashMap<Integer, Table> tables = new HashMap<>();

        private ArrayList<Token> tokens;
        private final String binOp[] = { "+", "-", "<<", ">>", "*", "&", "|", "/" };
        public int currentIndex = 0;

        //////// fonction qui teste si la table des variables contient le Token
        public int getIndexVar(Variable token) {
            int pos = variables.size();
            for (Map.Entry<Integer, Variable> m : variables.entrySet()) {
                if (m.getValue().getValue().equals(token.getValue())) {
                    pos = Integer.valueOf((int) m.getKey());
                    break;
                }
            }
            return pos;
        }

        boolean isThisKeyword(String value, Token test) {
            if (test.getType() == TokenType.KEY_WORD) {
                String st = keyWords.get(Integer.parseInt(test.getValue().toString()));
                if (st.equals(value))
                    return true;
            }
            return false;
        }

        public Parser() {
            this.keyWords = null;
            this.identifiers = null;
            this.tokens = new ArrayList<>();
        }

        public Parser(ArrayList<Token> tokens, HashMap<Integer, String> keyWords, HashMap<Integer, String> identifiers) {
            this.keyWords = keyWords;
            this.identifiers = identifiers;
            this.tokens = tokens;
        }

        private Token previousToken() {
            return tokens.get(currentIndex - 1);
        }

        private Token nextToken() {
            return tokens.get(currentIndex + 1);
        }

        //////////////////// getter d'un variables specifiques ////////////////////
        // regarde si un variable est deja declarer 
        private Variable getVariable(Token token) {
            for (Map.Entry<Integer, Variable> m : variables.entrySet()) {
                if ((m.getValue()).getValue().equals(token.getValue())) {

                    return m.getValue();
                }
            }
            return new Variable(token.getType(), "3", "int");
        }

        ////////// fonction qui ajout un variable declaré à la liste des variables
     // prend un token et verifie si une variable ou table existe deja  
        private void addVariableTable() {
            if (nextToken().getType() != TokenType.PARENTH_O && nextToken().getType() != TokenType.BRACKET_O) {/////////
                if (isThisKeyword("int", previousToken())) {
                    if (isVarDeclared(currentToken())) {
                        throw new RuntimeException("ERROR! vaiable already declared");

                    } else {
                        variables.put(Integer.parseInt(String.valueOf(variables.size())),
                                new Variable(currentToken().getType(), currentToken().getValue(), "int"));
                    }

                } else if (isThisKeyword("chaine", previousToken())) {
                    if (isVarDeclared(currentToken())) {
                        throw new RuntimeException("ERROR! vaiable already declared");

                    } else {
                        variables.put(Integer.parseInt(String.valueOf(variables.size())),
                                new Variable(currentToken().getType(), currentToken().getValue(), "chaine"));
                    }

                } else if (previousToken().getType() == TokenType.COMMA) {
                    int indexTest = currentIndex - 2;
                    while (tokens.get(indexTest).getType() != TokenType.KEY_WORD)
                        indexTest--;
                    if (isThisKeyword("int", tokens.get(indexTest))) {
                        if (isVarDeclared(currentToken())) {
                            throw new RuntimeException("ERROR! vaiable already declared");

                        } else {
                            variables.put(Integer.parseInt(String.valueOf(variables.size())),
                                    new Variable(currentToken().getType(), currentToken().getValue(), "int"));
                        }

                    } else if (isThisKeyword("chaine", previousToken())) {
                        if (isVarDeclared(currentToken())) {
                            throw new RuntimeException("ERROR! vaiable already declared");

                        } else {
                            variables.put(Integer.parseInt(String.valueOf(variables.size())),
                                    new Variable(currentToken().getType(), currentToken().getValue(), "chaine"));
                        }

                    }

                }

            } else if (nextToken().getType() == TokenType.BRACKET_O) {//////////// tableau
                int length = 0;
                if (Character.isDigit(String.valueOf(tokens.get(currentIndex + 2).getValue()).charAt(0)))
                    length = Integer.parseInt(String.valueOf(tokens.get(currentIndex + 2).getValue()));

                if (isThisKeyword("int", previousToken())) {
                    if (isTabDeclared(currentToken())) {
                        System.out.println("Table already declared1");

                    } else {
                        tables.put(Integer.parseInt(String.valueOf(tables.size())),
                                new Table(currentToken().getType(), currentToken().getValue(), length, "int"));
                    }

                } else if (isThisKeyword("chaine", previousToken())) {
                    if (isTabDeclared(currentToken())) {
                        System.out.println("Table already declared2");

                    } else {
                        tables.put(Integer.parseInt(String.valueOf(tables.size())),
                                new Table(currentToken().getType(), currentToken().getValue(), length, "chaine"));
                    }

                } else if (previousToken().getType() == TokenType.COMMA) {
                    int indexTest = currentIndex - 2;
                    while (tokens.get(indexTest).getType() != TokenType.KEY_WORD)
                        indexTest--;
                    if (isThisKeyword("int", tokens.get(indexTest))) {
                        if (isTabDeclared(currentToken())) {
                            System.out.println("table already declared3");

                        } else {
                            tables.put(Integer.parseInt(String.valueOf(tables.size())),
                                    new Table(currentToken().getType(), currentToken().getValue(), length, "int"));
                        }

                    } else if (isThisKeyword("chaine", previousToken())) {
                        if (isTabDeclared(currentToken())) {
                            System.out.println("table already declared4");

                        } else {
                            tables.put(Integer.parseInt(String.valueOf(tables.size())),
                                    new Table(currentToken().getType(), currentToken().getValue(), length, "chaine"));
                        }

                    }

                }

            }
        }

        ////////////////////////////// test de declaration d'u variable
        ////////////////////////////// //////////////////////////

        boolean isVarDeclared(Token token) {
            for (Map.Entry<Integer, Variable> m : variables.entrySet()) {
                if ((m.getValue()).getValue().equals(token.getValue())) {

                    return true;
                }
            }

            return false;
        }

        ////////////////////////////// test de declaration d'u table
        ////////////////////////////// //////////////////////////

        boolean isTabDeclared(Token token) {
            for (Map.Entry<Integer, Table> m : tables.entrySet()) {
                if ((m.getValue()).getValue().equals(token.getValue())) {

                    return true;
                }
            }

            return false;
        }

        ////////////////////////////// erreur de declarations
        ////////////////////////////// //////////////////////////////

        private void errorDeclared() {

            if (typeCurrentToken(TokenType.IDENTIF) && nextToken().getType() != TokenType.PARENTH_O) {
                if (!isVarDeclared(currentToken())) {
                    throw new RuntimeException("ERROR! variables non decalrer");
                }
            } else {

            }
        }

        ///////////////// fonction de extaction de type de variables///////
        String variableType(Token token) {
            for (Map.Entry<Integer, Variable> m : variables.entrySet()) {
                if ((m.getValue()).getValue().equals(token.getValue())) {
                    // System.out.print("declarer");
                    return m.getValue().varType;
                }
            }
            return "";
        }

        ///////////////////////////////////////// modifier la valeur du variables apres
        ///////////////////////////////////////// chque affectation faites;

        void variableValue() {
            String value = "";
            int indexTestType = currentIndex + 2;
            if (typeCurrentToken(TokenType.IDENTIF)) {
                Token testCurrentToken = tokens.get(indexTestType);
                while (testCurrentToken.getType() != TokenType.SEMICOLON) {
                    if (testCurrentToken.getType() == TokenType.IDENTIF) {

                        value += (getVariable(testCurrentToken).varValue);
                    } else if (testCurrentToken.getType() == TokenType.BINARY_OPERATOR) {
                        value += String.valueOf(binOp[Integer.parseInt(String.valueOf(testCurrentToken.getValue()))]);
                    } else if (testCurrentToken.getType() == TokenType.CONST_INT) {
                        value += testCurrentToken.getValue();
                    }
                    indexTestType++;
                    testCurrentToken = tokens.get(indexTestType);
                }
                System.out.println(value + "=" + String.valueOf((int) (Calculator.calculate(value))));

            }
            for (Map.Entry<Integer, Variable> m : variables.entrySet()) {
                if ((m.getValue()).getValue().equals(currentToken().getValue())) {
                    // System.out.print("declarer");
                    m.getValue().varValue = String.valueOf((int) (Calculator.calculate(value)));
                }
            }

        }

        ///////////////////////////////// fonction de controlle de types
        ///////////////////////////////// //////////////////////////////

        private void typeControl() {
            if (typeCurrentToken(TokenType.IDENTIF) && nextToken().getType() == TokenType.AFFECT) {
                int indexTestType1 = currentIndex + 2;
                String typeIdentif = variableType(currentToken());
                Token testCurrentToken = tokens.get(indexTestType1);
                while (testCurrentToken.getType() != TokenType.SEMICOLON) {
                    if (testCurrentToken.getType() == TokenType.CONST_INT && typeIdentif == "chaine") {
                        throw new RuntimeException("Types des operandes est incompatible");
                    } else if (testCurrentToken.getType() == TokenType.CONST_STRING && typeIdentif == "int") {
                        throw new RuntimeException("Types des operandes est incompatible");
                        
                    } else if (testCurrentToken.getType() == TokenType.IDENTIF
                            && typeIdentif != variableType(testCurrentToken)) {
                                throw new RuntimeException("Types des operandes est incompatible");
                    } else {
                        ///// no probleme
                    }
                    indexTestType1++;
                    testCurrentToken = tokens.get(indexTestType1);
                }
            } else if (typeCurrentToken(TokenType.IDENTIF) && nextToken().getType() == TokenType.BRACKET_O) {
                String value = "";
                int indexTestType1 = currentIndex + 2;
                Token testCurrentToken = tokens.get(indexTestType1);
                while (testCurrentToken.getType() != TokenType.BRACKET_F) {
                    if (testCurrentToken.getType() == TokenType.IDENTIF) {

                        value += (getVariable(testCurrentToken).varValue);
                    } else if (testCurrentToken.getType() == TokenType.BINARY_OPERATOR) {
                        value += String.valueOf(binOp[Integer.parseInt(String.valueOf(testCurrentToken.getValue()))]);
                    } else if (testCurrentToken.getType() == TokenType.CONST_INT) {
                        value += testCurrentToken.getValue();
                    }
                    indexTestType1++;
                    testCurrentToken = tokens.get(indexTestType1);
                }
                // System.out.println(value + "=" + String.valueOf((int)
                // (Calculator.calculate(value))));
                String typeIdentif = variableType(currentToken());
                testCurrentToken = tokens.get(indexTestType1);
                while (testCurrentToken.getType() != TokenType.SEMICOLON) {
                    if (testCurrentToken.getType() == TokenType.CONST_INT && typeIdentif == "chaine") {
                        System.out.println("Error:1");
                    } else if (testCurrentToken.getType() == TokenType.CONST_STRING && typeIdentif == "int") {
                        System.out.println("Error:2");
                    } else if (testCurrentToken.getType() == TokenType.IDENTIF
                            && typeIdentif != variableType(testCurrentToken)) {
                        System.out.println("Error:3");
                    } else {
                        ///// no probleme
                    }
                    indexTestType1++;
                    testCurrentToken = tokens.get(indexTestType1);
                }
            }

        }

        /////////// fonction qui test si unite l'exicale courante est un mot cle
        /////////// value/////////

        boolean isThisKey(String value) {
            if (currentToken().getType() == TokenType.KEY_WORD) {
                String st = keyWords.get(Integer.parseInt(currentToken().getValue().toString()));
                if (st.equals(value))
                    return true;
            }
            return false;

        }

        public void parse() {
            // start with the <Programme> rule
            parseProgramme();
            if (currentIndex != tokens.size() - 2) {
                throw new RuntimeException("Unexpected tokens at the end of input");
            }

        }

        public ArrayList<Token> getTokens() {
            return tokens;
        }

        private boolean match(TokenType expected) {
            if (currentToken().getType() == expected) {
                currentIndex++;

                return true;
            } else {
                return false;
            }
        }

        private Token currentToken() {
            return tokens.get(currentIndex);
        }

        private boolean typeCurrentToken(TokenType expected) {
            return currentToken().getType() == expected;
        }

        ////////////////////////// 1
        private void parseProgramme() {
            System.out.println("Régle 1");
            parseListDeclarations();
            parseListeFonctions();
        }

        ////////////////////////// 2
        private void parseListDeclarations() {
            System.out.println("Régle 2");
            if (isThisKey("int") || isThisKey("chaine")) {
                match(TokenType.KEY_WORD);
                addVariableTable();
                parseDeclaration();

            } else {

            }
        }

        ////////////////////////// 3
        private void parseDeclaration() {
            System.out.println("Régle 3");
            if (typeCurrentToken(TokenType.IDENTIF)) {

                parseListeDeclarateurs();
                if (match(TokenType.SEMICOLON)) {
                    parseListDeclarations();
                } else if (typeCurrentToken(TokenType.PARENTH_O)) {
                    parseFonction();
                    parseListeFonctions();
                } else {
                    throw new RuntimeException("Erreur dans la régle 3!!! Un point virgulle ou une parenthese ouvrante sont expectes a ce niveau, mais vous avez: " + currentToken().getType());

                }

            } else {
                throw new RuntimeException("Erreur dans la régle 3!!! La regle de production <declaration> n'est bien definie a ce niveau, mais vous avez: " + currentToken().getType());
                
            }
        }

        ////////////////////////// 4
        private void parseListeDeclarateurs() {
            System.out.println("Régle 4");
            parseDeclarateur();
            parseListeDeclarateursPrime();
        }

        ////////////////////////// 5
        private void parseListeDeclarateursPrime() {
            System.out.println("Régle 5");
            if (match(TokenType.COMMA)) {
                addVariableTable();
                parseDeclarateur();
                parseListeDeclarateursPrime();
            } else {
                // epsilon
            }
        }

        ////////////////////////// 6
        private void parseDeclarateur() {
            System.out.println("Régle 6");
            if (match(TokenType.IDENTIF)) {
                parseDeclarateurPrime();
            } else {
                throw new RuntimeException("Erreur dans la régle 6!!! Un identificateur est expexte a ce niveau, mais vous avez: " + currentToken().getType());

            }
        }

        ////////////////////////// 7
        private void parseDeclarateurPrime() {
            System.out.println("Régle 7");
            if (match(TokenType.BRACKET_O)) {
                if (match(TokenType.CONST_INT)) {
                    if (match(TokenType.BRACKET_F)) {
                    } else
                    throw new RuntimeException("Erreur dans la regle 7!!! Une bracket fermante est expectee a ce niveau, mais vous avez: " + currentToken().getType());


                } else
                throw new RuntimeException("Erreur dans la regle 7!!! Une constante de type 'int' est expectee a ce niveau, mais vous avez: " + currentToken().getType());

            } else {
                //////////// epsilon
            }
        }

        ////////////////////////// 8
        private void parseListeFonctions() {
            System.out.println("Régle 8");
            if (isThisKey("int") || isThisKey("chaine")) {
                match(TokenType.KEY_WORD);
                if (match(TokenType.IDENTIF)) {
                    parseFonction();
                    parseListeFonctions();
                } else {
                    throw new RuntimeException("Erreur dans la regle 8!!! Un identificateur est expectee a ce niveau, mais vous avez: " + currentToken().getType());

                }
            } else if (isThisKey("void") || isThisKey("extern")) {
                parseListeFonctionsPrime();
            } else {
                throw new RuntimeException("Erreur dans la regle 8!!! La regle <liste-fonctions> n'est bien definie a ce niveau. Vous avez: " + currentToken().getType());


            }
        }

        ////////////////////////// 9
        private void parseListeFonctionsPrime() {
            System.out.println(" Régle 9");
            if (isThisKey("void")) {
                match(TokenType.KEY_WORD);
                if (match(TokenType.IDENTIF)) {
                    parseFonction();
                    parseListeFonctions();
                } else {
                    throw new RuntimeException("Erreur dans la regle 9!!! Un identificateur est expexte a ce niveau, mais vous avez: " + currentToken().getType()); 

                }
            } else if (isThisKey("extern")) {
                parseExtern();
                parseListeFonctions();
            } else {
                //// epsilon
            }
        }

        ////////////////////////// 10

        private void parseExtern() {
            System.out.println(" Régle 10");
            if (isThisKey("extern")) {
                match(TokenType.KEY_WORD);
                parseType();
                if (match(TokenType.IDENTIF)) {
                    if (match(TokenType.PARENTH_O)) {
                        parseListeParms();
                        if (match(TokenType.PARENTH_F)) {
                            if (match(TokenType.SEMICOLON)) {
                            } else {
                                throw new RuntimeException("Erreur dans le regle 10!!! Un point virgulle est expecte a ce niveau, mais vous avez: " + currentToken().getType());

                            }
                        } else {
                            throw new RuntimeException("Erreur dans la regle 10!!! Une parenthese fermante est expectee a ce niveau, mais vous avez: " + currentToken().getType());

                        }
                    } else {
                        throw new RuntimeException("Erreur dans la regle 10!!! Une parenthese ouvrante est expectee a ce niveau, mais vous avez: " + currentToken().getType());

                    }
                } else {
                    throw new RuntimeException("Erreur dans la regle 10!!! Un identificateur est expecte a ce niveau, mais vous avez:" + currentToken().getType());

                }
            }
        }

        ////////////////////////// 11
        private void parseFonction() {
            System.out.println("Regle 11 : <fonction>");
            if (match(TokenType.PARENTH_O)) {
                parseListeParms();
                if (match(TokenType.PARENTH_F)) {
                    if (match(TokenType.CBRACKET_O)) {
                        parseListDeclarations();
                        parseListeInstructions();
                        if (match(TokenType.CBRACKET_F)) {} 
                        else {
                            throw new RuntimeException("Erreur dans la regle 11!!! Une bracket fermante est expectee a ce niveau, mais vous avez:" + currentToken().getType());
                        }
                    } 
                    else {
                        throw new RuntimeException("Erreur dans la regle 11!!! Une bracket ouvrante est expectee a ce niveau, mais vous avez: " + currentToken().getType());
                    }
                } 
                else {
                    throw new RuntimeException("Erreur dans la regle 11!!! Une bracket ouvrante est expectee a ce niveau, mais vous avez: " + currentToken().getType());
                }
            } 
            else {
                throw new RuntimeException("Erreur dans la regle 11!!! Une parenthese ouvrante est expextee a ce niveau, mais vous avez: " + currentToken().getType());
            }
        }

        ////////////////////////// 12
        private void parseType() {
            System.out.println("Regle 12: <type>");
            if (isThisKey("int") || isThisKey("chaine") || isThisKey("void")) {
                match(TokenType.KEY_WORD);
            } 
            else {
                throw new RuntimeException("Erreur dans la regle 12!!! Un type 'int', 'chaine' ou 'void' est expexte a ce niveau, mais vous avez: " + currentToken().getType());
            }
        }

        ////////////////////////// 13
        private void parseListeParms() {
            System.out.println("Regle 13 : <liste-parms>");
            parseListeParmsPrime();
        }

        ////////////////////////// 14
    private void parseListeParmsPrime() {
            //// System.out.println(" c");
            System.out.println("Regle 14: <liste'parms'>");
            parseParm();
            if (match(TokenType.COMMA)) {
                parseListeParmsPrime();
            } else {
                // epsilon
            }
        }

        ////////////////////////// 15
        private void parseParm() {
            System.out.println("Regle 15 : <parm>");
            if (isThisKey("int") || isThisKey("chaine")) {
                match(TokenType.KEY_WORD);
                if (match(TokenType.IDENTIF)) {

                } 
                else {
                    throw new RuntimeException("Erreur dans la regle 15!!! Un identificateur est expecte a ce niveau, mais vous avez: " + currentToken().getType());
                }
            } 
            else {
                ///// epsilon    
            }
        }

        ////////////////////////// 16
        private void parseListeInstructions() {
            System.out.println("Régle 16");
            parseListeInstructionsPrime();
        }

        ////////////////////////// 17
        private void parseListeInstructionsPrime() {
            System.out.println("Régle 17");
            if (typeCurrentToken(TokenType.CBRACKET_O) || typeCurrentToken(TokenType.IDENTIF) || isThisKey("for")
                    || isThisKey("while") || isThisKey("if") || isThisKey("return")) {
                errorDeclared();
                parseInstruction();
                parseListeInstructionsPrime();
            } else {
                //////// epsilon
            }
        }

        ////////////////////////// 18
        private void parseInstruction() {
            System.out.println("Régle 18");
            if (typeCurrentToken(TokenType.IDENTIF)) {
                typeControl();
                variableValue();
                errorDeclared();

                match(TokenType.IDENTIF);
                parseInstructionPrime();
            } else if (isThisKey("for") || isThisKey("while")) {
                parseIteration();
            } 
            else if (isThisKey("if")) {
                parseSelection();
            } 
            else if (isThisKey("return")) {
                parseSaut();
            } 
            else if (typeCurrentToken(TokenType.CBRACKET_O)) {
                parseBloc();
            } 
            else {
                throw new RuntimeException("Erreur dans la regle 18!!! La regle de production <instruction> n'est pas bien conçue a ce niveau. Vous devez avoir soient 'if', 'while', 'for' ou 'return' mais vous avez: " + currentToken().getType());
            }
        }

        ////////////////////////// 19
        private void parseInstructionPrime() {
            System.out.println("Regle 19: <instruction'>");
            if (match(TokenType.PARENTH_O)) {
                parseListeExpressions();
                if (match(TokenType.PARENTH_F)) {
                    if (match(TokenType.SEMICOLON)) {} 
                    else {
                        throw new RuntimeException("Erreur dans la regle 19 !!! Un point virgule est expecte a ce niveau, mais vous avez: " + currentToken().getType());
                    }
                } 
                else {
                    throw new RuntimeException("Erreur dans la regle 19!!! Une parenthese fermante est expectee a ce niveau, mais vous avez:" + currentToken().getType());
                }
            }
            else if (typeCurrentToken(TokenType.BRACKET_O) ||  typeCurrentToken(TokenType.AFFECT)) {
                parseVariablePrime();
                if (match(TokenType.AFFECT)) {
                    if (match(TokenType.CONST_STRING)) {} 
                    else if (typeCurrentToken(TokenType.PARENTH_O) || typeCurrentToken(TokenType.CONST_INT)
                            || typeCurrentToken(TokenType.IDENTIF) || typeCurrentToken(TokenType.BINARY_OPERATOR)) {
                        parseExpression();
                        if (match(TokenType.SEMICOLON)) {} 
                        else {
                            throw new RuntimeException("Erreur dans la regle 191 !!! Un point virgule est expecte a ce niveau, mais vous avez: " + currentToken().getType());
                        }
                    } 
                    else {
                        throw new RuntimeException("Erreur dans la regle 19 !!! Une parenthese ouvrante ou un identificateur ou une constante de type int ou un operateur binaire sont expectes a ce niveau, mais vous avez: " + currentToken().getType()); 
                    }
                    
                } 
                else {
                    throw new RuntimeException("Erreur dans la regle 19 !!! Une affectation est expectee a ce niveau, mais vous avez: " + currentToken().getType());
                }

            } 
            else {
                throw new RuntimeException("Erreur dans la regle 19!!! Une bracket ouvrante ou une parenthese ouvrante sont expectees a ce niveau. Vous avez mal conçue la regle de prodution <instruction'> mais vous avez: " + currentToken().getType());
            }
        }

        ////////////////////////// 20
        private void parseVariable() {
            System.out.println("Régle 20");
            if (match(TokenType.IDENTIF)) {
                parseVariablePrime();
            } else {
                throw new RuntimeException(
                        "Erreur dans la regle 20!!! Un identificateur est expecte a ce niveau, mais vous avez: "
                                + currentToken().getType());
            }
        }

        ////////////////////////// 21
        private void parseVariablePrime() {
            System.out.println("Régle 21");
            if (match(TokenType.BRACKET_O)) {
                parseExpression();
                if (match(TokenType.BRACKET_F)) {
                } else {
                    throw new RuntimeException("Erreur dans la regle  21!!! Une bracket fermante est expectee a ce niveau, mais vous avez: " + currentToken().getType());

                }
            } else {
                // epsilon
            }
        }

        ////////////////////////// 22
        private void parseIteration() {
            System.out.println("Régle 22");
            if (isThisKey("for")) {
                match(TokenType.KEY_WORD);
                if (match(TokenType.PARENTH_O)) {
                    parseAffectation();
                    if (match(TokenType.SEMICOLON)) {
                        parseCondition();
                        if (match(TokenType.SEMICOLON)) {
                            parseAffectation();
                            if (match(TokenType.PARENTH_F)) {
                                parseInstruction();
                            } else {
                                throw new RuntimeException("Erreur dans la regle 22!!! Une parenthese fermante est expectee a ce niveau, mais vous avez: " + currentToken().getType());
                            }
                        } 
                        else {
                            throw new RuntimeException("Erreur dans la regle 22 !!! Un point virgule est expecte a ce niveau, mais vous avez: " + currentToken().getType());
                        }
                    } 
                    else {
                        throw new RuntimeException("Erreur dans la regle 22!!! Un point virgule est expecte a ce niveau, mais vous avez: " + currentToken().getType());
                    }
                } 
                else {
                    throw new RuntimeException("Erreur dans la regle 22 !!! Une parenthese ouvrante est expecte a ce niveau, mais vous avez: " + currentToken().getType());
                }
            } else if (isThisKey("while")) {
                match(TokenType.KEY_WORD);
                if (match(TokenType.PARENTH_O)) {
                    parseCondition();
                    if (match(TokenType.PARENTH_F)) {
                        parseInstruction();
                    } else {
                        throw new RuntimeException("Erreur dans la regle 22!!! Une parenthese fermante est expectee a ce niveau, mais vous avez: " + currentToken().getType());
                    }
                } 
                else {
                    throw new RuntimeException("Erreur dans la regle 22!!! Une parenthese ouvrante est expectee a ce niveau, mais vous avez: " + currentToken().getType());
                }
            } 
            else {
                throw new RuntimeException("Erreur dans la regle 22 !!! Une boucle 'for' ou 'while' sont expectees a ce niveau. Vous avez mal conçue la regle de production <iteration> mais vous avez: " + currentToken().getType());
            }
        }

        ////////////////////////// 23
        private void parseSelection() {
            System.out.println("Régle 23");
            if (isThisKey("if")) {
                match(TokenType.KEY_WORD);
                if (match(TokenType.PARENTH_O)) {
                    parseCondition();
                    if (match(TokenType.PARENTH_F)) {
                        parseInstruction();
                        parseSelectionPrime();
                    } else {
                        throw new RuntimeException("Erreur dans la regle 23 !!! Une parenthse fermante est expectee a ce niveau, mais vous avez: " + currentToken().getType());
                    }
                } 
                else {
                    throw new RuntimeException("Erreur dans la regle 23 !!! Une parenthse ouvrante est expectee a ce niveau, mais vous avez: " + currentToken().getType());
                }
            }
            else{
                throw new RuntimeException("Erreur dans la regle 23 !!! Une boucle 'if' est expectee a ce niveau. Vous avez mal conçue la regle de production <selection> mais vous avez: " + currentToken().getType());
            }
        }

        ////////////////////////// 24
        private void parseSelectionPrime() {
            System.out.println("Régle 24");
            if (isThisKey("else")) {
                match(TokenType.KEY_WORD);
                parseInstruction();
            } else {
                ////////// epsilon
            }
        }

        ////////////////////////// 25
        private void parseSaut() {
            System.out.println("Régle 25");
            if (isThisKey("return")) {
                match(TokenType.KEY_WORD);
                parseSautPrime();
            } else {
                throw new RuntimeException("Erreur dans la regle 25 !!! Un 'return' est expecte au niveau de la regle de production <saut>, mais vous avez: " + currentToken().getType());

            }
        }

        ////////////////////////// 26
        private void parseSautPrime() {
            System.out.println("Régle 26");
            if (match(TokenType.SEMICOLON)) {

            } else if (typeCurrentToken(TokenType.IDENTIF) || typeCurrentToken(TokenType.CONST_INT)
                    || typeCurrentToken(TokenType.PARENTH_O) || typeCurrentToken(TokenType.BINARY_OPERATOR)) {
                errorDeclared();
                parseExpression();
                if (match(TokenType.SEMICOLON)) {
                } else {
                    throw new RuntimeException("Erreur dans la regle 26 !!! Un identificateur est expecte a ce niveau, mais vous avez: " + currentToken().getType());
                }
            } 
            else {
                throw new RuntimeException("Erreur dans la regle 26 !!! Un point virgule ou un identificateur sont expectes a ce niveau. Vous avez mal conçue la regle de production <saut'> mais vous avez: " + currentToken().getType());
            }
        }

        ////////////////////////// 27
        private void parseAffectation() {
            System.out.println("Régle 27");
            parseVariable();
            if (match(TokenType.AFFECT)) {
                parseExpression();
            } else {
                throw new RuntimeException(" Erreur dans la regle 27 !!! Une affectation est expectee a ce niveau, mais vous avez: " + currentToken().getType());

            }
        }

        ////////////////////////// 28
        private void parseBloc() {
            System.out.println("Régle 28");
            if (match(TokenType.CBRACKET_O)) {
                parseListeInstructions();
                if (match(TokenType.CBRACKET_F)) {
                } else {
                    throw new RuntimeException("Erreur dans la regle 28!!! Une crochet fermante est expectee a ce niveau, mais vous avez: " + currentToken().getType());
                }
            } 
            else {
                throw new RuntimeException(" Erreur dans la regle 28!!! Une crochet ouvrante est expectee a ce niveau. Vous avez mal conçue la regle de production <bloc> mais vous avez: " + currentToken().getType());
            }

        }

        ////////////////////////// 30
        private void parseExpression() {
            System.out.println("Régle 30");
            if (match(TokenType.PARENTH_O)) {
                parseExpression();
                if (match(TokenType.PARENTH_F)) {
                    parseExpressionPrime();
                } else {
                    throw new RuntimeException("Erreur dans la regle 30!!! Une parenthese fermante est expectee a ce niveau, mais vous avez: " + currentToken().getType());

                }
            } else if (match(TokenType.BINARY_OPERATOR)) {
                parseExpression();
                parseExpressionPrime();
            } else if (typeCurrentToken(TokenType.IDENTIF)) {
                errorDeclared();
                match(TokenType.IDENTIF);
                parseExpressionDoublePrime();
            } else if (match(TokenType.CONST_INT)) {
                parseExpressionPrime();
            } else {
                throw new RuntimeException("Erreur dans la regle 30!!! Une parenthese ouvrante ou un operateur binaire ou un identificateur ou une constante de type 'int' sont expectes a ce niveau. Vous avez mal concu la regle de production <expression> mais vous avez: " + currentToken().getType());

            }
        }

        ////////////////////////// 31
        private void parseExpressionPrime() {
            System.out.println("Régle 31");
            if (match(TokenType.BINARY_OPERATOR)) {
                parseExpression();
                parseExpressionPrime();
            } else {
                ///// : epsilon
            }
        }

        ////////////////////////// 32
        private void parseExpressionDoublePrime() {
            System.out.println("Régle 32");
            if (typeCurrentToken(TokenType.BRACKET_O) || typeCurrentToken(TokenType.BINARY_OPERATOR)) {
                parseVariablePrime();
                parseExpressionPrime();
            } else if (match(TokenType.PARENTH_O)) {
                parseListeExpressions();
                if (match(TokenType.PARENTH_F)) {
                    parseExpressionPrime();
                } else {
                    throw new RuntimeException("Erreur dans la regle 32!!! Une parenthese fermante est expectee a ce niveau, mais vous avez: " + currentToken().getType());

                }
            } else {
                throw new RuntimeException("Erreur dans la regle 32!!! Une parenthese fermante est expectee a ce niveau, mais vous avez: " + currentToken().getType());

            }
        }

        ////////////////////////// 33
        private void parseListeExpressions() {
            System.out.println("Régle 33");
            if (typeCurrentToken(TokenType.IDENTIF) || typeCurrentToken(TokenType.CONST_INT)
                    || typeCurrentToken(TokenType.PARENTH_O) || typeCurrentToken(TokenType.BINARY_OPERATOR)) {
                parseExpression();
                parseListeExpressionsPrime();
            }
        }

        ////////////////////////// 34
        private void parseListeExpressionsPrime() {
            System.out.println("Régle 34");
            if (match(TokenType.COMMA)) {
                parseExpression();
                parseListeExpressionsPrime();
            } else {
                ////////// epsilon
            }
        }

        ////////////////////////// 35 : <chaine>

        ////////////////////////// 36
        private void parseCondition() {
            boolean condExp = false;
            int index2 = currentIndex;
            System.out.println("Régle 36");
            if (match(TokenType.NEGATION)) {
                if (match(TokenType.PARENTH_O)) {
                    parseCondition();
                    if (match(TokenType.PARENTH_F)) {
                        parseConditionPrime();
                    } else {
                        throw new RuntimeException("Erreur dans la regle 36!!! Une parenthese fermante est expectee a ce niveau, mais vous avez: " + currentToken().getType());

                    }
                } else {
                    throw new RuntimeException("Erreur dans la regle 36!!! Une parenthese ouvrante est expectee a ce niveau, mais vous avez: " + currentToken().getType());

                }
            } else if (match(TokenType.PARENTH_O)) {
                parseCondition();
                if (match(TokenType.PARENTH_F)) {
                    parseConditionPrime();
                } else {
                    condExp = true;
                }
            }
            if (condExp == true || typeCurrentToken(TokenType.IDENTIF) || typeCurrentToken(TokenType.BINARY_OPERATOR)
                    || typeCurrentToken(TokenType.CONST_INT)) {
                // System.out.println("3mal9ach");
                currentIndex = index2;
                parseExpression();
                if (match(TokenType.BINARY_COMPARATOR)) {
                    // System.out.println("3mal9ach2");
                    parseExpression();
                    parseConditionPrime();
                } else {
                    throw new RuntimeException("Erreur dans la regle 36!!! Un operateur de comparaison est expecte a ce niveau, mais vous avez: " + currentToken().getType());

                }
            }
        }

        ////////////////////////// 37
        private void parseConditionPrime() {
            System.out.println("Régle 37");
            if (match(TokenType.DOUBLE_AND) || match(TokenType.DOUBLE_OR)) {
                parseCondition();
                parseConditionPrime();
            } else {
                // epsilon
            }
        }
    }