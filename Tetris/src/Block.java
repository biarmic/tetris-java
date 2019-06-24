import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Block extends JLabel{
	private ImageIcon icon;
	public Block(int row, int column) {
		setBounds(360+column*40,40+row*40,40,40);
	}
	public ImageIcon getType() {
		return icon;
	}
	public void setType(ImageIcon icon) {
		this.icon = icon;
		setIcon(icon);
	}
	public void removeType() {
		icon = null;
		setIcon(null);
	}
}
