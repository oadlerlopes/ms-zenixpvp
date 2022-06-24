package br.com.zenix.pvp.warps.type;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of
 * this file, via any medium is strictly prohibited proprietary and confidential
 */
public enum WarpType {

	NONE("Spawn", false),
	SPAWN("Spawn", false),
	FPS("FPS", false),
	LAVA("Lava Challenge", false),
	ONE_VS_ONE("1v1", true);

	private String name;
	private boolean status;

	private WarpType(String name, boolean status) {
		this.name = name;
		this.status = status;
	}

	public String getName() {
		return name;
	}
	
	public boolean getStatus(){
		return status;
	}
	
	public void setStatus(boolean status){
		this.status = status;
	}

	public static boolean contains(String warp) {
		for (WarpType warpType : WarpType.values()) {
			if (warp.equalsIgnoreCase(warpType.toString()))
				return true;
		}
		if (warp.contains("simulator"))
			return true;

		return false;
	}

	public static WarpType getFromString(String warpName) {
		for (WarpType warpType : WarpType.values()) {
			if (warpType.getName().equalsIgnoreCase(warpName))
				return warpType;
		}
		
		warpName = warpName.toLowerCase();
		
		if (warpName.contains("arena"))
			return WarpType.SPAWN;

		if (warpName.contains("1v1"))
			return WarpType.ONE_VS_ONE;

		if (warpName.contains("lava") || warpName.contains("challenge"))
			return WarpType.LAVA;
		
		return null;
	}

}

