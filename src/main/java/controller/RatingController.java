package controller;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import data.RatingDAO;
import exception.NoRatingException;

@Model
public class RatingController {
	
	@Inject
	RatingDAO ratingDAO; 
	
	@Inject
	LoggedUser user;
	
	private int rating;
	
	public String getRatingOfGame(String gameName){
		try {
			return ""+ratingDAO.getAverageRatingOfGame(gameName);
		} catch (NoRatingException e){
			return "-";
		}
	}
	
	public List<Integer> getRatingValues(){
		List<Integer> values = new ArrayList<>();
		for (int i = 0; i <= 10; i++){
			values.add(i);
		}
		return values;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public String rateGame(String gameName, String path) {
		ratingDAO.rateGame(gameName, user.getName(), rating);
		return path;
	}
}
