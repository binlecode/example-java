package objectbasic;

/**
 * Created by ble on 3/10/14.
 */

class Foo {

    private Foo() {
    }  // this makes it not publically instantiable

}

class Bar {
    public static final String LABEL = "BAR!!!";

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}


public class TestClassClass {


    public static void main(String[] args) {
        Class fooClass = Foo.class;
        System.out.println("foo class name: " + fooClass.getCanonicalName());

        try {
            // this will throw exception because constructor is private
            System.out.println("foo Class name: " + fooClass.newInstance().getClass());
        } catch (Exception e) {
            System.out.println("got exception: " + e.getMessage());
        }

        try {
            // Class.forName is a common way of loading class at run time
            Class barClass = Class.forName("Bar");
            // this wont' throw exception because default constructor is provided by compiler
            // but newInstance ALWAYS calls empty constructor
            // and it returns Object type that has to be down casted to specific type
            Bar bar = (Bar) barClass.newInstance();
            // therefore, bar.name is still null at this point
            System.out.println("bar name: " + bar.getName());
            System.out.println("bar label: " + Bar.LABEL);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
