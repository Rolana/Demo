package org.techzoo.servlet3;

import java.io.*;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Statement;

/**
 * Servlet implementation class ttfLoad
 */
@WebServlet("/ttfLoad")
public class ttfLoad extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection conn;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ttfLoad() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String userID = (String) request.getSession().getAttribute("userID");

		if (userID.length() > 0) {
			if (request.getQueryString() != null
					&& request.getQueryString().length() > 0) {
				try {
//					System.out.println("Uploading");
					Class.forName("com.mysql.jdbc.Driver");
					conn = DriverManager.getConnection(
							"jdbc:mysql://mysql1001.mochahost.com/wsaadat_fontifi",
							"wsaadat_fontifi", "no41pass");
					Statement stresult1 = (Statement) conn
							.createStatement();
					ResultSet r1 = stresult1
							.executeQuery("SELECT * FROM Fonts WHERE ID='"+ request.getQueryString()+"'");
					Blob font = null;
					byte[] data = null;
//					System.out.println(request.getQueryString());
				       while (r1.next()) {
				    	   font = r1.getBlob("Data");
				    	   data = font.getBytes(1, (int) font.length());
				        }
					r1.close();
					stresult1.close();
				
//				ServletContext ctx = getServletContext();
//				InputStream is = ctx.getResourceAsStream("/fonts"+"/"
//						+ request.getQueryString() + "/bin/"
//						+ request.getQueryString() + ".ttf");
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + "yourfont.ttf" + "\"");
					ServletOutputStream os = response.getOutputStream();
//					byte[] bufferData = new byte[1024];
//					int read = 0;
//					byte[] data = Bytes.getBytes();
					
//					InputStream is = new FileInputStream(f);
//					while ((read = is.read(bufferData)) != -1) {
//						os.write(bufferData, 0, read);
					os.write(data);
//					}
					os.flush();
					os.close();
//					is.close();
				
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					
//					System.out.println("Uploading");
					Class.forName("com.mysql.jdbc.Driver");
					conn = DriverManager.getConnection(
							"jdbc:mysql://mysql1001.mochahost.com/wsaadat_fontifi",
							"wsaadat_fontifi", "no41pass");
					Statement stresult1 = (Statement) conn
							.createStatement();
					ResultSet r1 = stresult1
							.executeQuery("SELECT * FROM Fonts WHERE ID='"+ userID+"'");
					Blob font = null;
					byte[] data = null;
//					System.out.println(request.getQueryString());
				       while (r1.next()) {
				    	   font = r1.getBlob("Data");
				    	   data = font.getBytes(1, (int) font.length());
				        }
					r1.close();
					stresult1.close();
				
//				ServletContext ctx = getServletContext();
//				InputStream is = ctx.getResourceAsStream("/fonts"+"/"
//						+ request.getQueryString() + "/bin/"
//						+ request.getQueryString() + ".ttf");
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + "yourfont.ttf" + "\"");
					ServletOutputStream os = response.getOutputStream();
//					byte[] bufferData = new byte[1024];
//					int read = 0;
//					byte[] data = Bytes.getBytes();
					
//					InputStream is = new FileInputStream(f);
//					while ((read = is.read(bufferData)) != -1) {
//						os.write(bufferData, 0, read);
					os.write(data);
//					}
					os.flush();
					os.close();
//					is.close();
				
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String userID = (String) request.getSession().getAttribute("userID");

		if (userID.length() > 0) {
			if (request.getQueryString() != null
					&& request.getQueryString().length() > 0) {
				try {
//					System.out.println("Uploading");
					Class.forName("com.mysql.jdbc.Driver");
					conn = DriverManager.getConnection(
							"jdbc:mysql://mysql1001.mochahost.com/wsaadat_fontifi",
							"wsaadat_fontifi", "no41pass");
					Statement stresult1 = (Statement) conn
							.createStatement();
					ResultSet r1 = stresult1
							.executeQuery("SELECT * FROM Fonts WHERE ID='"+ request.getQueryString()+"'");
					Blob font = null;
					byte[] data = null;
//					System.out.println(request.getQueryString());
				       while (r1.next()) {
				    	   font = r1.getBlob("Data");
				    	   data = font.getBytes(1, (int) font.length());
				        }
					r1.close();
					stresult1.close();
				
//				ServletContext ctx = getServletContext();
//				InputStream is = ctx.getResourceAsStream("/fonts"+"/"
//						+ request.getQueryString() + "/bin/"
//						+ request.getQueryString() + ".ttf");
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + "yourfont.ttf" + "\"");
					ServletOutputStream os = response.getOutputStream();
//					byte[] bufferData = new byte[1024];
//					int read = 0;
//					byte[] data = Bytes.getBytes();
					
//					InputStream is = new FileInputStream(f);
//					while ((read = is.read(bufferData)) != -1) {
//						os.write(bufferData, 0, read);
					os.write(data);
//					}
					os.flush();
					os.close();
//					is.close();
				
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
//					System.out.println("Uploading");
					Class.forName("com.mysql.jdbc.Driver");
					conn = DriverManager.getConnection(
							"jdbc:mysql://mysql1001.mochahost.com/wsaadat_fontifi",
							"wsaadat_fontifi", "no41pass");
					Statement stresult1 = (Statement) conn
							.createStatement();
					ResultSet r1 = stresult1
							.executeQuery("SELECT * FROM Fonts WHERE ID='"+ userID+"'");
					Blob font = null;
					byte[] data = null;
//					System.out.println(request.getQueryString());
				       while (r1.next()) {
				    	   font = r1.getBlob("Data");
				    	   data = font.getBytes(1, (int) font.length());
				        }
					r1.close();
					stresult1.close();
				
//				ServletContext ctx = getServletContext();
//				InputStream is = ctx.getResourceAsStream("/fonts"+"/"
//						+ request.getQueryString() + "/bin/"
//						+ request.getQueryString() + ".ttf");
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + "yourfont.ttf" + "\"");
					ServletOutputStream os = response.getOutputStream();
//					byte[] bufferData = new byte[1024];
//					int read = 0;
//					byte[] data = Bytes.getBytes();
					
//					InputStream is = new FileInputStream(f);
//					while ((read = is.read(bufferData)) != -1) {
//						os.write(bufferData, 0, read);
					os.write(data);
//					}
					os.flush();
					os.close();
//					is.close();
				
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
