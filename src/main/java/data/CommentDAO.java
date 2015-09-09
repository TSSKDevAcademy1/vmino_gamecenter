package data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import model.Comment;

@Stateless
public class CommentDAO {

	@Inject
	EntityManager em;

	@Inject
	UserDAO userDAO;

	@Inject
	GameDAO gameDAO;

	public Comment getComment(long id) {
		return em.createQuery("SELECT c FROM Comment c WHERE c.id = :id", Comment.class).setParameter("id", id)
				.getSingleResult();
	}

	public List<Comment> getCommentsOfGame(String gameName) {
		return em.createQuery("SELECT c FROM Comment c WHERE c.game = :game", Comment.class)
				.setParameter("game", gameDAO.findGameByName(gameName)).getResultList();
	}
	
	public void addComment(String gameName, String userName, String content){
		Comment comment = new Comment(gameDAO.findGameByName(gameName), userDAO.findUserByName(userName), content);
		em.persist(comment);
	}
	
}
