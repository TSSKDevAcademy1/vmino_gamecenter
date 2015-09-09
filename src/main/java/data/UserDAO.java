package data;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import model.User;

@Stateless
public class UserDAO {

	@Inject
	EntityManager em;

	public User getUser(long id) {
		return em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class).setParameter("ïd", id)
				.getSingleResult();
	}

	public User findUserByName(String username) {
		return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
				.setParameter("username", username).getSingleResult();
	}

	public boolean verifyUser(String username, String password) {
		try {
			User user = findUserByName(username);
			return (user.getUsername().equals(username) && user.getPassword().equals(password));
		} catch (NoResultException e) {
			return false;
		}
	}

	public boolean registerUser(String username, String password) {
		try {
			User newUser = new User(username, password);
			em.persist(newUser);
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}
}
