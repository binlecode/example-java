
/* hello.java

*/


import java.util.Map;
import java.util.Properties;
import java.util.Stack;

class TestString {

    public static void main(String args[]) {
        System.out.println("hello world");
        
        String str = "this is a normal string isn't it";
        String strTokens[] = str.split(" ");
        
        System.out.println(" strTokens length: " + strTokens.length);
        
        Stack stack = new Stack();
        for (String stk : strTokens) {
            stack.push(stk);
        }
        
        while (!stack.isEmpty()) {
            System.out.println(stack.pop());    
        }
            

        
    }

}
