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

package org.apache.fop.fo.properties;

import java.util.Vector;

import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.Numeric;

/**
 * Base class for all property objects
 * @author unascribed
 */
public class Property {

    /**
     * The original specified value for properties which inherit
     * specified values.
     */
    private String specVal;

    /**
     * Set the original value specified for the property attribute.
     * @param specVal The specified value.
     */
    public void setSpecifiedValue(String specVal) {
        this.specVal = specVal;
    }

    /**
     * Return the original value specified for the property attribute.
     * @return The specified value as a String.
     */
    public String getSpecifiedValue() {
        return specVal;
    }

/*
 * This section contains accessor functions for all possible Property datatypes
 */


    /**
     * This method expects to be overridden by subclasses
     * @return Length property value
     */
    public Length getLength() {
        return null;
    }

    /**
     * This method expects to be overridden by subclasses
     * @return ColorType property value
     */
    public ColorTypeProperty getColorType() {
        return null;
    }

    /**
     * This method expects to be overridden by subclasses
     * @return CondLength property value
     */
    public CondLengthProperty getCondLength() {
        return null;
    }

    /**
     * This method expects to be overridden by subclasses
     * @return LenghtRange property value
     */
    public LengthRangeProperty getLengthRange() {
        return null;
    }

    /**
     * This method expects to be overridden by subclasses
     * @return LengthPair property value
     */
    public LengthPairProperty getLengthPair() {
        return null;
    }

    /**
     * This method expects to be overridden by subclasses
     * @return Space property value
     */
    public SpaceProperty getSpace() {
        return null;
    }

    /**
     * This method expects to be overridden by subclasses
     * @return Keep property value
     */
    public KeepProperty getKeep() {
        return null;
    }

    /**
     * This method expects to be overridden by subclasses
     * @return integer equivalent of enumerated property value
     */
    public int getEnum() {
        return 0;
    }

    /**
     * This method expects to be overridden by subclasses
     * @return char property value
     */
    public char getCharacter() {
        return 0;
    }

    /**
     * This method expects to be overridden by subclasses
     * @return collection of other property (sub-property) objects
     */
    public Vector getList() {
        return null;
    }

    /**
     * This method expects to be overridden by subclasses
     * @return Number property value
     */
    public Number getNumber() {
        return null;
    }

    /**
     * This method expects to be overridden by subclasses
     * @return Numeric property value
     */
    public Numeric getNumeric() {
        return null;
    }

    /**
     * This method expects to be overridden by subclasses
     * @return NCname property value
     */
    public String getNCname() {
        return null;
    }

    /**
     * This method expects to be overridden by subclasses
     * @return Object property value
     */
    public Object getObject() {
        return null;
    }

    /**
     * This method expects to be overridden by subclasses.
     * @return String property value
     */
    public String getString() {
        return null;
    }

    /**
     * Return a string representation of the property value. Only used
     * for debugging.
     */
    public String toString() {
        Object obj = getObject();
        if (obj != this) {
            return obj.toString();
        }
        return null;
    }
}
