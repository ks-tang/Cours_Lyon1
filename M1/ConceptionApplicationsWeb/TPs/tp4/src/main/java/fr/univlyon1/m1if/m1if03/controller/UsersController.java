package fr.univlyon1.m1if.m1if03.controller;

import fr.univlyon1.m1if.m1if03.dao.UserDao;
import fr.univlyon1.m1if.m1if03.dto.user.UserDtoMapper;
import fr.univlyon1.m1if.m1if03.dto.user.UserRequestDto;
import fr.univlyon1.m1if.m1if03.dto.user.UserResponseDto;
import fr.univlyon1.m1if.m1if03.exceptions.ForbiddenLoginException;
import fr.univlyon1.m1if.m1if03.model.User;
import fr.univlyon1.m1if.m1if03.model.operations.UserResource;
import fr.univlyon1.m1if.m1if03.utils.UrlUtils;
import fr.univlyon1.m1if.m1if03.utils.ContentNegotiationHelper;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InvalidNameException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Contrôleur de CU "users".<br>
 * Cette servlet gère les CU liés aux opérations CRUD sur la collection d'utilisateurs :
 * <ul>
 *     <li>Création / modification / suppression d'un utilisateur : POST, PUT, DELETE</li>
 *     <li>Récupération de la liste d'utilisateurs / d'un utilisateur / d'une propriété d'un utilisateur : GET</li>
 * </ul>
 * Les opérations spécifiques (login &amp; logout) sont transférées à la servlet <a href="UsersBusinessController.html"><code>UsersBusinessController</code></a>.
 *
 * @author Lionel Médini
 */
@WebServlet(name = "UsersController", urlPatterns = {"/users", "/users/*"})
public class UsersController extends HttpServlet {
    private UserDtoMapper userMapper;
    private UserResource userResource;

