package model;

import java.text.DecimalFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
public class Score {

	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	private Game game;
	
	@ManyToOne
	private User user;
	
	private double score;

	public Score() {
	}
	
	public Score(Game game, User user, double score) {
		this.game = game;
		DecimalFormat df = new DecimalFormat("#.##");      
		score = Double.valueOf(df.format(score));
		this.score = score;
		this.user = user;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		DecimalFormat df = new DecimalFormat("#.##");      
		score = Double.valueOf(df.format(score));
		this.score = score;
	}

	public long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	

}
