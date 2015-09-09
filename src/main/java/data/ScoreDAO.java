package data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import model.Game;
import model.Score;

@Stateless
public class ScoreDAO {

	@Inject
	EntityManager em;
	
	@Inject
	UserDAO userDAO;
	
	@Inject
	GameDAO gameDAO;

	public Score getScore(long id) {
		return em.createQuery("SELECT s FROM Score WHERE s.id = :id", Score.class).setParameter("id", id).getSingleResult();
	}

	public List<Score> getTopScoreOfGame(String name) {
		List<Score> scores;
		scores = em.createQuery("SELECT s FROM Score s WHERE s.game = :game ORDER BY s.score", Score.class)
				.setParameter("game", gameDAO.findGameByName(name)).setMaxResults(10).getResultList();
		return scores;
	}
	
	public void addScore(String nameOfGame, String username, Double time){
		try {
			Score score = new Score(gameDAO.findGameByName(nameOfGame), userDAO.findUserByName(username), time);
			em.persist(score);
		} catch (Exception e){
			
		}
		
	}
	
}
