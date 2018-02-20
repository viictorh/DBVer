package br.com.dbver.bean;

import java.io.File;

public class FileParameter {
	private String value;
	private File file;

	public FileParameter(String value, File file) {
		this.value = value;
		this.file = file;
	}

	public FileParameter(File file) {
		this.file = file;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
