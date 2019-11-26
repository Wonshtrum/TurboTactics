package Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	
	static public ArrayList<Couple<Integer, Integer>> tracePath(HashMap<Triplet<Integer, Integer, Integer>, Triplet<Integer, Integer, Integer>> tree, Triplet<Integer, Integer, Integer> pos) {
		ArrayList<Couple<Integer, Integer>> result = new ArrayList<Couple<Integer, Integer>>();
		while (pos.z > 0) {
			result.add(new Couple<Integer, Integer>(pos.x, pos.y));
			pos = tree.get(pos);
		}
		return result;
	}
	
	static public String pathToString(List<Couple<Integer, Integer>> path) {
		String result = "[";
		int pa = path.size();
		for (int i = 0 ; i<pa ; i++) {
			result += "["+path.get(i)+"]";
			if (i < pa-1) {
				result += ",";
			}
		}
		return result+"]";
	}
}
