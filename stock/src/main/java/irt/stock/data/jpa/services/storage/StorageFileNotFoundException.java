package irt.stock.data.jpa.services.storage;

public class StorageFileNotFoundException extends StorageException {
	private static final long serialVersionUID = 8487790285783926987L;

	public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}