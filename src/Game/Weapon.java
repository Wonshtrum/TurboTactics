package Game;
public class Weapon {
	private String name; // Weapon's name
	private int rank; // 0:Common, 1:Rare, 2:Epic, 3:Legendary, 4:Mythical
	
	private int dmgFlat; // Weapon's base damage
	private int dmgRand; // Weapon's additional random damage
	private int range; // Weapon's range
	
	private int strMod; // Strength modifier
	private int hpMod; // Health Point modifier
	private int intMod; // Intelligence modifier
	private int manaMod; // Mana modifier
	private int apMod; // Action Point modifier
	
	public Weapon(String name, int rank, int dmgFlat, int dmgRand, int range, int strMod, int hpMod, 
			int intMod, int manaMod, int apMod) {
		this.name = name;
		this.rank = rank;
		this.dmgFlat = dmgFlat;
		this.dmgRand = dmgRand;
		this.range = range;
		this.strMod = strMod;
		this.hpMod = hpMod;
		this.intMod = intMod;
		this.manaMod = manaMod;
		this.apMod = apMod;
	}

	public String getName() {
		return name;
	}

	public int getRank() {
		return rank;
	}

	public int getDmgFlat() {
		return dmgFlat;
	}

	public int getDmgRand() {
		return dmgRand;
	}

	public int getRange() {
		return range;
	}

	public int getStrMod() {
		return strMod;
	}

	public int getIntMod() {
		return intMod;
	}

	public int getApMod() {
		return apMod;
	}

	public int getHpMod() {
		return hpMod;
	}

	public int getManaMod() {
		return manaMod;
	}

	public String toString() {
		return "Weapon [name=" + name + ", rank=" + rank + ", dmgFlat=" + dmgFlat + ", dmgRand=" + dmgRand + ", range="
				+ range + ", strMod=" + strMod + ", hpMod=" + hpMod + ", intMod=" + intMod + ", manaMod=" + manaMod
				+ ", apMod=" + apMod + "]";
	}
}
