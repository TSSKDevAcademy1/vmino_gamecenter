package controller;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import data.CommentDAO;
import model.Comment;

@Model
public class CommentController {
	
	@Inject
	CommentDAO commentDAO;
	
	@Inject
	LoggedUser user;
	
	private String currentComment;
	
	private List<Comment> comments;
	
	public List<Comment> getAllCommentsOfGame(String gameName){
		this.comments = commentDAO.getCommentsOfGame(gameName);
		return this.comments;
	}
	
	public String addComment(String gameName, String path){
		commentDAO.addComment(gameName, user.getName(), currentComment);
		return "path";
	}

	public String getCurrentComment() {
		return currentComment;
	}

	public void setCurrentComment(String currentComment) {
		this.currentComment = currentComment;
	}
	
	
}
