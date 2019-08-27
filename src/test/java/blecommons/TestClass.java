
import java.util.Map;

class TestClass {
    int varInt;
    double varDbl;
    String varStr;
    Map varMap;
    
    public int getVarInt() { return this.varInt; }
    public double getVarDbl() { return this.varDbl; }
    public String getVarStr() { return this.varStr; }
    
    
    
    
    public static void main(String[] args) {
        // Default constructor assigns default values to primitive 
        // fields and null to reference fields.
        TestClass tc = new TestClass();
        assert tc.getVarInt() == 0;
        assert tc.getVarStr() == null;
    
    }
    
    

}
