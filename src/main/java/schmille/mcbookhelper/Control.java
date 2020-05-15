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

    public Control() {
        JAVA_EDITION = new JavaEdition();
        BEDROCK_EDITION = new BedrockEdition();
        pages = new ArrayList<>();
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
            pages.add(sbPage.toString());

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
                if(list.isEmpty())
                    break;

                int pixel = 0;
                StringBuilder sbLine = new StringBuilder();

                // Run through all characters and check if the line can fit it by character count and pixel length (based on testing)
                while (!list.isEmpty() && (pixel = pixel + PixelLookupTable.lookup(list.get(0))) <= 90 && sbLine.toString().length() < edition.getLineLength(line)) {
                    sbLine.append(list.remove(0));
                }

                String linetext = sbLine.toString();

                // find the last optimal split point and create a new line based on it
                int lastSplit = TextUtils.furthestIndexOfAny(linetext, BREAK_ON);
                if(lastSplit != -1 && lastSplit != 0) {
                    sbPage.append(linetext, 0, lastSplit);
                    list.addAll(0, TextUtils.toCharList(linetext.substring(lastSplit)));
                }
                else {
                    sbPage.append(linetext);
                    break;
                }

            }
            pages.add(sbPage.toString());

            if(list.isEmpty())
                break;
        }

        if(!list.isEmpty())
            throw new WillNotFitException(edition, EnumMethod.SIZE);
    }

    public List<String> getPages() {
        return pages;
    }

}
