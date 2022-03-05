package tech.grasshopper.pdf.annotation;

import java.io.FileInputStream;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationFileAttachment;

import lombok.Builder;
import tech.grasshopper.pdf.data.ReportData;

@Builder
public class FileAnnotationProcessor {

	private PDDocument document;

	private ReportData reportData;

	private static final Logger logger = Logger.getLogger(FileAnnotationProcessor.class.getName());

	public void updateAttachments() {
		reportData.getExecutableData().getExecutables().forEach(e -> {

			e.getAttachAnnotations().forEach(f -> {
				try {
					PDAnnotationFileAttachment fileAtt = new PDAnnotationFileAttachment();
					fileAtt.setRectangle(f.getRectangle());
					PDColor pdColor = new PDColor(new float[] { 0.5f, 0.5f, 0.5f }, PDDeviceRGB.INSTANCE);
					fileAtt.setColor(pdColor);

					PDEmbeddedFile ef = new PDEmbeddedFile(document, new FileInputStream(f.getLink()));
					// ef.setSubtype("text/html; charset=UTF-8");
					ef.setModDate(new GregorianCalendar());
					ef.setCreationDate(new GregorianCalendar());

					PDComplexFileSpecification fs = new PDComplexFileSpecification();
					fs.setEmbeddedFile(ef);
					fs.setFile(f.getLink());
					fs.setFileUnicode(f.getLink());

					fileAtt.setFile(fs);

					f.getPage().getAnnotations().add(fileAtt);
				} catch (Exception ex) {
					logger.log(Level.SEVERE, "Unable to create file annotation link", ex);
				}
			});

		});
	}
}
