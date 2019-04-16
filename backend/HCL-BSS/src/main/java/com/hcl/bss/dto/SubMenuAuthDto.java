package com.hcl.bss.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class SubMenuAuthDto {
	private String subMenuName;

	public String getSubMenuName() {
		return subMenuName;
	}

	public void setSubMenuName(String subMenuName) {
		this.subMenuName = subMenuName;
	}
	
}
