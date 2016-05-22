package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class GalleryData implements Serializable {

	private static final long serialVersionUID = 7430989023102114173L;
	private static final int PROCESSED = 0;
	private static final int MOVED = 1;
	private static final int COPIED = 2;
	private static final int DELETED = 3;

	private HashMap<String, Integer> galleryMap;
	private HashMap<String, String> moved;
	private HashMap<String, ArrayList<String>> copied;
	private HashMap<String, Byte> deleted;

	public GalleryData() {
		galleryMap = new HashMap<String, Integer>();
		moved = new HashMap<String, String>();
		copied = new HashMap<String, ArrayList<String>>();
		deleted = new HashMap<String, Byte>();
	}

	public boolean isProcessed(String fileName) {
		return galleryMap.containsKey(fileName);
	}

	public boolean isMoved(String fileName) {
		if(isProcessed(fileName))
		return galleryMap.get(fileName) == MOVED;
		else
			return false;
	}

	public boolean isCopied(String fileName) {
		if(isProcessed(fileName))
		return galleryMap.get(fileName) == COPIED;
		else
			return false;
	}

	public boolean isDeleted(String fileName) {
		if(isProcessed(fileName))
			return galleryMap.get(fileName) == DELETED;
		else
			return false;
	}

	public HashMap<String, Integer> getGalleryMap() {
		return galleryMap;
	}

	public HashMap<String, String> getMoved() {
		return moved;
	}

	public HashMap<String, ArrayList<String>> getCopied() {
		return copied;
	}

	public HashMap<String, Byte> getDeleted() {
		return deleted;
	}

	public void addToProcessed(String fileName) {
		if (!isProcessed(fileName)) {
			galleryMap.put(fileName, PROCESSED);
		} else {
			System.err.println(fileName + " already processed.");
		}
	}

	public void addToMoved(String fileName, String destPath) {
		if (isProcessed(fileName)) {
			int n = galleryMap.get(fileName);
			switch (n) {
			case PROCESSED:
				galleryMap.put(fileName, MOVED);
				moved.put(fileName, destPath);
				break;
			case MOVED:
				System.err.println(fileName + " already moved.");
				break;
			case COPIED:
				ArrayList<String> list = copied.get(fileName);
				if (!list.contains(destPath)) {
					list.add(destPath);
				}
				deleted.put(fileName, null);
				galleryMap.put(fileName, DELETED);
				break;
			case DELETED:
				System.err.println(fileName + " is deleted.");
				break;
			}
		} else {
			galleryMap.put(fileName, MOVED);
			moved.put(fileName, destPath);
		}
	}
	
	public void addToCopied(String fileName, String destPath){
		if(isProcessed(fileName)){
			int n = galleryMap.get(fileName);
			switch (n) {
			case PROCESSED:
				galleryMap.put(fileName, COPIED);
				ArrayList<String> list = new ArrayList<String>();
				list.add(destPath);
				copied.put(fileName, list);
				break;
			case MOVED:
				System.err.println(fileName + " has been moved.");
				break;
			case COPIED:
				list = copied.get(fileName);
				if (!list.contains(destPath)) {
					list.add(destPath);
				}
				deleted.put(fileName, null);
				galleryMap.put(fileName, DELETED);
				break;
			case DELETED:
				System.err.println(fileName + " is deleted.");
				break;
			}
		}else{
			galleryMap.put(fileName, COPIED);
			ArrayList<String> list = new ArrayList<String>();
			list.add(destPath);
			copied.put(fileName, list);
		}
	}
	
	public void addToDelete(String fileName){
		if(isProcessed(fileName)){
			int n = galleryMap.get(fileName);
			switch (n) {
			case PROCESSED:
				galleryMap.put(fileName, DELETED);
				deleted.put(fileName, null);
				break;
			case MOVED:
				System.err.println(fileName + " has been moved.");
				break;
			case COPIED:
				if(copied.get(fileName).size()>1){
					deleted.put(fileName, null);
					galleryMap.put(fileName, DELETED);
				}else{
					String dest = copied.get(fileName).get(0);
					moved.put(fileName, dest);
					galleryMap.put(fileName, MOVED);
				}
				break;
			case DELETED:
				System.err.println(fileName + " is deleted.");
				break;
			}
		}else{
			galleryMap.put(fileName, DELETED);
			deleted.put(fileName, null);
		}
	}
}
