import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConcurrentModificationDemo {
    public static void main(String[] args) {
        System.out.println("Demonstrating ConcurrentModificationException...");
        demonstrateConcurrentModificationException();

        System.out.println("\nResolving with CopyOnWriteArrayList...");
        resolveWithCopyOnWriteArrayList();

        System.out.println("\nResolving with Explicit Synchronization...");
        resolveWithSynchronization();
    }

    // Demonstrate the issue using a standard ArrayList
    private static void demonstrateConcurrentModificationException() {
        List<Integer> sharedList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            sharedList.add(i);
        }

        Thread reader = new Thread(() -> {
            try {
                Iterator<Integer> iterator = sharedList.iterator();
                while (iterator.hasNext()) {
                    System.out.println("Reader Thread: " + iterator.next());
                    Thread.sleep(100); // Simulate delay
                }
            } catch (Exception e) {
                System.err.println("Exception in Reader Thread: " + e.getClass().getName());
            }
        });

        Thread writer = new Thread(() -> {
            try {
                Thread.sleep(150); // Ensure reader starts first
                sharedList.add(100);
                System.out.println("Writer Thread: Added 100 to the list.");
            } catch (Exception e) {
                System.err.println("Exception in Writer Thread: " + e.getClass().getName());
            }
        });

        reader.start();
        writer.start();

        try {
            reader.join();
            writer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Resolve the issue using CopyOnWriteArrayList
    private static void resolveWithCopyOnWriteArrayList() {
        List<Integer> sharedList = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 5; i++) {
            sharedList.add(i);
        }

        Thread reader = new Thread(() -> {
            for (Integer number : sharedList) {
                System.out.println("Reader Thread: " + number);
                try {
                    Thread.sleep(100); // Simulate delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread writer = new Thread(() -> {
            try {
                Thread.sleep(150); // Ensure reader starts first
                sharedList.add(100);
                System.out.println("Writer Thread: Added 100 to the list.");
            } catch (Exception e) {
                System.err.println("Exception in Writer Thread: " + e.getClass().getName());
            }
        });

        reader.start();
        writer.start();

        try {
            reader.join();
            writer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Resolve the issue using Explicit Synchronization
    private static void resolveWithSynchronization() {
        List<Integer> sharedList = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < 5; i++) {
            sharedList.add(i);
        }

        Thread reader = new Thread(() -> {
            synchronized (sharedList) {
                Iterator<Integer> iterator = sharedList.iterator();
                while (iterator.hasNext()) {
                    System.out.println("Reader Thread: " + iterator.next());
                    try {
                        Thread.sleep(100); // Simulate delay
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        Thread writer = new Thread(() -> {
            try {
                Thread.sleep(150); // Ensure reader starts first
                synchronized (sharedList) {
                    sharedList.add(100);
                    System.out.println("Writer Thread: Added 100 to the list.");
                }
            } catch (Exception e) {
                System.err.println("Exception in Writer Thread: " + e.getClass().getName());
            }
        });

        reader.start();
        writer.start();

        try {
            reader.join();
            writer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
