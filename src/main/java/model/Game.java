package model;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
@RequestScoped
public class Game {
	
	@Transient
	@Inject
	EntityManager em;
	
	@Id
	@GeneratedValue
	private long id;
	
	private String name;
	
	private String imagePath;
	
	private String gamePath;

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getGamePath() {
		return gamePath;
	}

	public void setGamePath(String gamePath) {
		this.gamePath = gamePath;
	}
	
	public List<Game> getGames(){
		return em.createQuery("SELECT g FROM Games g", Game.class).getResultList();
	}
	
}
