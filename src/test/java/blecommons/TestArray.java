package blecommons;



class TestArray {
    
    public static void linearSearch(int[] intArr, int target) {
        for (int i : intArr) {
            System.out.println("intArr element: " + i);
            if (i == target) {
                System.out.println("==> found target: " + target);
                break;
            }
        }
        
            
        
    }
    
    
    public static void main(String[] args) {
        System.out.println("testing array");
        
        int[] intArr = new int[20]; // set length and jvm assigns default 0 value to each. 
        
        int[] intArr2 = new int[] {12, 13, 14}; // directly initialize an array with values
        
        int[] intArr3 = {1,2,3};
        
        linearSearch(intArr2, 13);
        
        
        
        
            
        
    }
}
