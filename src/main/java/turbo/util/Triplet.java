package turbo.util;

public class Triplet<T1, T2, T3> {
	public T1 x;
	public T2 y;
	public T3 z;
	
	public Triplet(T1 x, T2 y, T3 z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public String toString() {
		return x+","+y+","+z;
	}
}
