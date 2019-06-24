
public enum Type {
	Red, Turquoise, Blue, Yellow, Purple, Green, Orange;
	public static Type randomType() {
		return Type.values()[(int)(Math.random()*7)];
	}
}
