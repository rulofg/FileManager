package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.ProcessHandler;

public class ActionGalleryListener implements ActionListener {

	private MainFrame frame;

	@Override
	public void actionPerformed(ActionEvent e) {
		frame = MainFrame.getInstance();
		int index = frame.getGalleryActionsComboBox().getSelectedIndex();
		switch (index) {
		case MainFrame.DEDUPLICATE :
			ProcessHandler.setAction(ProcessHandler.DEDUPLICATE);
			//ImageCompareDialog dialog = new ImageCompareDialog(frame.getFrmFileManager());
			break;
		case MainFrame.DELETE:
			ProcessHandler.setAction(ProcessHandler.DELETE);
			break;
		case MainFrame.SORT:
			try {
				ImageSortDialog dialog = new ImageSortDialog(frame.getFrmFileManager(), new File(frame.getSelectedFile().getAbsolutePath()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case MainFrame.RENAME:
			break;
		case MainFrame.CLEAN:
			break;
		}
	}
}
