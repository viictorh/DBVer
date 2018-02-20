package br.com.dbver.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @author victor
 *
 */
public class MimeTypeUtility {
	private static ResourceBundle bundle;
	private final static Logger logger = Logger.getLogger(MimeTypeUtility.class);
	static {
		bundle = ResourceBundle.getBundle("mimeTypes");
	}

	private MimeTypeUtility() {
	}

	public static String retrieveMimeType(File file) throws IOException {
		if (file != null) {
			String type = Files.probeContentType(file.toPath());
			if (StringUtils.isBlank(type)) {
				return getMimeType(file.getName());
			}
			return type;
		}
		return null;
	}

	public static String retrieveMimeType(String file) throws IOException {
		if (!StringUtils.isBlank(file)) {
			return retrieveMimeType(new File(file));
		}
		return null;
	}

	private static String getMimeType(String file) {
		logger.debug("Procurando mimeType para o arquivo " + file, null);
		try {
			return bundle.getString(retrieveFileExtension(file));
		} catch (MissingResourceException e) {
			logger.error("MimeType não identificado para o arquivo:" + file + " - " + e.getMessage(), null);
			return "application/octet-stream";
		}
	}

	private static String retrieveFileExtension(String filename) {
		return FilenameUtils.getExtension(filename);
	}

}
