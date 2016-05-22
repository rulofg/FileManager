package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import engine.FileTreeModel;

public class BrowseGalleryDirectoryActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent event) {
		MainFrame frame = MainFrame.getInstance();
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		try {
			chooser.setCurrentDirectory(new File(frame.getJtfGalleryDirPath().getText()));
		} catch (Exception e) {
		}
		chooser.setAcceptAllFileFilterUsed(false);
		int tmp = chooser.showOpenDialog(frame.getFrmFileManager());
		if (tmp == JFileChooser.APPROVE_OPTION) {
			String path = chooser.getSelectedFile().getPath();
			frame.setGalleryPath(path);
			frame.getJtfGalleryDirPath().setText(path);
			FileTreeModel.makeTree(frame.getGalleryTree(), path);
		}
	}

}
