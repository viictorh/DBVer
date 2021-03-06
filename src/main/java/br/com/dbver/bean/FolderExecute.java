package br.com.dbver.bean;

import java.io.File;

/**
 * 
 * @author victor.bello
 *
 */
public class FolderExecute {

	private File folder;
	private boolean master;

	public FolderExecute(File folder, boolean master) {
		this.folder = folder;
		this.master = master;
	}

	public File getFolder() {
		return folder;
	}

	public void setFolder(File folder) {
		this.folder = folder;
	}

	public boolean isMaster() {
		return master;
	}

	public void setMaster(boolean master) {
		this.master = master;
	}

	@Override
	public String toString() {
		return "FolderExecute [ master=" + master + ", folder=" + folder + "]";
	}

}
