import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class Board extends JLayeredPane{
	private GUI gui;
	private JLabel gameOver = new JLabel();
	private Block[][] block = new Block[20][10];
	private Block[][] nextBlock = new Block[2][4];
	private boolean[][] isFilled = new boolean[20][10];
	private static ImageIcon red;
	private static ImageIcon turquoise;
	private static ImageIcon blue;
	private static ImageIcon yellow;
	private static ImageIcon purple;
	private static ImageIcon green;
	private static ImageIcon orange;
	private Tetromino current;
	private Tetromino next;
	private boolean isMoving = true;
	private boolean isGameOver = false;
	private int level = 1;
	private int score = 0;
	private int lines = 0;
	public Board(GUI gui) {
		this.gui = gui;
		JLabel background = new JLabel();
		try {
			background.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/playground.png"))));
			gameOver.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/gameover.png"))));
			red = new ImageIcon(ImageIO.read(getClass().getResource("images/red_block.png")));
			turquoise = new ImageIcon(ImageIO.read(getClass().getResource("images/turquoise_block.png")));
			blue = new ImageIcon(ImageIO.read(getClass().getResource("images/blue_block.png")));
			yellow = new ImageIcon(ImageIO.read(getClass().getResource("images/yellow_block.png")));
			purple = new ImageIcon(ImageIO.read(getClass().getResource("images/purple_block.png")));
			green = new ImageIcon(ImageIO.read(getClass().getResource("images/green_block.png")));
			orange = new ImageIcon(ImageIO.read(getClass().getResource("images/orange_block.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		background.setBounds(0,0,1080,880);
		gameOver.setBounds(360,40,400,800);
		add(background,JLayeredPane.DEFAULT_LAYER);
		add(gameOver,JLayeredPane.POPUP_LAYER);
		gameOver.setVisible(false);
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 10; j++) {
				block[i][j] = new Block(i,j);
				add(block[i][j],JLayeredPane.PALETTE_LAYER);
			}
		}
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 4; j++) {
				nextBlock[i][j] = new Block(i+2,j+12);
				add(nextBlock[i][j],JLayeredPane.PALETTE_LAYER);
			}
		}
		next = new Tetromino(2,12,Type.randomType());
	}
	public boolean isMoving() {
		return isMoving;
	}
	public boolean isGameOver() {
		return isGameOver;
	}
	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}
	public void newTetromino() {
		current = next;
		current.setRow(0);
		current.setColumn(3);
		next = new Tetromino(2,12,Type.randomType());
		current.update();
		next.update();
		if(gameOver()) {
			isMoving = false;
			isGameOver = true;
			gameOver.setVisible(true);
		}
		updateTetromino();
		updateNextTetromino();
	}
	public void moveDown(boolean pressed) {
		closeTetromino();
		current.moveDown();
		int[] columns = current.getColumns();
		int[] rows = current.getRows();
		if(pressed)
			score += level;
		if(!canMove(rows,columns)) {
			if(pressed)
				score -= level;
			current.moveUp();
			updateTetromino();
			int clearCount = clearRow(0);
			if(clearCount!=0 && (lines+clearCount)/10!=lines/10)
				level++;
			score += level*(clearCount==1 ? 40 : clearCount==2 ? 100 : clearCount==3 ? 300 : clearCount==4 ? 1200 : 0);
			lines += clearCount;
			newTetromino();
		}
		gui.setLevel(level);
		gui.setScore(score);
		gui.setLines(lines);
		updateTetromino();
	}
	public void drop() {
		closeTetromino();
		int[] rows = current.getRows();
		int[] columns = current.getColumns();
		int dropCount = 0;
		while(canMove(rows,columns)) {
			dropCount++;
			current.moveDown();
			if(!canMove(rows,columns)) {
				dropCount--;
				current.moveUp();
				updateTetromino();
				int clearCount = clearRow(0);
				if(clearCount!=0 && (lines+clearCount)/10!=lines/10)
					level++;
				score += clearCount*2*level;
				score += level*(clearCount==1 ? 40 : clearCount==2 ? 100 : clearCount==3 ? 300 : clearCount==4 ? 1200 : 0);
				lines += clearCount;
				newTetromino();
				break;
			}
		}
		score += level*dropCount*2;
		gui.setLevel(level);
		gui.setScore(score);
		gui.setLines(lines);
		updateTetromino();
	}
	public void moveHorizontal(boolean toLeft) {
		closeTetromino();
		current.moveHorizontal(toLeft);
		int[] columns = current.getColumns();
		int[] rows = current.getRows();
		if(!canMove(rows,columns))
			current.moveHorizontal(!toLeft);
		updateTetromino();
	}
	public void rotate() {
		closeTetromino();
		current.setPosition(current.getPosition().next());
		current.update();
		int[] columns = current.getColumns();
		int[] rows = current.getRows();
		if(!canMove(rows,columns)) {
			current.setPosition(current.getPosition().previous());
			current.update();
		}
		updateTetromino();
	}
	public void closeTetromino() {
		int[] rows = current.getRows();
		int[] columns = current.getColumns();
		for(int i = 0; i < 4; i++) {
			isFilled[rows[i]][columns[i]] = false;
			block[rows[i]][columns[i]].removeType();
		}
	}
	public void updateTetromino() {
		int[] rows = current.getRows();
		int[] columns = current.getColumns();
		Type color = current.getType();
		for(int i = 0; i < 4; i++) {
			isFilled[rows[i]][columns[i]] = true;
			if(color==Type.Red)
				block[rows[i]][columns[i]].setType(red);
			else if(color==Type.Turquoise)
				block[rows[i]][columns[i]].setType(turquoise);
			else if(color==Type.Blue)
				block[rows[i]][columns[i]].setType(blue);
			else if(color==Type.Yellow)
				block[rows[i]][columns[i]].setType(yellow);
			else if(color==Type.Purple)
				block[rows[i]][columns[i]].setType(purple);
			else if(color==Type.Green)
				block[rows[i]][columns[i]].setType(green);
			else
				block[rows[i]][columns[i]].setType(orange);
		}
	}
	private void updateNextTetromino() {
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 4; j++)
				nextBlock[i][j].removeType();
		}
		int[] rows = next.getRows();
		int[] columns = next.getColumns();
		Type color = next.getType();
		for(int i = 0; i < 4; i++) {
			if(color==Type.Red)
				nextBlock[rows[i]-2][columns[i]-12].setType(red);
			else if(color==Type.Turquoise)
				nextBlock[rows[i]-2][columns[i]-12].setType(turquoise);
			else if(color==Type.Blue)
				nextBlock[rows[i]-2][columns[i]-12].setType(blue);
			else if(color==Type.Yellow)
				nextBlock[rows[i]-2][columns[i]-12].setType(yellow);
			else if(color==Type.Purple)
				nextBlock[rows[i]-2][columns[i]-12].setType(purple);
			else if(color==Type.Green)
				nextBlock[rows[i]-2][columns[i]-12].setType(green);
			else
				nextBlock[rows[i]-2][columns[i]-12].setType(orange);
		}
	}
	public boolean canMove(int[] row, int[] column) {
		for(int i = 0; i < 4; i++) {
			if(row[i]<0 || row[i]>19 || column[i]<0 || column[i]>9 || isFilled[row[i]][column[i]])
				return false;
		}
		return true;
	}
	public int clearRow(int count) {
		for(int i = 0; i < 20; i++) {
			boolean rowFilled = true;
			for(int j = 0; j < 10; j++) {
				if(!isFilled[i][j])
					rowFilled = false;
			}
			if(rowFilled) {
				shift(i);
				return clearRow(count+1);
			}
		}
		return count;
	}
	public void shift(int row) {
		for(int i = row; i > 0; i--) {
			for(int j = 0; j < 10; j++) {
				isFilled[i][j] = isFilled[i-1][j];
				block[i][j].setType(block[i-1][j].getType());
			}
		}
		for(int j = 0; j < 10; j++) {
			isFilled[0][j] = false;
			block[0][j].removeType();
		}
	}
	public boolean gameOver() {
		int[] row = current.getRows();
		int[] column = current.getColumns();
		for(int i = 0; i < 4; i++) {
			if(isFilled[row[i]][column[i]])
				return true;
		}
		return false;
	}
}
