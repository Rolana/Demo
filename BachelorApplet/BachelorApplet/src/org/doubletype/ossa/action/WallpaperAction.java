/*
 * $Id: WallpaperAction.java,v 1.2 2004/09/04 14:26:00 eed3si9n Exp $
 * 
 * $Copyright: copyright (c) 2003-2004, e.e d3si9n $
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
 
package org.doubletype.ossa.action;

import java.awt.*;
import java.awt.event.*;
import org.doubletype.ossa.module.*;

/**
 * @author e.e
 */
public class WallpaperAction extends GlyphAction {
	public WallpaperAction() {
		super("wallpaper", k_arrowMove);
	}
	
	public void mousePressed(MouseEvent a_event, GlyphFile a_file) {
		m_lastDragged = a_event.getPoint();	
	}
	
	public void mouseDragged(MouseEvent a_event, GlyphFile a_file) {
		if (m_lastDragged == null) {
			m_lastDragged = a_event.getPoint();
			return;
		} // if
		
		Point endPoint = a_event.getPoint();
		Point startPoint = m_lastDragged;
		m_lastDragged = a_event.getPoint();
			
		int x = endPoint.x - startPoint.x;
		int y = endPoint.y - startPoint.y;
			
		Point delta = new Point(x, y);
		m_display.moveWallpaper(delta);
	}
	
	public void mouseReleased(MouseEvent a_event, GlyphFile a_file) {
		m_lastDragged = null;
	}
}
