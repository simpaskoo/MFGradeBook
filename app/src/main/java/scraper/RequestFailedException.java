package scraper;

public class RequestFailedException extends Throwable {
    public RequestFailedException(String message) {
        super(message);
    }
}
