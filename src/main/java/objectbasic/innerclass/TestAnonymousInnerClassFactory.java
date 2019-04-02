package objectbasic.innerclass;

/**
 * Created by ble on 3/8/14.
 */


interface MyService {
    void fooService();
}

interface MyServiceFactory {
    MyService getMyServiceInstance();
}

class MyServiceImpl implements MyService {

    private MyServiceImpl() {
    }

    // use anonymous inner class for static factory interface implementation
    public static MyServiceFactory factory = new MyServiceFactory() {
        public MyService getMyServiceInstance() {
            // we can do either singleton or prototype instantiation
            return new MyServiceImpl();
        }
    };

    public void fooService() {
        System.out.println("running foo service");
    }

}

public class TestAnonymousInnerClassFactory {

    public static void main(String[] args) {

        MyServiceImpl.factory.getMyServiceInstance().fooService();

    }

}
