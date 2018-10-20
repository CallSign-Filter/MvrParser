package com.brandonhessler.java.mvr;

import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;
import de.javasoft.plaf.synthetica.filechooser.SyntheticaFileChooser;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.*;
import java.io.File;

public class TxtFileToParse extends JDialog {
    private JPanel panel;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tfFilePath;
    private JButton btnBrowse;
    private File fileToOpen;

    public static void main(String[] args) {
        try {
            UIManager.put("Synthetica.extendedFileChooser.useSystemFileIcons", false);
            UIManager.put("Synthetica.extendedFileChooser.sortEnabled", false);

            UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());

        } catch (Exception e) {
            e.printStackTrace();
        }


        TxtFileToParse dialog = new TxtFileToParse();
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    public TxtFileToParse() {
        setContentPane(panel);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        panel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        btnBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SyntheticaFileChooser fileChooser = new SyntheticaFileChooser();
//                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("D:\\Documents\\Work\\HIS"));
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fileChooser.setMultiSelectionEnabled(false);
                fileChooser.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return (f.canRead() &&
                                (f.getPath().endsWith(".pdf") || f.getPath().endsWith(".txt")) ||
                                f.isDirectory()) ;
                    }

                    @Override
                    public String getDescription() {
                        return ".txt file";
                    }
                });
                fileChooser.showOpenDialog(panel);
                if (fileChooser.getSelectedFile() != null) {
                    tfFilePath.setText(fileChooser.getSelectedFile().getPath());
                    fileToOpen = fileChooser.getSelectedFile();
                }
            }
        });
    }

    private void onOK() {
        ParseMvr.parse(fileToOpen);

        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public File getFileToOpen() {
        return fileToOpen;
    }

}
