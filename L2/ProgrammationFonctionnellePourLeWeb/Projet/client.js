/* ******************************************************************
 * Constantes de configuration
 */
const apiKey = "968015a5-a505-4e1b-8e03-fefb1d287cfb";
const serverUrl = "https://lifap5.univ-lyon1.fr";

/* ******************************************************************
 * Gestion des tabs "Voter" et "Toutes les citations" */

function fetchCitations(url)											//fonction qui extrait les donnees des citations
{
	return fetch(url+"/citations")
		.then( (response) => {return response.text()} )					//crée un texte à partir des données extraits
		.then( (txt) => {return JSON.parse(txt)} );						// crée des objets JSON à partir du texte extrait
}



//Affichage de chaque citation

fetchCitations(serverUrl)					
.then( (donnees) => { 
	const tbody = document.getElementById('tbody'); 				//placement à l'endroit du tbody dans le html
	for(let indice in donnees){							            		//boucle d'affichage des données
		const ligne = tbody.insertRow(-1);					        	//crée une ligne à la fin du tableau
		const colonne1 = ligne.insertCell(0);					      	//crée une cellule
		colonne1.innerHTML = Number(indice)+1;				      	//attribution des données dans la cellule
		const colonne2 = ligne.insertCell(1);
		colonne2.innerHTML = donnees[indice]["character"];
		const colonne3 = ligne.insertCell(2);
		colonne3.innerHTML = donnees[indice]["quote"];
		const colonne4 = ligne.insertCell(3);					      	//Dernière colonne qui affiche les informations détaillées avec un clic
		ligne.addEventListener("click", () => {
		colonne4.innerHTML = "<b>Personnage :</b> " + donnees[indice]["character"] + "</br>"+
		  "<b>Citation :</b> " + donnees[indice]["quote"] + "</br>"+
		  "<b>Image :</b> " + "<a href=\"" + donnees[indice]["image"] + "\">Lien</a> " + "</br>"+
		  "<b>Origine :</b> " + donnees[indice]["origin"] + "</br>"+
		  "<b>Direction :</b> " + donnees[indice]["characterDirection"];
		} );
	}
})







	
	
//Affichage aléatoire des duels
fetchCitations(serverUrl)
.then( (donnees) => {
	const nbrandom1 = RandomInt(0,donnees.length);							//pour obtenir un chiffre au hasard entre 0 et le nombre de citations total
	const nbrandom2 = RandomInt(0,donnees.length);
	const card1 = donnees[nbrandom1];									        	// choisis la citation correspondante
	const card2 = donnees[nbrandom2]; 
	ChangePosition(card1, card2);											          //change la direction de l'image selon sa place
		
	document.getElementById('image1').setAttribute('src', card1["image"]);	//pour placer les éléments sur la page html
	document.getElementById('citation1').innerHTML = card1["quote"];
	document.getElementById('origine1').innerHTML = card1["character"]+" dans "+card1["origin"];
	document.getElementById('image2').setAttribute('src', card2["image"]);
	document.getElementById('citation2').innerHTML = card2["quote"];
	document.getElementById('origine2').innerHTML = card2["character"]+" dans "+card2["origin"];
	
	document.getElementById('voteCardLeft').addEventListener("click", () =>       //Gestion des boutons de vote
    voterLeft(card1,card2));		
	document.getElementById('voteCardRight').addEventListener("click", () =>      //au clic, appelle les fonctions
    voterRight(card1,card2));		
})
	
//fonction qui renvoie un nombre aléatoire entre min et max
function RandomInt(min,max)
{
	return Math.floor( Math.random() * (max-min+1) ) + min;
}

// fonction qui adapte la direction de l'image des citations
function ChangePosition(card1, card2)			//prend en paramètres les deux citations: citation de gauche(card1) et de droite (card2)
{
	if(card1['characterDirection'] != "Left")				//la citation de gauche doit avoir la direction Left
	{														//change la direction si ce n'est pas le cas
		document.getElementById('image1')
		.setAttribute('style', "transform: scaleX(-1)");
	}
	if(card2['characterDirection'] != "Right")				//la citation de droite doit avoir la direction Right
	{														//change la direction si ce n'est pas le cas
		document.getElementById('image2')
		.setAttribute('style', "transform: scaleX(-1)");
	}
}


