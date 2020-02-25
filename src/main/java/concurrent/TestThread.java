package concurrent;

class TestThread extends Thread {
    public void run() {
        for (int i=0; i<100; i++) {
            System.out.println("saying hello in a thread, count " + i);
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
                // do nothing
            }
        }
        
    }

    public static void main(String[] args) {
        TestThread tt = new TestThread();
        System.out.println("thread max priority: "+tt.MAX_PRIORITY);
        System.out.println("thread min priority: "+tt.MIN_PRIORITY);
        tt.start();
        
        
    }
        

}


