import java.lang.ProcessBuilder.Redirect.Type;
import java.util.*;
import java.util.Stack;
import java.io.*;


class Table extends Token {
    int length;
    String tabType;
    public Object[] cases;

    public Table(TokenType type,Object value,int length,String tabType){
        super(type,value);
        this.tabType=tabType;
        this.length=length;
        this.cases=new Object[length];
    }
}