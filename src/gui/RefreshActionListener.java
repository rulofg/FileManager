package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

public class RefreshActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent event) {
		JComponent component = (JComponent) event.getSource();
		component.setFocusable(false);
		component.setFocusable(true);
	}
}
