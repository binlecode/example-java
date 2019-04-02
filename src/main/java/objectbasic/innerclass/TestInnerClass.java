package objectbasic.innerclass;

/**
 *
 */
public class TestInnerClass {
    static String type;

    String name;

    public TestInnerClass(String name) {
        this.name = name;
    }

    public NonStaticInner getNewNonStaticInnerInstance(String innerName) {
        return new NonStaticInner(innerName);
    }


    public static void main(String[] args) {

        TestInnerClass tic = new TestInnerClass("TIC instance");
        NonStaticInner nsi1 = tic.getNewNonStaticInnerInstance("nsi thing 1");
        NonStaticInner nsi2 = tic.getNewNonStaticInnerInstance("nsi thing 2");

        nsi1.callOuterInstanceName();
        nsi2.callOuterInstanceName();

        nsi1.showThis();

        TestInnerClass.StaticInner.callOuterStaticType();
    }

    // non static inner class instance holds an implicit awareness (context binding) of outer class instance content
    private class NonStaticInner {
        String innnerName;

        public NonStaticInner(String name) {
            this.innnerName = name;
        }

        public void callOuterInstanceName() {
            System.out.println("inner Name: " + innnerName + " calling outer instance name: " + name);
        }

        public void showThis() {
            // 'this' always point to the current instance of the corresponding class
            System.out.println("inner this: " + this.getClass().getName());
            System.out.println("outer this: " + TestInnerClass.this.getClass().getName());
        }
    }

    // static inner class is more commonly called as nested class


    // static inner class holds the binding to outer class
    // static inner class instance has NO binding to outer class instance content
    private static class StaticInner {

        // static inner class can not have static fields

        static void callOuterStaticType() {
            type = "TestInnner";
            System.out.println("outer static type: " + type);
        }


    }

}
