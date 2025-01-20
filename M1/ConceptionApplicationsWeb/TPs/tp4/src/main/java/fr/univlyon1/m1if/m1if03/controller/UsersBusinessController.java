package fr.univlyon1.m1if.m1if03.controller;

import fr.univlyon1.m1if.m1if03.model.User;
import fr.univlyon1.m1if.m1if03.dao.UserDao;
import fr.univlyon1.m1if.m1if03.dao.SalonDao;
import fr.univlyon1.m1if.m1if03.model.operations.UserBusiness;
import fr.univlyon1.m1if.m1if03.utils.ChatonsM1if03JwtHelper;
import fr.univlyon1.m1if.m1if03.utils.ContentNegotiationHelper;
import fr.univlyon1.m1if.m1if03.dto.user.UserRequestDto;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;
import java.io.IOException;

/**
 * Contrôleur délégué aux CU concernant les opérations métier sur les users.
 * Concrètement : login et logout.
 *
 * @author Lionel Médini
 */
@WebServlet(name = "UsersBusinessController", urlPatterns = {})
public class UsersBusinessController extends HttpServlet {
    private UserBusiness userBusiness;
    private UserDao userDao;
    private SalonDao salonDao;

    //<editor-fold desc="Méthode de gestion du cycle de vie">
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        UserDao userDao = (UserDao) config.getServletContext().getAttribute("userDao");
        SalonDao salonDao = (SalonDao) config.getServletContext().getAttribute("salonDao");
        userBusiness = new UserBusiness(userDao);
    }
    //</editor-fold>

    //<editor-fold desc="Méthode de service">
    /**
     * Appelle l'opération demandée à la fin de l'URL de la requête.
     * @param request  Voir doc...
     * @param response Voir doc...
     * @throws IOException Voir doc...
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getRequestURI().endsWith("login")) {
            // TODO Parsing des paramètres "old school". Sera amélioré par la suite.
            //String login = request.getParameter("login");
            //String password = request.getParameter("password");
            UserRequestDto user = (UserRequestDto) ContentNegotiationHelper.getDtoFromRequest(request, UserRequestDto.class);
            if (user.getLogin() != null && !user.getLogin().equals("")) {
                try {
                    User userFind = (User) userDao.findOne(user.getLogin());
                    if (userFind.verifyPassword(user.getPassword())) {
                        String token = ChatonsM1if03JwtHelper.generateToken(
                            user.getLogin(), 
                            ChatonsM1if03JwtHelper.setSalon(salonDao.findByMember(user.getLogin())), 
                            ChatonsM1if03JwtHelper.setSalon(salonDao.findByOwner(user.getLogin())),
                            request);
                        response.addHeader("Authorization", "Bearer" + token);
                        request.setAttribute("hasLogin", user.getLogin());
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    } else {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Les login et mot de passe ne correspondent pas.");
                    }
                } catch (IllegalArgumentException ex) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                } catch (NameNotFoundException e) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "L'utilisateur " + user.getLogin() + " n'existe pas.");
                } catch (InvalidNameException ignored) {
                    // Ne doit pas arriver car les logins des utilisateurs sont des Strings
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if (request.getRequestURI().endsWith("logout")) {
            //userBusiness.logout(request);
            request.setAttribute("hasLogout", true);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            // Ne devrait pas arriver mais sait-on jamais...
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    //</editor-fold>
}
