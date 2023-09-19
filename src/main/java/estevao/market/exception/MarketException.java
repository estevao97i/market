package estevao.market.exception;

// excecao customizada do sistema
public class MarketException extends RuntimeException {

    public MarketException() {
    }

    public MarketException(String message) {
        super(message);
    }

    public MarketException(String message, Throwable cause) {
        super(message, cause);
    }
}
