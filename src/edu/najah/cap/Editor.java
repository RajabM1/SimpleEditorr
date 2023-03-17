package edu.najah.cap;

import edu.najah.cap.ex.EditorSaveAsException;
import edu.najah.cap.ex.EditorSaveException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@SuppressWarnings("serial")
public class Editor extends JFrame implements ActionListener, DocumentListener {

	public static  void main(String[] args) {
		new Editor();
	}

	protected JEditorPane textPanel;//Text Panel
	JMenuBar menu;//Menu
	 JMenuItem copy ;
	 JMenuItem paste;
	 JMenuItem cut;
	 JMenuItem move;
	 boolean changed = false;
	protected File file;
	private static final String SAVE_FILE_TITLE = "Save file";
	private static final String USER_HOME = "user.home";
	private static final String ERROR_MESSAGE = "Cannot write file!";
	private static final String SAVE_FILE_MESSAGE = "The file has changed. You want to save it?";


	private String[] actions = {"Open","Save","New","Edit","Quit", "Save as..."};

	protected JMenu jmfile;

	public Editor() {
		//Editor the name of our application
		super("Editor");
		textPanel = new JEditorPane();
		// center means middle of container.
		add(new JScrollPane(textPanel), "Center");
		textPanel.getDocument().addDocumentListener(this);

		menu = new JMenuBar();
		setJMenuBar(menu);
		buildMenu();
		//The size of window
		setSize(500, 500);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	private void buildMenu() {
		buildFileMenu();
		buildEditMenu();
	}

	private void buildFileMenu() {
		jmfile = new JMenu("File");
		jmfile.setMnemonic('F');
		menu.add(jmfile);
		JMenuItem n = new JMenuItem(actions[2]);
		n.setMnemonic('N');
		n.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		n.addActionListener(this);
		jmfile.add(n);
		JMenuItem open = new JMenuItem(actions[0]);
		jmfile.add(open);
		open.addActionListener(this);
		open.setMnemonic('O');
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		JMenuItem save = new JMenuItem(actions[1]);
		jmfile.add(save);
		save.setMnemonic('S');
		save.addActionListener(this);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		JMenuItem saveas = new JMenuItem(actions[5]);
		jmfile.add(saveas);
		saveas.addActionListener(this);
		JMenuItem quit = new JMenuItem(actions[4]);
		jmfile.add(quit);
		quit.addActionListener(this);
		quit.setMnemonic('Q');
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
	}

	private void buildEditMenu() {
		JMenu edit = new JMenu(actions[3]);
		menu.add(edit);
		edit.setMnemonic('E');
		// cut
		cut = new JMenuItem("Cut");
		cut.addActionListener(this);
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
		cut.setMnemonic('T');
		edit.add(cut);
		// copy
		copy = new JMenuItem("Copy");
		copy.addActionListener(this);
		copy.setMnemonic('C');
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
		edit.add(copy);
		// paste
		paste = new JMenuItem("Paste");
		paste.setMnemonic('P');
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
		edit.add(paste);
		paste.addActionListener(this);
		//move 

		JMenuItem find = new JMenuItem("Find");
		find.setMnemonic('F');
		find.addActionListener(this);
		edit.add(find);
		find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
		// select all
		JMenuItem sall = new JMenuItem("Select All");
		sall.setMnemonic('A');
		sall.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		sall.addActionListener(this);
		edit.add(sall);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		switch(action) {
			case "Exit":
				System.exit(0);
				break;
			case "Open":
				loadFile();
				break;
			case "Save":
				saveFile();
				break;
			case "New":
				newFile();
				break;
			case "Save As":
				saveAs(actions[5]);
				break;
			case "Select All":
				textPanel.selectAll();
				break;
			case "Copy":
				textPanel.copy();
				break;
			case "Cut":
				textPanel.cut();
				break;
			case "Paste":
				textPanel.paste();
				break;
			case "Find":
				FindDialog find = new FindDialog(this, true);
				find.showDialog();
				break;
			default:
				break;
		}
	}

	private void saveFile() {
		int ans = 0;
		if (changed) {
			ans = JOptionPane.showConfirmDialog(null, SAVE_FILE_MESSAGE, SAVE_FILE_TITLE, 0, 2);
		}
		if (ans != 1) {
			if (file == null) {
				saveAs(actions[1]);
			} else {
				String text = textPanel.getText();
				logger.info(text);
				try (PrintWriter writer = new PrintWriter(file)){
					if (!file.canWrite())
						throw new EditorSaveException(ERROR_MESSAGE);
					writer.write(text);
					changed = false;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	private void newFile() {
		if (changed) {
			int ans = JOptionPane.showConfirmDialog(null, SAVE_FILE_MESSAGE, SAVE_FILE_TITLE, 0, 2);
			if (ans == 1) {
				return;
			}
			if (file == null) {
				saveAs(actions[1]);
				return;
			}
			String text = textPanel.getText();
			logger.info(text);
			try (PrintWriter writer = new PrintWriter(file);){
				if (!file.canWrite())
					throw new EditorSaveException(ERROR_MESSAGE);
				writer.write(text);
				changed = false;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		file = null;
		textPanel.setText("");
		changed = false;
		setTitle("Editor");
	}

	private void loadFile() {
		JFileChooser dialog = new JFileChooser(System.getProperty(USER_HOME));
		dialog.setMultiSelectionEnabled(false);
		try {
			int result = dialog.showOpenDialog(this);

			if (result == 1)//1 value if cancel is chosen.
				return;
			if (result == 0) {// value if approve (yes, ok) is chosen.
				file = dialog.getSelectedFile();
				String rs = readFile(file);

				textPanel.setText(rs);
				changed = false;
				setTitle("Editor - " + file.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e, "Error", 0);
		}
	}

	private static String readFile(File file) {
		StringBuilder rs = new StringBuilder();
		try (FileReader fr = new FileReader(file); BufferedReader reader = new BufferedReader(fr)) {
			String line;
			while ((line = reader.readLine()) != null) {
				rs.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cannot read file !", "Error !", 0);
		}
		return rs.toString();
	}


	
	private void saveAs(String dialogTitle) {
		dialogTitle = dialogTitle.toUpperCase();
		JFileChooser dialog = new JFileChooser(System.getProperty(USER_HOME));
		dialog.setDialogTitle(dialogTitle);
		int result = dialog.showSaveDialog(this);
		if (result != 0)//0 value if approve (yes, ok) is chosen.
			return;
		file = dialog.getSelectedFile();
		PrintWriter writer = getWriter(file);
		if(writer!=null){
			writer.write(textPanel.getText());
			changed = false;
			setTitle("Editor - " + file.getName());
	}
	else {
	logger.info("Failed to write");
	}

	}
	private static PrintWriter getWriter(File file) {
		try {
			return new PrintWriter(file);
		} catch (Exception e){
			return null;
		}
	}

	 void saveAsText(String dialogTitle) throws EditorSaveAsException {
		JFileChooser dialog = new JFileChooser(System.getProperty(USER_HOME));
		dialog.setDialogTitle(dialogTitle);
		int result = dialog.showSaveDialog(this);
		if (result != 0)//0 value if approve (yes, ok) is chosen.
			return;
		file = dialog.getSelectedFile();
		try (PrintWriter writer = new PrintWriter(file);){
			writer.write(textPanel.getText());
			changed = false;
			setTitle("Save as Text Editor - " + file.getName());
		} catch (FileNotFoundException e) {
			throw new EditorSaveAsException(e.getMessage());
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		changed = true;
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		changed = true;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		changed = true;
	}

}