package concurrent.queue;


/**
 * This is the generics wrapper class for queue unit.
 * This class is useful for adding job or other metadata without changing the T class.
 *
 * @param <T>
 */
public class Appointment<T extends Pet> {
    private final T toBeChecked;

    Appointment(T toBeChecked) {
        this.toBeChecked = toBeChecked;
    }

    public T getPatient() {
        return toBeChecked;
    }
}
