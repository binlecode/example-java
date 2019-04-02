package objectbasic;
/*

Default methods and abstract methods in interfaces are inherited like instance methods.
However, when the supertypes of a class or interface provide multiple default methods with
the same signature, the Java compiler follows inheritance rules to resolve the name conflict.

These rules are driven by the following two principles:
 1) Instance methods are preferred over interface default methods.
 
 */

class Horse {
    public String showMyself() {
        return "I am a horse.";
    }
}

interface Flyer {
    default String showMyself() {
        return "I am able to fly.";
    }
}

interface Mythical {
    default String showMyself() {
        return "I am a mythical creature.";
    }

}

class Pegasus extends Horse implements Flyer, Mythical {

    public static void main(String[] args) {
        Pegasus pegasus = new Pegasus();
        System.out.println(pegasus.showMyself());   // "I am a horse."
    }

}

/*
Rule #2:
 2) Methods that are already overridden by other candidates are ignored. This circumstance can arise when
    supertypes share a common ancestor.
 */

interface QuickFlyer extends Flyer {
    @Override
    default String showMyself() {
        return "I fly fast.";
    }
}

interface SlowFlyer extends Flyer {

}

// diamond inheritance here, no compilation error as one branch (objectbasic.QuickFlyer) has overridden default method
class Peg implements QuickFlyer, SlowFlyer {

    public static void main(String[] args) {
        Peg peg = new Peg();
        System.out.println(peg.showMyself());   // "I fly fast."
    }
}

/*
In a general diamond inheritance case where multiple interface default methods share same method signature,
the subtype (class or interface) has to override that method and designate which super's method is referenced.
 */

class Peggy implements Flyer, Mythical {

    // overriding is a must otherwise compilation error
    @Override
    public String showMyself() {
        // explicitly direct super's method to avoid ambiguity
        // here 'super' is NOT used as a super class constructor, but a keyword to follow a specific interface
        return "objectbasic.Flyer: " + Flyer.super.showMyself() + ", objectbasic.Mythical: " + Mythical.super.showMyself();
    }

    public static void main(String[] args) {
        Peggy peggy = new Peggy();
        System.out.println(peggy.showMyself());
    }
}
