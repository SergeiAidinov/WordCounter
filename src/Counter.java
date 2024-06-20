import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Phaser;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Counter implements Runnable {

    private final File workingFile;
    private final Pattern pattern;
    private final ConcurrentHashMap<String, Integer> totalWordsAndQuantity;
    private final Phaser phaser;

    public Counter(final Phaser phaser, final ConcurrentHashMap<String, Integer> totalWordsAndQuantity, final File workingFile) {
        this.totalWordsAndQuantity = totalWordsAndQuantity;
        this.workingFile = workingFile;
        this.phaser = phaser;
        pattern = Pattern.compile(Main.regExp, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public void run() {
        procedure();
        phaser.arriveAndDeregister();
    }

    private void procedure() {
        System.out.println(Thread.currentThread() + " started at " + System.currentTimeMillis());
        final List<String> wordList = getWordList();
        Map<String, Integer> wordsAndQuantity = new HashMap<>();
        wordList.stream().forEach(word -> wordsAndQuantity.put(word, Collections.frequency(wordList, word)));
        for (Map.Entry<String, Integer> entry : wordsAndQuantity.entrySet()){
            totalWordsAndQuantity.merge(entry.getKey(), entry.getValue(), (oldValue, newValue) -> oldValue + newValue);
        }
        System.out.println(Thread.currentThread() + " finished at " + System.currentTimeMillis());
    }

    private List<String> getWordList() {
        InputStream inputStream = null;
        byte[] bytes;
        try {
            inputStream = new FileInputStream(workingFile);
            bytes = inputStream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (Objects.nonNull(inputStream)) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.exit(1);
                }
            }
        }
        final List<String> wordList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            final char symbol = (char) b;
            if (doesMatchRegexp(symbol)) {
                stringBuilder.append(symbol);
            } else {
                if (stringBuilder.toString().length() > Main.IGNORE_LENGTH) wordList.add(stringBuilder.toString().toLowerCase());
                stringBuilder = new StringBuilder();
            }
        }
        return wordList;
    }

    private boolean doesMatchRegexp(final char symbol) {
        final String word = String.valueOf(symbol);
        if (word.isEmpty() || word.isBlank()) return false;
        Matcher matcher = pattern.matcher(word);
        return matcher.find();
    }
}

