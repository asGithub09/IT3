import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class DeadlockSimulation {
    private static final Object Resource1 = new Object();
    private static final Object Resource2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            synchronized (Resource1) {
                System.out.println("Thread 1: Locked Resource1");

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println("Thread 1: Waiting to lock Resource2...");
                synchronized (Resource2) {
                    System.out.println("Thread 1: Locked Resource2");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (Resource2) {
                System.out.println("Thread 2: Locked Resource2");

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println("Thread 2: Waiting to lock Resource1...");
                synchronized (Resource1) {
                    System.out.println("Thread 2: Locked Resource1");
                }
            }
        });
        thread1.start();
        thread2.start();

        Thread deadlockDetector = new Thread(() -> detectDeadlock());
        deadlockDetector.setDaemon(true); // Mark this thread as a daemon thread
        deadlockDetector.start();
    }

    private static void detectDeadlock() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        while (true) {
            long[] deadlockedThreadIds = threadMXBean.findDeadlockedThreads();

            if (deadlockedThreadIds != null) {
                System.out.println("\nDEADLOCK DETECTED!");
                ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(deadlockedThreadIds);
                for (ThreadInfo threadInfo : threadInfos) {
                    System.out.println("Thread Name: " + threadInfo.getThreadName());
                    System.out.println("Locked on: " + threadInfo.getLockName());
                    System.out.println("Blocked by: " + threadInfo.getLockOwnerName());
                    System.out.println();
                }

                System.out.println("Terminating program to resolve deadlock...");
                System.exit(1); 
            }

            try {
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
