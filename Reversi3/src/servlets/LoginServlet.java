package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import engine.User;
import engine.UsersManager;
import utils.SessionsUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "LoginServlet",
        urlPatterns = {"/LoginServlet"}
)
public class LoginServlet extends HttpServlet {
    UsersManager usersManager = UsersManager.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("OMEr");
        resp.setContentType("application/json");
        Gson gson = new Gson();

        PrintWriter out = resp.getWriter();
        String action = req.getParameter("action");

        switch (action) {
            case "checkIfLogged":
                checkIfLogged(req, gson, out);
                break;
            case "getUsers":
                out.println((gson.toJson(usersManager.getUsers())));
                break;
            case "logout":
                logout(req, gson, out);
                break;
            case "login":
                login(req, gson, out);
                break;
        }

        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void login(HttpServletRequest req, Gson gson, PrintWriter out) {
        String newUserName = req.getParameter("userName");
        boolean isComputer = ("true").equals(req.getParameter("computerFlag"));

        if (newUserName.equals("")) {
            out.println(gson.toJson("Please fill your name!"));
        } else if (SessionsUtils.isLoggedIn(req.getSession()) == false) {
            if (usersManager.isUserExists(newUserName)) {
                out.println(gson.toJson("User name already exists"));
            } else {
                usersManager.addUser(newUserName, isComputer);
                HttpSession newSession = req.getSession();
                SessionsUtils.loginUser(newSession, newUserName, isComputer);
                out.println(gson.toJson(""));
            }
        } else {
            out.println(gson.toJson("User is already logged in"));
        }

    }

    private void logout(HttpServletRequest req, Gson gson, PrintWriter out) {
        HttpSession curSession = req.getSession(false);
        usersManager.removeUser(curSession.getAttribute("userName").toString());
        SessionsUtils.logoutUser(curSession);
        out.println(gson.toJson(""));
    }

    private void checkIfLogged(HttpServletRequest req, Gson gson, PrintWriter out) {
        HttpSession curSession = req.getSession(false);
        String jsonStr = null;
        User user = UsersManager.getInstance().getUserByName(SessionsUtils.getUsername(curSession));
        if (curSession != null) {
            jsonStr = "{\"userName\" : " + user.getName() + ", \"connected\": true" + ",\"inGame\": " + user.getInGameNumber() + "}";
        } else {
            jsonStr = "{\"connected\":false  }";
        }
        JsonElement element = gson.fromJson(jsonStr, JsonElement.class); //Converts the json string to JsonElement without POJO
        JsonObject jsonObj = element.getAsJsonObject();
        out.println(gson.toJson(jsonObj));


    }
}
