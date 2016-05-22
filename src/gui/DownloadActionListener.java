package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import engine.DownloadHandler;

public class DownloadActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent event) {
		//Downloader.download(jtfURL.getText(), downloadPath);
		MainFrame frame = MainFrame.getInstance();
		URL url;
		try {
			url = new URL(frame.getJtfDownloadURL().getText());
			DownloadHandler.getInstance().startDownload(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
