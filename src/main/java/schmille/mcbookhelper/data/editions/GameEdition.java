package schmille.mcbookhelper.data.editions;

public abstract class GameEdition {
    protected int maxPages;
    private final int maxCharsPerPage = 328;
    private final int maxLineCount = 14;

    protected GameEdition(int maxPages) {
        this.maxPages = maxPages;
    }

    public int getMaxPages() {
        return maxPages;
    }

    public int getMaxCharsPerPage() {
        return maxCharsPerPage;
    }

    public int getMaxLineCount() {
        return maxLineCount;
    }

    public int getLineLength(int line) {
        if(line == 13)
            return 21;

        return 22;
    }

    public int getTotalChars() {
        return maxPages * maxCharsPerPage;
    }

    public abstract String getName();
}
