package schmille.mcbookhelper;

import schmille.mcbookhelper.data.PixelLookupTable;
import schmille.mcbookhelper.data.editions.BedrockEdition;
import schmille.mcbookhelper.data.editions.GameEdition;
import schmille.mcbookhelper.data.editions.JavaEdition;
import schmille.mcbookhelper.data.enums.EnumGameEdition;
import schmille.mcbookhelper.data.enums.EnumMethod;
import schmille.mcbookhelper.exceptions.WillNotFitException;
import schmille.mcbookhelper.util.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Control {

    private final GameEdition JAVA_EDITION;
    private final GameEdition BEDROCK_EDITION;

    private final String[] BREAK_ON = {" ", ",", ";", ".", "?", "!", "\n"};

    private String raw;
    private List<String> pages;
    private GameEdition edition;
    private int current;

    public Control() {
        JAVA_EDITION = new JavaEdition();
        BEDROCK_EDITION = new BedrockEdition();
        pages = new ArrayList<>();
        current = 0;
    }

    public void autopage(String input, EnumMethod method, EnumGameEdition gameEdition) throws WillNotFitException {
        raw = input;
        pages.clear();
        this.edition = gameEdition == EnumGameEdition.JAVA ? JAVA_EDITION : BEDROCK_EDITION;

        if(method == EnumMethod.SIZE)
            sizeAutopage(input.trim().replaceAll("\n", " ").replaceAll(" +", " "));

        if(method == EnumMethod.FANCY)
            fancyAutopage(input);
    }

    private void sizeAutopage(String input) throws WillNotFitException {
        if(input.length() <= edition.getMaxCharsPerPage()) {
            pages.add(input.trim());
            return;
        }

        Queue<Character> queue = TextUtils.toCharQueue(input);
        for(int page = 0; page < edition.getMaxPages(); page++) {
            StringBuilder sbPage = new StringBuilder();

            for (int line = 0; line < edition.getMaxLineCount(); line++) {
                if(queue.isEmpty())
                    break;

                int pixel = 0;
                StringBuilder sbLine = new StringBuilder();

                // Run through all characters and check if the line can fit it by character count and pixel length (based on testing)
                while (!queue.isEmpty() && (pixel = pixel + PixelLookupTable.lookup(queue.peek())) <= 90 && sbLine.toString().length() < edition.getLineLength(line)) {
                    sbLine.append(queue.poll());
                }

                String linetext = sbLine.append("\n").toString();
                sbPage.append(linetext.startsWith(" ") ? linetext.replaceFirst(" ", "") : linetext);
            }
            pages.add(sbPage.toString().trim());

            if(queue.isEmpty())
                break;
        }

        if(!queue.isEmpty())
            throw new WillNotFitException(edition, EnumMethod.SIZE);
    }

    private void fancyAutopage(String input) throws WillNotFitException {

      if(input.length() <= edition.getMaxCharsPerPage()) {
            pages.add(input.trim());
            return;
        }

        List<Character> list = TextUtils.toCharList(input);
        for(int page = 0; page < edition.getMaxPages(); page++) {
            StringBuilder sbPage = new StringBuilder();

            for (int line = 0; line < edition.getMaxLineCount(); line++) {

                int pixel = 0;
                StringBuilder sbLine = new StringBuilder();

                // Run through all characters and check if the line can fit it by character count and pixel length (based on testing)
                while (!list.isEmpty() && (pixel = pixel + PixelLookupTable.lookup(list.get(0))) <= 90 && sbLine.toString().length() < edition.getLineLength(line)) {
                    if(list.get(0) =='\n') {
                        sbLine.append(list.remove(0));
                        line++;
                        break;
                    }

                    sbLine.append(list.remove(0));
                }

                String linetext = sbLine.toString();

                // find the last optimal split point and create a new line based on it
                int lastSplit = TextUtils.furthestIndexOfAny(linetext, BREAK_ON);
                if(lastSplit != -1 && lastSplit != 0) {
                    sbPage.append(linetext.substring( 0, lastSplit).trim());
                    sbPage.append("\n");

                    list.addAll(0, TextUtils.toCharList(linetext.substring(lastSplit)));
                }
                else {
                    sbPage.append(linetext.trim());
                    break;
                }

            }
            pages.add(sbPage.toString().trim());

            if(list.isEmpty())
                break;
        }

        if(!list.isEmpty())
            throw new WillNotFitException(edition, EnumMethod.SIZE);
    }

    public String nextPage() {
        if(pages.isEmpty())
            return "";

        if(current < pages.size() - 1)
            current++;

        return pages.get(current);
    }

    public String previousPage() {
        if(pages.isEmpty())
            return "";

        if(current > 0)
            current--;

        return pages.get(current);
    }

    public String currentPage() {
        if(pages.isEmpty())
            return "";

        return pages.get(current);
    }

    public int getPageCount() {
        return pages.size();
    }

    public int getCurrentPageIndex() {
        return current;
    }

    public void clearCurrent() {
        pages.set(current, "");
    }

    public void clearPages() {
        pages.clear();
        current = 0;
    }

    public void beforeFirst() {
        current = -1;
    }

    public String getAllPages() {
        StringBuilder sb = new StringBuilder();

        for(String s : pages) {
            sb.append(s.trim());
            sb.append("\n");
        }

        return sb.toString();
    }

    public boolean isOnLast() {
        return current == pages.size() - 1;
    }

}
