package games.minesweeper;

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
import games.minesweeper.Tile.State;
import model.Score;

@WebServlet("/mines")
public class MinesweeperServlet extends HttpServlet {

	private static final String FIELD_ATTR = "fieldMine";

	private static final int ROW_COUNT = 16;

	private static final int COLUMN_COUNT = 30;

	private static final int MINE_COUNT = 80;
	
	private static final String GAME_NAME = "Minesweeper";
	
	@Inject
	LoggedUser user;

	@Inject
	ScoreDAO scoreDAO;
	
	@Inject 
	RatingDAO ratingDAO;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		boolean failed = false;

		PrintWriter out = response.getWriter();
		Field field = (Field) request.getSession().getAttribute(FIELD_ATTR);

		if (field == null || request.getParameter("new") != null) {
			field = new Field(ROW_COUNT, COLUMN_COUNT, MINE_COUNT);
			request.getSession().setAttribute(FIELD_ATTR, field);
		}

		boolean mark = (request.getParameter("marked") != null);

		try {
			int row = Integer.parseInt(request.getParameter("row"));
			int column = Integer.parseInt(request.getParameter("column"));
			if (field.getState() == GameState.PLAYING) {
				if (field.getTile(row, column).getState() == State.OPEN) {
					field.openTilesAround(row, column);
				} else {
					if (mark) {
						field.markTile(row, column);
					} else {
						field.openTile(row, column);
					}
				}
			}
		} catch (Exception e) {
		}

		out.print("Mark tile? ");
		if (mark) {
			out.println("<a href=\"?\"><img src=\"resources/images/mines/marked.png\"></a><br>");
		} else {
			out.println("<a href=\"?marked=true\"><img src=\"resources/images/mines/closed.png\"></a><br>");
		}
		out.printf("Remaining mine count: %s <br>", field.getRemainingMineCount());

		if (field.getState() == GameState.FAILED) {
			failed = true;
			field.openAll();
		}

		for (int row = 0; row < ROW_COUNT; row++) {
			for (int column = 0; column < COLUMN_COUNT; column++) {
				Tile tile = field.getTile(row, column);

				switch (tile.getState()) {
				case CLOSED:

					printTile(out, row, column, mark, "closed");
					break;
				case OPEN:
					if (tile instanceof Mine) {
						printTile(out, row, column, mark, "mine");
					}
					if (tile instanceof Clue) {
						Clue clue = (Clue) tile;
						int value = clue.getValue();
						printTile(out, row, column, mark, "open" + value);
					}
					break;
				case MARKED:
					printTile(out, row, column, mark, "marked");
					break;
				}

			}
			out.println("<br>");
		}
		if (failed) {
			out.printf("<p id=\"lose\">You Lost!</p>");
			request.getSession().setAttribute(FIELD_ATTR, null);
		} else if (field.getState() == GameState.SOLVED) {
			double currentTime = (double) (System.currentTimeMillis() - field.getTime()) / 1000;
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

	private void printTile(PrintWriter out, int row, int column, boolean mark, String image) {
		if (mark) {
			out.printf("<a href=\"?row=%s&column=%s&marked=%s\"><img src=\"resources/images/mines/%s.png\"></a>", row,
					column, mark, image);
		} else {
			out.printf("<a href=\"?row=%s&column=%s\"><img src=\"resources/images/mines/%s.png\"></a>", row, column,
					image);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		doGet(request, response);
	}

}
