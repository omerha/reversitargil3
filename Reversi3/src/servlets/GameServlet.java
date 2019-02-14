package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import engine.GamesManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "GameServlet",
        urlPatterns = {"/GameServlet"}
)
public class GameServlet extends HttpServlet {
    GamesManager gamesManager = GamesManager.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("TEST");
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        Gson gson = new Gson();
        if(("getAllGames").equals(req.getParameter("action"))){
            out.println(gson.toJson(gamesManager.getGamesMap()));
        }
        else {
            try {
                gamesManager.addGame(req.getParameter("file"), "OMER");
                out.println(gson.toJson(""));
            } catch (Exception e) {

                String jsonStr = "{\"text\" : \"" + e.getMessage() + "\", \"error\": true }";
                JsonElement element = gson.fromJson(jsonStr, JsonElement.class); //Converts the json string to JsonElement without POJO
                JsonObject jsonObj = element.getAsJsonObject();
                out.println(gson.toJson(jsonObj));
                e.printStackTrace();
            }
        }
    }
}
