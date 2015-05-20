package org.eac.opinion;

import java.util.ArrayList;

import org.eac.critic.Critic;
import org.eac.opinion.Opinion;

public class OpinionList<E> extends ArrayList<E> {

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
