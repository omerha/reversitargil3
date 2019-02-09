package servlets;

import engine.GamesManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "GameServlet",
        urlPatterns = {"/GameServlet"}
)
public class GameServlet extends HttpServlet {
    GamesManager gamesManager = GamesManager.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("TEST");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("TEST");
        try {
            gamesManager.addGame(req.getParameter("file"),"OMER");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
