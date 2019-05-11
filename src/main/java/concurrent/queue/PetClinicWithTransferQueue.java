package concurrent.queue;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Now the Pet clinic veterinarian workflow
 */
class Veterinary extends Thread {

    /**
     * each veterinarian has a working queue
     */
    protected final TransferQueue<Appointment<Pet>> appointments;

    protected final String title;

    /**
     * rest time between appointments
     */
    protected final int restTime;

    private boolean shutdown = false;

    Veterinary(TransferQueue<Appointment<Pet>> appointments, String title, int pauseTime) {
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
                System.out.println("Veterinary (" + title + "): ... let's have a rest ...");
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
            System.out.println("Veterinary (" + title + "): work on appointment");
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
class CallCenter extends Thread {
    protected final TransferQueue<Appointment<Pet>> appointments;

    /**
     * a global sequence to index appointments
     */
    protected AtomicLong patientIndex = new AtomicLong(0);

    /**
     * a global flag for the open/close status of the call center
     */
    protected volatile boolean isClosed = false;

    /**
     * interval between appointment bookings
     */
    final int bookInterval;

    public CallCenter(TransferQueue<Appointment<Pet>> appointments, int bookInterval) {
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
                System.out.println("CallCenter: ... silence between patient calls ...");
                Thread.sleep(bookInterval);
            } catch (InterruptedException e) {
                isClosed = true;
            }
        }
    }

    public void bookAppointmentForPatient(Pet patient) {
        System.out.println("CallCenter: try to add appointment to the queue for Pet: " + patient.name);

        // transferQueue.transfer() tries to pass the queue item to a consumer that is waiting,
        // otherwise it will block until a consumer
        /*
        try {
            appointments.transfer(new Appointment<>(patient));
            System.out.println("CallCenter: your appointment is confirmed and being worked on now!");
        } catch (InterruptedException e) {
            isClosed = true;
        }
        */

        // transferQueue.tryTransfer() keeps trying for a defined time window, it gives up and returns false
        // when there's no consumer available when time window expires

        try {
            // change the timeout to a larger value to allow more waiting time for available consumer, and
            // see the transfer failure drops.
            // on the other hand, put a small value will cause more failed en-queuing

            if (appointments.tryTransfer(new Appointment<>(patient), 100, TimeUnit.MILLISECONDS)) {
                System.out.println("CallCenter: your appointment is confirmed and being worked on now!");
            } else {
                System.out.println("CallCenter: we can not take any more appointments for now, sorry, try later!");
            }
        } catch (InterruptedException e) {
            isClosed = true;
        }

    }

}


class DemoForTransferQueue {

    public static void main(String[] args) {

        TransferQueue<Appointment<Pet>> appointments = new LinkedTransferQueue<>();  // LinkedTransferQueue is unbounded

        CallCenter cc = new CallCenter(appointments, 300); // appointment booking interval is short than clinic rest interval => queue overflow

        Veterinary vt1 = new Veterinary(appointments, "Quick Clinic", 800);

        Veterinary vt2 = new Veterinary(appointments, "Slow Clinic", 1200);

        cc.start();

        vt1.start();
        vt2.start();
    }


}


