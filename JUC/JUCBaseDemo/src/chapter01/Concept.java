package chapter01;

public class Concept {

    public static void main(String[] args) {
        Thread aa = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "::" + Thread.currentThread().isDaemon());
            while (true){
                
            }
        }, "aa");
        
//        aa.setDaemon(true);
        aa.start();
        
        System.out.println(Thread.currentThread().getName() + "over");
    }
}
