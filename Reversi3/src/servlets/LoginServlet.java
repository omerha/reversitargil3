package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
        boolean isComputer = false;
        if(req.getParameter("computerFlag")!=null) {
             isComputer = req.getParameter("computerFlag").equals("true");
        }
        String newUserName = req.getParameter("userName");
    if(action.equals("checkIfLogged")){

        HttpSession curSession = req.getSession(false);
        String jsonStr=null;
        if(curSession!=null) {
            jsonStr = "{\"userName\" : " + SessionsUtils.getUsername(curSession) + ", \"connected\": true }";
        }else{
            jsonStr = "{\"connected\":false  }";
        }
            JsonElement element = gson.fromJson (jsonStr, JsonElement.class); //Converts the json string to JsonElement without POJO
            JsonObject jsonObj = element.getAsJsonObject();
            out.println(gson.toJson(jsonObj));


    }
    else if(action.equals("getUsers")){
        out.println((gson.toJson(usersManager.getUsers())));
    }
    else {
        //   out.println(gson.toJson("Hello " + newUserName));
        if (newUserName.equals("")) {
            out.println(gson.toJson("Please fill your name!"));
        } else if (SessionsUtils.isLoggedIn(req.getSession()) == false) {
            if(usersManager.isUserExists(newUserName)){
                out.println(gson.toJson("User name already exists"));
            }
            else {
                usersManager.addUser(newUserName, isComputer);
               HttpSession newSession = req.getSession();
                SessionsUtils.loginUser(newSession,newUserName,isComputer);
                out.println(gson.toJson(""));
            }
        } else {
            out.println(gson.toJson("User is already logged in"));
        }
    }
    out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Harel");
    }

}
