package com.hcl.bss.dto;

public class GraphRequestDto {
	
	private String timePeriod;
	private String typeOfGraph;
	public String getTimePeriod() {
		return timePeriod;
	}
	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}
	public String getTypeOfGraph() {
		return typeOfGraph;
	}
	public void setTypeOfGraph(String typeOfGraph) {
		this.typeOfGraph = typeOfGraph;
	}
}
