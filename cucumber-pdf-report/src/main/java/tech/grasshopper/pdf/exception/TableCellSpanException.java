package tech.grasshopper.pdf.exception;

public class TableCellSpanException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TableCellSpanException() {
		super();
	}

	public TableCellSpanException(String message) {
		super(message);
	}

	public TableCellSpanException(String message, Throwable cause) {
		super(message, cause);
	}

	public TableCellSpanException(Throwable cause) {
		super(cause);
	}

}
