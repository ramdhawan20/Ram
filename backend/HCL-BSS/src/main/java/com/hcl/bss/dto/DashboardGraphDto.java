package com.hcl.bss.dto;

import java.util.List;

public class DashboardGraphDto {
	
//	private String title;
	private List<String> timePeriod;
	private List<Long> activCustValues;
	private List<Long> activSubValues;
	private List<Long> cancelSubValues;
	private List<Long> newSubValues;
	private List<Long> renewedSubValues;
	public List<String> getTimePeriod() {
		return timePeriod;
	}
	public void setTimePeriod(List<String> timePeriod) {
		this.timePeriod = timePeriod;
	}
	public List<Long> getActivCustValues() {
		return activCustValues;
	}
	public void setActivCustValues(List<Long> activCustValues) {
		this.activCustValues = activCustValues;
	}
	public List<Long> getActivSubValues() {
		return activSubValues;
	}
	public void setActivSubValues(List<Long> activSubValues) {
		this.activSubValues = activSubValues;
	}
	public List<Long> getCancelSubValues() {
		return cancelSubValues;
	}
	public void setCancelSubValues(List<Long> cancelSubValues) {
		this.cancelSubValues = cancelSubValues;
	}
	public List<Long> getNewSubValues() {
		return newSubValues;
	}
	public void setNewSubValues(List<Long> newSubValues) {
		this.newSubValues = newSubValues;
	}
	public List<Long> getRenewedSubValues() {
		return renewedSubValues;
	}
	public void setRenewedSubValues(List<Long> renewedSubValues) {
		this.renewedSubValues = renewedSubValues;
	}
	
}
