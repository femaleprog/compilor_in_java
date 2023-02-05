class SyntaxeException extends Exception {
    public SyntaxeException(String message) {
        System.out.println(message);
    }
}

class LexicalException extends Exception {
    public LexicalException(String message) {
        System.out.println(message);
    }
}


class SemanticException extends Exception {
    public SemanticException(String message) {
        System.out.println(message);
    }
}