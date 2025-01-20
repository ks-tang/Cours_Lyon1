package fr.univlyon1.m1if.m1if03.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import fr.univlyon1.m1if.m1if03.utils.ChatonsM1if03JwtHelper;

/**
 * Filtre d'authentification.
 * Autorise les requêtes suivantes :
 * <ol>
 *     <li>URLs ne nécessitant pas d'authentification (whitelist)</li>
 *     <li>Requêtes d'authentification</li>
 *     <li>Requêtes d'utilisateurs déjà authentifiés</li>
 * </ol>
 * Dans les cas contraires, renvoie un code d'erreur HTTP 401 (Unauthorized).
 *
 * @author Lionel Médini
 */
@WebFilter
public class AuthenticationFilter extends HttpFilter {
    private static final String[] WHITELIST = {"/", "/index.jsp", "/login.html", "/css/style.css", "/users", "/users/", "/users/login"};
    private final List<String> logouts = new ArrayList<String>();

    /**
     * Vérifie qu'une requête est authentifiée, sauf requêtes autorisées.
     * @param request Voir doc...
     * @param response Voir doc...
     * @param chain Voir doc...
     * @throws IOException Voir doc...
     * @throws ServletException Voir doc...
     */
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Permet de retrouver la fin de l'URL (après l'URL du contexte) -> indépendant de l'URL de déploiement
        String url = request.getRequestURI().replace(request.getContextPath(), "");

        // 1) Laisse passer les URLs ne nécessitant pas d'authentification
        for(String tempUrl: WHITELIST) {
            if(url.equals(tempUrl)) {
                chain.doFilter(request, response);
                if(request.getAttribute("hasLogin") != null) {
                    logouts.remove((String)request.getAttribute("hasLogin"));
                }
                return;
            }
        }

        // 2) Traite les requêtes qui doivent être authentifiées
        // Note :
        //   le paramètre false dans request.getSession(false) permet de récupérer null si la session n'est pas déjà créée.
        //   Sinon, l'appel de la méthode getSession() la crée automatiquement.
        try {
            String token = request.getHeader("Authorization").split(" ")[1];
            String user = ChatonsM1if03JwtHelper.verifyToken(token, request);
            request.setAttribute("loggedUser", user);

            DecodedJWT jwt = JWT.decode(token);
            request.setAttribute("loggedUserSalons", jwt.getClaim("member").asString());
            request.setAttribute("loggedUserOwnedSalons", jwt.getClaim("owned").asString());
            if(logouts.contains(user)){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous vous êtes déjà déconnecté.");
            } else {
                chain.doFilter(request, response); // Le token existe donc l'utilisateur est authentifié
                if(request.getAttribute("hasLogout") != null) {
                    logouts.add(user);
                }
            }
            return;
        } catch (NullPointerException | JWTVerificationException e) { // Le token n'existe pas donc l'utilisateur n'est pas authentifié
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous devez vous connecter.");
        } catch (Exception e){
            //Ne devrait pas arriver
        }
    }

}
