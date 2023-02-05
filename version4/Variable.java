import java.lang.ProcessBuilder.Redirect.Type;
import java.util.*;
import java.util.Stack;
import java.io.*;

class Variable extends Token {
    public String  varType;
    public Object varValue="";



    public Variable(TokenType type,Object value,String varType){
        super(type,value);
        this.varType=varType;
    }
}