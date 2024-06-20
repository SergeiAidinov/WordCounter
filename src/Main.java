public class Main {

    public static final String regExp ="[A-Z]";
    public static final int IGNORE_LENGTH = 3;
    public static final int QUANTITY_MAX_OFTEN_WORDS = 10;

    public static void main(String[] args) {
        System.out.println("Counting words...");
        long start = System.currentTimeMillis();
        System.out.println(WorkFlow.instance().countWords());
        System.out.println("IN: " + (System.currentTimeMillis() - start) + " ms.");
    }
}