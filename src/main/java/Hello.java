
import java.util.Map;
import java.util.Properties;
import java.util.Stack;

class Hello {

    public static void main(String args[]) {
        System.out.println("hello world");
        Map<String, String> sysEnvMap = System.getenv();
        for (String envVarName : sysEnvMap.keySet()) {
            System.out.println("system env var: " + envVarName + " -> " + sysEnvMap.get(envVarName));   
        }        
        
        System.out.println("\n-> system properties list");
        Properties sysProps = System.getProperties();
        sysProps.list(System.out);
        
        System.out.println("\n-> receive runEnv: " + sysProps.getProperty("runEnv"));


        System.out.println("\n-> system propertie " + args[0]);
        System.out.println(sysProps.getProperty(args[0]));
        
        
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