//Fonctions de vote
function voterLeft(card1, card2)
{
	fetch(serverUrl + "/citations/duels", {
    method: 'POST',
    headers: { "x-api-key": apiKey, "Content-Type": "application/json"},
    body: JSON.stringify({"winner": card1['_id'], "looser": card2['_id']})
    })
	.then(console.log("A voté, pour la citation de gauche."));
	//.then(window.location.reload());
}

function voterRight(card1, card2)
{
	fetch(serverUrl + "/citations/duels", {
    method: 'POST',
    headers: { "x-api-key": apiKey ,"Content-Type": "application/json"},
    body: JSON.stringify({"winner": card2['_id'], "looser": card1['_id']})
    })
	.then(console.log("A voté, pour la citation de droite."));
	//.then(window.location.reload());
}






//fonction pour ajouter une citation dans la base de données
function ajouter(formulaire)
{
	const quote = formulaire.elements['citation'].value;
	const character = formulaire.elements['perso'].value;
	const origin = formulaire.elements['origine'].value;
	const img = formulaire.elements['image'].value;
	const characterDirection = formulaire.elements['PersoDirection'].value;
	
	fetch(serverUrl+"/citations", 
	{
		method: 'POST',
		headers: { "x-api-key": apiKey , "Content-Type": "application/json"},
		body: JSON.stringify( { 
			"quote":quote,
			"character":character,
			"image":img,
			"characterDirection":characterDirection,
			"origin":origin 
			} )
    })
}


 
 
/**
 * Affiche/masque les divs "div-duel" et "div-tout"
 * selon le tab indiqué dans l'état courant.
 *
 * @param {Etat} etatCourant l'état courant
 */
 
 
 
function majTab(etatCourant) {
  console.log("CALL majTab");
  const dDuel = document.getElementById("div-duel");
  const dTout = document.getElementById("div-tout");
  const tDuel = document.getElementById("tab-duel");
  const tTout = document.getElementById("tab-tout");
  //const dAjout = document.getElementById("div-ajout");
  //const tAjout = document.getElementById("tab-ajout");
  if (etatCourant.tab === "duel") {
    dDuel.style.display = "flex";
    tDuel.classList.add("is-active");
    dTout.style.display = "none";
    tTout.classList.remove("is-active");
  } else {
    dTout.style.display = "flex";
    tTout.classList.add("is-active");
    dDuel.style.display = "none";
    tDuel.classList.remove("is-active");
  }
  
  /*
  if (etatCourant.tab === "div-ajout")
  {
	dAjout.style.display = "flex";
    tAjout.classList.add("is-active");
    dDuel.style.display = "none";
	dTout.style.display = "none";
    tDuel.classList.remove("is-active");
	tTout.classList.remove("is-active");
  }
  
  
  */
  
}

/**
 * Mets au besoin à jour l'état courant lors d'un click sur un tab.
 * En cas de mise à jour, déclenche une mise à jour de la page.
 *
 * @param {String} tab le nom du tab qui a été cliqué
 * @param {Etat} etatCourant l'état courant
 */
function clickTab(tab, etatCourant) {
  console.log(`CALL clickTab(${tab},...)`);
  if (etatCourant.tab !== tab) {
    etatCourant.tab = tab;
    majPage(etatCourant);
  }
}

/**
 * Enregistre les fonctions à utiliser lorsque l'on clique
 * sur un des tabs.
 *
 * @param {Etat} etatCourant l'état courant
 */
function registerTabClick(etatCourant) {
  console.log("CALL registerTabClick");
  document.getElementById("tab-duel").onclick = () =>
    clickTab("duel", etatCourant);
  document.getElementById("tab-tout").onclick = () =>
    clickTab("tout", etatCourant);
	
  //document.getElementById("form-ajout").onclick = () =>
	//clickTab("div-ajout", etatCourant);
}

