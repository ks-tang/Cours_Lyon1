package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;

import fr.univlyon1.m1if.m1if03.daos.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;

import java.io.IOException;

/**
 * Cette servlet récupère les infos de l'utilisateur dans sa session et affiche l'interface du chat (sans modifier l'URL).<br>
 * Renvoie une erreur 409 (Conflict) si le login de l'utilisateur existe déjà.<br>
 * &Agrave; noter le fait que l'URL à laquelle elle répond ("/chat") n'est pas le nom de la servlet.
 */
@WebServlet(name = "Connect", value = "/chat", loadOnStartup = 2)
public class Connect extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupère un User dans l'attribut de session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Ajout de l'User dans le DAO (qui est un attribut de contexte)
        //TODO Renvoie un 409 Conflict quand on se relogue ou quand on modifie son profil.
        // Ce sera réglé quand tous les formulaires ne pointeront plus vers la même URL "/chat" (MVC).
        UserDao dao = (UserDao) this.getServletContext().getAttribute("userDao");
        try {
            if(request.getParameter("action").equals("login")) { 
                dao.add(user);
            }
            if(request.getParameter("action").equals("update")) {
                dao.findOne(user.getLogin()).setName(request.getParameter("name"));
            }

        } catch (NameAlreadyBoundException e) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "Le login " + user.getLogin() + " n'est plus disponible.");
            return;
        } catch (NameNotFoundException e) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "Le login " + user.getLogin() + " est introuvable.");
            return;
        }

        // Utilise un RequestDispatcher pour "transférer" la requête à un autre objet, en interne du serveur.
        // Ceci n'est pas une redirection HTTP ; le client n'est pas informé de cette redirection et "voit" toujours la même URL.
        // Note :
        //     il existe deux méthodes pour transférer une requête (et une réponse) à l'aide d'un RequestDispatcher : include et forward
        //     voir les différences ici : https://docs.oracle.com/javaee/6/tutorial/doc/bnagi.html
        request.getRequestDispatcher("interface.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Ceci est une redirection HTTP ; le client est informé qu'il doit requêter une autre ressource
        response.sendRedirect("index.html");
    }
}
