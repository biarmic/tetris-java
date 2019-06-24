
public enum Position {
	Up, Left, Down, Right;
	public Position next() {
		if(this==Up)
			return Right;
		if(this==Right)
			return Down;
		if(this==Down)
			return Left;
		return Up;
	}
	public Position previous() {
		if(this==Up)
			return Left;
		if(this==Right)
			return Up;
		if(this==Down)
			return Right;
		return Down;
	}
}
