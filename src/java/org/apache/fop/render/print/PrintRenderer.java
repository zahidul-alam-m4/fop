/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id$ */

package org.apache.fop.render.print;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.Map;
import java.util.Vector;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.java2d.Java2DRenderer;

/**
 * Renderer that prints through java.awt.PrintJob.
 * The actual printing is handled by Java2DRenderer
 * since both PrintRenderer and AWTRenderer need to
 * support printing.
 */
public class PrintRenderer extends Java2DRenderer implements Pageable {

    /**
     * Printing parameter: the preconfigured PrinterJob to use,
     * datatype: java.awt.print.PrinterJob
     */
    public static final String PRINTER_JOB = "printerjob";
  
    /**
     * Printing parameter: the pages to be printed (all, even or odd),
     * datatype: the strings "all", "even" or "odd" or one of PagesMode.*
     */
    public static final String PAGES_MODE = "even-odd";

    /**
     * Printing parameter: the page number (1-based) of the first page to be printed,
     * datatype: a positive Integer
     */
    public static final String START_PAGE = "start-page";

    /**
     * Printing parameter: the page number (1-based) of the last page to be printed,
     * datatype: a positive Integer
     */
    public static final String END_PAGE = "end-page";
    
    /**
     * Printing parameter: the number of copies of the document to be printed,
     * datatype: a positive Integer
     */
    public static final String COPIES = "copies";
    
    
    private int startNumber = 0;
    private int endNumber = -1;

    private PagesMode mode = PagesMode.ALL;

    private int copies = 1;

    private PrinterJob printerJob;

    /**
     * Creates a new PrintRenderer with the options set from system properties if a custom
     * PrinterJob is not given in FOUserAgent's renderer options.
     */
    public PrintRenderer() {
        setupFromSystemProperties();
    }
    
    /**
     * Creates a new PrintRenderer and allows you to pass in a specific PrinterJob instance
     * that this renderer should work with.
     * @param printerJob the PrinterJob instance
     * @deprecated Please use the rendering options on the user agent to pass in the PrinterJob!
     */
    public PrintRenderer(PrinterJob printerJob) {
        this();
        this.printerJob = printerJob;
        printerJob.setPageable(this);
    }
    
    private void initializePrinterJob() {
        if (this.printerJob == null) {
            printerJob = PrinterJob.getPrinterJob();
            printerJob.setJobName("FOP Document");
            printerJob.setCopies(copies);
            if (System.getProperty("dialog") != null) {
                if (!printerJob.printDialog()) {
                    throw new RuntimeException(
                            "Printing cancelled by operator");
                }
            }
            printerJob.setPageable(this);
        }
    }

    private void setupFromSystemProperties() {
        //TODO Remove me! This is not a beautiful way to do this.
        // read from command-line options
        copies = getIntProperty("copies", 1);
        startNumber = getIntProperty("start", 1);
        endNumber = getIntProperty("end", -1);
        String str = System.getProperty("even");
        if (str != null) {
            mode = Boolean.valueOf(str).booleanValue() ? PagesMode.EVEN : PagesMode.ODD;
        }
    }
    
    /** {@inheritDoc} */
    public void setUserAgent(FOUserAgent agent) {
        super.setUserAgent(agent);
        
        Map rendererOptions = agent.getRendererOptions();
        
        Object printerJobO = rendererOptions.get(PrintRenderer.PRINTER_JOB);
        if (printerJobO != null) {
            if (!(printerJobO instanceof PrinterJob)) {
                throw new IllegalArgumentException(
                    "Renderer option " + PrintRenderer.PRINTER_JOB
                    + " must be an instance of java.awt.print.PrinterJob, but an instance of "
                    + printerJobO.getClass().getName() + " was given.");
            }
            printerJob = (PrinterJob)printerJobO;
            printerJob.setPageable(this);
        }
        Object o = rendererOptions.get(PrintRenderer.PAGES_MODE);
        if (o != null) {
            if (o instanceof PagesMode) {
                this.mode = (PagesMode)o;
            } else if (o instanceof String) {
                this.mode = PagesMode.byName((String)o);
            } else {
                throw new IllegalArgumentException(
                        "Renderer option " + PrintRenderer.PAGES_MODE
                        + " must be an 'all', 'even', 'odd' or a PagesMode instance.");
            }
        }
        
        o = rendererOptions.get(PrintRenderer.START_PAGE);
        if (o != null) {
            this.startNumber = getPositiveInteger(o);
        }
        o = rendererOptions.get(PrintRenderer.END_PAGE);
        if (o != null) {
            this.endNumber = getPositiveInteger(o);
        }
        if (this.endNumber >= 0 && this.endNumber < this.endNumber) {
            this.endNumber = this.startNumber;
        }
        o = rendererOptions.get(PrintRenderer.COPIES);
        if (o != null) {
            this.copies = getPositiveInteger(o);
        }
        initializePrinterJob();
    }

