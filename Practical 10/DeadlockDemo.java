public class DeadlockDemo {

    static class Account {
        private final int id;
        private int balance;

        public Account(int id, int balance) {
            this.id = id;
            this.balance = balance;
        }

        public int getId() {
            return id;
        }

        public int getBalance() {
            return balance;
        }
    }

    // 1. Deadlock-prone transfer: Locks in the order of arguments (opposite locks cause deadlock)
    public static void transferDeadlock(Account from, Account to, int amount) {
        synchronized (from) {
            System.out.println(Thread.currentThread().getName() + " locked Account " + from.getId());
            try {
                // Sleep briefly to ensure the other thread acquires the second lock
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println(Thread.currentThread().getName() + " waiting for lock on Account " + to.getId() + "...");
            synchronized (to) {
                from.balance -= amount;
                to.balance += amount;
                System.out.println(Thread.currentThread().getName() + " completed transfer of " + amount);
            }
        }
    }

    // 2. Safe transfer: Consistent lock ordering prevents deadlock completely
    public static void transferSafe(Account from, Account to, int amount) {
        Account firstLock = from.getId() < to.getId() ? from : to;
        Account secondLock = from.getId() < to.getId() ? to : from;

        synchronized (firstLock) {
            System.out.println(Thread.currentThread().getName() + " locked Account " + firstLock.getId());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            synchronized (secondLock) {
                System.out.println(Thread.currentThread().getName() + " locked Account " + secondLock.getId());
                from.balance -= amount;
                to.balance += amount;
                System.out.println(Thread.currentThread().getName() + " safely transferred " + amount +
                        " from " + from.getId() + " to " + to.getId());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Account accA = new Account(1, 1000);
        Account accB = new Account(2, 2000);

        System.out.println("=== Demonstrating Deadlock Prevention using Consistent Lock Order ===");

        // Run safe version
        Thread t1 = new Thread(() -> transferSafe(accA, accB, 200), "Thread-1 (A->B)");
        Thread t2 = new Thread(() -> transferSafe(accB, accA, 300), "Thread-2 (B->A)");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("\nAll safe transfers completed without deadlock!");
        System.out.println("Final Account 1 balance: " + accA.getBalance());
        System.out.println("Final Account 2 balance: " + accB.getBalance());

        System.out.println("\n(Note: The deadlocked version with opposite locking order would hang indefinitely.)");
    }
}

