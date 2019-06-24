
public class Tetromino {
	private Type type;
	private Position position;
	private int[] rows = new int[4];
	private int[] columns = new int[4];
	private int row;
	private int column;
	public Tetromino(int row, int column, Type type) {
		this.row = row;
		this.column = column;
		this.type = type;
		position = Position.Up;
	}
	public int[] getColumns() {
		return columns;
	}
	public int[] getRows() {
		return rows;
	}
	public Type getType() {
		return type;
	}
	public Position getPosition() {
		return position;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public void update() {
		for(int i = 0; i < 4; i++) {
			if(type==Type.Red) {
				if(position==Position.Up || position==Position.Down) {
					rows[i] = row + (i<2 ? 0 : 1);
					columns[i] = column + (i%2==0 ? 0 : 1) + (i<2 ? 0 : 1);
				}else {
					rows[i] = row + (i==0 ? 0 : i==3 ? 2 : 1);
					columns[i] = column + (i==0 ? 2 : i==3 ? 1 : i);
				}
			}else if(type==Type.Turquoise) {
				if(position==Position.Up || position==Position.Down) {
					rows[i] = row;
					columns[i] = column + i;
				}else {
					rows[i] = row + i - 1;
					columns[i] = column + 1;
				}
			}else if(type==Type.Blue) {
				if(position==Position.Up) {
					rows[i] = row + (i==0 ? 0 : 1);
					columns[i] = column + (i==0 ? 0 : i-1);
				}else if(position==Position.Down) {
					rows[i] = row + (i!=3 ? 1 : 2);
					columns[i] = column + (i!=3 ? i : 2);
				}else if(position==Position.Right) {
					rows[i] = row + (i<2 ? 0 : i-1);
					columns[i] = column + (i!=1 ? 1 : 2);
				}else {
					rows[i] = row + (i<2 ? i : 2);
					columns[i] = column + (i!=2 ? 1 : 0);
				}
			}else if(type==Type.Yellow) {
				rows[i] = row + (i<2 ? 0 : 1);
				columns[i] = column + 1 + i%2;
			}else if(type==Type.Purple) {
				if(position==Position.Up) {
					rows[i] = row + (i==0 ? 0 : 1);
					columns[i] = column + (i==0 ? 1 : i-1);
				}else if(position==Position.Down) {
					rows[i] = row + (i!=3 ? 1 : 2);
					columns[i] = column + (i!=3 ? i : 1);
				}else if(position==Position.Right) {
					rows[i] = row + (i==0 ? 0 : i==3 ? 2 : 1);
					columns[i] = column + (i==2 ? 2 : 1);
				}else {
					rows[i] = row + (i==0 ? 0 : i==3 ? 2 : 1);
					columns[i] = column + (i==2 ? 0 : 1);
				}
			}else if(type==Type.Green) {
				if(position==Position.Up || position==Position.Down) {
					rows[i] = row + (i<2 ? 0 : 1);
					columns[i] = column + (i%2==0 ? 0 : 1) + (i<2 ? 1 : 0);
				}else {
					rows[i] = row + (i==0 ? 0 : i==3 ? 2 : 1);
					columns[i] = column + (i==0 ? 1 : i==3 ? 2 : i);
				}
			}else {
				if(position==Position.Up) {
					rows[i] = row + (i==0 ? 0 : 1);
					columns[i] = column + (i==0 ? 2 : i-1);
				}else if(position==Position.Down) {
					rows[i] = row + (i==3 ? 2 : 1);
					columns[i] = column + (i==3 ? 0 : i);
				}else if(position==Position.Right) {
					rows[i] = row + (i<2 ? i : 2);
					columns[i] = column + (i!=3 ? 1 : 2);
				}else {
					rows[i] = row + (i<2 ? 0 : i-1);
					columns[i] = column + (i==1 ? 0 : 1);
				}
			}
		}
	}
	public void moveDown() {
		row++;
		for(int i = 0; i < 4; i++)
			rows[i]++;
	}
	public void moveUp() {
		row--;
		for(int i = 0; i < 4; i++)
			rows[i]--;
	}
	public void moveHorizontal(boolean toLeft) {
		column += toLeft ? -1 : 1;
		for(int i = 0; i < 4; i++)
			columns[i] += toLeft ? -1 : 1;
	}
}
