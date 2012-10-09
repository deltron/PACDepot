package com.oreilly.javaxslt.swingtrans;

import java.awt.*;
//import java.awt.event.*;
import java.io.*;
import javax.swing.*;

// XML-related imports
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * Displays XML text in a scrolling text area. A status label indicates
 * whether or not the XML is well formed and valid.
 */
public class XMLOutputPanel extends JPanel {
    // displays the XML
    private JTextArea xmlArea = new JTextArea(20,70);
    private String xml;
    private JLabel statusLabel = new JLabel();

    /**
     * Construct the panel and layout the GUI components.
     */
    public XMLOutputPanel() {
        super(new BorderLayout());
        add(new JScrollPane(this.xmlArea), BorderLayout.CENTER);
        add(this.statusLabel, BorderLayout.NORTH);
    }

    /**
     * @param xml the actual XML data to display.
     * @param uri the location of the XML, thus allowing the parser
     * to locate the DTD.
     */
    public void setXML(String xml, String uri) {
        // return quickly if the XML has already been set
        if (xml == null || xml.equals(this.xml)) {
            return;
        }
        this.xml = xml;

        // use JDOM to parse the XML
        Document xmlDoc = null;
        try {
            // attempt to validate the XML
            SAXBuilder saxBuilder = new SAXBuilder(true);
            xmlDoc = saxBuilder.build(new StringReader(this.xml), uri);
            this.statusLabel.setText("XML is well formed and valid");
        } catch (Exception ignored) {
            // the data is not valid, but we should parse it again
            // to see if it is well formed
        }

        if (xmlDoc == null) {
            try {
                // don't validate
                SAXBuilder saxBuilder = new SAXBuilder(false);
                xmlDoc = saxBuilder.build(new StringReader(this.xml));
                this.statusLabel.setText("XML is well formed, but not valid");
            } catch (Exception ex) {
                this.statusLabel.setText("Data is not well formed XML");

                // show the stack trace in the text area
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                this.xmlArea.setText(sw.toString());
            }
        }

        // if the document was parsed, show it
        if (xmlDoc != null) {
            try {
                // pretty-print the XML by indenting two spaces
                XMLOutputter xmlOut = new XMLOutputter("  ", true);
               // xmlOut.setNewlines(false);  // JTextArea doesn't like newlines
                StringWriter sw = new StringWriter();
                xmlOut.output(xmlDoc, sw);
                this.xmlArea.setText(sw.toString());
            } catch (Exception ex) {
                this.statusLabel.setText("Data could not be displayed.");

                // show the stack trace in the text area
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                this.xmlArea.setText(sw.toString());
            }
        }
    }
}