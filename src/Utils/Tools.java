package Utils;

public class Tools {
	static public int randInt(int a, int b) {
		return a+(int)(Math.random()*(b-a+1));
	}
	static public int randInt(int a) {
		return randInt(0, a);
	}
	static public double randFloat(double a, double b) {
		return a+Math.random()*(b-a);
	}
	static public double randFloat(double a) {
		return randFloat (0, a);
	}
}
