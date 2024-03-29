/**
 * Fontastic
 * A font file writer for Processing.
 * http://code.andreaskoller.com/libraries/fontastic
 *
 * Copyright (C) 2013 Andreas Koller http://andreaskoller.com
 *
 * Uses:
 *  doubletype http://sourceforge.net/projects/doubletype/ for TTF creation
 *  sfntly http://code.google.com/p/sfntly/ for WOFF creation
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      Andreas Koller http://andreaskoller.com
 * @modified    02/27/2013
 * @version     0.3 (3)
 */

package fontastic;


import com.mysql.jdbc.Statement;
import fontastic.FGlyph;
import fontastic.FContour;
import fontastic.FPoint;

import org.doubletype.ossa.*;
import org.doubletype.ossa.module.*;
import org.doubletype.ossa.adapter.*;


import java.io.*;
import java.net.SocketException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

/**
 * Fontastic A font file writer for Processing. http://code.andreaskoller.com/libraries/fontastic
 * 
 */
public class Fontastic{
	
	Connection conn;

	private org.doubletype.ossa.Engine m_engine;

	private String fontname;

	private String TTFfilename;
	private String WOFFfilename;
	private String HTMLfilename;
	String serv;
	
	private List<FGlyph> glyphs;

	private int advanceWidth = 512;

