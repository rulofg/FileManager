package gui;

import io.FileRW;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;

import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;

import data.GalleryData;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ImageSortDialog extends JDialog {

	private static final long serialVersionUID = -8618421980478446980L;

	private BufferedImage image;
	private final JPanel contentPanel = new JPanel();
	private File directory;
	private GalleryData data;
	private ArrayList<String> children = new ArrayList<String>();
	private int index;
	
	private JLabel lblNewLabel_3;
	private ScalablePane imagePane;
	private JPanel imagePanel;

	/**
	 * Create the dialog.
	 * 
	 * @throws IOException
	 */
	public ImageSortDialog(JFrame owner, File file) throws IOException {
		super(owner);
		BufferedImage image = null;
		String imageName = "";
		
		if (file.isFile()) {
			image = ImageIO.read(file);
			directory = file.getParentFile();
		} else if (file.isDirectory()) {
			directory = file;
		}
		String[] childrenTab = directory.list();
		for (String child : childrenTab) {
			children.add(child);
		}
		data = (GalleryData) FileRW.readObjectFile(directory
				+ File.separator + "info.dat");
		if (data == null)
			data = new GalleryData();
		
		children.removeAll(data.getGalleryMap().keySet());
		
		int i = 0;
		while (image == null && i < children.size()) {
			try {
				image = ImageIO.read(new File(file, children.get(i)));
				imageName = childrenTab[i];
				i++;
			} catch (Exception e) {
				image = null;
				children.remove(i);
				i++;
			}
		}
		index = children.indexOf(imageName);

		if (image == null) {
			JOptionPane.showConfirmDialog(owner, "No image found");
		} // else {
		this.image = image;
		initialize();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(400, 300));
		setLocationRelativeTo(owner);
		setVisible(true);
		// }
	}

	private void initialize() {
		setBounds(100, 100, 609, 442);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		imagePanel = new JPanel();
		imagePanel.setLayout(new BorderLayout(0, 0));
		imagePane = new ScalablePane(image);
		imagePanel.add(imagePane, BorderLayout.CENTER);
		contentPanel.add(imagePanel, BorderLayout.CENTER);

		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new GridLayout(6, 1, 0, 0));

		JScrollPane scrollPane = new JScrollPane(dataPanel);

		JPanel panel = new JPanel();
		dataPanel.add(panel);
		panel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblNewLabel = new JLabel("Nom :");
		panel.add(lblNewLabel);

		lblNewLabel_3 = new JLabel("New label");
		panel.add(lblNewLabel_3);

		JPanel panel_1 = new JPanel();
		dataPanel.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblNewLabel_1 = new JLabel("Taille :");
		panel_1.add(lblNewLabel_1);

		JLabel lblNewLabel_4 = new JLabel("New label");
		panel_1.add(lblNewLabel_4);

		JPanel panel_2 = new JPanel();
		dataPanel.add(panel_2);
		panel_2.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblNewLabel_2 = new JLabel("Dimensions :");
		panel_2.add(lblNewLabel_2);

		JLabel lblNewLabel_5 = new JLabel("New label");
		panel_2.add(lblNewLabel_5);

		JPanel panel_3 = new JPanel();
		dataPanel.add(panel_3);
		panel_3.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panel_4 = new JPanel();
		dataPanel.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panel_5 = new JPanel();
		dataPanel.add(panel_5);
		panel_5.setLayout(new GridLayout(0, 2, 0, 0));
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentPanel.add(scrollPane, BorderLayout.EAST);

		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setLayout(new BorderLayout(0, 0));

		JPanel jpWest = new JPanel();
		buttonPane.add(jpWest, BorderLayout.WEST);

		JButton btnNewButton = new JButton("Pr\u00E9c\u00E9dente");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				data.addToProcessed(children.get(index));
				index = (index -1)%children.size();
				image = null;
				while (image == null && !children.isEmpty()) {
					try {
						image = ImageIO.read(new File(directory, children.get(index)));
						index = (index-1)%children.size();
					} catch (Exception exc) {
						image = null;
						children.remove(index);
						index = (index-1)%children.size();
					}
				}
				update();
			}
		});
		jpWest.add(btnNewButton);

		JPanel jpCenter = new JPanel();
		buttonPane.add(jpCenter, BorderLayout.CENTER);

		JButton btnNewButton_2 = new JButton("Copier");
		jpCenter.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("D\u00E9placer");
		jpCenter.add(btnNewButton_3);

		JButton btnNewButton_4 = new JButton("Supprimer");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				data.addToDelete(children.get(index));
				children.remove(index);
				actionNext();
			}
		});
		jpCenter.add(btnNewButton_4);

		JPanel jpEast = new JPanel();
		buttonPane.add(jpEast, BorderLayout.EAST);

		JButton btnNewButton_1 = new JButton("Suivante");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionNext();
			}
		});
		jpEast.add(btnNewButton_1);
	}
	
	private void actionNext(){
		data.addToProcessed(children.get(index));
		index = (index +1)%children.size();
		image = null;
		while (image == null && !children.isEmpty()) {
			try {
				image = ImageIO.read(new File(directory, children.get(index)));
				index = (index+1)%children.size();
			} catch (Exception exc) {
				image = null;
				children.remove(index);
				index = (index+1)%children.size();
			}
		}
		FileRW.writeObjectFile(data, directory+File.separator+"info.dat");
		update();
		if((data.getGalleryMap().size()-data.getDeleted().size())==children.size())
			this.dispose();
	}

	private void update(){
		lblNewLabel_3.setText(children.get(index));
		imagePane = new ScalablePane(image);
		imagePanel.removeAll();
		imagePanel.add(imagePane);
		this.revalidate();
	}
}
