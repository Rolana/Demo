import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class ImageLoaderGUI extends JPanel {

	JLabel selected;
	File imageFile;
	JPanel center;
	JPanel rect;
	int state = 0;
	int count = 0;
	String userID;
	JLabel image;
	JScrollPane imageScroll;
	JPanel buttonschecked;
	int x = 0, y = 0, w = 0, h = 0;
	ArrayList<Character> characterwith = new ArrayList<Character>();
	ArrayList<Integer> xs = new ArrayList<Integer>();
	ArrayList<Integer> ys = new ArrayList<Integer>();
	ArrayList<Integer> ws = new ArrayList<Integer>();
	ArrayList<Integer> hs = new ArrayList<Integer>();

	int xrec;
	int yrec;
	int wrec;
	int hrec;

	boolean clicked = false;
	JPanel buttons;
	JLayeredPane main;
	JButton gen;

	public ImageLoaderGUI(String userID) {
		super(null);
		this.userID = userID;
		this.setVisible(true);
		this.setBackground(new Color(238, 238, 238));
		home();
	}

	private class MenuButton extends JButton {
		// Override paintComponent to perform your own painting

		public MenuButton(String s) {
			super(s);
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);// paint parent's background
			g.setColor(new Color(12, 37, 173));
			if (getText().equals("Generate Font")) {
				g.fillRoundRect(0, 0, 200, 100, 20, 20);
			} else {
				g.fillRoundRect(0, 0, 60, 20, 10, 10);
			}
			g.setColor(Color.white);
			if (getText().equals("Browse")) {
				g.drawString(getText(), 8, 15);
			} else {
				if (getText().equals("Next")) {
					g.drawString(getText(), 15, 15);
				} else {
					g.setFont(new Font("Arial", 20, 20));
					g.drawString(getText(), 35, 55);
				}
			}
		}
	}

	public void home() {
		this.removeAll();
		center = new JPanel(null);
		center.setBounds(200, 50, 650, 210);
		add(center);
		center.setVisible(true);
		JLabel text = new JLabel("Step1:");
		text.setBounds(50, 30, 100, 50);
		text.setVisible(true);
		center.add(text);
		
		JTextArea hint = new JTextArea("Fontifi needs a scanned image of your handwriting, \nto select characters from it for the creation of your font");
		hint.setEditable(false);
		AbstractBorder brdr = new TextBubbleBorder(Color.WHITE, 2, 10, 0);
		hint.setBorder(brdr);
//		JLabel hint = new JLabel(
//				"Fontifi needs a scanned image of your handwriting,");
		hint.setBounds(55, 65, 400, 50);
		hint.setVisible(true);
		center.add(hint);
		
//		JLabel hint1 = new JLabel("to select characters from it for the creation of your font");
//		hint1.setBounds(55, 85, 650, 20);
//		hint1.setVisible(true);
//		center.add(hint1);
		

		selected = new JLabel("Choose an image");
		selected.setBounds(120, 130, 200, 20);
		center.add(selected);

		JButton browse = new MenuButton("Browse");
		browse.setBounds(50, 130, 60, 20);
		browse.setVisible(true);
		browse.setBorderPainted(false);

		browse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Images", "jpg", "gif", "png");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(center);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					selected.setText(chooser.getSelectedFile().getName());
					imageFile = chooser.getSelectedFile();
				}
			}
		});
		center.add(browse);

		JButton next = new MenuButton("Next");
		next.setBounds(140, 170, 60, 20);
		next.setBorderPainted(false);
		next.setVisible(true);
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							while (center.getX() + center.getWidth() > 0) {
								Thread.sleep(1);
								int x = center.getX() - 1;
								int y = center.getY();
								center.setLocation(x, y);
							}
							viewImage();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
				t.start();

			}
		});
		center.add(next);
		center.setBackground(new Color(238, 238, 238));

		validate();
		repaint();
	}

	public void viewImage() {
		this.removeAll();
		main = new JLayeredPane();
		main.setBounds(0, 0, this.getWidth(), this.getHeight());
		main.setVisible(true);
		main.setBackground(new Color(238, 238, 238));
		this.add(main);
		image = new JLabel();
		ImageIcon icon = new ImageIcon(imageFile.getPath());
		image.setIcon(icon);
		image.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
		image.setVisible(true);
		rect = new JPanel();
		rect.setBackground(new Color(0, 0, 0, 50));
		rect.setSize(0, 0);
		rect.setLocation(0, 0);

		// buttons = new JPanel(new GridLayout(3, 28));
		buttons = new JPanel(null);
		buttons.setVisible(true);
		buttons.setCursor(new Cursor(Cursor.HAND_CURSOR));
		buttons.setBounds(50, 470, 700, 90);

		// Buttons Checklist
		buttonschecked = new JPanel(new GridLayout(28, 3));
		buttonschecked.setVisible(true);
		buttonschecked.setCursor(new Cursor(Cursor.HAND_CURSOR));
		buttonschecked.setBounds(50, 5, 70, 550);

		// Image Holder and controller
		final JPanel imageHolder = new JPanel();
		imageHolder.setBounds(130, 50, 530, 400);
		imageHolder.setBackground(new Color(238, 238, 238));
		imageHolder.setVisible(true);
		imageHolder.add(image);

		imageScroll = new JScrollPane(imageHolder);
		imageScroll.setVisible(true);
		imageScroll.setBounds(130, 50, 530, 400);
		image.add(rect);
		image.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		image.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				clicked = false;

			}

			@Override
			public void mousePressed(MouseEvent e) {
				xrec = e.getX();
				yrec = e.getY();
				rect.setLocation(xrec, yrec);
				rect.setSize(0, 0);
				clicked = true;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		image.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent arg0) {

			}

			@Override
			public void mouseDragged(MouseEvent arg0) {
				if (clicked) {
					wrec = Math.abs(arg0.getX() - xrec);
					hrec = Math.abs(arg0.getY() - yrec);
					if (arg0.getY() > yrec && xrec > arg0.getX()) {
						rect.setLocation(arg0.getX(), yrec);
					}
					if (arg0.getY() < yrec && xrec < arg0.getX()) {
						rect.setLocation(xrec, arg0.getY());
					}

					if (arg0.getY() < yrec && xrec > arg0.getX()) {
						rect.setLocation(arg0.getX(), arg0.getY());
					}
					rect.setSize(wrec, hrec);
					validate();
					repaint();
				}
			}
		});

		gen = new MenuButton("Generate Font");
		gen.setBounds(280, 550, 200, 100);
		gen.setBorderPainted(false);
		gen.setVisible(true);
		gen.addActionListener(new ActionListener() {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					state = 1;
					int[][] CharData = { {} };
					char[] Characters = {};
					int[] Width = {};
					int[] Height = {};
					BufferedImage image = ImageIO.read(imageFile);
					CharData = new int[characterwith.size()][];
					Width = new int[characterwith.size()];

					Height = new int[characterwith.size()];

					Characters = new char[characterwith.size()];
					int[] imagePixels = {};
					int[] RGBholder = {};
					int counter = 0;
					for (int i = 0; i < characterwith.size(); i++) {
						Characters[i] = characterwith.get(i);
						int x1 = xs.get(i);
						int y1 = ys.get(i);
						int w = ws.get(i);
						int h = hs.get(i);
						imagePixels = new int[w * h];
						if (image.getColorModel().getPixelSize() == 32) {
							RGBholder = new int[w * h * 4];
							image.getRaster()
									.getPixels(x1, y1, w, h, RGBholder);
							for (int j = 0; j < RGBholder.length; j += 4) {
								imagePixels[counter] = RGBholder[j];
								counter++;
							}
							counter = 0;

						} else if (image.getColorModel().getPixelSize() == 24) {
							RGBholder = new int[w * h * 3];
							image.getRaster()
									.getPixels(x1, y1, w, h, RGBholder);
							for (int j = 0; j < RGBholder.length; j += 3) {
								imagePixels[counter] = RGBholder[j];
								counter++;
							}
							counter = 0;

						} else {
							image.getRaster().getPixels(x1, y1, w, h,
									imagePixels);
						}
						Width[i] = w;
						Height[i] = h;
						CharData[i] = imagePixels;
					}

					final FontCreation a = new FontCreation(CharData,
							Characters, Width, Height, userID, imageFile
									.getParent());

					java.security.AccessController
							.doPrivileged(new java.security.PrivilegedAction() {
								public Object run() {
									// execute the privileged command
									a.makeFont();
									// we must return an object, so we'll return
									// an empty string
									return new Object();
								}
							});
					JOptionPane.showMessageDialog(null,
							"Your Font was created successfully");
					state = 2;
					home();
				} catch (IOException e) {
					System.out.println("IOException");
				}
			}
		});

		// buttons actionListener
		drawButtonswithBounds();

		main.add(gen, 1000);
		main.add(imageScroll, 1000);
		main.add(buttons, 1000);
		main.add(buttonschecked, 1000);
		validate();
		repaint();
	}

	class characterSelected implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				final JButton clicked = (JButton) arg0.getSource();
				final char a = clicked.getText().charAt(0);
				if (rect.getWidth() == 0) {
					JOptionPane.showMessageDialog(null,
							"Please Select a letter from the image first");
					return;
				}
				if (characterwith.contains(a)) {
					int i = characterwith.indexOf(a);
					xs.set(i, rect.getX());
					ys.set(i, rect.getY());
					ws.set(i, rect.getWidth());
					hs.set(i, rect.getHeight());
				} else {
					characterwith.add(a);
					xs.add(rect.getX());
					ys.add(rect.getY());
					ws.add(rect.getWidth());
					hs.add(rect.getHeight());
				}

				final JLabel characterToMove = new JLabel(a + "");
				characterToMove.setFont(new Font("Arial", 10, rect.getWidth()));
				characterToMove.setBounds(imageScroll.getX() + image.getX()
						+ rect.getX(),
						imageScroll.getY() + image.getY() + rect.getY(),
						rect.getWidth(), rect.getHeight());
				characterToMove.setVisible(true);

