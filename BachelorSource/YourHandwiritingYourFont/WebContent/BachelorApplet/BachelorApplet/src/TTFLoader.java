import javax.swing.JApplet;
import javax.swing.SwingUtilities;



public class TTFLoader extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	String userID;
	ImageLoaderGUI gui;
	
	public void start() {
		try {
			userID = this.getParameter("userID");
            SwingUtilities.invokeAndWait( new Runnable() {
                public void run() {
                    gui = new ImageLoaderGUI(userID);
                    getContentPane().add(gui);
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
	}
	
	public String getState(){
		return gui.state + "";
	}
	
}
