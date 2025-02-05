// MENU 2
#ifndef _MENU_H
#define _MENU_H


typedef void (*MenuFonction)();


struct MenuLigne
{
	char texte[64];
	MenuFonction fonction_commande;
};

class Menu
{
public:
	Menu();
	~Menu();
	void afficher();
	void ajouterLigne(const char txt[64], MenuFonction fonct);
	void loop();
private:
	MenuLigne lignes[32];
	int nb_lignes;
	
	int question();
};


#endif
