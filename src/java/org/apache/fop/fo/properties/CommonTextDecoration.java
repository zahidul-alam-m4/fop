/*
 * Copyright 2005 The Apache Software Foundation.
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

package org.apache.fop.fo.properties;

import java.util.Iterator;
import java.util.List;

import org.apache.fop.datatypes.ColorType;
import org.apache.fop.fo.Constants;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

/**
 * Stores all information concerning text-decoration.
 */
public class CommonTextDecoration {

    //using a bit-mask here
    private static final int UNDERLINE    = 1;
    private static final int OVERLINE     = 2;
    private static final int LINE_THROUGH = 4;
    private static final int BLINK        = 8;
    
    private int decoration;
    private ColorType underColor;
    private ColorType overColor;
    private ColorType throughColor;
    
    /**
     * Creates a new CommonTextDecoration object with default values.
     */
    public CommonTextDecoration() {
    }
    
    /**
     * Creates a CommonTextDecoration object from a property list.
     * @param pList the property list to build the object for
     * @return a CommonTextDecoration object or null if the obj would only have default values
     * @throws PropertyException if there's a problem while processing the property
     */
    public static CommonTextDecoration createFromPropertyList(PropertyList pList) 
                throws PropertyException {
        return calcTextDecoration(pList);
    }
    
    private static CommonTextDecoration calcTextDecoration(PropertyList pList) 
                throws PropertyException {
        CommonTextDecoration deco = null;
        PropertyList parentList = pList.getParentPropertyList();
        if (parentList != null) {
            //Parent is checked first
            deco = calcTextDecoration(parentList);
        }
        //For rules, see XSL 1.0, chapters 5.5.6 and 7.16.4
        List list = pList.get(Constants.PR_TEXT_DECORATION).getList();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            Property prop = (Property)i.next(); 
            int enum = prop.getEnum();
            if (enum == Constants.EN_NONE) {
                if (deco != null) {
                    deco.decoration = 0;
                }
                return deco;
            } else if (enum == Constants.EN_UNDERLINE) {
                if (deco == null) {
                    deco = new CommonTextDecoration();
                }
                deco.decoration |= UNDERLINE;
                deco.underColor = pList.get(Constants.PR_COLOR).getColorType();
            } else if (enum == Constants.EN_NO_UNDERLINE) {
                if (deco != null) {
                    deco.decoration &= OVERLINE | LINE_THROUGH | BLINK;
                    deco.underColor = pList.get(Constants.PR_COLOR).getColorType();
                }
            } else if (enum == Constants.EN_OVERLINE) {
                if (deco == null) {
                    deco = new CommonTextDecoration();
                }
                deco.decoration |= OVERLINE;
                deco.overColor = pList.get(Constants.PR_COLOR).getColorType();
            } else if (enum == Constants.EN_NO_OVERLINE) {
                if (deco != null) {
                    deco.decoration &= UNDERLINE | LINE_THROUGH | BLINK;
                    deco.overColor = pList.get(Constants.PR_COLOR).getColorType();
                }
            } else if (enum == Constants.EN_LINE_THROUGH) {
                if (deco == null) {
                    deco = new CommonTextDecoration();
                }
                deco.decoration |= LINE_THROUGH;
                deco.throughColor = pList.get(Constants.PR_COLOR).getColorType();
            } else if (enum == Constants.EN_NO_LINE_THROUGH) {
                if (deco != null) {
                    deco.decoration &= UNDERLINE | OVERLINE | BLINK;
                    deco.throughColor = pList.get(Constants.PR_COLOR).getColorType();
                }
            } else if (enum == Constants.EN_BLINK) {
                if (deco == null) {
                    deco = new CommonTextDecoration();
                }
                deco.decoration |= BLINK;
            } else if (enum == Constants.EN_NO_BLINK) {
                if (deco != null) {
                    deco.decoration &= UNDERLINE | OVERLINE | LINE_THROUGH;
                }
            } else {
                throw new PropertyException("Illegal value encountered: " + prop.getString());
            }
        }
        return deco;
    }
    
    /** @return true if underline is active */
    public boolean hasUnderline() {
        return (this.decoration & UNDERLINE) != 0;
    }

    /** @return true if overline is active */
    public boolean hasOverline() {
        return (this.decoration & OVERLINE) != 0;
    }

    /** @return true if line-through is active */
    public boolean hasLineThrough() {
        return (this.decoration & LINE_THROUGH) != 0;
    }

    /** @return true if blink is active */
    public boolean isBlinking() {
        return (this.decoration & BLINK) != 0;
    }
    
    /** @return the color of the underline mark */
    public ColorType getUnderlineColor() {
        return this.underColor;
    }
    
    /** @return the color of the overline mark */
    public ColorType getOverlineColor() {
        return this.overColor;
    }

    /** @return the color of the line-through mark */
    public ColorType getLineThroughColor() {
        return this.throughColor;
    }

}
