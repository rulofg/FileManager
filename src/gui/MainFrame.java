package gui;

import engine.DownloadHandler;
import engine.FileTreeModel;
import engine.GuiUpdateHandler;
import engine.ProcessHandler;
import io.DownloadParameters;
import io.Downloader;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;

import javax.swing.border.BevelBorder;
import javax.swing.tree.TreePath;
import javax.swing.JPanel;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.FlowLayout;

import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.BoxLayout;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.io.File;

import javax.swing.JSplitPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.Color;
import java.awt.Font;

public class MainFrame {
	public static final String[] GALLERY_ACTIONS = new String[] {
			"D\u00E9doubler les fichiers", "Supprimer les copies","Trier manuellement",
			"Renommer par date", "Nettoyer" };
	public static final int DEDUPLICATE = 0;
	public static final int DELETE = 1;
	public static final int SORT = 2;
	public static final int RENAME = 3;
	public static final int CLEAN = 4;

	private static final String FRAME_ICON_PATH = "res" + File.separator
			+ "miku_icon.png";
	private static final String REFRESH_ICON_PATH = "res" + File.separator
			+ "refresh.png";

	private String galleryPath;
	private String downloadPath;
	private File selectedFile;

	private static MainFrame window = null;

	private JFrame frmFileManager;
	private JTextField jtfGalleryDirPath;
	private JTextField textField_1;
	private JTextField jtfDownloadURL;
	private JTextField jtfDownloadDirPath;
	private JProgressBar downloadProgressBar;
	private JProgressBar processProgressBar;
	private JTree galleryTree;
	@SuppressWarnings("rawtypes")
	private JComboBox galleryActionsComboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		DownloadParameters opt = new DownloadParameters("/coser/detail/\\d*/\\d*", "src='.{0,150}/w650", "href=\".{0,60}\">&gt;");
		opt.setImgDelimitorStart("'");
		opt.setMultipleImgPerLine(true);
		System.out.println("Start");
		//TODO Test URL
		//Downloader.download("http://bcy.net/coser/listhotcharacter/2220", "testDir",opt );
		System.out.println("End");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = getInstance();
					window.frmFileManager.setVisible(true);
					DownloadHandler.getInstance().begin();
					ProcessHandler.getInstance().begin();
					GuiUpdateHandler.getInstance().begin();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @wbp.parser.entryPoint
	 */
	private MainFrame() {
		initialize();
	}

	public static MainFrame getInstance() {
		if (window == null)
			window = new MainFrame();
		return window;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize() {
		frmFileManager = new JFrame();
		frmFileManager.setTitle("FileManager");
		frmFileManager.setBounds(100, 100, 788, 477);
		frmFileManager.setMinimumSize(new Dimension(600, 400));
		frmFileManager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		ImageIcon img = new ImageIcon(FRAME_ICON_PATH);
		frmFileManager.setIconImage(img.getImage());
		// frmFilemanager.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(FRAME_ICON_PATH)));

		JMenuBar menuBar = new JMenuBar();
		frmFileManager.setJMenuBar(menuBar);

		JMenu mnNewMenu_1 = new JMenu("Fichier");
		menuBar.add(mnNewMenu_1);

		JMenuItem mntmNewMenuItem = new JMenuItem("New menu item");
		mnNewMenu_1.add(mntmNewMenuItem);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("New menu item");
		mnNewMenu_1.add(mntmNewMenuItem_1);

		JMenuItem mntmNewMenuItem_2 = new JMenuItem("New menu item");
		mnNewMenu_1.add(mntmNewMenuItem_2);

		JMenuItem mntmNewMenuItem_3 = new JMenuItem("New menu item");
		mnNewMenu_1.add(mntmNewMenuItem_3);

		JMenu mnNewMenu_2 = new JMenu("Edition");
		menuBar.add(mnNewMenu_2);

		JMenuItem mntmNewMenuItem_4 = new JMenuItem("New menu item");
		mnNewMenu_2.add(mntmNewMenuItem_4);

		JMenuItem mntmNewMenuItem_6 = new JMenuItem("New menu item");
		mnNewMenu_2.add(mntmNewMenuItem_6);

		JMenuItem mntmNewMenuItem_5 = new JMenuItem("New menu item");
		mnNewMenu_2.add(mntmNewMenuItem_5);

		JMenu mnNewMenu_3 = new JMenu("Outils");
		menuBar.add(mnNewMenu_3);

		JMenuItem mntmNewMenuItem_7 = new JMenuItem("Options");
		mnNewMenu_3.add(mntmNewMenuItem_7);

		JMenu mnNewMenu = new JMenu("?");
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem_8 = new JMenuItem("New menu item");
		mnNewMenu.add(mntmNewMenuItem_8);
		frmFileManager.getContentPane().setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmFileManager.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Fichiers", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_5 = new JPanel();
		panel.add(panel_5, BorderLayout.NORTH);
		panel_5.setLayout(new BorderLayout(0, 0));

		JButton btnRefresh = new JButton(new ImageIcon(REFRESH_ICON_PATH));
		btnRefresh.setBorderPainted(false);
		btnRefresh.setOpaque(false);
		btnRefresh.setContentAreaFilled(false);
		panel_5.add(btnRefresh, BorderLayout.WEST);

		JPanel panel_2 = new JPanel();
		panel_2.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		panel_2.setAlignmentY(JPanel.CENTER_ALIGNMENT);
		panel_5.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));

