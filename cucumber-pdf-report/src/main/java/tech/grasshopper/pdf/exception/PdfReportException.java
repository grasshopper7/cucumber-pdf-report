package tech.grasshopper.pdf.exception;

public class PdfReportException extends RuntimeException {

	private static final long serialVersionUID = -7550714776422516622L;
	
	public PdfReportException() {
        super();
    }
	
	public PdfReportException(String message) {
        super(message);
    }
	
	public PdfReportException(String message, Throwable cause) {
        super(message, cause);
    }
	
	public PdfReportException(Throwable cause) {
        super(cause);
    }
}
