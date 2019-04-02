package concurrent.queue;

public class Dog extends Pet {

    public Dog(String name) {
        super(name);
    }

    @Override
    public void healthCheck() {
        System.out.println("Woof! from " + this.name);
    }
}