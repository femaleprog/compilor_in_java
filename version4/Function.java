class Function extends Token{
    public String funType;
    public Function(TokenType type,Object value,String funType){
        super(type,value);
        this.funType=funType;
    }


}