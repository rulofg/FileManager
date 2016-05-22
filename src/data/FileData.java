package data;

import java.io.File;

public abstract class FileData {
	private FileData parent;
	private String path;
	private String name;
	private String size;
	private boolean directory;

	public FileData(File f) {
		this.parent = null;
		this.name = f.getName();
		this.path = f.getAbsolutePath();
		this.directory = f.isDirectory();
		this.size = getFormattedFileSize(getFileSize(f));
	}

	public FileData(FileData parent, String path, String name, String size,
			boolean directory) {
		this.parent = parent;
		this.path = path;
		this.name = name;
		this.size = size;
		this.directory = directory;
	}

	public static String getFormattedFileSize(long size) {
		String[] suffixes = new String[] { "o", "Ko", "Mo", "Go", "To" };

		double tmpSize = size;
		int i = 0;

		while (tmpSize >= 1024 && i < suffixes.length - 1) {
			tmpSize /= 1024.0;
			i++;
		}

		// arrondi à 10^-2
		tmpSize *= 100;
		tmpSize = (int) (tmpSize + 0.5);
		tmpSize /= 100;

		return tmpSize + " " + suffixes[i];
	}

	private long getFileSize(File file) {
		long temporaySize = 0;
		if (file.isFile())
			return file.length();

		File[] list = file.listFiles();
		for (int i = 0; i < list.length; i++)
			temporaySize += this.getFileSize(list[i]);

		return temporaySize;
	}

	public void renameDirectSubFiles(String name) {
		// TODO
	}

	public void renameDirectSubFilesByLastModified() {
		// /TODO
	}

	public void renameAllSubFilesByLastModified() {
		// TODO
	}

	public FileData getParent() {
		return parent;
	}

	public void setParent(FileData parent) {
		this.parent = parent;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public boolean isDirectory() {
		return directory;
	}

	public void setDirectory(boolean directory) {
		this.directory = directory;
	}

}
