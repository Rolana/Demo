/*
 * $Id: GlyphModule.java,v 1.12 2004/09/04 21:54:19 eed3si9n Exp $
 * 
 * $Copyright: copyright (c) 2003, e.e d3si9n $
 * $License: 
 * This source code is part of DoubleType.
 * DoubleType is a graphical typeface designer.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This Program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * In addition, as a special exception, e.e d3si9n gives permission to
 * link the code of this program with any Java Platform that is available
 * to public with free of charge, including but not limited to
 * Sun Microsystem's JAVA(TM) 2 RUNTIME ENVIRONMENT (J2RE),
 * and distribute linked combinations including the two.
 * You must obey the GNU General Public License in all respects for all 
 * of the code used other than Java Platform. If you modify this file, 
 * you may extend this exception to your version of the file, but you are not
 * obligated to do so. If you do not wish to do so, delete this exception
 * statement from your version.
 * $
 */

package org.doubletype.ossa.module;

import org.doubletype.ossa.adapter.*;
import org.doubletype.ossa.*;
import java.util.*;

/**
 * @author e.e
 */
public class GlyphModule implements VarStackFrame {
	protected VarStack m_stack = VarStack.getSingletonInstance();
	private Hashtable m_vars = new Hashtable();
	protected ActiveList m_actives = ActiveList.getSingletonInstance();
	
	// --------------------------------------------------------------
	
	public GlyphModule() {
	}
	
	public Hashtable getVars() {
		return m_vars;
	}
	
	// for var stack frame
	public boolean hasVariable(String a_name) {
		return m_vars.containsKey(a_name);
	}
	
	// for var stack frame.
	public double getValue(String a_name) {		
		Double d = (Double) m_vars.get(a_name);
		return d.doubleValue();
	}
	
	// for var stack frame. 
	public void beforePush() {
	}
	
	/**
	 * Base for generating contours from modules.
	 * @param a_moduleInvoke
	 * @return XContour object which represents the module.
	 */
	public EContour toContour(EModuleInvoke a_moduleInvoke) {
		throw new RuntimeException("Unimplemented contour creation.");	
	}
	
	public void addVar(String a_name, double a_value) {
		m_vars.put(a_name, new Double(a_value));	
	}
	
	public int getMinimumPointCount() {
		return 2;
	}
}
