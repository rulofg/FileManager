package data;

public class MediaFile extends FileData {
	private boolean viewed;
	
	public MediaFile(FileData parent, String path, String name, String size,boolean directory) {
		super(parent, path, name, size,directory);
	}

	public MediaFile(FileData parent, String path, String name, String size, boolean directory,
			boolean viewed) {
		super(parent, path, name, size,directory);
		this.viewed = viewed;
	}

	public boolean isViewed() {
		return viewed;
	}

	public void setViewed(boolean viewed) {
		this.viewed = viewed;
	}
	
	
}
