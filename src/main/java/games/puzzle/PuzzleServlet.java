package games.puzzle;

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

@WebServlet("/puzzle")
public class PuzzleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String FIELD_ATTR = "fieldPuzzle";
	
	private static final String GAME_NAME = "Puzzle";
	
	@Inject
	LoggedUser user;
	
	@Inject
	ScoreDAO scoreDAO;
	
	@Inject 
	RatingDAO ratingDAO;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int rowCount = 5;
		int columnCount = 5;

		PrintWriter out = response.getWriter();
		Field field = (Field) request.getSession().getAttribute(FIELD_ATTR);

		if (field == null || request.getParameter("new") != null) {
			field = new Field(rowCount, columnCount);
			request.getSession().setAttribute(FIELD_ATTR, field);
		}

		try {
			if (!field.checkGameState()) {
				if (request.getParameter("row2") != null && request.getParameter("column2") != null) {
					int row1 = Integer.parseInt(request.getParameter("row1"));
					int column1 = Integer.parseInt(request.getParameter("column1"));
					int row2 = Integer.parseInt(request.getParameter("row2"));
					int column2 = Integer.parseInt(request.getParameter("column2"));
					field.swapTiles(field.getTile(row1, column1), field.getTile(row2, column2));
				}
			}
		} catch (Exception e) {
		}

		out.print("<table>");
		for (int row = 0; row < rowCount; row++) {
			for (int column = 0; column < columnCount; column++) {
				Tile tile = field.getTile(row, column);
				if (request.getParameter("row2") != null && request.getParameter("column2") != null) {
					out.printf("<a href=\"?row1=%s&column1=%s\" id=\"stone\">%s </a>", row, column, tile.getValue());
				}
				else if (request.getParameter("row1") != null && request.getParameter("column1") != null) {
					out.printf("<a href=\"?row1=%s&column1=%s&row2=%s&column2=%s\" id=\"stone\">%s </a>",
							request.getParameter("row1"), request.getParameter("column1"), row, column, tile.getValue());
				} else {
					out.printf("<a href=\"?row1=%s&column1=%s\" id=\"stone\">%s </a>", row, column, tile.getValue());
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
