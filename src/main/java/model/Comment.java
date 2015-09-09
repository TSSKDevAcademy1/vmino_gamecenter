package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;

@Entity
public class Comment {
	
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Game game;
	
//	@Max(256)
	private String content;
	
	public Comment() {
	}

	public Comment(Game game, User user, String content) {
		this.user = user;
		this.game = game;
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getId() {
		return id;
	}
	
	
	
}
