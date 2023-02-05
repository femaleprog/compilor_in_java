import java.util.*;
import java.io.*;


public class Main {
    public static void main(String[] args)throws IOException {
        //int main() {int t[10];int i;int som;int res;som=0;t[0]=0;    for (i=1; i<10; i=i+1) {som= som+t[i-1];t[i]=som;}res= t[i-1]; }
        Lexer lexer = new Lexer("script.txt");
        lexer.tokenize();
        ArrayList<Token> tokens = lexer.getTokens();
        for (Token token : tokens) {
            System.out.println("Type: " + token.getType() + " Value: " + token.getValue());
        }
        System.out.println(tokens.size());
        Parser par=new Parser(tokens,lexer.getKeyWords(),lexer.getIdentifiers());
        try{
            par.parse();
            
        }catch(RuntimeException e){
            
            System.out.print(e); System.out.print(par.currentIndex);

        }


        System.out.println("\nlise des variables");
        for (Map.Entry<Integer,Variable> m : par.variables.entrySet()) {
            
                System.out.println(m.getValue().varType+ " "+m.getValue().varValue);
        }

        System.out.println("\nlise des tables");
        for (Map.Entry<Integer,Table> m : par.tables.entrySet()) {
            
                System.out.print("\n"+(m.getValue()).tabType+ "  "+(m.getValue()).length+"    values: ");
                for(int i=0; i<m.getValue().length;i++)System.out.print("  "+(m.getValue()).cases[i]);
        }
        
    }
}
