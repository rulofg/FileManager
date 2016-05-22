package engine;

import java.io.File;

public class FileTree extends File {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8132533930539792175L;

	public FileTree(String pathname) {
		super(pathname);
	}

	public FileTree(FileTree parent, String string) {
		super(parent, string);
	}

	@Override
	public FileTree[] listFiles() {
		String[] names = this.list();
		FileTree[] children = new FileTree[names.length];
		for (int i = 0; i < names.length; i++)
			children[i] = new FileTree(this, names[i]);

		return children;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileTree other = (FileTree) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if(this.getName()==null)
			return this.getAbsolutePath();
		return this.getName();
	}

}
