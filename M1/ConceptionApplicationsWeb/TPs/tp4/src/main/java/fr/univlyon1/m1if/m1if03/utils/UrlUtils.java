package fr.univlyon1.m1if.m1if03.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Classe utilitaire permettant l'analyse des URLs.<br>
 * Destinée à être utilisée par les contrôleurs.
 *
 * @author Lionel Médini
 */
public final class UrlUtils {
    /**
     * Pour valider la règle <code>HideUtilityClassConstructorCheck</code> de CheckStyle...
     * Une classe utilitaire qui n'a que des méthodes statiques ne doit pas avoir de constructeur public, parce qu'il ne servirait à rien de l'instancier.
     */
    private UrlUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Renvoie un tableau de String contenant les différentes parties de l'URL (entre les slashs).<br>
     * Ne renvoie que les informations utiles. Supprime :
     * <ul>
     *     <li>le contexte applicatif</li>
     *     <li>la chaîne identifiant le contrôleur (p.ex. "users" pour <code>UsersController</code>)</li>
     * </ul>
     * @param request La requête HTTP à analyser
     * @return un tableau de String potentiellement vide contenant les parties non vides de l'URL
     */
    public static String[] getUrlParts(HttpServletRequest request) {
        return request.getRequestURI().replace(request.getContextPath() + "/", "").split("/");
    }

    /**
     * Renvoie la fin d'une URL, calculée à partir d'un certain terme.<br>
     * Utilisée pour les redirections en fonction d'une partie de l'URL.
     * @param request Une requête avec une URL "à rallonge"
     * @param start L'index (par rapport à un tableau renvoyé par <a href="#getUrlEnd(jakarta.servlet.http.HttpServletRequest,int)">getUrlParts()</a>) à partir duquel renvoyer l'URL
     * @return La fin de l'URL après l'élément indiqué
     */
    public static String getUrlEnd(HttpServletRequest request, int start) {
        String[] url = getUrlParts(request);
        StringBuilder urlEnd = new StringBuilder();
        for (int i = start; i < url.length; i++) {
            urlEnd.append("/").append(url[i]);
        }
        return urlEnd.toString();
    }
}
