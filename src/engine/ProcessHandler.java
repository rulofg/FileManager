package engine;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import gui.MainFrame;

public class ProcessHandler extends Handler {
	private static ProcessHandler handler = null;

	private static final String COPY_PATTERN = "(.*)\\(\\d+\\)\\..(.*)";
	private static final String NAME_COPY_PATTERN = "(.*)\\(\\d+\\)";
	private static final int MAX_BEGIN = 3;

	private MainFrame frame;
	private ArrayList<String> children;
	private static ArrayList<File> imagesToDeduplicate = new ArrayList<File>();
	private static ReentrantLock deduplicateLock = new ReentrantLock(true);
	private static boolean deduplicationEnded;
	private static int action;

	public static final int NOTHING = 0;
	public static final int DELETE = 1;
	public static final int DEDUPLICATE = 2;

	private ProcessHandler() {
		super();
	}

	public static ProcessHandler getInstance() {
		if (handler == null)
			handler = new ProcessHandler();
		return handler;
	}

	public static void setAction(int n) {
		action = n;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			switch (action) {
			case DELETE:
				deleteCopies(frame.getSelectedFile());
				action = NOTHING;
				break;
			case DEDUPLICATE:
				deduplicateImages(frame.getSelectedFile());
				action = NOTHING;
				break;
			}

		}
	}

	@Override
	public void begin() {
		action = NOTHING;
		frame = MainFrame.getInstance();
		getThread().start();

	}

	private void deleteCopies(File file) {
		try {
			if (file.isFile()) {
				retrieveChildrenFiles(file.getParentFile());
				Collections.sort(children);
				deleteCopiesOf(file);
			}
			if (file.isDirectory()) {
				retrieveChildrenFiles(file);
				Collections.sort(children);
				deleteCopiesIn(file);
			}
			children = new ArrayList<String>();
			frame.getGalleryTree().updateUI();

		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}
	}

	private void deleteCopiesOf(File file) {
		//TODO if start with tumblr overlook last number
		//TODO add option to chose if care about special characters
		//TODO add number of deleted files in UI after processing
		if (file.exists()) {
			File parent = file.getParentFile();
			String name = file.getName();
			String nameOfFile = name, fileExt = "";
			int lastDot = nameOfFile.lastIndexOf(".");
			if (lastDot != -1) {
				nameOfFile = nameOfFile.substring(0, lastDot);
			}

			if (nameOfFile.matches(NAME_COPY_PATTERN)) {
				nameOfFile = nameOfFile.substring(0,
						nameOfFile.lastIndexOf("("));
			}

			ArrayList<String> childrenList = new ArrayList<String>();
			for (String child : children) {
				boolean sameBegining = true;
				boolean isAfter = false;
				int i = 0;
				//TODO
				try {
					while (sameBegining && i < MAX_BEGIN) {
						sameBegining = sameBegining
								&& (child.charAt(i) == name.charAt(i));
						if (child.charAt(i) > name.charAt(i))
							isAfter = true;
						i++;
					}
				} catch (IndexOutOfBoundsException e) {

				}
				if (isAfter)
					break;
				if (child.startsWith(nameOfFile))
					if (new File(parent, child).exists())
						childrenList.add(child);
			}

			try {
				File bigger = new File(parent, childrenList.get(0));
				RenderedImage imageA = ImageIO.read(bigger);
				if(imageA == null) 
					throw new IllegalArgumentException(bigger.getAbsolutePath()+" is not an image");
				RenderedImage imageB;
				
				for (String copy : childrenList) {
					try {
					File f = new File(parent, copy);
					imageB = ImageIO.read(file);
					
					if (ImageComparator.compareSize(imageA, imageB) == ImageComparator.BIGGER) {
						imageA = imageB;
						bigger = f;
					}
					} catch (IOException e){
						continue;
					}
				}

				childrenList.remove(bigger.getName());
				for (String fileName : childrenList) {
					File f = new File(parent, fileName);
					if (f.isDirectory())
						throw new IllegalArgumentException(
								"File can't be a directory");
					f.delete();
					children.remove(f.getName());
				}
				children.remove(bigger.getName());
				lastDot = bigger.getName().lastIndexOf(".");
				if (lastDot != -1) {
					fileExt = bigger.getName().substring(lastDot);
				}
				bigger.renameTo(new File(parent, nameOfFile + fileExt));

			} catch (IOException e) {
				e.printStackTrace();
				children.remove(file.getName());
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				children.remove(file.getName());
			} catch (IllegalArgumentException e){
				System.err.println(e.getMessage());
				children.remove(file.getName());
			} catch (Exception e){
				System.err.println(file.getAbsolutePath());
				children.remove(file.getName());
				e.printStackTrace();
			}
		}else{
			children.remove(file.getName());
		}
	}

	private void deleteCopiesIn(File directory) {
		int initSize = children.size();
		if (initSize > 0) {
			MainFrame.setProcessProgressMax(initSize * 100);
			MainFrame.setProcessProgressValue(0);
			while (!children.isEmpty()) {
				File child = new File(directory, children.get(0));
				deleteCopiesOf(child);
				MainFrame
						.setProcessProgressValue((initSize - children.size()) * 100);
			}
		}
	}

	private void retrieveChildrenFiles(File file) {
		String[] children = new String[0];
		File parent = null;
		if (file.isFile())
			parent = file.getParentFile();
		if (file.isDirectory()) {
			parent = file;
		}
		children = parent.list();
		ArrayList<String> list = new ArrayList<String>();
		for (String child : children)
			if (child.matches(FileTreeModel.FILE_PATTERN))
				list.add(child);
			else if (new File(child).isFile())
				list.add(child);

		this.children = list;
	}

	private void deduplicateImages(File file) {
		setDeduplicationEnded(false);
		try {
			if (file.isFile()) {
				retrieveChildrenFiles(file.getParentFile());
				deduplicateImagesOf(file);
			}
			if (file.isDirectory()) {
				retrieveChildrenFiles(file);
				deduplicateImagesIn(file);
			}
			children = new ArrayList<String>();
			frame.getGalleryTree().updateUI();

		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}
	}

	private void deduplicateImagesOf(File file) {
		File parent = file.getParentFile();
		ImageComparator comparator;
		try {
			comparator = new ImageComparator(file);

			children.remove(file.getName());
			for (String child : children) {
				try {
					File childFile = new File(parent, child);
					BufferedImage image = ImageIO.read(childFile);
					if (comparator.isSimilarTo(image)) {
						addImageToDeduplicate(childFile);
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for (File f : imagesToDeduplicate)
			setDeduplicationEnded(true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		System.out.println("end");
	}

	private void deduplicateImagesIn(File file) {

	}

	public static void setDeduplicationEnded(boolean b) {
		deduplicateLock.lock();
		deduplicationEnded = true;
		deduplicateLock.unlock();
	}

	public static boolean isDeduplicationEnded() {
		boolean b;
		deduplicateLock.lock();
		b = deduplicationEnded;
		deduplicateLock.unlock();
		return b;
	}

	public static File getFirstImage() {
		File file = null;
		deduplicateLock.lock();
		if (imagesToDeduplicate.size() > 0) {
			file = imagesToDeduplicate.get(0);
		}
		deduplicateLock.unlock();
		return file;
	}

	public static File getImageToDeduplicate() {
		File file = null;
		deduplicateLock.lock();
		if (imagesToDeduplicate.size() > 1) {
			file = imagesToDeduplicate.get(1);
			imagesToDeduplicate.remove(1);
		}
		deduplicateLock.unlock();
		return file;
	}

	private static void addImageToDeduplicate(File file) {
		deduplicateLock.lock();
		imagesToDeduplicate.add(file);
		deduplicateLock.unlock();
	}
}
