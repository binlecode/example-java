import java.util.List;
import java.util.ArrayList;

class TestSubClass {

    class Animal {
        String tag;
        void setTag(String tag) {
            this.tag = tag;
        }
        
        public void bark() {
            System.out.println("animal bark");
        }
    }

    
    class Cat extends Animal {
        

        public void bark() {
            System.out.println("cat miao!");
        }
    }
            
    public static void main(String[] args) {
        TestSubClass tsc = new TestSubClass();
        
        Cat cat1 = tsc.new Cat();
        
        List<Animal> animals = new ArrayList<Animal>();
        animals.add(cat1);
        
        animals.get(0).bark();
        
    }


}
