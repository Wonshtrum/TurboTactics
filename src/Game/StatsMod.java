package Game;

public class StatsMod {
	private int[][] stats;
	
	public StatsMod(int[][] stats, int duration) {
		this.stats=stats;
	}
	
	public int get(int key) {
		for (int[] item : this.stats) {
			if (item[0] == key) return item[1];
		}
		return 0;
	}
}
