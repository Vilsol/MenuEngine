package me.vilsol.menuengine.enums;

public enum InventorySize {

	S_9(9),
	S_18(18),
	S_27(27),
	S_36(36),
	S_45(45),
	S_54(54);
	
	private int size;
	
	private InventorySize(int size){
		this.size = size;
	}
	
	public int getSize(){
		return size;
	}
	
	public static InventorySize getMinAfter(int amount){
		for(InventorySize i : values()){
			if(amount < i.getSize()) return i;
		}
		return S_54;
	}
	
}
