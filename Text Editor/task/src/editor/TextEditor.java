package editor;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame {
    private JTextArea TextArea;
    private JPanel MainPanel;
    private JPanel TopPanel;
    private JTextField SearchField;
    private JButton SaveButton;
    private JButton LoadButton;
    private JPanel BorderPanel;
    private JButton SearchButton;
    private JButton NextButton;
    private JButton PrevButton;
    private JCheckBox RegExCheckBox;
    private JFileChooser chooser;
    private Deque<ValuePair> foundPositions;

    public TextEditor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("The first stage");
        foundPositions = new ArrayDeque<>();

        //setContentPane(MainPanel);
        //setContentPane( );
        MainPanel = new JPanel();
        MainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        MainPanel.setLayout(new BorderLayout());
        MainPanel.setSize(new Dimension(300, 300));
        MainPanel.add(TextArea = new JTextArea(), BorderLayout.CENTER);

        TextArea.setName("TextArea");
        TextArea.setSize(new Dimension(300, 300));
        TextArea.setEditable(true);

        chooser = new JFileChooser("FileChooser");
        chooser.setName("FileChooser");
        //chooser.setVisible(false);
        add(chooser);


        JScrollPane ScrollPane = new JScrollPane(TextArea);
        ScrollPane.setName("ScrollPane");
        // ScrollPane.createHorizontalScrollBar();
        // ScrollPane.createVerticalScrollBar();
        MainPanel.add(ScrollPane);
        //TextArea.add(new JScrollPane(ScrollVertical), BorderLayout.SOUTH);
        //TextArea.add(new JScrollPane(ScrollHorizontal), BorderLayout.EAST);
        MainPanel.add(TopPanel = new JPanel(), BorderLayout.NORTH);
//        TopPanel.setLayout(new GridLayout());
        TopPanel.add(LoadButton = new JButton());
        LoadButton.setName("OpenButton");
        LoadButton.setIcon(new ImageIcon("D:\\InelliJ_IDEA\\Text Editor\\Text Editor\\task\\src\\neu-laden.png"));
        TopPanel.add(SaveButton = new JButton());
        SaveButton.setName("SaveButton");
        SaveButton.setIcon(new ImageIcon("D:\\InelliJ_IDEA\\Text Editor\\Text Editor\\task\\src\\speichern.png"));
        TopPanel.add(SearchField = new JTextField(20));
        SearchField.setName("SearchField");
        TopPanel.add(SearchButton = new JButton());
        SearchButton.setName("StartSearchButton");
        SearchButton.setIcon(new ImageIcon("D:\\InelliJ_IDEA\\Text Editor\\Text Editor\\task\\src\\suche.png"));
        TopPanel.add(PrevButton = new JButton());
        PrevButton.setName("PreviousMatchButton");
        PrevButton.setIcon(new ImageIcon("D:\\InelliJ_IDEA\\Text Editor\\Text Editor\\task\\src\\ruckwarts.png"));
        TopPanel.add(NextButton = new JButton());
        NextButton.setName("NextMatchButton");
        NextButton.setIcon(new ImageIcon("D:\\InelliJ_IDEA\\Text Editor\\Text Editor\\task\\src\\vorwarts-taste.png"));
        TopPanel.add(RegExCheckBox = new JCheckBox("Use regex"));
        RegExCheckBox.setName("UseRegExCheckbox");
        add(MainPanel);
//        pack();
        // menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem loadMenuItem = new JMenuItem("Load");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.setName("MenuFile");
        loadMenuItem.setName("MenuOpen");
        saveMenuItem.setName("MenuSave");
        exitMenuItem.setName("MenuExit");
        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        JMenu searchMenu = new JMenu("Search");
        JMenuItem searchMenuItem = new JMenuItem("Start search");
        JMenuItem prevMenuItem = new JMenuItem("Previous search");
        JMenuItem nextMenuItem = new JMenuItem("Next match");
        JMenuItem regexMenuItem = new JMenuItem("Use regular expressions");
        searchMenu.setName("MenuSearch");
        searchMenuItem.setName("MenuStartSearch");
        prevMenuItem.setName("MenuPreviousMatch");
        nextMenuItem.setName("MenuNextMatch");
        regexMenuItem.setName("MenuUseRegExp");
        searchMenu.add(searchMenuItem);
        searchMenu.add(prevMenuItem);
        searchMenu.add(nextMenuItem);
        searchMenu.add(regexMenuItem);
        menuBar.add(searchMenu);
        setJMenuBar(menuBar);

        setSize(800, 600);
        setVisible(true);

        SaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveFile();

            }
        });
        LoadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                readFile();
            }
        });

        SearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doSearch();
            }
        });

        NextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                findNext();
            }
        });

        PrevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                findPrev();
            }
        });
        loadMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                readFile();
            }
        });

        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveFile();
            }
        });

        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
                System.exit(0);
            }
        });

        searchMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doSearch();
            }
        });

        nextMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                findNext();
            }
        });

        prevMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                findPrev();
            }
        });

        regexMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(RegExCheckBox.isSelected()){
                    RegExCheckBox.setSelected(false);
                }else{
                    RegExCheckBox.setSelected(true);
                }
            }
        });
    }

    private void readFile()
    {
//        chooser = new JFileChooser();
//        chooser.setName("FileChooser");
//        int r = chooser.showOpenDialog(TextEditor.this);
        int r = chooser.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            try {
                String content = new String(Files.readAllBytes(Path.of(chooser.getSelectedFile().getAbsolutePath())));
                TextArea.setText(content);
            } catch (IOException e) {
                TextArea.setText("");
                e.printStackTrace();
            }
        }
    }

    private void saveFile(){
//        chooser = new JFileChooser();
//        chooser.setName("FileChooser");
//        int r = chooser.showSaveDialog(TextEditor.this);
        int r = chooser.showSaveDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {

            try {
                Files.writeString(Path.of(chooser.getSelectedFile().getAbsolutePath()), TextArea.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doSearch(){
        foundPositions.clear();
        String regex = SearchField.getText();
        Pattern pattern = null;
        if(RegExCheckBox.isSelected()){
            pattern = Pattern.compile(regex);
        }else{
            pattern = Pattern.compile(Pattern.quote(regex));
        }
        Matcher matcher = pattern.matcher(TextArea.getText());
        while(matcher.find()){
            ValuePair v = new ValuePair(matcher.start(), matcher.end());
            foundPositions.add(v);
        }

        if(foundPositions.size() > 0){
            ValuePair v = foundPositions.peekFirst();
            TextArea.setCaretPosition(v.start + v.getLength() );
            TextArea.select(v.start, v.end);
            TextArea.grabFocus();
        }
    }

    private void findNext(){
        if(foundPositions.size() > 0){
            foundPositions.add(foundPositions.removeFirst());
            ValuePair v = foundPositions.peekFirst();
            TextArea.setCaretPosition(v.start + v.getLength());
            TextArea.select(v.start, v.end);
            TextArea.grabFocus();
        }
    }

    private void findPrev(){
        if(foundPositions.size() > 0) {
            foundPositions.addFirst(foundPositions.removeLast());
            ValuePair v = foundPositions.peekFirst();
            TextArea.setCaretPosition(v.start + v.getLength());
            TextArea.select(v.start, v.end);
            TextArea.grabFocus();
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

class ValuePair{
    int start = 0;
    int end = 0;

    public void setValue(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public ValuePair(int start, int end){
        this.start = start;
        this.end = end;
    }

    int getLength(){
        return end - start;
    }
}
