package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Cette servlet récupère les infos de l'utilisateur dans sa session et affiche l'interface du chat (sans modifier l'URL).
 * &Agrave; noter le fait que l'URL à laquelle elle répond ("/chat") n'est pas le nom de la servlet.
 */
@WebServlet(name = "Connect", value = "/chat")
public class Connect extends HttpServlet {
    // Map d'objets User destinée à être stockée dans le contexte applicatif et à être accédée par tous les objets de l'application
    private final Map<String, User> users = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        // Cette instruction doit toujours être au début de la méthode init() pour pouvoir accéder à l'objet config.
        super.init(config);
        //Récupère le contexte applicatif et y place la map d'User
        ServletContext context = config.getServletContext();
        context.setAttribute("users", users);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupère l'User dans l'attribut de session et le place dans la map (qui est un attribut de contexte)
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        users.put(user.getLogin(), user);
        // Utilise un RequestDispatcher pour "transférer" la requête à un autre objet, en interne du serveur.
        // Ceci n'est pas une redirection HTTP ; le client n'est pas informé de cette redirection.
        // Note :
        //     il existe deux méthodes pour transférer une requête (et une réponse) à l'aide d'un RequestDispatcher : include et forward
        //     voir les différences ici : https://docs.oracle.com/javaee/6/tutorial/doc/bnagi.html
        request.getRequestDispatcher("interface.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Ceci est une redirection HTTP ; le client est informé qu'il doit requêter une auter ressource
        response.sendRedirect("index.html");
    }
}
