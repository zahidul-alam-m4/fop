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

package org.apache.fop.fo.pagination;

import java.awt.Rectangle;

import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.FODimension;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FOTreeVisitor;
import org.apache.fop.fo.FObj;
import org.xml.sax.Attributes;

/**
 * This is an abstract base class for pagination regions
 */
public abstract class Region extends FObj {

    /** Key for before regions */
    public static final String BEFORE = "before";
    /** Key for start regions */
    public static final String START =  "start";
    /** Key for end regions */
    public static final String END =    "end";
    /** Key for after regions */
    public static final String AFTER =  "after";
    /** Key for body regions */
    public static final String BODY =   "body";

    /* The following codes correspond to those found in area/RegionReference */
    /** Integer Code for before regions */
    public static final int BEFORE_CODE = 0;
    /** Integer Code for start regions */
    public static final int START_CODE = 1;
    /** Integer Code for body regions */
    public static final int BODY_CODE = 2;
    /** Integer Code for end regions */
    public static final int END_CODE = 3;
    /** Integer Code for after regions */
    public static final int AFTER_CODE = 4;

    private SimplePageMaster layoutMaster;
    private String regionName;

    /** Holds the overflow attribute */
    public int overflow;
    /** Holds the writing mode */
    protected int wm;

    /**
     * @see org.apache.fop.fo.FONode#FONode(FONode)
     */
    protected Region(FONode parent) {
        super(parent);
    }

    /**
     * @see org.apache.fop.fo.FONode#handleAttrs(Attributes)
     */
    public void handleAttrs(Attributes attlist) throws FOPException {
        super.handleAttrs(attlist);

        // regions may have name, or default
        if (null == this.propertyList.get(PR_REGION_NAME)) {
            setRegionName(getDefaultRegionName());
        } else if (this.propertyList.get(PR_REGION_NAME).getString().equals("")) {
            setRegionName(getDefaultRegionName());
        } else {
            setRegionName(this.propertyList.get(PR_REGION_NAME).getString());
            // check that name is OK. Not very pretty.
            if (isReserved(getRegionName())
                    && !getRegionName().equals(getDefaultRegionName())) {
                throw new FOPException("region-name '" + regionName
                        + "' for " + this.name
                        + " not permitted.");
            }
        }

        if (parent instanceof SimplePageMaster) {
            layoutMaster = (SimplePageMaster)parent;
        } else {
            throw new FOPException(this.name + " must be child "
                    + "of simple-page-master, not "
                    + parent.getName());
        }
        this.wm = this.propertyList.get(PR_WRITING_MODE).getEnum();

        // this.propertyList.get("clip");
        // this.propertyList.get("display-align");
        this.overflow = this.propertyList.get(PR_OVERFLOW).getEnum();
    }

    public abstract Rectangle getViewportRectangle(FODimension pageRefRect);

    /**
     * Returns the default region name (xsl-region-before, xsl-region-start,
     * etc.)
     * @return the default region name
     */
    protected abstract String getDefaultRegionName();


    /**
     * Returns the region class name.
     * @return the region class name
     */
    public abstract String getRegionClass();

    /**
     * Returns the region class code.
     * @return the region class code
     */
    public abstract int getRegionClassCode();

    /**
     * Returns the name of this region.
     * @return the region name
     */
    public String getRegionName() {
        return this.regionName;
    }

    /**
     * Sets the name of the region.
     * @param name the name
     */
    private void setRegionName(String name) {
        this.regionName = name;
    }

    /**
     * Returns the page master associated with this region.
     * @return a simple-page-master
     */
    protected SimplePageMaster getPageMaster() {
        return this.layoutMaster;
    }

    /**
     * Checks to see if a given region name is one of the reserved names
     *
     * @param name a region name to check
     * @return true if the name parameter is a reserved region name
     */
    protected boolean isReserved(String name) /*throws FOPException*/ {
        return (name.equals("xsl-region-before")
                || name.equals("xsl-region-start")
                || name.equals("xsl-region-end")
                || name.equals("xsl-region-after")
                || name.equals("xsl-before-float-separator")
                || name.equals("xsl-footnote-separator"));
    }

    /**
     * @see org.apache.fop.fo.FObj#generatesReferenceAreas()
     */
    public boolean generatesReferenceAreas() {
        return true;
    }

    /**
     * Returns a sibling region for this region.
     * @param regionClass the class of the requested region
     * @return the requested region
     */
    protected Region getSiblingRegion(String regionClass) {
        // Ask parent for region
        return  layoutMaster.getRegion(regionClass);
    }

    /**
     * Indicates if this region gets precedence.
     * @return True if it gets precedence
     */
    public boolean getPrecedence() {
        return false;
    }

    public int getExtent() {
        return 0;
    }

    /**
     * This is a hook for an FOTreeVisitor subclass to be able to access
     * this object.
     * @param fotv the FOTreeVisitor subclass that can access this object.
     * @see org.apache.fop.fo.FOTreeVisitor
     */
    public void acceptVisitor(FOTreeVisitor fotv) {
        fotv.serveRegion(this);
    }

}
