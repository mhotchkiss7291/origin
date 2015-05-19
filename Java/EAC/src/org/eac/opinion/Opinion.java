package org.eac.opinion;

import org.eac.critic.Critic;

public class Opinion {

	private int id;
	private Critic critic;
	private String description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Critic getCritic() {
		return critic;
	}

	public void setCritic(Critic critic) {
		this.critic = critic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