//				for (int i = 0; i < buttonschecked.getComponents().length; i++) {
//					JButton comp = (JButton) buttonschecked.getComponents()[i];
//					if (comp.getText().charAt(0) == a) {
//						comp.setEnabled(true);
//					}
//				}

				main.add(characterToMove);
				main.setLayer(characterToMove, 1000);
				validate();
				repaint();

				// Animation after Selection
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						int x = characterToMove.getX();
						int y = characterToMove.getY();

						int cW = characterToMove.getWidth();
						int cH = characterToMove.getHeight();

						characterToMove.setFont(new Font("Arial", 10, cW));

						int xTo = buttons.getX() + clicked.getX() + 3;
						int yTo = buttons.getY() + clicked.getY();

						int xW = clicked.getWidth();
						int xH = clicked.getHeight();

						while (y != yTo || x != xTo) {
							try {
								if (x > xTo && y != yTo)
									characterToMove.setLocation(x--, y++);
								if (x > xTo && y == yTo)
									characterToMove.setLocation(x--, y);

								if (x < xTo && y == yTo)
									characterToMove.setLocation(x++, y);

								if (x < xTo && y != yTo)
									characterToMove.setLocation(x++, y++);

								if (x == xTo && y != yTo)
									characterToMove.setLocation(x, y++);

								if (cH > xH && cW > xW) {
									characterToMove.setSize(cW--, cH--);
								}

								if (cH > xH && cW == xW) {
									characterToMove.setSize(cW, cH--);
								}

								if (cH == xH && cW > xW) {
									characterToMove.setSize(cW--, cH);
								}

								characterToMove.setFont(new Font("Arial", 20,
										cW));

								Thread.sleep(2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();

							}
						}
						characterToMove.setVisible(false);
						rect.setBounds(0, 0, 0, 0);
						for (int i = 0; i < buttons.getComponents().length; i++) {
							JButton comp = (JButton) buttons.getComponents()[i];
							if (comp.getText().charAt(0) == a) {
								comp.setForeground(new Color(0, 200, 0));
							}
						}

						for (int i = 0; i < buttonschecked.getComponents().length; i++) {
							JButton comp = (JButton) buttonschecked
									.getComponents()[i];
							if (comp.getText().charAt(0) == a) {
								comp.setEnabled(false);
							}
						}

					}
				});
				t.start();
				validate();
				repaint();
			} catch (NullPointerException e) {

			} catch (StringIndexOutOfBoundsException f) {

			}
		}

	}

	public void drawButtonswithBounds() {
		int countButtons = 0;
		int w = 25;
		int h = 25;
		// add Characters
		for (int i = 65; i <= 90; i++) {
			char label = (char) i;
			JButton charac = new JButton(label + "");
			charac.addActionListener(new characterSelected());
			int x = countButtons % 28 * w;
			int y = countButtons / 28 * h;
			charac.setBounds(x, y, w, h);
			countButtons++;
			buttons.add(charac);
		}

		for (int i = 97; i <= 122; i++) {
			char label = (char) i;
			JButton charac = new JButton(label + "");
			charac.addActionListener(new characterSelected());
			int x = countButtons % 28 * w;
			int y = countButtons / 28 * h;
			charac.setBounds(x, y, w, h);
			countButtons++;
			buttons.add(charac);
		}

		// Add numbers
		for (int i = 48; i <= 57; i++) {
			char label = (char) i;
			JButton charac = new JButton(label + "");
			charac.addActionListener(new characterSelected());
			int x = countButtons % 28 * w;
			int y = countButtons / 28 * h;
			charac.setBounds(x, y, w, h);
			countButtons++;
			buttons.add(charac);
		}

		// Add symbols
		for (int i = 33; i <= 47; i++) {
			char label = (char) i;
			JButton charac = new JButton(label + "");
			charac.addActionListener(new characterSelected());
			int x = countButtons % 28 * w;
			int y = countButtons / 28 * h;
			charac.setBounds(x, y, w, h);
			countButtons++;
			buttons.add(charac);
		}

		for (int i = 58; i <= 64; i++) {
			char label = (char) i;
			JButton charac = new JButton(label + "");
			charac.addActionListener(new characterSelected());
			int x = countButtons % 28 * w;
			int y = countButtons / 28 * h;
			charac.setBounds(x, y, w, h);
			countButtons++;
			buttons.add(charac);
		}
	}

	public static void main(String[] args) {
		JFrame testFrame = new JFrame("Test only");
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.setVisible(true);
		testFrame.setBounds(0, 0, 800, 800);
		testFrame.setContentPane(new ImageLoaderGUI("testing font"));
		testFrame.validate();
		testFrame.repaint();
	}

}
