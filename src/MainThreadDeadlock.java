public class MainThreadDeadlock {
    public static final Object LOCK1 = new Object() {
        @Override
        public String toString() {
            return "lock1";
        }
    };
    public static final Object LOCK2 = new Object() {
        @Override
        public String toString() {
            return "lock2";
        }
    };

    public static void main(String[] args) {
        deadLock(LOCK1, LOCK2);
        deadLock(LOCK2, LOCK1);
    }

    private synchronized static void deadLock() {
        try {
            Thread t = new Thread(MainThreadDeadlock::deadLock);
            t.start();
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deadLock(Object lock1, Object lock2) {
        new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Holding " + lock1);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (lock2) {
                    System.out.println("Holding " + lock2);
                }
            }
        }).start();
    }
}
