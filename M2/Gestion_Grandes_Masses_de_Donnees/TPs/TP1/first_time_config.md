# Première configuration sur chaque VM

| IP ggmd1           | IP ggmd2           | IP ggmd3          |
|--------------------|--------------------|-------------------|
| `192.168.246.150`  | `192.168.246.146`  | `192.168.246.227` |

- Se connecter sur une des VM
```
ssh -i ggmd-etus.key ubuntu@192.168.246.X
```
- Lancer cette commande
```
sudo nano /etc/postgresql/15/main/pg_hba.conf
```
- Aller vers la fin du fichier au niveau de la partie `# IPv4 local connections` et sous cette ligne :
```
host    all             all             192.168.246.0/8         trust
```
- Rajouter a ligne suivante :
```
host    all             all             127.0.0.1/8             trust
```
- Enregistrer puis quitter (`Ctrl + X`).
- Enfin taper la commande suivante pour relancer PGSQL : `sudo systemctl restart postgresql`
- Se connecter à la base de données avec PostgreSQL :
```
sudo -i -u postgres
```
```
psql
```
- Créer la base de données en local pour la première fois :
```postgresql
CREATE DATABASE insee_deces;
```
```postgresql
CREATE USER etum2 WITH PASSWORD 'etum2';
```
```postgresql
GRANT ALL PRIVILEGES ON DATABASE insee_deces to etum2;
```