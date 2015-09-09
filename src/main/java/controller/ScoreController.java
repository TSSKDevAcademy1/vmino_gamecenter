package controller;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import data.ScoreDAO;
import model.Score;

@Model
public class ScoreController {
	
	@Inject
	ScoreDAO scoreDAO;
	
	private List<Score> scores;
	
	public List<Score> getTopScoreOfGame(String gameName) {
		this.scores = (scoreDAO.getTopScoreOfGame(gameName));
		return this.scores;
	}
	
    private transient int[] row = new int[4];
    
    public int getRow(int i) {
    	return ++row[i];
    }
}
