package scraper;

public class NotLoggedInException extends Throwable {
    public NotLoggedInException(String message) {
        super(message);
    }
}
