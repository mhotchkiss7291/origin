package org.eac.opinion;

import org.eac.critic.Critic;

public class Opinion {

	private int id;
	private Critic critic;
	private String description;
	private String expertise;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCritic() {

		// This is a table join of sorts // Bad!!
		//return critic.toString();
		return this.critic.getName();
	}

	public void setCritic(String string) {
		this.critic = critic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExpertise() {
		return expertise;
	}

	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}

}
