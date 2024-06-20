public class Main {

    public static final String regExp ="[A-Z]";
    public static final int IGNORE_LENGTH = 3;

    public static void main(String[] args) {
        System.out.println("Hello and welcome!");
        WorkFlow.instance().countWords();
    }
}