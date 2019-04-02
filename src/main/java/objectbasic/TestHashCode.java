package objectbasic;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by ble on 5/7/15.
 */
public class TestHashCode {


    public static void main(String[] args) {
        HashCodeType hst = new HashCodeType("foo", 123, true, 12.34F);
        System.out.println("hash code: " + hst.hashCode());
    }


}


class HashCodeType {

    int volume;
    boolean isChecked;
    float quantity;

    String name;
    Set<HashCodeSubType> subTypes;

    public HashCodeType(String name, int volume, boolean isChecked, float quantity) {
        this.name = name;
        this.volume = volume;
        this.isChecked = isChecked;
        this.quantity = quantity;
    }

    public boolean addSubType(HashCodeSubType hashCodeSubType) {
        if (subTypes == null) {
            subTypes = new LinkedHashSet<HashCodeSubType>();
        }
        return subTypes.add(hashCodeSubType);
    }

    //todo: overloaded constructors

    // equals and hashCode should always be overridden together

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HashCodeType that = (HashCodeType) o;

        if (volume != that.volume) return false;
        if (isChecked != that.isChecked) return false;
        if (Float.compare(that.quantity, quantity) != 0) return false;
        // as object reference, name could be null
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        // collection reference could be null
        return !(subTypes != null ? !subTypes.equals(that.subTypes) : that.subTypes != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + volume;
        result = 31 * result + (isChecked ? 1 : 0);
        result = 31 * result + (quantity != +0.0f ? Float.floatToIntBits(quantity) : 0);
        result = 31 * result + (subTypes != null ? subTypes.hashCode() : 0);
        return result;
    }
}

class HashCodeSubType {
    String name;
    boolean isChecked;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HashCodeSubType that = (HashCodeSubType) o;

        if (isChecked != that.isChecked) return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (isChecked ? 1 : 0);
        return result;
    }
}
