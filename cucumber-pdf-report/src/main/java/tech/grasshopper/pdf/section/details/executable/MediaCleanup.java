package tech.grasshopper.pdf.section.details.executable;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.Getter;
import tech.grasshopper.pdf.data.ExecutableData;

@Data
@Builder
public class MediaCleanup {

	private ExecutableData executableData;

	private MediaCleanupOption mediaCleanupOption;

	private static final Logger logger = Logger.getLogger(MediaCleanup.class.getName());

	public void deleteMedia() {
		if (mediaCleanupOption == null || mediaCleanupOption.getCleanUpType() == CleanupType.NONE)
			return;

		List<String> medias = executableData.getExecutables().stream().flatMap(e -> e.getMedia().stream())
				.collect(Collectors.toList());

		medias.forEach(m -> {
			try {
				if (checkMediaForDeletion(m))
					Files.deleteIfExists(Paths.get(m));
			} catch (Exception e) {
				e.printStackTrace();
				logger.log(Level.WARNING, "Unable to delete media at location " + m);
			}
		});
	}

	private boolean checkMediaForDeletion(String mediaPath) {
		if (mediaCleanupOption.getCleanUpType() == CleanupType.NONE)
			return false;

		String fileNameWithExt = Paths.get(mediaPath).getFileName().toString();
		int extDotIndex = fileNameWithExt.lastIndexOf('.');

		if (extDotIndex < 1) {
			logger.log(Level.WARNING, "Check media name at location " + mediaPath);
			return false;
		}

		if (mediaCleanupOption.getCleanUpType() == CleanupType.ALL)
			return true;

		String fileName = fileNameWithExt.substring(0, extDotIndex);

		if (Pattern.matches(mediaCleanupOption.getPattern(), fileName))
			return true;

		return false;
	}

	public static enum CleanupType {
		ALL, NONE, PATTERN
	}

	@Builder
	@Getter
	public static class MediaCleanupOption {

		@Default
		private CleanupType cleanUpType = CleanupType.NONE;

		@Default
		private String pattern = "";
	}
}
