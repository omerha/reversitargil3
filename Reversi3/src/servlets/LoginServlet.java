package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import engine.UsersManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        Cookie ck[] = req.getCookies();
        String jsonStr=null;
        if(ck!=null) {
            jsonStr = "{\"userName\" : " + ck[0].getValue() + ", \"connected\": true }";
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
        } else if (isUserAlreadyLoggedIn(req, newUserName) == false) {
            if(usersManager.isUserExists(newUserName)){
                out.println(gson.toJson("User name already exists"));
            }
            else {
                usersManager.addUser(newUserName, isComputer);
                Cookie newCookie = new Cookie("name", newUserName);
                resp.addCookie(newCookie);
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
    boolean isUserAlreadyLoggedIn(HttpServletRequest req,String userName){
       boolean res = false;
        Cookie ck[] = req.getCookies();
        if(ck!=null) {
            for (Cookie cok : ck) {
                if (cok.getValue().toLowerCase().equals(userName.toLowerCase())) {
                    res = true;
                    break;
                }
            }
        }

        return res;
    }
}
