package objectbasic;
// interface default method is a good example of favoring composition over inheritance
// we can build method-rich java interfaces as traits in groovy or scala, or like 'module's in ruby

// interface static methods can also replace the traditional xxx's util classes of static methods, such as Arrays, Files, etc.

public class TestInterfaceDefaultMethod {


    public static void main(String[] args) {

        Car sedan = new Car(120, 400, false);

        System.out.println("car horsepower: " + sedan.getCarHorsePower());
        sedan.horn();
        sedan.drive();

        Car suv = new Car(250, 600, true);
        System.out.println("suv horsepower: " + suv.getCarHorsePower());
        suv.horn();
        suv.drive();


        Truck truck = new Truck(400, 1000);
        // interface now can be used alone with its own static (class-level) method !
        System.out.println("truck horsepower: " + Vehicle.getHorsePower(truck.getRpm(), truck.getTorque()));
        truck.horn();
        truck.drive();

    }


    static class Car implements Vehicle {

        int rpm;
        int torque;
        boolean isSuv;

        public Car(int rpm, int torque, boolean isSuv) {
            this.rpm = rpm;
            this.torque = torque;
            this.isSuv = isSuv;
        }

        public int getCarHorsePower() {
            return Vehicle.getHorsePower(rpm, torque);  // have to call interface static method explicitly
        }

        @Override
        public void horn() {
            System.out.println("bii-bii...!");
        }

    }

    static class Truck implements Vehicle, AllWheelDrive {

        int rpm;
        int torque;
        boolean isDiesel;

        public Truck(int rpm, int torque) {
            this(rpm, torque, false);
        }

        public Truck(int rpm, int torque, boolean isDiesel) {
            this.rpm = rpm;
            this.torque = torque;
            this.isDiesel = isDiesel;
        }

        @Override
        public void drive() {  // have to override due to conflicting interface default methods
            AllWheelDrive.super.drive();  // have to call Interface.super to remove conflict
        }

        public int getRpm() {
            return rpm;
        }

        public void setRpm(int rpm) {
            this.rpm = rpm;
        }

        public int getTorque() {
            return torque;
        }

        public void setTorque(int torque) {
            this.torque = torque;
        }

        public boolean isDiesel() {
            return isDiesel;
        }

        public void setDiesel(boolean diesel) {
            isDiesel = diesel;
        }
    }

}


interface Vehicle {

    // static (default) method
    static int getHorsePower(int rpm, int torque) {
        return (rpm * torque) / 525;
    }

    default void horn() {
        System.out.println("Honk!");
    }

    default void drive() {
        System.out.println("Front wheel drive");
    }

}


interface AllWheelDrive {

    default void drive() {
        System.out.println("All wheel drive");
    }

}
