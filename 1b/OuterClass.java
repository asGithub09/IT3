public class OuterClass {

    // Static variable in OuterClass
    private static int staticCounter = 0;

    // Instance variable in OuterClass
    private int instanceCounter = 0;

    // Synchronized method to demonstrate thread-safe access to static variables
    public static synchronized void incrementStaticCounter() {
        synchronized (OuterClass.class) {
            staticCounter++;
            System.out.println("Static Counter: " + staticCounter);
        }
    }

    // Overriding toString from Object class
    @Override
    public String toString() {
        return "OuterClass instanceCounter=" + instanceCounter;
    }

    /**
     * Static nested class
     */
    public static class StaticNestedClass {

        // Static block in the static nested class
        static {
            System.out.println("Static block in StaticNestedClass executed.");
        }

        // Instance block in the static nested class
        {
            System.out.println("Instance block in StaticNestedClass executed.");
        }

        // Constructor in the static nested class
        public StaticNestedClass() {
            System.out.println("Constructor in StaticNestedClass executed.");
        }

        // Access OuterClass static variables
        public void accessOuterClass() {
            System.out.println("Accessing OuterClass static variable: " + staticCounter);
        }

        public void referToOuterClass(OuterClass outer) {
            System.out.println("Using OuterClass.this: " + outer.toString());
        }
    }

    /**
     * Final nested inner class
     */
    public final class FinalNestedClass {
        // Final variable
        private final String finalVar = "This is immutable.";

        // Final method
        public final void displayFinalMethod() {
            System.out.println("Final method executed. Final variable: " + finalVar);
        }
    }

    // Static method in OuterClass
    public static void staticMethod() {
        System.out.println("Static method in OuterClass.");
        System.out.println("Static counter: " + staticCounter);
        // Cannot access instanceCounter since it is not static
        // System.out.println("Instance counter: " + instanceCounter); // Uncommenting will cause a compile-time error
    }

    /**
     * Utility class
     */
    public static final class UtilityClass {
        private UtilityClass() {
            // Prevent instantiation
        }

        public static void utilityMethod() {
            System.out.println("Utility method executed.");
        }
    }

    public static void main(String[] args) {
        // Demonstrate the static nested class
        System.out.println("Creating an instance of the static nested class:");
        StaticNestedClass nested = new StaticNestedClass();
        nested.accessOuterClass();

        // Refer to the outer class using OuterClass.this
        OuterClass outer = new OuterClass();
        nested.referToOuterClass(outer);

        // Demonstrate the final nested inner class
        System.out.println("\nDemonstrating final nested inner class:");
        FinalNestedClass finalNested = outer.new FinalNestedClass();
        finalNested.displayFinalMethod();

        // Demonstrate synchronized block
        System.out.println("\nDemonstrating synchronized block:");
        incrementStaticCounter();

        // Demonstrate static method
        System.out.println("\nDemonstrating static method:");
        staticMethod();

        // Demonstrate utility class
        System.out.println("\nDemonstrating utility class:");
        UtilityClass.utilityMethod();
    }
}
