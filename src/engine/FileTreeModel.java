package engine;

import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class FileTreeModel implements TreeModel {
	public static final String FILE_PATTERN = "(.*)\\..(\\S{1,5})";
	private static final int DEFAULT_MAXFILES = 50;

	private int maxFiles;

	private FileTree root;

	public static void makeTree(JTree tree, String path) {
		FileTree file = new FileTree(path);
		tree.setModel(new FileTreeModel(file));
	}

	public FileTreeModel(FileTree root) {
		this(root, DEFAULT_MAXFILES);
	}

	public FileTreeModel(FileTree root, int maxFiles) {
		this.root = root;
		this.maxFiles = maxFiles;
	}

	@Override
	public FileTree getRoot() {
		return root;
	}

	@Override
	public Object getChild(Object parent, int index) {
		FileTree parentFile = (FileTree) parent;
		String[] children = parentFile.list();

		if ((children == null) || (index >children.length))
			return null;

		ArrayList<String> list = new ArrayList<String>();
		int numberOfDirectories = 0;
		for (String name : children){
			if (!name.matches(FILE_PATTERN)){
				list.add(name);
				numberOfDirectories++;
			}
		}

		int numberOfFiles = 0;
		for (String name : children) {
			if (name.matches(FILE_PATTERN) && numberOfFiles <maxFiles) {
				numberOfFiles++;
				list.add(name);
			}
		}

		if (numberOfFiles >= maxFiles)
			list.add("[...]");

		int n = numberOfFiles + numberOfDirectories;
		if(index >= n)
			return new FileTree(list.get(index));
		else
			return new FileTree(parentFile, list.get(index));
	}

	@Override
	public int getChildCount(Object parent) {
		String[] children = ((FileTree) parent).list();
		if (children == null)
			return 0;

		int numberOfDirectories = 0;
		for (String name : children)
			if (!name.matches(FILE_PATTERN))
				numberOfDirectories++;

		int numberOfFiles = 0;
		for (String name : children) {
			if (name.matches(FILE_PATTERN) && numberOfFiles < maxFiles) {
				numberOfFiles++;
			}
		}
		int n=numberOfDirectories + numberOfFiles;
		if(numberOfFiles >= maxFiles)
			n++;
		return n;
	}

	@Override
	public boolean isLeaf(Object node) {
		FileTree file = (FileTree) node;
		if(file.getName().equals("[...]"))
			return true;
		return file.isFile();
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		String[] children = ((FileTree) parent).list();
		if (children == null)
			return -1;

		ArrayList<String> list = new ArrayList<String>();
		for (String name : children)
			if (!name.matches(FILE_PATTERN))
				list.add(name);

		int n = 0;
		for (String name : children)
			if (name.matches(FILE_PATTERN) && n < maxFiles) {
				n++;
				list.add(name);
			}
		
		if(n >= maxFiles)
			list.add("[...]");

		return list.indexOf(child);
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
	}
}
