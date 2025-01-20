package fr.univlyon1.m1if.m1if03.controller;

import fr.univlyon1.m1if.m1if03.dto.salon.SalonDtoMapper;
import fr.univlyon1.m1if.m1if03.dto.salon.SalonRequestDto;
import fr.univlyon1.m1if.m1if03.dto.salon.SalonResponseDto;
import fr.univlyon1.m1if.m1if03.model.operations.SalonResource;
import fr.univlyon1.m1if.m1if03.model.Salon;
import fr.univlyon1.m1if.m1if03.dao.SalonDao;
import fr.univlyon1.m1if.m1if03.dao.UserDao;
import fr.univlyon1.m1if.m1if03.model.User;
import fr.univlyon1.m1if.m1if03.utils.UrlUtils;
import fr.univlyon1.m1if.m1if03.utils.ChatonsM1if03JwtHelper;
import fr.univlyon1.m1if.m1if03.utils.ContentNegotiationHelper;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
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
 * Contrôleur de CU "salons".<br>
 * Cette servlet gère les CU liés aux opérations CRUD sur la collection de salons :
 * <ul>
 *     <li>Création / modification / suppression d'un salon : POST, PUT, DELETE</li>
 *     <li>Récupération de la liste d'utilisateurs / d'un utilisateur / d'une propriété d'un salon : GET</li>
 * </ul>
 * Les opérations spécifiques (login &amp; logout) sont transférées à la servlet <a href="SalonsBusinessController.html"><code>SalonsBusinessController</code></a>.
 */
@WebServlet(name = "SalonsController", urlPatterns = {"/salons", "/salons/*"})
public class SalonsController extends HttpServlet {
    private SalonDtoMapper salonMapper;
    private SalonResource salonResource;
    private SalonDao salonDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        UserDao userDao = (UserDao) config.getServletContext().getAttribute("userDao");
        salonDao = (SalonDao) config.getServletContext().getAttribute("salonDao");

