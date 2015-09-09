package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
public class Rating {
	
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	private Game game;
	
	@Min(0)
	@Max(10)
	private int rating;
	
	@ManyToOne
	private User user;
	
	public Rating() {
	}
	
	public Rating (Game game, User user, int rating) {
		this.game = game;
		this.user = user;
		this.rating = rating;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getId() {
		return id;
	}
	
	
	
	
}
