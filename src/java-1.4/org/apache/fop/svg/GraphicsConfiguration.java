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
 

package org.apache.fop.svg;
/**
 * Adapter to allow subclassing java.awt.GraphicsConfiguration without
 * compilation errors.
 * The version for JDK 1.4 needs to add an override for the abstract
 * createCompatibleVolatileImage() method. It can't be overidden
 * for JDK 1.3 because there is no VolatileImage there. 
 * 
 */
abstract public class GraphicsConfiguration extends java.awt.GraphicsConfiguration {

    public java.awt.image.VolatileImage createCompatibleVolatileImage(int width, int height) {
        return null;
    }

}