/* ******************************************************************
 * Gestion de la boîte de dialogue (a.k.a. modal) d'affichage de
 * l'utilisateur.
 * ****************************************************************** */

/**
 * Fait une requête GET authentifiée sur /whoami
 * @returns une promesse du login utilisateur ou du message d'erreur
 */
function fetchWhoami() {
  return fetch(serverUrl + "/whoami", { headers: { "x-api-key": apiKey } })
    .then((response) => response.json())
    .then((jsonData) => {
      if (jsonData.status && Number(jsonData.status) != 200) {
        return { err: jsonData.message };
      }
      return jsonData;
    })
    .catch((erreur) => ({ err: erreur }));
}

/**
 * Fait une requête sur le serveur et insère le login dans
 * la modale d'affichage de l'utilisateur.
 * @returns Une promesse de mise à jour
 */
function lanceWhoamiEtInsereLogin() {
  return fetchWhoami().then((data) => {
    const elt = document.getElementById("elt-affichage-login");
    const ok = data.err === undefined;
    if (!ok) {
      elt.innerHTML = `<span class="is-error">${data.err}</span>`;
    } else {
      elt.innerHTML = `Bonjour ${data.login}.`;
    }
    return ok;
  });
}

/**
 * Affiche ou masque la fenêtre modale de login en fonction de l'état courant.
 *
 * @param {Etat} etatCourant l'état courant
 */
function majModalLogin(etatCourant) {
  const modalClasses = document.getElementById("mdl-login").classList;
  if (etatCourant.loginModal) {
    modalClasses.add("is-active");
    lanceWhoamiEtInsereLogin();
  } else {
    modalClasses.remove("is-active");
  }
}

/**
 * Déclenche l'affichage de la boîte de dialogue du nom de l'utilisateur.
 * @param {Etat} etatCourant
 */
function clickFermeModalLogin(etatCourant) {
  etatCourant.loginModal = false;
  majPage(etatCourant);
}

/**
 * Déclenche la fermeture de la boîte de dialogue du nom de l'utilisateur.
 * @param {Etat} etatCourant
 */
function clickOuvreModalLogin(etatCourant) {
  etatCourant.loginModal = true;
  majPage(etatCourant);
}

/**
 * Enregistre les actions à effectuer lors d'un click sur les boutons
 * d'ouverture/fermeture de la boîte de dialogue affichant l'utilisateur.
 * @param {Etat} etatCourant
 */
function registerLoginModalClick(etatCourant) {
  document.getElementById("btn-close-login-modal1").onclick = () =>
    clickFermeModalLogin(etatCourant);
  document.getElementById("btn-close-login-modal2").onclick = () =>
    clickFermeModalLogin(etatCourant);
  document.getElementById("btn-open-login-modal").onclick = () =>
    clickOuvreModalLogin(etatCourant);
}

/* ******************************************************************
 * Initialisation de la page et fonction de mise à jour
 * globale de la page.
 * ****************************************************************** */

/**
 * Mets à jour la page (contenu et événements) en fonction d'un nouvel état.
 *
 * @param {Etat} etatCourant l'état courant
 */
function majPage(etatCourant) {
  console.log("CALL majPage");
  majTab(etatCourant);
  majModalLogin(etatCourant);
  registerTabClick(etatCourant);
  registerLoginModalClick(etatCourant);
}

/**
 * Appelé après le chargement de la page.
 * Met en place la mécanique de gestion des événements
 * en lançant la mise à jour de la page à partir d'un état initial.
 */
function initClientCitations() {
  console.log("CALL initClientCitations");
  const etatInitial = {
    tab: "duel",
    loginModal: false,
  };
  majPage(etatInitial);
}

// Appel de la fonction init_client_duels au après chargement de la page
document.addEventListener("DOMContentLoaded", () => {
  console.log("Exécution du code après chargement de la page");
  initClientCitations();
});
