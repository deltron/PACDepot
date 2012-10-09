package com.oreilly.javaxslt.swingtrans;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/**
 * The entry point into this application. This class displays the main
 * window, allowing the user to select an XML file and an XSLT file.
 */
public class SwingTransformer extends JFrame {
    private JTextField xmlFileFld = new JTextField(30);
    private JTextField xsltFileFld = new JTextField(30);

    // file filters used with the JFileChooser class
    private XMLFileFilter xmlFilter = new XMLFileFilter();
    private XSLTFileFilter xsltFilter = new XSLTFileFilter();
    private JFileChooser fileChooser = new JFileChooser();

    // actions are hooked up to the JButtons
    private Action loadXMLAction =
            new javax.swing.AbstractAction("Select XML") {
        public void actionPerformed(ActionEvent evt) {
            selectXMLFile();
        }
    };

    private Action loadXSLTAction =
            new javax.swing.AbstractAction("Select XSLT") {
        public void actionPerformed(ActionEvent evt) {
            selectXSLTFile();
        }
    };

    private Action transformAction =
            new javax.swing.AbstractAction("Transform") {
        public void actionPerformed(ActionEvent evt) {
            File xmlFile = new File(xmlFileFld.getText());
            File xsltFile = new File(xsltFileFld.getText());

            if (!xmlFile.exists() || !xmlFile.canRead()) {
                showErrorDialog("Unable to read XML file");
                return;
            }
            if (!xsltFile.exists() || !xsltFile.canRead()) {
                showErrorDialog("Unable to read XSLT file");
                return;
            }

            // show the results of the transformation in a new window
            new TransformerWindow().transform(xmlFile, xsltFile);
        }
    };

    /**
     * The entry point into the application; shows the main window.
     */
    public static void main(String[] args) {
        new SwingTransformer().setVisible(true);
    }

    /**
     * Construct the main window and layout the GUI.
     */
    public SwingTransformer() {
        super("Swing XSLT Transformer");

        // note: this line requires Java 2 v1.3
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container cp = getContentPane();
        cp.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.gridy = 0;
        gbc.insets.top = 2;
        gbc.insets.left = 2;
        gbc.insets.right = 2;

        cp.add(new JLabel("XML File:"), gbc);
        gbc.weightx = 1.0;
        cp.add(this.xmlFileFld, gbc);
        gbc.weightx = 0.0;
        cp.add(new JButton(this.loadXMLAction), gbc);

        gbc.gridy++;
        cp.add(new JLabel("XSLT File:"), gbc);
        gbc.weightx = 1.0;
        cp.add(this.xsltFileFld, gbc);
        gbc.weightx = 0.0;
        cp.add(new JButton(this.loadXSLTAction), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        cp.add(new JButton(this.transformAction), gbc);

        pack();
    }


    /**
     * Show the file chooser, listing all XML files.
     */
    private void selectXMLFile() {
        this.fileChooser.setDialogTitle("Select XML File");
        this.fileChooser.setFileFilter(this.xmlFilter);
        int retVal = this.fileChooser.showOpenDialog(this);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            this.xmlFileFld.setText(
                    this.fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    /**
     * Show the file chooser, listing all XSLT files.
     */
    private void selectXSLTFile() {
        this.fileChooser.setDialogTitle("Select XSLT File");
        this.fileChooser.setFileFilter(this.xsltFilter);
        int retVal = this.fileChooser.showOpenDialog(this);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            this.xsltFileFld.setText(
                    this.fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void showErrorDialog(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}

/**
 * Used with JFileChooser to only show files ending with .xml or .XML.
 */
class XMLFileFilter extends javax.swing.filechooser.FileFilter {
    public boolean accept(File f) {
        String name = f.getName();
        return f.isDirectory() || name.endsWith(".xml")
                || name.endsWith(".XML");
    }

    public String getDescription() {
        return "XML Files";
    }

}

/**
 * Used with JFileChooser to only show files ending with .xslt or .XSLT.
 */
class XSLTFileFilter extends javax.swing.filechooser.FileFilter {
    public boolean accept(File f) {
        String name = f.getName();
        return f.isDirectory() || name.endsWith(".xsl")
                || name.endsWith(".xslt") || name.endsWith(".XSL")
                || name.endsWith(".XSLT");
    }

    public String getDescription() {
        return "XSLT Files";
    }
}
