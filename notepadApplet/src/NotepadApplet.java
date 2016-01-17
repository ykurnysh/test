import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.io.*;
import java.util.*;
import java.util.Timer;
import java.util.List;
import java.net.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.Highlighter.HighlightPainter;

import java.text.AttributedCharacterIterator.Attribute;

public class NotepadApplet extends JApplet {
	private JToolBar toolbar;
	private JButton boldButton;
	private JButton italicButton;
	private JButton underlinedButton;
	private JButton strikedButton;
	private JButton leftAlignmentButton;
	private JButton centerAlignmentButton;
	private JButton rightAlignmentButton;
	private JButton increaseIndentButton;
	private JButton decreaseIndentButton;
	private JButton tableButton;
	private JButton undoButton;
	private JButton redoButton;
	private JTextPane textpane;
	private int beforeDeleted;
	private int afterDeleted;
	private String buffer;
	private Timer t;
	private TimerTask tt = null;
	private int caret;

	private void reset() {

		buffer = "";
		beforeDeleted = 0;
		afterDeleted = 0;
		caret = textpane != null ? textpane.getCaretPosition()-1 : 0;
	}

	private void bold() {
		try {
			for (Highlight i : textpane.getHighlighter().getHighlights()) {
				getAppletContext().showDocument(
						new URL(getCodeBase() + "documents/bold/" + new Integer(i.getStartOffset() - 1).toString() + "/"
								+ new Integer(i.getEndOffset() - 2).toString()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void italic() {
		try {
			for (Highlight i : textpane.getHighlighter().getHighlights()) {
				getAppletContext().showDocument(
						new URL(getCodeBase() + "documents/italic/" + new Integer(i.getStartOffset() - 1).toString()
								+ "/" + new Integer(i.getEndOffset() - 2).toString()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void underlined() {
		try {
			for (Highlight i : textpane.getHighlighter().getHighlights()) {
				getAppletContext().showDocument(
						new URL(getCodeBase() + "documents/underlined/" + new Integer(i.getStartOffset() - 1).toString()
								+ "/" + new Integer(i.getEndOffset() - 2).toString()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void striked() {
		try {
			for (Highlight i : textpane.getHighlighter().getHighlights()) {
				getAppletContext().showDocument(
						new URL(getCodeBase() + "documents/striked/" + new Integer(i.getStartOffset() - 1).toString()
								+ "/" + new Integer(i.getEndOffset() - 2).toString()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void alignment(final int x) {
		try {
			for (Highlight i : textpane.getHighlighter().getHighlights()) {
				getAppletContext().showDocument(new URL(getCodeBase() + "documents/alignment/"
						+ new Integer(x).toString() + "/" + new Integer(i.getStartOffset() - 1)));
			}
			getAppletContext().showDocument(new URL(getCodeBase() + "documents/alignment/" + new Integer(x).toString()
					+ "/" + new Integer(textpane.getCaretPosition()).toString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void indent(final double x) {
		try {
			for (Highlight i : textpane.getHighlighter().getHighlights()) {
				getAppletContext().showDocument(new URL(getCodeBase() + "documents/alignment/"
						+ new Double(x).toString() + "/" + new Integer(i.getStartOffset() - 1)));
			}
			getAppletContext().showDocument(new URL(getCodeBase() + "documents/indent/" + new Double(x).toString() + "/"
					+ new Integer(textpane.getCaretPosition()).toString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void undo() {
		try {
			getAppletContext().showDocument(new URL(getCodeBase() + "documents/undoaction"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void redo() {
		try {
			getAppletContext().showDocument(new URL(getCodeBase() + "documents/redoaction"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void deleteAfter() {
		try {
			getAppletContext()
					.showDocument(new URL(getCodeBase() + "documents/deleteafter/" + afterDeleted + "/" + caret));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void deleteBefore() {
		try {
			getAppletContext()
					.showDocument(new URL(getCodeBase() + "documents/deletebefore/" + beforeDeleted + "/" + caret));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void insert() {
		try {
			String encode = URLEncoder.encode(buffer, "UTF-8");
			getAppletContext().showDocument(new URL(getCodeBase() + "documents/insert/" + encode + "/" + caret));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void init() {
		reset();
		t = new Timer();
		toolbar = new JToolBar();
		toolbar.setSize(500, 30);
		toolbar.setLocation(0, 0);
		textpane = new JTextPane();
		textpane.setLocation(0, 60);
		textpane.setSize(900, 350);
		boldButton = new JButton("B");
		boldButton.setSize(16, 16);
		boldButton.setFont(new Font("Times New Roman", Font.BOLD, 15));
		italicButton = new JButton("I");
		italicButton.setSize(16, 16);
		italicButton.setFont(new Font("Times New Roman", Font.ITALIC, 15));
		underlinedButton = new JButton("U");
		underlinedButton.setSize(16, 16);
		Map<? extends Attribute, ?> m = new HashMap<TextAttribute, Integer>();
		((HashMap<TextAttribute, Integer>) m).put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		underlinedButton.setFont(new Font("Times New Roman", Font.PLAIN, 15).deriveFont(m));
		strikedButton = new JButton("S");
		strikedButton.setSize(16, 16);
		m = new HashMap<TextAttribute, Boolean>();
		((HashMap<TextAttribute, Boolean>) m).put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		strikedButton.setFont(new Font("Times New Roman", Font.PLAIN, 15).deriveFont(m));
		leftAlignmentButton = new JButton("\ue800");
		try {
			leftAlignmentButton.setFont(Font
					.createFont(Font.TRUETYPE_FONT,
							getClass().getResourceAsStream("/resources/fontawesome-etherpad.ttf"))
					.deriveFont(Font.PLAIN, 15));
		} catch (FontFormatException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		leftAlignmentButton.setSize(16, 16);
		centerAlignmentButton = new JButton("\ue80f");
		try {
			centerAlignmentButton.setFont(Font
					.createFont(Font.TRUETYPE_FONT,
							getClass().getResourceAsStream("/resources/fontawesome-etherpad.ttf"))
					.deriveFont(Font.PLAIN, 15));
		} catch (FontFormatException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		centerAlignmentButton.setSize(16, 16);
		rightAlignmentButton = new JButton("\ue810");
		try {
			rightAlignmentButton.setFont(Font
					.createFont(Font.TRUETYPE_FONT,
							getClass().getResourceAsStream("/resources/fontawesome-etherpad.ttf"))
					.deriveFont(Font.PLAIN, 15));
		} catch (FontFormatException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		rightAlignmentButton.setSize(16, 16);
		increaseIndentButton = new JButton("\ue814");
		try {
			increaseIndentButton.setFont(Font
					.createFont(Font.TRUETYPE_FONT,
							getClass().getResourceAsStream("/resources/fontawesome-etherpad.ttf"))
					.deriveFont(Font.PLAIN, 15));
		} catch (FontFormatException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		decreaseIndentButton = new JButton("\ue813");
		try {
			decreaseIndentButton.setFont(Font
					.createFont(Font.TRUETYPE_FONT,
							getClass().getResourceAsStream("/resources/fontawesome-etherpad.ttf"))
					.deriveFont(Font.PLAIN, 15));
		} catch (FontFormatException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		decreaseIndentButton.setSize(16, 16);
		tableButton = new JButton();
		tableButton.setSize(16, 16);
		undoButton = new JButton("\ue823");
		try {
			undoButton.setFont(Font
					.createFont(Font.TRUETYPE_FONT,
							getClass().getResourceAsStream("/resources/fontawesome-etherpad.ttf"))
					.deriveFont(Font.PLAIN, 15));
		} catch (FontFormatException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		undoButton.setSize(16, 16);
		redoButton = new JButton("\ue824");
		try {
			redoButton.setFont(Font
					.createFont(Font.TRUETYPE_FONT,
							getClass().getResourceAsStream("/resources/fontawesome-etherpad.ttf"))
					.deriveFont(Font.PLAIN, 15));
		} catch (FontFormatException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		redoButton.setSize(16, 16);
		boldButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				bold();
			}
		});
		italicButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				italic();
			}
		});
		underlinedButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				underlined();
			}
		});
		strikedButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				striked();
			}
		});
		leftAlignmentButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				alignment(0);
			}
		});
		centerAlignmentButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				alignment(1);
			}
		});
		rightAlignmentButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				alignment(2);
			}
		});
		increaseIndentButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				double x = StyleConstants.getLeftIndent(textpane.getParagraphAttributes());
				x += 0.75;
				indent(x);
			}
		});
		decreaseIndentButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				double x = StyleConstants.getLeftIndent(textpane.getParagraphAttributes());
				x -= 0.75;
				indent(x);
			}
		});
		undoButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				undo();
			}
		});
		redoButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				redo();
			}
		});
		tableButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int x, y;
				x = Integer.valueOf(JOptionPane.showInputDialog("Enter number of rows:"));
				y = Integer.valueOf(JOptionPane.showInputDialog("Enter number of columns:"));
				try {
					getAppletContext().showDocument(new URL(
							getCodeBase() + "documents/table/" + x + "/" + y + "/" + new Integer(textpane.getCaretPosition()-1)));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		toolbar.add(boldButton);
		toolbar.add(italicButton);
		toolbar.add(underlinedButton);
		toolbar.add(strikedButton);
		toolbar.add(leftAlignmentButton);
		toolbar.add(centerAlignmentButton);
		toolbar.add(rightAlignmentButton);
		toolbar.add(increaseIndentButton);
		toolbar.add(decreaseIndentButton);
		toolbar.add(tableButton);
		toolbar.add(undoButton);
		toolbar.add(redoButton);
		textpane.setContentType("text/html");
		textpane.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
					if (e.getKeyChar() == 'b')
						bold();
					if (e.getKeyChar() == 'i')
						italic();
					if (e.getKeyChar() == 'u')
						underlined();
					if (e.getKeyChar() == 'z')
						undo();
					if (e.getKeyChar() == 'y')
						redo();
				} else {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						if ((e.getModifiers() & InputEvent.SHIFT_MASK) != InputEvent.SHIFT_MASK)
							textpane.getHighlighter().removeAllHighlights();
						caret=textpane.getCaretPosition()-1;
						break;
					case KeyEvent.VK_RIGHT:
						if ((e.getModifiers() & InputEvent.SHIFT_MASK) != InputEvent.SHIFT_MASK)
							textpane.getHighlighter().removeAllHighlights();
						caret=textpane.getCaretPosition()-1;
						break;
					case KeyEvent.VK_UP:
						if ((e.getModifiers() & InputEvent.SHIFT_MASK) != InputEvent.SHIFT_MASK)
							textpane.getHighlighter().removeAllHighlights();
						caret=textpane.getCaretPosition()-1;
						break;
					case KeyEvent.VK_DOWN:
						if ((e.getModifiers() & InputEvent.SHIFT_MASK) != InputEvent.SHIFT_MASK)
							textpane.getHighlighter().removeAllHighlights();
						caret=textpane.getCaretPosition()-1;
						break;
					case KeyEvent.VK_BACK_SPACE:
						if (textpane.getHighlighter().getHighlights().length == 0) {
							if (tt != null)
								tt.cancel();
							t.purge();
							tt = new TimerTask() {
								public void run() {
									if (buffer.length() > 0)
										insert();
									if (beforeDeleted > 0)
										deleteBefore();
									if (afterDeleted > 0)
										deleteAfter();
									reset();
								}
							};
							t.schedule(tt, 1000);
							beforeDeleted++;
						} else {
							List<Highlight> l = new ArrayList<Highlight>();
							for (Highlight i : textpane.getHighlighter().getHighlights())
								l.add(i);
							Collections.<Highlight> sort(l, new Comparator<Highlight>() {

								@Override
								public int compare(Highlight arg0, Highlight arg1) {
									// TODO Auto-generated method stub
									return arg0.getStartOffset() < arg1.getStartOffset() ? -1
											: (arg0.getStartOffset() == arg1.getStartOffset() ? 0 : 1);
								}
							});
							for (Iterator<Highlight> i = l.iterator(); i.hasNext();) {
								Highlight h = i.next();
								try {
									getAppletContext().showDocument(new URL(getCodeBase() + "documents/deleteafter/"
											+ new Integer(h.getEndOffset() - h.getStartOffset()) + "/"
											+ new Integer(h.getStartOffset() - 1)));
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}

						}
						break;
					case KeyEvent.VK_DELETE:

						if (textpane.getHighlighter().getHighlights().length == 0) {
							if (tt != null)
								tt.cancel();
							t.purge();
							tt = new TimerTask() {
								public void run() {
									if (buffer.length() > 0)
										insert();
									if (beforeDeleted > 0)
										deleteBefore();
									if (afterDeleted > 0)
										deleteAfter();
									reset();
								}
							};
							t.schedule(tt, 1000);
							afterDeleted++;
						} else {
							List<Highlight> l = new ArrayList<Highlight>();
							for (Highlight i : textpane.getHighlighter().getHighlights())
								l.add(i);
							Collections.<Highlight> sort(l, new Comparator<Highlight>() {

								@Override
								public int compare(Highlight arg0, Highlight arg1) {
									// TODO Auto-generated method stub
									return arg0.getStartOffset() < arg1.getStartOffset() ? -1
											: (arg0.getStartOffset() == arg1.getStartOffset() ? 0 : 1);
								}
							});
							for (Iterator<Highlight> i = l.iterator(); i.hasNext();) {
								Highlight h = i.next();
								try {
									getAppletContext().showDocument(new URL(getCodeBase() + "documents/deleteafter/"
											+ new Integer(h.getEndOffset() - h.getStartOffset()) + "/"
											+ new Integer(h.getStartOffset() - 1)));
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}

						}

						break;
					}
				}

			}

			public void keyReleased(KeyEvent e) {

			}

			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() >= ' ' && (int) e.getKeyChar() != 127 || e.getKeyChar() == '\n'
						|| e.getKeyChar() == '\t') {
					textpane.getHighlighter().removeAllHighlights();
					if (tt != null)
						tt.cancel();
					t.purge();
					tt = new TimerTask() {
						public void run() {
							if (buffer.length() > 0)
								insert();
							if (beforeDeleted > 0)
								deleteBefore();
							if (afterDeleted > 0)
								deleteAfter();
							reset();
						}
					};
					t.schedule(tt, 1000);
					buffer = buffer + e.getKeyChar();
				}
			}

		});
		textpane.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				try {
					if(textpane.getSelectionStart()!=textpane.getSelectionEnd())
					textpane.getHighlighter().addHighlight(textpane.getSelectionStart(), textpane.getSelectionEnd(),
							new DefaultHighlighter.DefaultHighlightPainter(textpane.getSelectionColor()));
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				caret=textpane.getCaretPosition()-1;
			}

			public void mousePressed(MouseEvent e) {
				if ((e.getModifiers() & InputEvent.CTRL_MASK) != InputEvent.CTRL_MASK)
					textpane.getHighlighter().removeAllHighlights();
			}
		});
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(toolbar, c);
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		getContentPane().add(textpane, c);
		setSize(1000, 500);
		textpane.setText(getParameter("text"));
		setVisible(true);
	}
}