	public final static String VERSION = "0.3";
	/** Uppercase alphabet 26 characters **/
	public final static char alphabet[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z' };
	/** Lowercase alphabet 26 characters **/
	public final static char alphabetLc[] = { 'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z' };

	/**
	 * Return the version of the library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}

	/**
	 * Constructor
	 * 
	 * @example Fontastic f = new Fontastic(this, "MyFont");
	 * 
	 * @param theParent
	 *            Your processing sketch (this).
	 * @param fontname
	 *            Font name
	 * 
	 */
	public Fontastic(String fontname,String serv) {
		this.serv = serv;
		this.fontname = fontname;
		intitialiseFont();
		this.glyphs = new ArrayList<FGlyph>();
	}
	
	/**
	 * Creates and initialises a new typeface. Font data is put into sketch
	 * folder data/fontname.
	 */
	private void intitialiseFont() {
		File data_dir = new File(serv+"/"+fontname);
		if (!data_dir.exists()) {
			data_dir.mkdir();
		}

		File a_dir = new File(serv+"/"+fontname);
		if (!a_dir.exists()) {
			a_dir.mkdir();
		} else {
			deleteFolderContents(a_dir, false);
		}

		m_engine = Engine.getSingletonInstance();
		m_engine.buildNewTypeface(fontname, a_dir);

		this.setFontFamilyName(fontname);
		this.setVersion("CC BY-SA 3.0 http://creativecommons.org/licenses/by-sa/3.0/"); // default
																						// license

		String directoryName = a_dir + File.separator + "bin" + File.separator;

		TTFfilename = directoryName + fontname + ".ttf";
		WOFFfilename = directoryName + fontname + ".woff";
		HTMLfilename = directoryName + "template.html";
	}

	/**
	 * Builds the font and writes the .ttf and the .woff file as well as a HTML template for previewing the WOFF.
	 * If debug is set (default is true) then you'll see the .ttf and .woff file name in the console.
	 * @throws IOException 
	 * @throws SocketException 
	 */
	public void buildFont() {
		// Create TTF file with doubletype	
		m_engine.addDefaultGlyphs();
		
		for (FGlyph glyph : glyphs) {

			GlyphFile glyphFile = m_engine.addNewGlyph(glyph.getGlyphChar());
			glyphFile.setAdvanceWidth(glyph.getAdvanceWidth());

			for (FContour contour : glyph.getContours()) {

				EContour econtour = new EContour();
				econtour.setType(EContour.k_cubic);

				for (FPoint point : contour.points) {

					EContourPoint e = new EContourPoint(point.x, point.y, true);

					if (point.hasControlPoint1()) {
						EControlPoint cp1 = new EControlPoint(true,
								point.controlPoint1.x, point.controlPoint1.y);
						e.setControlPoint1(cp1);
					}

					if (point.hasControlPoint2()) {
						EControlPoint cp2 = new EControlPoint(false,
								point.controlPoint2.x, point.controlPoint2.y);
						e.setControlPoint2(cp2);
					}
					econtour.addContourPoint(e);
				}

				glyphFile.addContour(econtour);
			}
		}

		byte[] fontBytes = m_engine.buildTrueType(false);
		FileOutputStream m;
		File f = new File("testing.ttf");
		try {
			if(!f.exists()) f.createNewFile();
			System.out.println(f.getPath());
			m = new FileOutputStream(f);
			m.write(fontBytes);
			m.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			conn = DriverManager.getConnection(
//					"jdbc:mysql://mysql1001.mochahost.com/wsaadat_fontifi",
//					"wsaadat_fontifi", "no41pass");
//			Statement s1 = (Statement) conn
//					.createStatement();
//			
//			ResultSet r1 = s1
//					.executeQuery("SELECT * FROM Fonts WHERE ID='"+ fontname+"'");
//			
//			if(!r1.next()){
//			PreparedStatement stresult1 = conn
//					.prepareStatement("INSERT INTO `Fonts`(`ID`, `Data`) VALUES('"
//							+ fontname
//							+ "',?)");
//			Blob blob = new SerialBlob(fontBytes);
//			stresult1.setBlob(1, blob);
//			stresult1.executeUpdate();
//			stresult1.close();
//			s1.close();
//			r1.close();
//			System.out.println("Done");
//			}else{
//				PreparedStatement stresult1 = conn
//						.prepareStatement("UPDATE `Fonts` SET `Data`= ? WHERE ID="
//								+ fontname);
//				Blob blob = new SerialBlob(fontBytes);
//				stresult1.setBlob(1, blob);
//				stresult1.executeUpdate();
//				stresult1.close();
//				s1.close();
//				r1.close();
//				System.out.println("Done");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

	/**
	 * Deletes all the glyph files created by doubletype in your data/fontname
	 * folder.
	 */
	public void cleanup() {

		File a_dir = new File(serv+"/"+fontname+"/");
		File[] filesToExclude = new File[3];
		filesToExclude[0] = new File(getTTFfilename());
		filesToExclude[1] = new File(getWOFFfilename());
		filesToExclude[2] = new File(HTMLfilename);

		deleteFolderContents(a_dir, true, filesToExclude);

	}

	/**
	 * Sets the author of the font.
	 */
	public void setAuthor(String author) {
		m_engine.setAuthor(author);
	}

	/**
	 * Sets the version of the font (default is "0.1").
	 */
	public void setVersion(String version) {
		m_engine.getTypeface().getGlyph().getHead().setVersion(version);
	}

	/**
	 * Sets the font family name of the font. Also called in the constructor. If
	 * changed with setFontFamilyName() it won't affect folder the font is
	 * stored in.
	 */
	public void setFontFamilyName(String fontFamilyName) {
		m_engine.setFontFamilyName(fontFamilyName);
	}

	/**
	 * Sets the value of debug
	 * 
	 * @param debug
	 *            true or false
	 */
	public void setDebug(boolean debug) {
	}

	/**
	 * Add a glyph
	 * 
	 * @param c
	 *            Character of the glyph.
	 * 
	 * @return FGlyph that has been created.
	 * 
	 */
	public FGlyph addGlyph(char c) {

		FGlyph glyph = new FGlyph(c);
		glyph.setAdvanceWidth(advanceWidth);
		glyphs.add(glyph);
		return glyph;

	}

	/**
	 * Add a glyph and its one contour
	 * 
	 * @param c
	 *            Character of the glyph.
	 * 
	 * @param FContour
	 *            Shape of the glyph as FContour.
	 * 
	 * @return The glyph FGlyph that has been created. You can use this to store
	 *         the glyph and add contours afterwards. Alternatively, you can
	 *         call getGlyph(char c) to retrieve it.
	 */
	public FGlyph addGlyph(char c, FContour contour) {

		FGlyph glyph = new FGlyph(c);
		glyphs.add(glyph);

		glyph.addContour(contour);

		glyph.setAdvanceWidth(advanceWidth);
		return glyph;

	}

	/**
	 * Add a glyph and its contours
	 * 
	 * @param c
	 *            Character of the glyph.
	 * 
	 * @param FContour
	 *            [] Shape of the glyph in an array of FContour.
	 * 
	 * @return The FGlyph that has been created. You can use this to store the
	 *         glyph and add contours afterwards. Alternatively, you can call
	 *         getGlyph(char c) to retrieve it.
	 */
	public FGlyph addGlyph(char c, FContour[] contours) {

		FGlyph glyph = new FGlyph(c);
		glyphs.add(glyph);

		for (FContour contour : contours) {
			glyph.addContour(contour);
		}
		glyph.setAdvanceWidth(advanceWidth);
		return glyph;

	}

	/**
	 * Get glyph by character
	 * 
	 * @param c
	 *            The character of the glyph
	 * 
	 * @return The glyph
	 */

	public FGlyph getGlyph(char c) {

		FGlyph glyph = null;
		for (int i = 0; i < glyphs.size(); i++) {
			if (glyphs.get(i).getGlyphChar() == c) {
				glyph = glyphs.get(i);
				break;
			}
		}
		return glyph;

	}

	/**
	 * Engine getter
	 * 
	 * @return The doubletype Engine used for font creation, so that you can
	 *         access all functions of doubletype in case you need them.
	 */
	public Engine getEngine() {
		return m_engine;
	}

	/**
	 * Returns the TypefaceFile
	 * 
	 * @return The doubletype TypefaceFile used for font creation, so that you
	 *         can access functions of doubletype in case you need them.
	 */
	public TypefaceFile getTypefaceFile() {
		return m_engine.getTypeface();
	}

	/**
	 * Returns the .ttf file name
	 * 
	 * @return The .ttf file name, which is being created when you call build()
	 */
	public String getTTFfilename() {
		return TTFfilename;
	}

	/**
	 * Returns the .woff file name
	 * 
	 * @return The .woff file name, which is being created when you call build()
	 */
	public String getWOFFfilename() {
		return WOFFfilename;
	}

	private static void deleteFolderContents(File folder,
			boolean deleteFolderItself) {
		File[] files = folder.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolderContents(f, true);
					f.delete();
				} else {
					f.delete();
				}
			}
		}
		if (deleteFolderItself)
			folder.delete();
	}

	private static void deleteFolderContents(File folder,
			boolean deleteFolderItself, File[] exceptions) {
		File[] files = folder.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
					if (f.isDirectory()) {
						deleteFolderContents(f, true, exceptions);
						f.delete();
					} else {
						f.delete();
					}
			}
		}
		if (deleteFolderItself)
			folder.delete();
	}
}
