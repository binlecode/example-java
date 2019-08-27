

enum TestEnum {
    VAL1("this is the value 1", 45),
    VAL2("THIS IS THE VALUE 2", 27);
    
    private final String value;
    private final int size;
    
    TestEnum(String val, int siz) {
        this.value = val;
        this.size = siz;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public static void main(String[] args) {
        for (TestEnum te : TestEnum.values()) {
            System.out.println("test enum: " + te.getValue() + " -> " + te.getSize());
        }
    }
            
        
        
        

}
