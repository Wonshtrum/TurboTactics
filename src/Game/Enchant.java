package Game;
public class Enchant {
	private String enchantName; // Name of the enchant
	private int wType; // 0:both, 1:physical, 2:magical
	
	private int flatMod; // Weapon's base damage
	private int randMod; // Weapon's additional random damage
	private int strMod; // Strength modifier
	private int hpMod; // Health Point modifier
	private int intMod; // Intelligence modifier
	private int manaMod; // Mana modifier
	private int apMod; // Action Point modifier
	
	public Enchant(String enchantName,int wType, int flatMod, int randMod, int strMod, int hpMod, int intMod, int manaMod, int apMod) {
		this.enchantName = enchantName;
		this.wType = wType;
		this.flatMod = flatMod;
		this.randMod = randMod;
		this.strMod = strMod;
		this.hpMod = hpMod;
		this.intMod = intMod;
		this.manaMod = manaMod;
		this.apMod = apMod;
	}
	
	public String getEnchantName() {
		return enchantName;
	}

	public int getwType() {
		return wType;
	}

	public int getFlatMod() {
		return flatMod;
	}

	public int getRandMod() {
		return randMod;
	}

	public int getStrMod() {
		return strMod;
	}

	public int getHpMod() {
		return hpMod;
	}

	public int getIntMod() {
		return intMod;
	}

	public int getManaMod() {
		return manaMod;
	}

	public int getApMod() {
		return apMod;
	}

	public String toString() {
		return "Enchant [enchantName=" + enchantName + ", wType=" + wType + ", flatMod=" + flatMod + ", randMod="
				+ randMod + ", strMod=" + strMod + ", hpMod=" + hpMod + ", intMod=" + intMod + ", manaMod=" + manaMod
				+ ", apMod=" + apMod + "]";
	}
	
	
}
