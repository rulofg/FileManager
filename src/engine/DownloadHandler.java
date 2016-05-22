package engine;

import gui.MainFrame;
import io.Downloader;

import java.net.URL;
import java.util.ArrayList;

public class DownloadHandler extends Handler {
	private static DownloadHandler handler;
	private static String[] arrayTags = {};
	
	private URL url;
	private boolean startDownload = false;
	private boolean downloadDone = false;
	
	private DownloadHandler(){
		super();
	}
	
	public static DownloadHandler getInstance() {
		if (handler == null)
			handler = new DownloadHandler();
		return handler;
	}
	
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (startDownload) {
				startDownload = false;
				ArrayList<URL> filesUrl = Downloader.getFilesUrl(url);
				MainFrame.setDownloadProgressMax(filesUrl.size());
				MainFrame.setDownloadProgressValue(0);
				ArrayList<String> tags = new ArrayList<String>();
				for(String s : arrayTags)
					tags.add(s);
				
				int i = 0;
				for(URL url : filesUrl){
					Object[] data = Downloader.getImageUrlAndName(url, tags);
					System.out.println(data[0]+"\t"+data[1]+"\t"+data[2]);
					MainFrame.setDownloadProgressValue(++i);
				}
				MainFrame.setDownloadProgressValue(filesUrl.size());
			} 
		}
	}

	@Override
	public void begin() {
		getThread().start();
		
	}
	
	public void startDownload(URL url){
		this.url = url;
		this.downloadDone = false;
		this.startDownload = true;
	}

}
