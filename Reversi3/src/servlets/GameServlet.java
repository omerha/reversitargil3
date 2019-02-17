package servlets;

import com.google.gson.Gson;
import engine.GamesManager;
import engine.User;
import engine.UsersManager;
import engine.gamelogic.GameManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GameServlet",
        urlPatterns = {"/GameServlet"}
)
public class GameServlet extends HttpServlet {
    GamesManager gamesManager = GamesManager.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        switch (action) {
            case "getGameManager":
                getGameManager(request, out, gson);
        }
        out.close();
    }

    private void getGameManager(HttpServletRequest req, PrintWriter out, Gson gson) {
        GameManager res = null;
        UsersManager userManager = UsersManager.getInstance();
        User currUSer = userManager.getUserByName((String) req.getSession().getAttribute("userName"));
        if (currUSer.getInGameNumber() != -1) {
            res = gamesManager.getGameByNumber(currUSer.getInGameNumber());
        } else {
            res = gamesManager.getGameByName(currUSer.getName());
        }
        out.println(gson.toJson(res));
    }
}
