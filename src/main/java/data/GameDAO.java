package data;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import model.Game;

@Stateless
public class GameDAO {
	
	@Inject
	EntityManager em;
	
	public Game findGameByName(String name){
		return em.createQuery("SELECT g FROM Game g WHERE g.name = :name", Game.class).setParameter("name", name).getSingleResult();
	}
}