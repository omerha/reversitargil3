package servlets;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("OMEr");
        resp.setContentType("application/json");
        Gson gson = new Gson();
        PrintWriter out = resp.getWriter();
        boolean isComputer = req.getParameter("ComputerCheckBox") != null;
        String newUserName = req.getParameter("UserNameInput");
      //   out.println(gson.toJson("Hello " + newUserName));
        if(newUserName.equals("")){
            out.println(gson.toJson("Please fill your name!"));
        }
        else{
            out.println(gson.toJson(""));
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Harel");
    }
}