    private int getPositiveInteger(Object o) {
        if (o instanceof Integer) {
            Integer i = (Integer)o;
            if (i.intValue() < 1) {
                throw new IllegalArgumentException(
                        "Value must be a positive Integer");
            }
            return i.intValue();
        } else if (o instanceof String) {
            return Integer.parseInt((String)o);
        } else {
            throw new IllegalArgumentException(
                    "Value must be a positive integer");
        }
    }
    
    /** @return the PrinterJob instance that this renderer prints to */
    public PrinterJob getPrinterJob() {
        return this.printerJob;
    }

    /** @return the ending page number */
    public int getEndNumber() {
        return endNumber;
    }
    
    /**
     * Sets the number of the last page to be printed.
     * @param end The ending page number
     */
    public void setEndPage(int end) {
        this.endNumber = end;
    }
    
    /** @return the starting page number */
    public int getStartPage() {
        return startNumber;
    }
    
    /**
     * Sets the number of the first page to be printed.
     * @param start The starting page number
     */
    public void setStartPage(int start) {
        this.startNumber = start;
    }
    
    /** {@inheritDoc} */
    public void stopRenderer() throws IOException {
        super.stopRenderer();

        if (endNumber == -1) {
            // was not set on command line
            endNumber = getNumberOfPages();
        }

        Vector numbers = getInvalidPageNumbers();
        for (int i = numbers.size() - 1; i > -1; i--) {
            int page = ((Integer)numbers.elementAt(i)).intValue();
            pageViewportList.remove(page - 1);
        }

        try {
            printerJob.print();
        } catch (PrinterException e) {
            log.error(e);
            throw new IOException("Unable to print: " + e.getClass().getName()
                    + ": " + e.getMessage());
        }
        clearViewportList();
    }

    private static int getIntProperty(String name, int def) {
        String propValue = System.getProperty(name);
        if (propValue != null) {
            try {
                return Integer.parseInt(propValue);
            } catch (Exception e) {
                return def;
            }
        } else {
            return def;
        }
    }

    private Vector getInvalidPageNumbers() {
        Vector vec = new Vector();
        int max = getNumberOfPages();
        boolean isValid;
        for (int i = 1; i <= max; i++) {
            isValid = true;
            if (i < startNumber || i > endNumber) {
                isValid = false;
            } else if (mode != PagesMode.ALL) {
                if (mode == PagesMode.EVEN && (i % 2 != 0)) {
                    isValid = false;
                } else if (mode == PagesMode.ODD && (i % 2 == 0)) {
                    isValid = false;
                }
            }

            if (!isValid) {
                vec.add(new Integer(i));
            }
        }
        return vec;
    }

    /** {@inheritDoc} */
    public PageFormat getPageFormat(int pageIndex)
            throws IndexOutOfBoundsException {
        try {
            if (pageIndex >= getNumberOfPages()) {
                return null;
            }
    
            PageFormat pageFormat = new PageFormat();
    
            Paper paper = new Paper();
    
            Rectangle2D dim = getPageViewport(pageIndex).getViewArea();
            double width = dim.getWidth();
            double height = dim.getHeight();
    
            // if the width is greater than the height assume lanscape mode
            // and swap the width and height values in the paper format
            if (width > height) {
                paper.setImageableArea(0, 0, height / 1000d, width / 1000d);
                paper.setSize(height / 1000d, width / 1000d);
                pageFormat.setOrientation(PageFormat.LANDSCAPE);
            } else {
                paper.setImageableArea(0, 0, width / 1000d, height / 1000d);
                paper.setSize(width / 1000d, height / 1000d);
                pageFormat.setOrientation(PageFormat.PORTRAIT);
            }
            pageFormat.setPaper(paper);
            return pageFormat;
        } catch (FOPException fopEx) {
            throw new IndexOutOfBoundsException(fopEx.getMessage());
        }
    }

    /** {@inheritDoc} */
    public Printable getPrintable(int pageIndex)
            throws IndexOutOfBoundsException {
        return this;
    }
}
