package games.stones;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.LoggedUser;
import data.RatingDAO;
import data.ScoreDAO;

@WebServlet("/stones")
public class StonesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String FIELD_ATTR = "fieldStones";
	
	private static final String GAME_NAME = "Stones";
	
	@Inject
	LoggedUser user;
	
	@Inject
	ScoreDAO scoreDAO;
	
	@Inject 
	RatingDAO ratingDAO;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int rowCount = 3;
		int columnCount = 3;

		PrintWriter out = response.getWriter();
		Field field = (Field) request.getSession().getAttribute(FIELD_ATTR);

		if (field == null || request.getParameter("new") != null) {
			field = new Field(rowCount, columnCount);
			request.getSession().setAttribute(FIELD_ATTR, field);
		}

		try {
			int row = Integer.parseInt(request.getParameter("row"));
			int column = Integer.parseInt(request.getParameter("column"));
			if (!field.checkGameState()){
			field.moveTile(row, column);
			}
		} catch (Exception e) {
		}

		out.print("<table>");
		for (int row = 0; row < rowCount; row++) {
			for (int column = 0; column < columnCount; column++) {
				Tile tile = field.getTile(row, column);
				if (tile instanceof Stone){
					out.printf("<a href=\"?row=%s&column=%s\" id=\"stone\">%s</a>", row, column, tile.getValue());
				} else if (tile instanceof Space){
					out.printf("<a href=\"?row=%s&column=%s\" id=\"space\">&nbsp</a>", row, column);
				}
			}
			out.println("<br>");
		}
		out.print("</table>");
		
		if (field.checkGameState()) {
			double currentTime = (double) (System.currentTimeMillis() - field.getTime())/1000;
			out.printf("<p id=\"win\">You Won! (in %.2f seconds)</p>", currentTime);
			if (user.isLogged()) {
				scoreDAO.addScore(GAME_NAME, user.getName(), currentTime);
			}
			request.getSession().setAttribute(FIELD_ATTR, null);
		}
		
		if (request.getParameter("rating") != null && user.isLogged()) {
			ratingDAO.rateGame(GAME_NAME, user.getName(), Integer.parseInt(request.getParameter("rating")));
		}
		
		if (user.isLogged()) {
			int rating;
			if (ratingDAO.getRatingOfUser(GAME_NAME, user.getName()) != null) {
				rating = ratingDAO.getRatingOfUser(GAME_NAME, user.getName()).getRating();
			} else {
				rating = 0;
			}
			out.printf("<br/>");
			out.printf("<form>");
			out.printf("Rate game: ");
			out.printf("<select name=\"rating\">");
			
			for (int i = 0; i <= 10; i++) {
				out.printf("<option value=\"%s\" %s>%s</option>\n", i, rating == i ? "selected" : "" ,i);
			}
			out.printf("</select>");
			out.printf(" <input type=\"submit\" value=\"Submit\">");
			out.printf("</form>");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	


}
