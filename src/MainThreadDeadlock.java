public class MainThreadDeadlock {

    public static void main(String[] args)  {
        deadlock();
    }

    public synchronized static void deadlock() {
        try {
            Thread t = new Thread(MainThreadDeadlock::deadlock);
            t.start();
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
