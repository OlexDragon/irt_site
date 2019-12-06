package irt.stock.data.jpa.services.storage;

public class StorageException extends RuntimeException {
	private static final long serialVersionUID = 6233020954746114660L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
