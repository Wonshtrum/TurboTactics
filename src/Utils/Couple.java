package Utils;

public class Couple<T1, T2> {
	public T1 x;
	public T2 y;
	
	public Couple(T1 x, T2 y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return x+","+y;
	}
}
