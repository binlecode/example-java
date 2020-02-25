package objectbasic;

import java.util.List;
import java.util.ArrayList;

class TestSubClass {

    class Animal {
        String name;

        public Animal(String name) {
            this.name = name;
        }

        public void bark() {
            System.out.println("animal bark");
        }
    }

    
    class Cat extends Animal {
        public Cat(String name) {
            super(name);
        }

        public void bark() {
            System.out.println("cat '" + this.name + "': miao!");
        }
    }
            
    public static void main(String[] args) {
        TestSubClass tsc = new TestSubClass();
        
        Cat cat1 = tsc.new Cat("kitty");
        
        List<Animal> animals = new ArrayList<Animal>();
        animals.add(cat1);
        
        animals.get(0).bark();
        
    }


}
