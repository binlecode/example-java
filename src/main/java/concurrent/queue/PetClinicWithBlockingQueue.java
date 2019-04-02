package concurrent.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Now the Pet clinic veterinarian workflow
 */
class Veterinarian extends Thread {

    /**
     * each veterinarian has a working queue
     */
    protected final BlockingQueue<Appointment<Pet>> appointments;

    protected final String title;

    /**
     * rest time between appointments
     */
    protected final int restTime;

    private boolean shutdown = false;

    Veterinarian(BlockingQueue<Appointment<Pet>> appointments, String title, int pauseTime) {
        this.appointments = appointments;
        this.title = title;
        this.restTime = pauseTime;
    }

    public synchronized void setShutdown() {
        this.shutdown = true;
    }

    @Override
    public void run() {
        while (!shutdown) {
            seePatient();
            try {
                System.out.println("Veterinarian (" + title + "): ... let's have a rest ...");
                Thread.sleep(restTime);
            } catch (InterruptedException ex) {
                shutdown = true;
            }
        }
    }

    /**
     * The thread will dequeue appointments and examine the pets corresponding to each in turn,
     * and will block if there are no appointments currently waiting on the queue.
     */
    public void seePatient() {
        try {
            System.out.println("Veterinarian (" + title + "): work on appointment");
            Appointment<Pet> appt = appointments.take();  // this is a blocking call when queue is already empty
            Pet patient = appt.getPatient();
            patient.healthCheck();
        } catch (InterruptedException ex) {
            shutdown = true;
        }
    }
}

/**
 * Now the front desk that gets phone calls and set appointments
 */
class FrontDesk extends Thread {
    protected final BlockingQueue<Appointment<Pet>> appointments;

    // Internally, the atomic classes make heavy use of compare-and-swap (CAS), an atomic instruction
    // directly supported by most modern CPUs. Those instructions usually are much faster than
    // synchronizing via locks.
    // So the advice is to prefer atomic classes over locks in case you just have to change a single
    // mutable variable concurrently.

    /**
     * a global sequence to index appointments
     */
    protected AtomicLong patientIndex = new AtomicLong(0);

    protected volatile boolean isClosed = false;

    /**
     * interval between appointment bookings
     */
    final int bookInterval;

    public FrontDesk(BlockingQueue<Appointment<Pet>> appointments, int bookInterval) {
        this.appointments = appointments;
        this.bookInterval = bookInterval;
    }

    @Override
    public void run() {
        while (!isClosed) {

            long idx = patientIndex.incrementAndGet();  // atomic increment and get
            if (idx % 2 == 0) {
                bookAppointmentForPatient(new Cat("Sick-Cat-" + idx));
            } else {
                bookAppointmentForPatient(new Dog("Sick-Dog-" + idx));
            }

            try {
                System.out.println("FrontDesk: ... silence between patient calls ...");
                Thread.sleep(bookInterval);
            } catch (InterruptedException e) {
                isClosed = true;
            }
        }
    }

    public void bookAppointmentForPatient(Pet patient) {
        System.out.println("FrontDesk: current queue vacancy: " + appointments.remainingCapacity());
        System.out.println("FrontDesk: try to add appointment to the queue for Pet: " + patient.name);

        // queue.offer basically drops the unit when queue is full

        if (appointments.offer(new Appointment<>(patient))) {
            System.out.println("FrontDesk: your appointment is confirmed!");
        } else {
            System.out.println("FrontDesk: queue is full, sorry, try next time!");
        }
        // queue.add raises exception when queue is full
        /*
        try {
            appointments.add(new Appointment<>(patient));
            System.out.println("FrontDesk: your appointment is confirmed!");
        } catch (IllegalStateException ex) {  // exp for queue being full
            System.out.println("FrontDesk: queue is full, sorry, try next time!");
        }
        */
    }

}


class DemoForBlockingQueue {

    public static void main(String[] args) {

        BlockingQueue<Appointment<Pet>> appointments = new LinkedBlockingDeque<>(4);  // set capacity

        FrontDesk fd = new FrontDesk(appointments, 100); // appointment booking interval is short than clinic rest interval => queue overflow

        Veterinarian vt1 = new Veterinarian(appointments, "Quick Clinic", 800);

        Veterinarian vt2 = new Veterinarian(appointments, "Slow Clinic", 1200);

        fd.start();

        vt1.start();
        vt2.start();
    }


}


