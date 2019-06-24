import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.KeyStroke;

public class GUI extends JFrame {
	private static GUI gui;
	private Board board = new Board(this);
	private static JLabel level = new JLabel("1");
	private static JLabel score = new JLabel("0");
	private JLabel lines = new JLabel("0");
	private static JLabel highestScore = new JLabel("");
	private JLabel timerLabel = new JLabel("-1");
	private static int time = 1500;
	private Timer timer = new Timer();
	private TimerTask task;
	public GUI() {
		super("Tetris");
		time = 1000;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(1086,909);
		setResizable(false);
		setLocation((int)(screenSize.width-1086)/2,(int)(screenSize.height-909)/2);
		setResizable(false);
		JLabel newGame = new JLabel();
		JLabel pause = new JLabel();
		JLabel resume = new JLabel();
		JLabel gamePaused = new JLabel();
		level.setForeground(Color.WHITE);
		level.setFont(new Font("Arial",Font.BOLD,33));
		score.setForeground(Color.WHITE);
		score.setFont(new Font("Arial",Font.BOLD,33));
		lines.setForeground(Color.WHITE);
		lines.setFont(new Font("Arial",Font.BOLD,33));
		highestScore.setForeground(Color.WHITE);
		highestScore.setFont(new Font("Arial",Font.BOLD,33));
		timerLabel.setForeground(Color.WHITE);
		timerLabel.setFont(new Font("Arial",Font.BOLD,33));
		try {
			newGame.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/newgame.png"))));
			pause.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/pause.png"))));
			resume.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/resume.png"))));
			gamePaused.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/gamepaused.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		newGame.setBounds(28,28,304,104);
		pause.setBounds(28,148,304,64);
		resume.setBounds(28,148,304,64);
		gamePaused.setBounds(360,40,400,800);
		level.setBounds(808,328,464,24);
		score.setBounds(808,448,464,24);
		lines.setBounds(808,568,464,24);
		highestScore.setBounds(48,328,504,24);
		timerLabel.setBounds(48,448,504,24);
		board.add(newGame,JLayeredPane.PALETTE_LAYER);
		board.add(pause,JLayeredPane.MODAL_LAYER);
		board.add(resume,JLayeredPane.PALETTE_LAYER);
		board.add(gamePaused,JLayeredPane.POPUP_LAYER);
		board.add(level,JLayeredPane.PALETTE_LAYER);
		board.add(score,JLayeredPane.PALETTE_LAYER);
		board.add(lines,JLayeredPane.PALETTE_LAYER);
		board.add(highestScore,JLayeredPane.PALETTE_LAYER);
		board.add(timerLabel,JLayeredPane.PALETTE_LAYER);
		add(board);
		gamePaused.setVisible(false);
		setHighestScore();
		newGame.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				setVisible(false);
				dispose();
				gui = new GUI();
			}
		});
		pause.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				if(!board.isGameOver()) {
					board.setMoving(false);
					pause.setVisible(false);
					gamePaused.setVisible(true);
					timer.cancel();
				}
			}
		});
		resume.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				if(!board.isGameOver()) {
					board.setMoving(true);
					pause.setVisible(true);
					gamePaused.setVisible(false);
					restartTimer();
				}
			}
		});
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "pressLeft");
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "pressRight");
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "pressDown");
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "pressUp");
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "pressSpace");
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "pressEscape");
		getRootPane().getActionMap().put("pressLeft", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(board.isMoving())
					board.moveHorizontal(true);
			}
		});
		getRootPane().getActionMap().put("pressRight", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(board.isMoving())
					board.moveHorizontal(false);
			}
		});
		getRootPane().getActionMap().put("pressDown", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(board.isMoving())
					board.moveDown(true);
			}
		});
		getRootPane().getActionMap().put("pressUp", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(board.isMoving())
					board.rotate();
			}
		});
		getRootPane().getActionMap().put("pressSpace", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(board.isMoving())
					board.drop();
				else if(board.isGameOver()) {
					setVisible(false);
					dispose();
					gui = new GUI();
 				}
			}
		});
		getRootPane().getActionMap().put("pressEscape", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(board.isMoving() && !board.isGameOver()) {
					board.setMoving(false);
					pause.setVisible(false);
					gamePaused.setVisible(true);
					timer.cancel();
				}else if(!board.isGameOver()){
					board.setMoving(true);
					pause.setVisible(true);
					gamePaused.setVisible(false);
					restartTimer();
				}else {
					setVisible(false);
					dispose();
					System.exit(0);
				}
			}
		});
		level.setText(""+1);
		score.setText(""+0);
		setVisible(true);
		board.newTetromino();
		restartTimer();
	}
	public void setLevel(int level) {
		GUI.level.setText(""+level);
	}
	public void setScore(int score) {
		GUI.score.setText(""+score);
	}
	public void setLines(int lines) {
		if(!this.lines.getText().equals(""+lines)) {
			time *= 0.9;
			this.lines.setText(""+lines);
		}
	}
	public void setHighestScore() {
		try {
			Scanner file = new Scanner(new File("src/highestscore.txt"));
			highestScore.setText  (""+file.nextInt());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void editFile(int score) {
		try {
			FileWriter writer = new FileWriter("src/highestscore.txt");
			writer.write(""+score);
            writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void restartTimer() {
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				int value = Integer.parseInt(timerLabel.getText())+1;
				timerLabel.setText(""+value);
			}
		};
		timer.schedule(task,0,1000);
	}
	public static void main(String[] args) {
		gui = new GUI();
		while(true) {
				try {
					Thread.sleep(time-10*Integer.parseInt(level.getText()));
					if(gui.board.isMoving())
						gui.board.moveDown(false);
					else if(gui.board.isGameOver() && Integer.parseInt(score.getText())>Integer.parseInt(highestScore.getText())) {
						gui.editFile(Integer.parseInt(score.getText()));
						gui.setHighestScore();
						gui.timer.cancel();
					}else if(gui.board.isGameOver())
						gui.timer.cancel();
						
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}
}