        salonMapper = new SalonDtoMapper(config.getServletContext());
        salonResource = new SalonResource(salonDao, userDao);

    }

    //<editor-fold desc="Méthodes de service">

    /**
     * Réalise l'aiguillage des requêtes POST.
     * <ul>
     *     <li>Création d'un salon<br>
     *     Renvoie un code 201 (Created) en cas de création d'un salon, ou une erreur HTTP appropriée sinon.</li>
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
            case 1: // Création d'un salon
                SalonRequestDto salonRequestDto = (SalonRequestDto) ContentNegotiationHelper.getDtoFromRequest(request, SalonRequestDto.class);
                String owner = salonRequestDto.getOwner();
                String name = salonRequestDto.getName();

                try {
                    int salonId = salonResource.create(name, owner);
                    response.setHeader("Location", "salons/" + salonId);

                    String token = ChatonsM1if03JwtHelper.generateToken(
                        owner, 
                        ChatonsM1if03JwtHelper.setSalon(salonDao.findByMember(owner)), 
                        ChatonsM1if03JwtHelper.setSalon(salonDao.findByOwner(owner)),
                        request);
                    response.setHeader("Authorization", "Bearer" + token);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                        
                } catch (InvalidNameException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                } catch (NameNotFoundException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                } catch (NameAlreadyBoundException e) {
                    response.sendError(HttpServletResponse.SC_CONFLICT, "Le nom de salon " + name + " n'est plus disponible.");
                } catch (IllegalArgumentException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                }
                break;
            case 3: // redirection (interne) vers le contrôleur délégué
                getServletContext().getNamedDispatcher("SalonsBusinessController").forward(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


    /**
     * Réalise l'aiguillage des requêtes GET.
     * Renvoie une représentation de la ressource demandée.
     * <ul>
     *     <li>Soit la liste de tous les salons</li>
     *     <li>Soit un salon</li>
     *     <li>soit une propriété d'un salon</li>
     *     <li>soit une redirection vers une sous-propriété</li>
     * </ul>
     * Renvoie un code de réponse 200 (OK) en cas de représentation, 302 (Found) en cas de redirection, sinon une erreur HTTP appropriée.
     *
     * @param request  Une requête vide
     * @param response Une réponse contenant :
     *                 <ul>
     *                     <li>la liste des liens vers les instances de <code>User</code> existantes</li>
     *                     <li>les propriétés d'un salon</li>
     *                     <li>une propriété donnée d'un salon donné</li>
     *                 </ul>
     * @throws ServletException Voir doc...
     * @throws IOException      Voir doc...
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] url = UrlUtils.getUrlParts(request);
        if (url.length == 1) { // Renvoie la liste de tous les utilisateurs
            Set<Serializable> salons = salonResource.readAll();
            List<String> salonList = new ArrayList<>();
            for(Serializable s : salons) {
                salonList.add("salons/" + s.toString());
            }
            request.setAttribute("list", salonList);
            request.setAttribute("htmlPath", "/WEB-INF/components/salons.jsp");
            return;
        }
        try {
            Salon salon = salonResource.readOne(url[1]);
            SalonResponseDto salonDto = salonMapper.toDto(salon);
            request.setAttribute("dto", salonDto);
            switch (url.length) {
                case 2: // Renvoie un DTO de salon (avec toutes les infos le concernant pour pouvoir le templater dans la vue)
                    //request.setAttribute("salonDto", salonDto);
                    // Transfère la gestion de l'interface à une JSP
                    // Utilise un RequestDispatcher pour "transférer" la requête à un autre objet, en interne du serveur.
                    // Ceci n'est pas une redirection HTTP ; le client n'est pas informé de cette redirection et "voit" toujours la même URL.
                    // Note :
                    //     il existe deux méthodes pour transférer une requête (et une réponse) à l'aide d'un RequestDispatcher : include et forward
                    //     voir les différences ici : https://docs.oracle.com/javaee/6/tutorial/doc/bnagi.html
                    //request.getRequestDispatcher("/WEB-INF/components/user.jsp").include(request, response);
                    request.setAttribute("htmlPath", "/WEB-INF/components/salons.jsp");
                    break;
                case 3: // Renvoie une propriété d'un salon
                    switch (url[2]) {
                        case "name":
                            request.setAttribute("dto", new SalonResponseDto(salonDto.getName(), null, null, null));
                            //request.getRequestDispatcher("/WEB-INF/components/salon.jsp").include(request, response);
                            request.setAttribute("htmlPath", "/WEB-INF/components/salons.jsp");
                            break;
                        case "owner":
                            request.setAttribute("dto", new SalonResponseDto(null, salonDto.getOwner(), null, null));
                            //request.getRequestDispatcher("/WEB-INF/components/salon.jsp").include(request, response);
                            request.setAttribute("htmlPath", "/WEB-INF/components/salons.jsp");
                            break;
                        case "members":
                            request.setAttribute("dto", new SalonResponseDto(null, null, salonDto.getMembers(), null));
                            //request.getRequestDispatcher("/WEB-INF/components/salon.jsp").include(request, response);
                            request.setAttribute("htmlPath", "/WEB-INF/components/salons.jsp");
                            break;
                        case "messages":
                            request.setAttribute("dto", new SalonResponseDto(null, null, null, salonDto.getMessages()));
                            //request.getRequestDispatcher("/WEB-INF/components/salon.jsp").include(request, response);
                            request.setAttribute("htmlPath", "/WEB-INF/components/salons.jsp");
                            break;
                        case "content":
                            getServletContext().getNamedDispatcher("SalonsBusinessController").forward(request, response);
                        break;
                        default:
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
                    break;
                default: // Redirige vers l'URL qui devrait correspondre à la sous-propriété demandée (qu'elle existe ou pas ne concerne pas ce contrôleur)
                    // Construction de la fin de l'URL vers laquelle rediriger
                    String urlEnd = UrlUtils.getUrlEnd(request, 3);
                    switch (url[2]) {
                        case "owner":
                            response.sendRedirect("users/" + salon.getOwner() + urlEnd);
                        case "members":
                            response.sendRedirect("users" + urlEnd);
                            break;
                        case "messages":
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
     *     <li>création du salon s'il n'existe pas</li>
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
        String salonId = url[1];
        SalonRequestDto salonRequestDto = (SalonRequestDto) ContentNegotiationHelper.getDtoFromRequest(request, SalonRequestDto.class);
        String name = salonRequestDto.getName();

        if (url.length == 2) {
            try {
                salonResource.update(salonId, name);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (IllegalArgumentException ex) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            } catch (NameNotFoundException e) {
                try {
                    String login = salonRequestDto.getOwner();
                    salonResource.create(name, login);
                    response.setHeader("Location", "salons/" + salonId);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } catch (NameAlreadyBoundException n) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Le salon" + name + "existe déjà");
                } catch (InvalidNameException i) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, i.getMessage());
                } catch (NameNotFoundException n) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, n.getMessage());
                } catch (IllegalArgumentException i) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, i.getMessage());
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
     * En clair : appelle simplement l'opération de suppression du salon.<br>
     * Renvoie un code 204 (No Content) si succès ou une erreur HTTP appropriée sinon.
     *
     * @param request  Une requête dont l'URL est de la forme <code>/users/{login}</code>
     * @param response Une réponse vide (si succès)
     * @throws IOException Voir doc...
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] url = UrlUtils.getUrlParts(request);
        //SalonRequestDto salonRequestDto = (SalonRequestDto) ContentNegotiationHelper.getDtoFromRequest(request, SalonRequestDto.class);
        //String salonId = Integer.toString(salonRequestDto.getId());
        String salonId = url[1];

        if (url.length == 2) {
            try {
                salonResource.delete(salonId);
                User user = (User) request.getAttribute("user");
                String token = ChatonsM1if03JwtHelper.generateToken(
                        user.getLogin(), 
                        ChatonsM1if03JwtHelper.setSalon(salonDao.findByMember(user.getLogin())), 
                        ChatonsM1if03JwtHelper.setSalon(salonDao.findByOwner(user.getLogin())),
                        request);
                response.setHeader("Authorization", "Bearer" + token);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (IllegalArgumentException ex) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            } catch (NameNotFoundException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Le salon " + salonId + " n'existe pas.");
            } catch (InvalidNameException ignored) {
                // Ne devrait pas arriver car les paramètres sont déjà des Strings
            }
        } else if(url.length == 3) {
            try {
                getServletContext().getNamedDispatcher("SalonBusinessController").forward(request, response);
            } catch (ServletException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
            
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    //</editor-fold>
}
