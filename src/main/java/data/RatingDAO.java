package data;

import java.text.DecimalFormat;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import exception.NoRatingException;
import model.Game;
import model.Rating;

@Stateless
public class RatingDAO {

	@Inject
	EntityManager em;

	@Inject
	UserDAO userDAO;

	@Inject
	GameDAO gameDAO;

	public Rating getRating(long id) {
		return em.createQuery("SELECT r FROM Rating r WHERE r.id = :id", Rating.class).setParameter("id", id)
				.getSingleResult();
	}

	public void rateGame(String gameName, String userName, int rating) {
		if (findIfUserAlreadyRatedGame(gameName, userName)) {
			changeRatingOfUser(gameName, userName, rating);
		} else {
			Rating rate = new Rating(gameDAO.findGameByName(gameName), userDAO.findUserByName(userName), rating);
			em.persist(rate);
		}

	}

	public double getAverageRatingOfGame(String name) throws NoRatingException {
		List<Rating> ratings = em.createQuery("SELECT r FROM Rating r WHERE r.game = :game", Rating.class)
				.setParameter("game", gameDAO.findGameByName(name)).getResultList();
		double result = 0;
		for (Rating r : ratings) {
			result += r.getRating();
		}
		try { 
			result /= ratings.size();
			DecimalFormat df = new DecimalFormat("#.##");
			result = Double.valueOf(df.format(result));
			return result;
		} catch (Exception e) {
			throw new NoRatingException();
		}
	}

	public Rating getRatingOfUser(String gameName, String userName) {
		try {
			return em.createQuery("SELECT r FROM Rating r WHERE r.game = :game AND r.user = :user", Rating.class)
					.setParameter("game", gameDAO.findGameByName(gameName))
					.setParameter("user", userDAO.findUserByName(userName)).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public boolean findIfUserAlreadyRatedGame(String gameName, String userName) {
		try {
			return getRatingOfUser(gameName, userName) != null;
		} catch (Exception e) {
			return false;
		}
	}

	public void changeRatingOfUser(String gameName, String userName, int rating) {
		em.createQuery("UPDATE Rating r SET r.rating = :rating WHERE r.game = :game AND r.user = :user")
				.setParameter("rating", rating).setParameter("game", gameDAO.findGameByName(gameName))
				.setParameter("user", userDAO.findUserByName(userName)).executeUpdate();
	}

}
