package Lesson_04;

public class Task_1 {
    private final Object mon = new Object();
    private static final char[] letters = {'A','B','C'};
    private volatile char currentLetter = letters[0];

    public static void main(String[] args) {
        Task_1 task = new Task_1();
        Thread t1 = new Thread(() -> task.printChar(0));
        Thread t2 = new Thread(() -> task.printChar(1));
        Thread t3 = new Thread(() -> task.printChar(2));
        t1.start();
        t2.start();
        t3.start();
    }

    public void printChar(int pos){
        char letter = letters[pos];
        char next = letters[(pos+1) % letters.length];
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != letter) {
                        mon.wait();
                    }
                    System.out.print(letter);
                    currentLetter = next;
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