		JPanel panel_6 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_6.getLayout();
		flowLayout.setAlignOnBaseline(true);
		panel_2.add(panel_6);

		JLabel lblFilePath = new JLabel("Chemin : ");
		panel_6.add(lblFilePath);
		lblFilePath.setHorizontalAlignment(SwingConstants.CENTER);

		jtfGalleryDirPath = new JTextField();
		panel_6.add(jtfGalleryDirPath);
		jtfGalleryDirPath.setColumns(30);

		JButton btnBrowse = new JButton("Parcourir");
		btnBrowse.addActionListener(new BrowseGalleryDirectoryActionListener());
		panel_6.add(btnBrowse);

		galleryTree = new JTree();
		galleryTree.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null,
				null, null));
		JScrollPane scrollPane = new JScrollPane(galleryTree);
		scrollPane.setMinimumSize(new Dimension(225, 400));
		FileTreeModel.makeTree(galleryTree, new JFileChooser()
				.getFileSystemView().getDefaultDirectory().toString());

		galleryTree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				doMouseClicked(me);
			}
		});

		JPanel panel_4 = new JPanel();
		panel_4.setMinimumSize(new Dimension(200, 400));

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				scrollPane, panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		JPanel panel_12 = new JPanel();

		galleryActionsComboBox = new JComboBox();
		panel_12.add(galleryActionsComboBox);
		galleryActionsComboBox.setModel(new DefaultComboBoxModel(GALLERY_ACTIONS));

		JButton btnNewButton = new JButton("Effectuer action");
		btnNewButton.addActionListener(new ActionGalleryListener());
		panel_12.add(btnNewButton);

		JScrollPane scrollPane_2 = new JScrollPane(panel_12);
		panel_12.setMinimumSize(new Dimension(200, 500));

		panel_4.add(scrollPane_2, BorderLayout.NORTH);

		splitPane.setDividerSize(5);
		panel.add(splitPane, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("S\u00E9ries&Films", null, panel_1, null);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));

		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);

		JLabel lblNewLabel_1 = new JLabel("New label");
		panel_3.add(lblNewLabel_1);

		textField_1 = new JTextField();
		panel_3.add(textField_1);
		textField_1.setColumns(10);

		JButton btnNewButton_2 = new JButton("New button");
		panel_3.add(btnNewButton_2);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		panel_3.add(horizontalStrut_1);

		JButton btnNewButton_3 = new JButton("New button");
		panel_3.add(btnNewButton_3);

		JPanel panelDownload = new JPanel();
		tabbedPane.addTab("T\u00E9l\u00E9chargements", null, panelDownload,
				null);
		panelDownload.setLayout(new BorderLayout(0, 0));

		Component horizontalStrut = Box.createHorizontalStrut(50);
		panelDownload.add(horizontalStrut, BorderLayout.WEST);

		JPanel panel_7 = new JPanel();
		panelDownload.add(panel_7, BorderLayout.CENTER);
		panel_7.setLayout(new GridLayout(4, 1, 0, 0));

		JPanel panel_9 = new JPanel();
		panel_7.add(panel_9);

		JLabel lblType = new JLabel("Type : ");
		panel_9.add(lblType);

		JComboBox list = new JComboBox();
		panel_9.add(list);
		list.setModel(new DefaultComboBoxModel(
				new String[] { "Défaut" }));

		JButton btnDownload = new JButton("T\u00E9l\u00E9charger");
		btnDownload.addActionListener(new DownloadActionListener());
		panel_9.add(btnDownload);

		JPanel panel_8 = new JPanel();
		panel_7.add(panel_8);

		JLabel lblURL = new JLabel("URL :");
		panel_8.add(lblURL);

		jtfDownloadURL = new JTextField();
		panel_8.add(jtfDownloadURL);
		jtfDownloadURL.setColumns(50);

		JPanel panel_10 = new JPanel();
		panel_7.add(panel_10);

		JLabel lblDirPath = new JLabel("Dossier :");
		panel_10.add(lblDirPath);

		jtfDownloadDirPath = new JTextField();
		panel_10.add(jtfDownloadDirPath);
		jtfDownloadDirPath.setColumns(10);

		JButton btnDownloadDir = new JButton("Parcourir");
		btnDownloadDir
				.addActionListener(new BrowseDownloadDirectoryActionListener());
		panel_10.add(btnDownloadDir);
		
		JPanel galleryPanel = new JPanel();
		tabbedPane.addTab("Galleries", null, galleryPanel, null);
		
		JPanel panel_11 = new JPanel();
		frmFileManager.getContentPane().add(panel_11, BorderLayout.SOUTH);
				panel_11.setLayout(new GridLayout(2, 1, 0, 0));
		
				downloadProgressBar = new JProgressBar();
				panel_11.add(downloadProgressBar);
				downloadProgressBar.setStringPainted(true);
				downloadProgressBar.setValue(0);
				downloadProgressBar.setMaximum(1);
				
				processProgressBar = new JProgressBar();
				processProgressBar.setFont(new Font("Tahoma", Font.PLAIN, 11));
				processProgressBar.setForeground(new Color(255, 140, 0));
				processProgressBar.setStringPainted(true);
				processProgressBar.setValue(0);
				processProgressBar.setMaximum(1);
				panel_11.add(processProgressBar);
	}

	private void doMouseClicked(MouseEvent me) {
		TreePath tp = galleryTree.getPathForLocation(me.getX(), me.getY());
		if (tp != null) {
			System.out.println(tp.getLastPathComponent()+"\t"+tp.getLastPathComponent().toString().matches(FileTreeModel.FILE_PATTERN));
			selectedFile = (File) tp.getLastPathComponent();
		}
	}

	public JProgressBar getDownloadProgressBar() {
		return downloadProgressBar;
	}

	public static void setDownloadProgressValue(int value) {
		getInstance().getDownloadProgressBar().setValue(value);
	}

	public static void setDownloadProgressMax(int max) {
		getInstance().getDownloadProgressBar().setMaximum(max);
	}
	
	public JProgressBar getProcessProgressBar() {
		return processProgressBar;
	}

	public static void setProcessProgressValue(int value) {
		getInstance().getProcessProgressBar().setValue(value);
	}

	public static void setProcessProgressMax(int max) {
		getInstance().getProcessProgressBar().setMaximum(max);
	}

	public String getGalleryPath() {
		return galleryPath;
	}

	public void setGalleryPath(String galleryPath) {
		this.galleryPath = galleryPath;
	}

	public JFrame getFrmFileManager() {
		return frmFileManager;
	}

	public JTextField getJtfGalleryDirPath() {
		return jtfGalleryDirPath;
	}

	public JTree getGalleryTree() {
		return galleryTree;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public JTextField getJtfDownloadDirPath() {
		return jtfDownloadDirPath;
	}

	public JTextField getJtfDownloadURL() {
		return jtfDownloadURL;
	}

	@SuppressWarnings("rawtypes")
	public JComboBox getGalleryActionsComboBox() {
		return galleryActionsComboBox;
	}

	public File getSelectedFile() {
		return selectedFile;
	}
}
