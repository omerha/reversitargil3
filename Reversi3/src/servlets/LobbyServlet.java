package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import engine.GamesManager;
import engine.User;
import engine.UsersManager;
import engine.gamelogic.GameManager;
import utils.SessionsUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "LobbyServlet",
        urlPatterns = {"/LobbyServlet"}
)
public class LobbyServlet extends HttpServlet {
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
        String action = req.getParameter("action");
        switch(action){
            case "getAllGames":
                out.println(gson.toJson(gamesManager.getGamesMap()));
                break;
            case "uploadGame":
                uploadGameHelper(req,out,gson);
                break;
            case "joinGame":
                joinGame(req,out,gson);
                break;
            default:
                break;
        }
        out.close();
    }
    private void uploadGameHelper(HttpServletRequest req,PrintWriter out,Gson gson){
        try {
            gamesManager.addGame(req.getParameter("file"), (String) req.getSession(false).getAttribute("userName"));
            out.println(gson.toJson(""));
        } catch (Exception e) {

            String jsonStr = "{\"text\" : \"" + e.getMessage() + "\", \"error\": true }";
            JsonElement element = gson.fromJson(jsonStr, JsonElement.class); //Converts the json string to JsonElement without POJO
            JsonObject jsonObj = element.getAsJsonObject();
            out.println(gson.toJson(jsonObj));
            e.printStackTrace();
        }
    }
    private void joinGame(HttpServletRequest req,PrintWriter out,Gson gson){
        String gameID = req.getParameter("gameID");
        GameManager currGameManager = gamesManager.getGameByNumber(Integer.parseInt(gameID));
        UsersManager usersManager = UsersManager.getInstance();
        String jsonStr = null;
        JsonElement element; //Converts the json string to JsonElement without POJO
        JsonObject jsonObj = null;
        String userName = (String) req.getSession(false).getAttribute("userName");
        User user =  usersManager.getUserByName(userName);
        if(currGameManager.putUserInPlayer(user)) {
          jsonStr = "{\"isActiveGame\":false,\"error\":\"\"}";
            if(currGameManager.isActiveGame()){
                jsonStr = "{\"isActiveGame\":true,\"error\":\"\"}";
            }
        }
        else{
            jsonStr = "{\"error\":\"The game is full\"}";

        }
        element = gson.fromJson(jsonStr, JsonElement.class);
        jsonObj = element.getAsJsonObject();
        out.println((gson.toJson(jsonObj)));
    }
}
