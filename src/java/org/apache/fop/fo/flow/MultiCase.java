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

package org.apache.fop.fo.flow;

// FOP
import org.apache.fop.fo.FONode;
import org.apache.fop.layoutmgr.AddLMVisitor;
import org.apache.fop.fo.ToBeImplementedElement;
import org.apache.fop.fo.properties.CommonAccessibility;

/**
 * Class modelling the fo:multi-case object. See Sec. 6.9.4 of the XSL-FO
 * Standard.
 */
public class MultiCase extends ToBeImplementedElement {

    /**
     * @param parent FONode that is the parent of this object
     */
    public MultiCase(FONode parent) {
        super(parent);
    }

    private void setup() {

        // Common Accessibility Properties
        CommonAccessibility mAccProps = propMgr.getAccessibilityProps();

        setupID();
        // this.propertyList.get("starting-state");
        // this.propertyList.get("case-name");
        // this.propertyList.get("case-title");

    }

    public void acceptVisitor(AddLMVisitor aLMV) {
        aLMV.serveMultiCase(this);
    }

    public String getName() {
        return "fo:multi-case";
    }
}
