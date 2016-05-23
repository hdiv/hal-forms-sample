package sample.halforms;

public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException() {
		super("Requested resource doesn't exist.");
	}

	public EntityNotFoundException(String message) {
		super(message);
	}
}
