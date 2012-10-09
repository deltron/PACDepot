package com.oreilly.javaxslt.swingtrans;

import java.io.*;
import java.util.*;
import javax.swing.table.*;

// XML-related imports
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;

/**
 * A JTable data model that provides detail information about a list
 * of javax.xml.transform.TransformerException objects.
 */
public class ErrorListenerModel extends AbstractTableModel
        implements ErrorListener {

    // column positions in the table
    private static final int LINE_COL = 0;
    private static final int COLUMN_COL = 1;
    private static final int PUBLIC_ID_COL = 2;
    private static final int SYSTEM_ID_COL = 3;
    private static final int MESSAGE_AND_LOC_COL = 4;
    private static final int LOCATION_COL = 5;
    private static final int EXCEPTION_COL = 6;
    private static final int CAUSE_COL = 7;

    private static final String[] COLUMN_NAMES = {
        "Line",
        "Column",
        "Public ID",
        "System ID",
        "Message & Location",
        "Location",
        "Exception",
        "Cause"
    };

    // the actual data
    private List exceptionList = null;

    /**
     * @return a detailed text report of the exception at the specified row.
     */
    public String getDetailReport(int row) {
        if (this.exceptionList == null
                || row < 0 || row >= this.exceptionList.size()) {
            return "";
        }

        TransformerException te = (TransformerException)
                this.exceptionList.get(row);
        SourceLocator loc = te.getLocator(); // may be null

        // buffer the report
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        pw.println(te.getClass().getName());
        pw.println("-----------------------------------------------------");
        if (loc == null) {
            pw.println("Line Number  : [null SourceLocator]");
            pw.println("Column Number: [null SourceLocator]");
            pw.println("Public ID    : [null SourceLocator]");
            pw.println("System ID    : [null SourceLocator]");
        } else {
            pw.println("Line Number  : " + loc.getLineNumber());
            pw.println("Column Number: " + loc.getColumnNumber());
            pw.println("Public ID    : " + loc.getPublicId());
            pw.println("System ID    : " + loc.getSystemId());
        }

        pw.println("Message & Location : " + te.getMessageAndLocation());
        pw.println("Location           : " + te.getLocationAsString());

        pw.println("Exception          : " + te.getException());
        if (te.getException() != null) {
            te.getException().printStackTrace(pw);
        }

        pw.println("Cause              : " + te.getCause());
        if (te.getCause() != null && (te.getCause() != te.getException())) {
            te.getCause().printStackTrace(pw);
        }

        return sw.toString();
    }

    /**
     * Part of the TableModel interface.
     */
    public Object getValueAt(int row, int column) {
        if (this.exceptionList == null) {
            return "No errors or warnings";
        } else {
            TransformerException te = (TransformerException)
                    this.exceptionList.get(row);
            SourceLocator loc = te.getLocator();

            switch (column) {
            case LINE_COL:
                return (loc != null)
                        ? String.valueOf(loc.getLineNumber()) : "N/A";
            case COLUMN_COL:
                return (loc != null)
                        ? String.valueOf(loc.getColumnNumber()) : "N/A";
            case PUBLIC_ID_COL:
                return (loc != null) ? loc.getPublicId() : "N/A";
            case SYSTEM_ID_COL:
                return (loc != null) ? loc.getSystemId() : "N/A";
            case MESSAGE_AND_LOC_COL:
                return te.getMessageAndLocation();
            case LOCATION_COL:
                return te.getLocationAsString();
            case EXCEPTION_COL:
                return te.getException();
            case CAUSE_COL:
                return te.getCause();
            default:
                return "[error]"; // shouldn't happen
            }
        }
    }

    /**
     * Part of the TableModel interface.
     */
    public int getRowCount() {
        return (this.exceptionList == null) ? 1 :
                this.exceptionList.size();
    }

    /**
     * Part of the TableModel interface.
     */
    public int getColumnCount() {
        return (this.exceptionList == null) ? 1 :
                COLUMN_NAMES.length;
    }

    /**
     * Part of the TableModel interface.
     */
    public String getColumnName(int column) {
        return (this.exceptionList == null)
                ?  "Transformation Problems"
                : COLUMN_NAMES[column];
    }

    /**
     * @return true if any errors occurred.
     */
    public boolean hasErrors() {
        return this.exceptionList != null;
    }

    /**
     * This is part of the javax.xml.transform.ErrorListener interface.
     * Indicates that a warning occurred. Transformers are required to
     * continue processing after warnings, unless the error listener
     * throws TransformerException.
     */
    public void warning(TransformerException te) throws TransformerException {
        report(te);
    }

    /**
     * This is part of the javax.xml.transform.ErrorListener interface.
     * Indicates that a recoverable error occurred.
     */
    public void error(TransformerException te) throws TransformerException {
        report(te);
    }

    /**
     * This is part of the javax.xml.transform.ErrorListener interface.
     * Indicates that a non-recoverable error occurred.
     */
    public void fatalError(TransformerException te) throws TransformerException {
        report(te);
    }

    // adds the exception to exceptionList and notifies the JTable that
    // the content of the table has changed.
    private void report(TransformerException te) {
        if (this.exceptionList == null) {
            this.exceptionList = new ArrayList();
            this.exceptionList.add(te);
            fireTableStructureChanged();
        } else {
            this.exceptionList.add(te);
            int row = this.exceptionList.size()-1;
            super.fireTableRowsInserted(row, row);
        }
    }
}
