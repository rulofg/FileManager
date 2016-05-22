package engine;

import gui.MainFrame;

public class GuiUpdateHandler extends Handler {
	private static GuiUpdateHandler handler;
		
	private GuiUpdateHandler(){
		super();
	}
	
	public static GuiUpdateHandler getInstance() {
		if (handler == null)
			handler = new GuiUpdateHandler();
		return handler;
	}
	
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			MainFrame.getInstance().getDownloadProgressBar().revalidate();
			MainFrame.getInstance().getProcessProgressBar().revalidate();
			
		}
	}

	@Override
	public void begin() {
		getThread().start();
		
	}
}
