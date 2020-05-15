package schmille.mcbookhelper.exceptions;

import schmille.mcbookhelper.data.editions.GameEdition;
import schmille.mcbookhelper.data.enums.EnumMethod;

public class WillNotFitException extends Exception {

    private final GameEdition edition;
    private final EnumMethod method;

    public WillNotFitException(GameEdition edition, EnumMethod method) {
        super(String.format("String is too large for book (%s only supports %d characters)", edition.getName(), edition.getTotalChars()));
        this.edition = edition;
        this.method = method;
    }

    public GameEdition getEdition() {
        return edition;
    }

    public EnumMethod getMethod() {
        return method;
    }
}
