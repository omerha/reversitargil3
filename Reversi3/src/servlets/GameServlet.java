package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import engine.GamesManager;
import engine.User;
import engine.UsersManager;
import engine.gamelogic.GameManager;
import javafx.beans.property.StringProperty;

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
                break;
            case "getPlayerIndex":
                getPlayerIndex(request, out, gson);
                break;
            case "runHumanTurn":
                runHumanTurn(request, out, gson);
                break;
            case "undoTurn":
                undoTurn(request,out,gson);
                break;
            case "beginnerMode":
                beginnerMode(request,out,gson);
                break;
            case "leaveGame":
                leaveGame(request,out,gson);
                break;
            case "checkForWinner":
                checkForWinner(request,out);
                break;
            case "endGame":
                endGame(request,out);
                break;
            case "playComputerTurn":
                playComputerTurn(request,gson,out);
            default:
                break;

        }
        out.close();
    }

    private void playComputerTurn(HttpServletRequest req,Gson gson, PrintWriter out) {
        GameManager gameManager = gamesManager.getGameByName((String) req.getSession().getAttribute("userName"));
        try {
            gameManager.playComputerTurn();
            gameManager.setTotalNumOfTurns(gameManager.getTotalNumOfTurns() + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.println("");
    }

    private void endGame(HttpServletRequest req, PrintWriter out) {
        UsersManager userManager = UsersManager.getInstance();
        User currUSer = userManager.getUserByName((String) req.getSession().getAttribute("userName"));
        JsonObject jsonObj = new JsonObject();
        String jsonStr = "";
        GameManager gameManager = gamesManager.getGameByNumber(currUSer.getInGameNumber());
        if(gameManager!=null) {
            userManager.removeUsersFromGame(gameManager.getPlayers());
            try {
                gameManager.endAndRestartGame();
            } catch (Exception e) {
                jsonStr = e.getMessage();
            }
        }
        jsonObj.addProperty("error",jsonStr);
        out.println(jsonObj);
    }

    private void checkForWinner(HttpServletRequest req, PrintWriter out) {
        JsonObject jsonObj = new JsonObject();
        int winnerIndex =-1;
        GameManager gameManager = gamesManager.getGameByName((String) req.getSession().getAttribute("userName"));
        if (gameManager.getGameBoard().isFullBoard()) {
             winnerIndex = gameManager.getWinnerIndex();
            gameManager.setReplayMode(true);
        }
        String winnerName = "";
        jsonObj.addProperty("winnerIndex",winnerIndex);
        if(winnerIndex!=-1){
            winnerName = gameManager.getPlayers()[winnerIndex].getPlayerName();
        }
        jsonObj.addProperty("winnerName",winnerName);
        out.println(jsonObj);
    }

    private void leaveGame(HttpServletRequest req, PrintWriter out, Gson gson) {
        UsersManager userManager = UsersManager.getInstance();
        User currUSer = userManager.getUserByName((String) req.getSession().getAttribute("userName"));
        JsonObject jsonObj = new JsonObject();
        GameManager gameManager = gamesManager.getGameByNumber(currUSer.getInGameNumber());
        try {
            if(gameManager.retirePlayerFromGame()){
                jsonObj.addProperty("endGame",false);

            }else{
                jsonObj.addProperty("endGame",true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        currUSer.setInGameNumber(-1);


        out.println(gson.toJson(jsonObj));
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

    private void getPlayerIndex(HttpServletRequest req, PrintWriter out, Gson gson) {
        UsersManager userManager = UsersManager.getInstance();
        User currUSer = userManager.getUserByName((String) req.getSession().getAttribute("userName"));
        GameManager currGameManager = gamesManager.getGameByNumber(Integer.parseInt(req.getParameter("gameIndex")));
        out.println(gson.toJson(currGameManager.getPlayerIndexByName(currUSer.getName())));
    }

    private void runHumanTurn(HttpServletRequest req, PrintWriter out, Gson gson) {
        String jsonStr = "";
        GameManager gameManager = gamesManager.getGameByName((String) req.getSession().getAttribute("userName"));
        JsonObject jsonObj = new JsonObject();
        try {
            gameManager.checkIfTurnIsValidAndPerform(Integer.parseInt(req.getParameter("row")), Integer.parseInt(req.getParameter("col")),Integer.parseInt(req.getParameter("playerIndex")), true);
            gameManager.setTotalNumOfTurns(gameManager.getTotalNumOfTurns() + 1);

        } catch (Exception e) {
            e.printStackTrace();
          jsonStr = e.getMessage();
        }
        jsonObj.addProperty("error",jsonStr);

        out.println(gson.toJson(jsonObj));

    }

    private void undoTurn(HttpServletRequest req, PrintWriter out, Gson gson) {
        String jsonStr = "";
        GameManager gameManager = gamesManager.getGameByName((String) req.getSession().getAttribute("userName"));
        JsonObject jsonObj = new JsonObject();

        try {
            gameManager.undoTurn(true);

        } catch (Exception e) {
            e.printStackTrace();
            jsonStr = e.getMessage();
        }
        jsonObj.addProperty("error",jsonStr);
        out.println(gson.toJson(jsonObj));
    }
    private void beginnerMode(HttpServletRequest req, PrintWriter out, Gson gson) {
        String jsonStr = "";
        int [][] res = new int[0][];
        UsersManager userManager = UsersManager.getInstance();
        User currUSer = userManager.getUserByName((String) req.getSession().getAttribute("userName"));
        GameManager gameManager = gamesManager.getGameByNumber(currUSer.getInGameNumber());
        JsonObject jsonObj = new JsonObject();

        try {
            res = gameManager.displayPossibleMovesScore(currUSer.getPlayerSign());

        } catch (Exception e) {
            e.printStackTrace();
            jsonStr = e.getMessage();
        }
        jsonObj.addProperty("error",jsonStr);
        jsonObj.addProperty("result",gson.toJson(res));
        out.println(gson.toJson(jsonObj));
    }
}
