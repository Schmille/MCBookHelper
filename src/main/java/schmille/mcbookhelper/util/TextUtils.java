package schmille.mcbookhelper.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class TextUtils {

    public static List<Character> toCharList(String input) {
        List<Character> list = new LinkedList<>();

        for(char c: input.toCharArray())
            list.add(c);

        return list;
    }

    public static Queue<Character> toCharQueue(String input) {
        Queue<Character> queue = new LinkedList<>();

        for(char c: input.toCharArray())
            queue.add(c);

        return queue;
    }

    public static int furthestIndexOfAny(String input, String... strings) {
        int high = -1;

        for(String s : strings) {
            high = Math.max(input.lastIndexOf(s), high);
        }

        return high;
    }
}