    //<editor-fold desc="Méthode de gestion du cycle de vie">
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        UserDao userDao = (UserDao) config.getServletContext().getAttribute("userDao");
        userMapper = new UserDtoMapper(config.getServletContext());
        userResource = new UserResource(userDao);
    }
    //</editor-fold>

    //<editor-fold desc="Méthodes de service">

    /**
     * Réalise l'aiguillage des requêtes POST.
     * <ul>
     *     <li>Création d'un utilisateur<br>
     *     Renvoie un code 201 (Created) en cas de création d'un utilisateur, ou une erreur HTTP appropriée sinon.</li>
     *     <li>Dispatching des requêtes de login et de logout vers le contrôleur d'opérations</li>
     * </ul>
     *
     * @param request  Une requête de création, contenant un body avec un login, un password et un nom ; ou une requête de login / logout
     * @param response Une réponse vide, avec uniquement un code de réponse et éventuellement un header <code>Location</code>
     * @throws IOException Voir doc...
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] url = UrlUtils.getUrlParts(request);

        switch (url.length) {
            case 1: // Création d'un utilisateur
                // TODO Parsing des paramètres "old school" ; sera amélioré dans la partie négociation de contenus...
                //String login = request.getParameter("login");
                //String password = request.getParameter("password");
                //String name = request.getParameter("name");
                UserRequestDto user = (UserRequestDto) ContentNegotiationHelper.getDtoFromRequest(request, UserRequestDto.class);

                try {
                    userResource.create(user.getLogin(), user.getPassword(), user.getName());
                    response.setHeader("Location", "users/" + user.getLogin());
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } catch (IllegalArgumentException | ForbiddenLoginException ex) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                } catch (NameAlreadyBoundException e) {
                    response.sendError(HttpServletResponse.SC_CONFLICT, "Le login " + user.getLogin() + " n'est plus disponible.");
                }
                break;
            case 2: // redirection (interne) vers le contrôleur délégué
                if (url[1].equals("login") || url[1].equals("logout")) {
                    getServletContext().getNamedDispatcher("UsersBusinessController").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Réalise l'aiguillage des requêtes GET.
     * Renvoie une représentation de la ressource demandée.
     * <ul>
     *     <li>Soit la liste de tous les utilisateurs</li>
     *     <li>Soit un utilisateur</li>
     *     <li>soit une propriété d'un utilisateur</li>
     *     <li>soit une redirection vers une sous-propriété</li>
     * </ul>
     * Renvoie un code de réponse 200 (OK) en cas de représentation, 302 (Found) en cas de redirection, sinon une erreur HTTP appropriée.
     *
     * @param request  Une requête vide
     * @param response Une réponse contenant :
     *                 <ul>
     *                     <li>la liste des liens vers les instances de <code>User</code> existantes</li>
     *                     <li>les propriétés d'un utilisateur</li>
     *                     <li>une propriété donnée d'un utilisateur donné</li>
     *                 </ul>
     * @throws ServletException Voir doc...
     * @throws IOException      Voir doc...
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("directory", "user");
        String[] url = UrlUtils.getUrlParts(request);

        if (url.length == 1) { // Renvoie la liste de tous les utilisateurs
            Set<Serializable> users = userResource.readAll();
            List<String> usersList = new ArrayList<>();
            for(Serializable u : users) {
                usersList.add("users/" + u.toString());
            }
            request.setAttribute("list", usersList);
            request.setAttribute("htmlPath", "/WEB-INF/components/users.jsp");
            return;
        }
        try {
            User user = userResource.readOne(url[1]);
            UserResponseDto userDto = userMapper.toDto(user);
            switch (url.length) {
                case 2: // Renvoie un DTO d'utilisateur (avec toutes les infos le concernant pour pouvoir le templater dans la vue)
                    request.setAttribute("dto", userDto);
                    // Transfère la gestion de l'interface à une JSP
                    // Utilise un RequestDispatcher pour "transférer" la requête à un autre objet, en interne du serveur.
                    // Ceci n'est pas une redirection HTTP ; le client n'est pas informé de cette redirection et "voit" toujours la même URL.
                    // Note :
                    //     il existe deux méthodes pour transférer une requête (et une réponse) à l'aide d'un RequestDispatcher : include et forward
                    //     voir les différences ici : https://docs.oracle.com/javaee/6/tutorial/doc/bnagi.html
                    //request.getRequestDispatcher("/WEB-INF/components/user.jsp").include(request, response);
                    request.setAttribute("htmlPath", "/WEB-INF/components/users.jsp");
                    break;
                case 3: // Renvoie une propriété d'un utilisateur
                    switch (url[2]) {
                        case "name":
                            request.setAttribute("dto", new UserResponseDto(userDto.getLogin(), userDto.getName(), null, null, null));
                            //request.getRequestDispatcher("/WEB-INF/components/user.jsp").include(request, response);
                            request.setAttribute("htmlPath", "/WEB-INF/components/users.jsp");
                            break;
                        case "ownedSalons":
                            request.setAttribute("dto", new UserResponseDto(userDto.getLogin(), null, userDto.getOwnedSalons(), null, null));
                            //request.getRequestDispatcher("/WEB-INF/components/user.jsp").include(request, response);
                            request.setAttribute("htmlPath", "/WEB-INF/components/users.jsp");
                            break;
                        case "memberOfSalons":
                            request.setAttribute("dto", new UserResponseDto(userDto.getLogin(), null, null, userDto.getMemberOfSalons(), null));
                            //request.getRequestDispatcher("/WEB-INF/components/user.jsp").include(request, response);
                            request.setAttribute("htmlPath", "/WEB-INF/components/users.jsp");
                            break;
                        case "createdMessages":
                            request.setAttribute("dto", new UserResponseDto(userDto.getLogin(), null, null, null, userDto.getCreatedMessages()));
                            //request.getRequestDispatcher("/WEB-INF/components/user.jsp").include(request, response);
                            request.setAttribute("htmlPath", "/WEB-INF/components/users.jsp");
                            break;
                        default:
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
                    break;
                default: // Redirige vers l'URL qui devrait correspondre à la sous-propriété demandée (qu'elle existe ou pas ne concerne pas ce contrôleur)
                    // Construction de la fin de l'URL vers laquelle rediriger
                    String urlEnd = UrlUtils.getUrlEnd(request, 3);
                    switch (url[2]) {
                        case "ownedSalons":
                        case "memberOfSalons":
                            response.sendRedirect("salons" + urlEnd);
                            break;
                        case "createdMessages":
                            response.sendRedirect("messages" + urlEnd);
                            break;
                        default:
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
            }
        } catch (IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (NameNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "L'utilisateur " + url[1] + " n'existe pas.");
        } catch (InvalidNameException ignored) {
            // Ne devrait pas arriver car les paramètres sont déjà des Strings
        }
    }

    /**
     * Réalise l'aiguillage des requêtes PUT.
     * En fonction du login passé dans l'URL :
     * <ul>
     *     <li>création de l'utilisateur s'il n'existe pas</li>
     *     <li>Mise à jour sinon</li>
     * </ul>
     * Renvoie un code de statut 204 (No Content) en cas de succès ou une erreur HTTP appropriée sinon.
     *
     * @param request  Une requête dont l'URL est de la forme <code>/users/{login}</code>, et contenant :
     *                 <ul>
     *                     <li>un password (obligatoire en cas de création)</li>
     *                     <li>un nom (obligatoire en cas de création)</li>
     *                 </ul>
     * @param response Une réponse vide (si succès)
     * @throws IOException Voir doc...
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] url = UrlUtils.getUrlParts(request);
        String login = url[1];
        // TODO Parsing des paramètres "old school" ; sera amélioré dans la partie négociation de contenus...
        //String password = request.getParameter("password");
        //String name = request.getParameter("name");
        UserRequestDto user = (UserRequestDto) ContentNegotiationHelper.getDtoFromRequest(request, UserRequestDto.class);

        if (url.length == 2) {
            try {
                userResource.update(login, user.getPassword(), user.getName());
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (IllegalArgumentException ex) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            } catch (NameNotFoundException e) {
                try {
                    userResource.create(login, user.getPassword(), user.getName());
                    response.setHeader("Location", "users/" + login);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } catch (NameAlreadyBoundException ignored) {
                } catch (IllegalArgumentException | ForbiddenLoginException ex) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                }
            } catch (InvalidNameException ignored) {
                // Ne devrait pas arriver car les paramètres sont déjà des Strings
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Réalise l'aiguillage des requêtes DELETE.<br>
     * En clair : appelle simplement l'opération de suppression de l'utilisateur.<br>
     * Renvoie un code 204 (No Content) si succès ou une erreur HTTP appropriée sinon.
     *
     * @param request  Une requête dont l'URL est de la forme <code>/users/{login}</code>
     * @param response Une réponse vide (si succès)
     * @throws IOException Voir doc...
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] url = UrlUtils.getUrlParts(request);
        String login = url[1];
        if (url.length == 2) {
            try {
                userResource.delete(login);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (IllegalArgumentException ex) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            } catch (NameNotFoundException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "L'utilisateur " + login + " n'existe pas.");
            } catch (InvalidNameException ignored) {
                // Ne devrait pas arriver car les paramètres sont déjà des Strings
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    //</editor-fold>
}
