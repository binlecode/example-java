package objectbasic;

import java.io.*;
import java.util.Date;
import java.util.Hashtable;

/**
 * Created by ble on 3/20/14.
 */


class SerializableObj implements Serializable {
    private static final long serialVersionUID = 1L;
    transient Date date;

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();

        // here is custom initialization process
        System.out.println(">> initializing object during object read ...");
//        date = new Date();
//        System.out.println("set date to " + date);
    }

//    @Override
//    public String toString() {
//        return "SerializableObj instance " + this + "]: " + (date == null ? "null" : date.toString());
//    }
}


public class TestObjectSerialization {

    public static void main(String[] args) {

        Hashtable<String, Object> hash = new Hashtable<String, Object>(); // Hashtable has implemented Serializable interface

        /*
         * One of the most important (and tricky) things about serialization is that when an object
         * is serialized, any object references it contains are also serialized. Serialization can capture
         * entire “graphs” of interconnected objects and put them back together on the receiving
         * end (we’ll demonstrate this in an upcoming example). The implication is that any object
         * we serialize must contain only references to other Serializable objects. We can prune
         * the tree and limit the extent of what is serialized by marking nonserializable variables
         * as transient or overriding the default serialization mechanisms
         */

        hash.put("string", "A Great Lake");
        hash.put("int", new Integer(123));
        hash.put("double", new Double(Math.PI));


        try {
            System.out.println("Serializing object: " + hash);
            FileOutputStream fileOutputStream = new FileOutputStream("hash.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(hash);
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fileInputStream = new FileInputStream("hash.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Hashtable dsHash = (Hashtable) objectInputStream.readObject();
            objectInputStream.close();
            System.out.println("Deserialized object: " + dsHash);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        SerializableObj so = new SerializableObj();

        try {
            System.out.println("Serializing object: " + so);
            System.out.println("Serializing object hashCode: " + so.hashCode());
            FileOutputStream fos = new FileOutputStream("so.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(so);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fis = new FileInputStream("so.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            SerializableObj dsSo = (SerializableObj) ois.readObject();
            ois.close();
            System.out.println("Deserialized object: " + dsSo);
            System.out.println("Deserialized object hashCode: " + dsSo.hashCode());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

}
