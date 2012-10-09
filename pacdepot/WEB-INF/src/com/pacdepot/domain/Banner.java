package com.pacdepot.domain;

import com.pacdepot.database.DataAdapter;

public class Banner extends Entity {
	public Banner() {}
		
	public DImage getImage() {
		return (DImage) getAttribute("image");	
	}
	
	public void fetch() {
		Banner banner = DataAdapter.getInstance().getBanner(getId());
		setAttribute("image", banner.getAttribute("image"));
	}
}