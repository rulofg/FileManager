package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ImageCompareDialog extends JDialog {

	private static final long serialVersionUID = -8618421980478446980L;
	
	private BufferedImage leftImage, rightImage;
	private final JPanel contentPanel = new JPanel();
	private JSplitPane splitPane;

	/**
	 * Create the dialog.
	 */
	public ImageCompareDialog(JFrame owner, BufferedImage leftImage) {
		this(owner,leftImage,null);
	}
	
	public ImageCompareDialog(JFrame owner, BufferedImage leftImage, BufferedImage rightImage){
		super(owner);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				splitPane.setDividerLocation(0.5);
			}
		});
		this.leftImage = leftImage;
		this.rightImage = rightImage;
		initialize();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(owner);
		setVisible(true);

		splitPane.setDividerLocation(0.5);
	}

	private void initialize() {
		setBounds(100, 100, 609, 442);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout(0, 0));
		ScalablePane leftPane = new ScalablePane(leftImage);
		leftPanel.add(leftPane, BorderLayout.CENTER);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout(0, 0));
		if (rightImage != null) {
			ScalablePane rightPane = new ScalablePane(rightImage);
			rightPanel.add(rightPane);
		}

		splitPane.setLeftComponent(leftPanel);

		JPanel leftInfoPanel = new JPanel();
		leftPanel.add(leftInfoPanel, BorderLayout.SOUTH);
		splitPane.setRightComponent(rightPanel);

		JPanel rightInfoPanel = new JPanel();
		rightPanel.add(rightInfoPanel, BorderLayout.SOUTH);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
	}

}
