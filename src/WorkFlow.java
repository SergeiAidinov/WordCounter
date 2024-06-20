import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Phaser;

public class WorkFlow {

    private final String directory;
    private final ConcurrentHashMap<String, Integer> totalWordsAndQuantity = new ConcurrentHashMap<>();
    private final Phaser phaser = new Phaser();
    private static WorkFlow workFlowInstance = null;

    private WorkFlow() {
        directory = System.getenv().get("PWD") + File.separator + "files";
    }

    public static WorkFlow instance(){
        if (Objects.isNull(workFlowInstance)) workFlowInstance = new WorkFlow();
        return workFlowInstance;
    }

    public SortedMap<Integer, List<String>> countWords() {
        File file = new File(directory);
        for (String fileName : file.list()) {
            File workingFile = new File(directory + File.separator + fileName);
            if (workingFile.isFile()) {
                phaser.register();
                new Thread (new Counter(phaser, totalWordsAndQuantity, workingFile)).start();
            }
        }
        while (!phaser.isTerminated()) {}
        SortedMap<Integer, List<String>> result = calculateWords(totalWordsAndQuantity);
        Optional<Integer> keyOptional = result.keySet().stream().skip(calculateOffset(result)).findAny();
        return keyOptional.isEmpty() ? result : result.tailMap(keyOptional.get());
    }

    private long calculateOffset(final SortedMap<Integer, List<String>> result) {
        return ((result.size() - Main.QUANTITY_MAX_OFTEN_WORDS) > 0) ? (result.size() - Main.QUANTITY_MAX_OFTEN_WORDS) : 0;
    }

    private SortedMap<Integer, List<String>> calculateWords(final Map<String, Integer> wordsAndQuantity) {
        SortedMap<Integer, List<String>> quantityAndWords = new TreeMap<>();
        for (Map.Entry<String, Integer> entry : wordsAndQuantity.entrySet()) {
            quantityAndWords.computeIfPresent(Integer.valueOf(entry.getValue()), (key, val) -> {
                final List<String> list = quantityAndWords.get(key);
                list.add(entry.getKey());
                return list;
            });
            quantityAndWords.computeIfAbsent(Integer.valueOf(entry.getValue()), integer -> {
                final List<String> list = new ArrayList<>();
                list.add(entry.getKey());
                return list;
            });
        }
        return quantityAndWords;
    }
}
