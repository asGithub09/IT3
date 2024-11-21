// ClassA
class ClassA {
  private String varA;

  // Parameterized constructor
  public ClassA(String varA) {
      this.varA = varA;
      System.out.println("ClassA Constructor called.");
  }

  // Copy constructor
  public ClassA(ClassA other) {
      this.varA = other.varA;
  }

  public String getVarA() {
      return varA;
  }

  public void setVarA(String varA) {
      this.varA = varA;
  }
}

// ClassB
class ClassB extends ClassA {
  protected String varB;

  // Parameterized constructor
  protected ClassB(String varA, String varB) {
      super(varA); 
      this.varB = varB;
      System.out.println("ClassB Constructor called.");
  }

  // Private constructor 
  private ClassB(String varB) {
      super(""); // Placeholder
      this.varB = varB;
  }

  // Copy constructor
  public ClassB(ClassB other) {
      super(other); // Copy constructor of ClassA
      this.varB = other.varB;
  }
}

// ClassC
class ClassC extends ClassB {
  public String varC;

  // Parameterized constructor
  public ClassC(String varA, String varB, String varC) {
      super(varA, varB); 
      this.varC = varC;
      System.out.println("ClassC Constructor called.");
  }

  // Copy constructor
  public ClassC(ClassC other) {
      super(other); // Copy constructor of ClassB
      this.varC = other.varC;
  }

  // Synchronized method to display all instance variables
  public synchronized void displayVariables() {
      System.out.println("varA (from ClassA): " + getVarA());
      System.out.println("varB (from ClassB): " + varB);
      System.out.println("varC (from ClassC): " + varC);
  }
}

// Main class 
public class MultiClassProgram {
  public static void main(String[] args) {
      // Creating an object of ClassC
      ClassC obj1 = new ClassC("ValueA", "ValueB", "ValueC");

      // Display variables
      obj1.displayVariables();

      // Creating a copy of obj1
      ClassC obj2 = new ClassC(obj1);

      // Modifying the copy to check deep copy
      obj2.setVarA("NewValueA");
      obj2.varB = "NewValueB";
      obj2.varC = "NewValueC";

      System.out.println("\nOriginal Object:");
      obj1.displayVariables();

      System.out.println("\nCopied Object:");
      obj2.displayVariables();
  }
}
