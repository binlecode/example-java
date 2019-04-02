/**
 * Created by ble on 3/10/14.
 */

enum Weekday {
    Sunday(8), Monday(0), Tuesday(1), Wednesday(2), Thursday(4), Friday(6), Saturday(10);

    final int fun;

    Weekday(int i) {
        this.fun = i;
    }

    public int getFunLevel() {
        if (this.name().equals("Sunday")) {   // match by name
            return fun * 10;
        } else if (this == Saturday) {  // match by reference
            return fun * 5;
        } else if (this.equals(Friday)) {  // match by equals
            return fun * 2;
        }
        return fun;
    }

    @Override
    public String toString() {
        return this.name() + " - fun: " + this.getFunLevel();
    }
}


public class TestEnum {

    public static void main(String[] args) {

        System.out.println("Weekday Monday fun level: " + Weekday.Monday.getFunLevel());
        System.out.println("Weekday sunday: " + Weekday.Sunday);
        System.out.println("Weekday Saturday: " + Weekday.Saturday);
        System.out.println("Weekday Friday: " + Weekday.Friday);

    }


}
