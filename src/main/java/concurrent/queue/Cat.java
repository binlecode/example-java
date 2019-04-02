package concurrent.queue;

public class Cat extends Pet {

    public Cat(String name) {
        super(name);
    }

    public void healthCheck() {
        System.out.println("Meow! from " + this.name);
    }
}
