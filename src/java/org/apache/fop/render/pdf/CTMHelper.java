/*
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
 
package org.apache.fop.render.pdf;

import org.apache.fop.area.CTM;
import org.apache.fop.pdf.PDFNumber;

/**
 * CTMHelper converts FOP transformation matrixis to those
 * suitable for use by the PDFRender. The e and f elements
 * of the matrix will be divided by 1000 as FOP uses millipoints
 * as it's default user space and PDF uses points.
 *
 * @see org.apache.fop.area.CTM
 *
 * @author <a href="kevin@rocketred.com>Kevin O'Neill</a>
 */
public final class CTMHelper {
    /**
     * <p>Converts the sourceMatrix to a string for use in the PDFRender cm operations.</p>
     * <p>For example:
     * <pre>
     *    org.apache.fop.area.CTM ctm = 
     *          new org.apache.fop.area.CTM(1.0, 0.0, 0.0, 1.0, 1000.0, 1000.0);
     *    String pdfMatrix =  org.apache.fop.render.pdf.CTMHelper.toPDFString(ctm);
     * </pre>
     * will return the string "<code>1.0 0.0 0.0 1.0 1.0 1.0</code>".
     *
     * @param sourceMatrix - The matrix to convert.
     *
     * @return  a space seperated string containing the matrix elements.
     */
    public static String toPDFString(CTM sourceMatrix) {
        if (null == sourceMatrix) {
            throw new NullPointerException("sourceMatrix must not be null");
        }

        final double matrix[] = toPDFArray(sourceMatrix);

        return PDFNumber.doubleOut(matrix[0], 8) + " " + 
               PDFNumber.doubleOut(matrix[1], 8) + " " + 
               PDFNumber.doubleOut(matrix[2], 8) + " " + 
               PDFNumber.doubleOut(matrix[3], 8) + " " + 
               PDFNumber.doubleOut(matrix[4], 8) + " " + 
               PDFNumber.doubleOut(matrix[5], 8);
    }

    /**
     * <p>Creates a new CTM based in the sourceMatrix.</p>
     * <p>For example:
     * <pre>
     *    org.apache.fop.area.CTM inCTM = 
     *          new org.apache.fop.area.CTM(1.0, 0.0, 0.0, 1.0, 1000.0, 1000.0);
     *    org.apache.fop.area.CTM outCTM = 
     *          org.apache.fop.render.pdf.CTMHelper.toPDFCTM(ctm);
     * </pre>
     * will return a new CTM where a == 1.0, b == 0.0, c == 0.0, d == 1.0, e == 1.0 and f == 1.0.
     *
     * @param sourceMatrix - The matrix to convert.
     *
     * @return  a new converted matrix.
     */
    public static CTM toPDFCTM(CTM sourceMatrix) {
        if (null == sourceMatrix) {
            throw new NullPointerException("sourceMatrix must not be null");
        }

        final double matrix[] = toPDFArray(sourceMatrix);

        return new CTM(matrix[0], matrix[1], matrix[2], matrix[3],
                       matrix[4], matrix[5]);
    }

    /**
     * <p>Creates an array of six doubles from the source CTM.</p>
     * <p>For example:
     * <pre>
     *    org.apache.fop.area.CTM inCTM = 
     *          new org.apache.fop.area.CTM(1.0, 0.0, 0.0, 1.0, 1000.0, 1000.0);
     *    double matrix[] = org.apache.fop.render.pdf.CTMHelper.toPDFArray(ctm);
     * </pre>
     * will return a new array where matrix[0] == 1.0, matrix[1] == 0.0,
     * matrix[2] == 0.0, matrix[3] == 1.0,
     * matrix[4] == 1.0 and matrix[5] == 1.0.
     *
     * @param sourceMatrix - The matrix to convert.
     * @return  an array of doubles containing the converted matrix.
     */
    public static double[] toPDFArray(CTM sourceMatrix) {
        if (null == sourceMatrix) {
            throw new NullPointerException("sourceMatrix must not be null");
        }

        final double matrix[] = sourceMatrix.toArray();

        return new double[]{matrix[0], matrix[1], matrix[2], matrix[3],
                            matrix[4] / 1000.0, matrix[5] / 1000.0};
    }

}

