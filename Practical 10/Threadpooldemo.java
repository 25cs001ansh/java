import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Threadpooldemo {
    public static void main(String[] arg)throws InterruptedException{
        ExecutorService pool=Executors.newFixedThreadPool(3);
            for (int i = 0; i < 10; i++) {
                final int id=i;
                pool.submit(() ->{
                    System.out.println("Thread "+id+" running on " +Thread.currentThread().getName());
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Thread "+id+" finished" );
                });
            }
            pool.shutdown();
            if(pool.awaitTermination(30,TimeUnit.SECONDS)){
                System.out.println("time out,forcing shutdown");
                pool.shutdownNow();
            }
            System.out.println("All tasks completed");
    
    }
}
