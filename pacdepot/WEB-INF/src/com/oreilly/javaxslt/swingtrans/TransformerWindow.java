package com.oreilly.javaxslt.swingtrans;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.io.File;
import java.io.StringWriter;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * A secondary JFrame that shows the result of a single XSLT
 * transformation. This frame has a JTabbedPane interface, showing
 * the transformation result, error messages, and the XML output.
 */
public class TransformerWindow extends JFrame {
    // the result of the XSLT transformation as text
    private String resultText;

    private JTabbedPane tabPane = new JTabbedPane();
    private JTextArea textOutputArea = new JTextArea(30, 70);
    private XMLOutputPanel xmlOutputPanel = new XMLOutputPanel();
    private ErrorListenerModel errModel = new ErrorListenerModel();
    private JTable errorTable = new JTable(this.errModel);
    private JTextArea errorDetailArea = new JTextArea(10, 70);
    private String xsltURL;

    /**
     * Construct a new instance and layout the GUI components.
     */
    public TransformerWindow() {
        super("XSLT Transformation");

        // add the tab pane to the frame
        Container cp = getContentPane();
        cp.add(this.tabPane, BorderLayout.CENTER);

        // add individual tabs
        this.tabPane.add("Text Output", new JScrollPane(this.textOutputArea));
        this.tabPane.add("Transformation Problems",
                createErrorPanel());
        this.tabPane.add("XML Output", this.xmlOutputPanel);

        // listen to new tab selections
        this.tabPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                tabChanged();
            }
        });

        this.textOutputArea.setEditable(false);

        // listen to selection changes on the table of errors
        this.errorTable.getSelectionModel().addListSelectionListener(
            new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent evt) {
                    if (!evt.getValueIsAdjusting()) {
                        showErrorDetails();
                    }
                }
            });
        pack();
    }

    /**
     * Show details for the currently selected error.
     */
    private void showErrorDetails() {
        int selRow = this.errorTable.getSelectedRow();
        this.errorDetailArea.setText(this.errModel.getDetailReport(selRow));
    }

    /**
     * Perform an XSLT transformation.
     */
    public void transform(File xmlFile, File xsltFile) {
        setVisible(true);
        try {
            // figure out the directory of the XSLT file. This will be
            // used to locate the DTD
            if (xsltFile != null) {
                File xsltDir = xsltFile.getParentFile();
                if (xsltDir.isDirectory()) {
                    this.xsltURL = xsltDir.toURL().toExternalForm();
                }
            }

            TransformerFactory transFact = TransformerFactory.newInstance();

            // register the table model as an error listener
            transFact.setErrorListener(this.errModel);

            Transformer trans = transFact.newTransformer(
                    new StreamSource(xsltFile));

            // check for null, because the factory might not throw
            // exceptions when the call to newTransformer() fails. This
            // is because we registered an error listener that does not
            // throw exceptions.
            if (trans != null) {
                trans.setErrorListener(this.errModel);

                // capture the result of the XSLT transformation
                StringWriter sw = new StringWriter();
                trans.transform(new StreamSource(xmlFile),
                        new StreamResult(sw));

                // show the results
                this.resultText = sw.toString();
                this.textOutputArea.setText(this.resultText);
            }

        } catch (TransformerConfigurationException tce) {
            try {
                this.errModel.fatalError(tce);
            } catch (TransformerException ignored) {
            }
        } catch (TransformerException te) {
            try {
                this.errModel.fatalError(te);
            } catch (TransformerException ignored) {
            }
        } catch (Exception unexpected) {
            System.err.println(
                    "The XSLT processor threw an unexpected exception");
            unexpected.printStackTrace();
        }

        // show the error tab
        if (this.errModel.hasErrors()) {
            this.tabPane.setSelectedIndex(1);
        }
    }

    // the user clicked on a different tab
    private void tabChanged() {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            int selIndex = this.tabPane.getSelectedIndex();
            String selTab = this.tabPane.getTitleAt(selIndex);

            // when the XML tab is selected, set the text on the XML panel.
            // Although the text may not be XML, we won't know that until
            // it is parsed.
            if ("XML Output".equals(selTab)) {
                this.xmlOutputPanel.setXML(this.resultText,
                        this.xsltURL);
            }
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }


    // a helper method to create the panel that displays errors
    private JComponent createErrorPanel() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        this.errorTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int size = this.errorDetailArea.getFont().getSize();
        this.errorDetailArea.setEditable(false);
        this.errorDetailArea.setFont(
                new Font("Monospaced", Font.PLAIN, size+2));

        splitPane.setTopComponent(new JScrollPane(this.errorTable));
        splitPane.setBottomComponent(new JScrollPane(this.errorDetailArea));
        return splitPane;
    }
}