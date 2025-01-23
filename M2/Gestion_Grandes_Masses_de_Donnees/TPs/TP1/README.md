# GGMD - TP1 : Fragmentation

### Sujet du TP
[Lien vers sujet TP1 - Fragmentation](https://forge.univ-lyon1.fr/ggmd/ggmd-tps-etudiants/-/blob/main/GGMD-TP1_Fragmentation.md)

### Liens utiles
- [Sujet CM1 Partie BDR](https://perso.univ-lyon1.fr/nicolas.lumineau/ue/ggmd/cm/GGMD-CM-PartieBDR.pdf)
- [Sujet TD1](https://perso.univ-lyon1.fr/nicolas.lumineau/ue/ggmd/td/GGMD_TD1_Sujet23.pdf)
- [Setup Foreign Data Wrapper](https://towardsdatascience.com/how-to-set-up-a-foreign-data-wrapper-in-postgresql-ebec152827f3)

## Informations VM

- Fichier `ggmd-etus.key` sur Tomuss à mettre dans le répertoire home.
- Mettre les droits 600 avec *chmod*.

| IP ggmd1           | IP ggmd2           | IP ggmd3          |
|--------------------|--------------------|-------------------|
| `192.168.246.150`  | `192.168.246.146`  | `192.168.246.227` |

### Première configuration de chaque VM

[Suivre cette procédure pour configurer chaque VM pour la première fois](./first_time_config.md)

### Se connecter à nos VM
- Se connecter en ssh en remplaçant le dernier nombre
```
ssh -i .ssh/ggmd-etus.key ubuntu@192.168.246.X
```
- Se connecter à la base de données avec PostgreSQL
```
sudo -i -u postgres
```
```
psql
```

### Se connecter à la VM professeur
```
psql -h 192.168.246.247 -U etum2 insee_deces
```

## Introduction

### Schéma des données

> personne(**idp**, nom, prenoms, datenaiss, lieunaiss, datedeces, lieudeces, age)  
region(**reg**, nom, #cheflieu, zone)  
departement(**dep**, nom, #cheflieu, #reg)  
commune(com, nom, #dep)  
mairie(#codeInsee, cp, nomOrga, nomCom, email, tel, url, adresse, latitude, longitude, dateMaj)

### Questions

**1/**  
*Expliquer ce que retourne la requête.*
```postgresql
SELECT * 
FROM personne
WHERE lieunaiss NOT IN (
	SELECT com
	FROM commune );
```
La requête retourne les personnes décédées en France mais qui ne sont pas nées en France.

**2/**  
*Donner les adresses (postales et mail) des mairies des communes de naissance des personnes décédées dans le département 63 durant le mois d'avril 2018. (509 n-uplets attendus)*
```postgresql
SELECT m.adresse, m.email
FROM mairie m
JOIN personne p ON p.lieunaiss = m.codeInsee
WHERE p.lieudeces LIKE '63%'
AND p.datedeces BETWEEN '2018-04-01' AND '2018-04-30';
```

## A - Définition des connexions inter-bases

**1/**  
*Définir le 'wrapper' qui vous permettra de vous connecter à la base insee_deces sur ggmd_prof.*  
- Si l'extension foreign data wrapper n'existe pas, le créer.
```postgresql
CREATE EXTENSION IF NOT EXISTS postgres_fdw;
```
- Création du foreign server
```postgresql
CREATE SERVER remote_insee_deces 
FOREIGN DATA WRAPPER postgres_fdw 
OPTIONS (
    host '192.168.246.247', 
    port '5432', 
    dbname 'insee_deces'
);
```
**2/**  
*Définir l'association utilisateur local/utilisateur distant qui vous donnera le droit
d'accès à la base insee_deces.*
- Création du user mapping
```postgresql
CREATE USER MAPPING
FOR CURRENT_USER
SERVER remote_insee_deces 
OPTIONS (
    user 'etum2', 
    password 'etum2'
);
```
**3/**  
*Définir les 'foreign tables' qui vous permettront d'accéder à la base insee_deces depuis vos VM.*
```postgresql
CREATE FOREIGN TABLE commune(
    com varchar(5), 
    nom varchar(254), 
    dep varchar(5)
) SERVER remote_insee_deces;
```
```postgresql
CREATE FOREIGN TABLE departement(
    dep varchar(5), 
    nom varchar(254), 
    cheflieu varchar(10), 
    reg varchar(5)
) SERVER remote_insee_deces;
```
```postgresql
CREATE FOREIGN TABLE mairie(
    codeinsee varchar(10), 
    cp varchar(10), 
    nomorga varchar(254), 
    nomcom varchar(80), 
    email varchar(150), 
    tel varchar(150), 
    url varchar(254), 
    adresse varchar(254), 
    latitude varchar(150), 
    longitude varchar(150), 
    datemaj varchar(254)
) SERVER remote_insee_deces;
```
```postgresql
CREATE FOREIGN TABLE personne(
    idp integer, 
    nom varchar(80), 
    prenoms varchar(80),
    datenaiss date, 
    lieunaiss varchar(8), 
    datedeces date, 
    lieudeces varchar(8), 
    age interval
) SERVER remote_insee_deces;
```
```postgresql
CREATE FOREIGN TABLE region(
    reg varchar(5), 
    nom varchar(254), 
    cheflieu varchar(10), 
    zone smallint
) SERVER remote_insee_deces;
```

## B - Fragmentation des données  

### Schéma à suivre
- ggmd1 : Paris
- ggmd2 : Lyon
- ggmd3 : Marseille

### Aide exemple pour faire les fragmentations
```postgresql
-- a comment

CREATE TABLE nom_table AS 
SELECT * 
FROM foreign_table 
WHERE condition = 'N';
```

### Question

*À partir des définitions algébriques dont vous disposez, créer et peupler les tables nécessaires à l'implantation de vos fragments.*

- `C1.` Le site de **Paris** stocke les régions de la zone 1. Le site de **Lyon** stocke les régions de la zone 2 et le site de **Marseille** stocke les régions de la zone 3.
```postgresql
-- ggmd1 / Paris

CREATE TABLE Paris AS
SELECT *
FROM region
WHERE zone = '1';
```
```postgresql
-- ggmd2 / Lyon

CREATE TABLE Lyon AS
SELECT * 
FROM region 
WHERE zone = '2';
```
```postgresql
-- ggmd3 / Marseille

CREATE TABLE Marseille AS
SELECT * 
FROM remote_region 
WHERE zone = '3';
```
   
- `C2.` Chaque site gère les départements rattachés aux régions qu’il stocke.
```postgresql
-- ggmd1 / Paris

CREATE TABLE departement_zone_1 AS
SELECT * 
FROM departement d 
WHERE d.reg IN (
    SELECT reg
    FROM Paris
);
```
```postgresql
-- ggmd2 / Lyon

CREATE TABLE departement_zone_2 AS
SELECT *
FROM departement d
WHERE d.reg IN (
    SELECT reg
    FROM Lyon
);
```
```postgresql
-- ggmd3 / Marseille

CREATE TABLE departement_zone_3 AS
SELECT *
FROM departement d
WHERE d.reg IN (
    SELECT reg
    FROM Marseille
);
```

- `C3.` Chaque site gère les communes rattachées aux départements qu’il stocke.
```postgresql
-- ggmd1 / Paris

CREATE TABLE commune_zone_1 AS
SELECT *
FROM commune c
WHERE c.dep IN (
    SELECT dep
    FROM departement_zone_1
);
```
```postgresql
-- ggmd2 / Lyon

CREATE TABLE commune_zone_2 AS
SELECT *
FROM commune c
WHERE c.dep IN (
    SELECT dep
    FROM departement_zone_2
);
```
```postgresql
-- ggmd3 / Marseille

CREATE TABLE commune_zone_3 AS
SELECT *
FROM commune c
WHERE c.dep IN (
    SELECT dep
    FROM departement_zone_3
);
```

- `C4.` L’âge des personnes est stockées sur le site de **Paris**.
```postgresql
-- ggmd1 / Paris

CREATE TABLE age_personne AS
SELECT idp, age
FROM personne;
```

- `C5.` Chaque site gère les noms et prénoms, date de naissance et lieu de naissance des personnes nées dans une commune qu’il stocke.
```postgresql
-- ggmd1 / Paris

CREATE TABLE personne_info_naissance_zone_1 AS
SELECT idp, nom, prenoms, datenaiss, lieunaiss
FROM personne;

CREATE TABLE personne_naissance_zone_1 AS
SELECT * FROM personne_info_naissance_zone_1 p
WHERE EXISTS (
    SELECT 1
    FROM commune_zone_1 c
    WHERE p.lieunaiss = c.com
);
```
```postgresql
-- ggmd2 / Lyon

CREATE TABLE personne_info_naissance_zone_2 AS
SELECT idp, nom, prenoms, datenaiss, lieunaiss
FROM personne;

CREATE TABLE personne_naissance_zone_2 AS
SELECT * FROM personne_info_naissance_zone_2 p
WHERE EXISTS (
    SELECT 1
    FROM commune_zone_2 c
    WHERE p.lieunaiss = c.com
);
```
```postgresql
-- ggmd3 / Marseille

CREATE TABLE personne_info_naissance_zone_3 AS
SELECT idp, nom, prenoms, datenaiss, lieunaiss
FROM personne;

CREATE TABLE personne_naissance_zone_3 AS
SELECT * FROM personne_info_naissance_zone_3 p
WHERE EXISTS (
    SELECT 1
    FROM commune_zone_3 c
    WHERE p.lieunaiss = c.com
);
```

- `C6.` Chaque site gère les noms et prénoms, date de décès et lieu de décès des personnes décédées dans une commune qu’il stocke.
```postgresql
-- ggmd1 / Paris

CREATE TABLE personne_info_deces_zone_1 AS
SELECT idp, nom, prenoms, datedeces, lieudeces
FROM personne;

CREATE TABLE personne_deces_zone_1 AS
SELECT * FROM personne_info_deces_zone_1 p
WHERE EXISTS (
    SELECT 1
    FROM commune_zone_1 c
    WHERE p.lieudeces = c.com
);
```
```postgresql
-- ggmd2 / Lyon

CREATE TABLE personne_info_deces_zone_2 AS
SELECT idp, nom, prenoms, datedeces, lieudeces
FROM personne;

CREATE TABLE personne_deces_zone_2 AS
SELECT * FROM personne_info_deces_zone_2 p
WHERE EXISTS (
    SELECT 1
    FROM commune_zone_2 c
    WHERE p.lieudeces = c.com
);
```
```postgresql
-- ggmd3 / Marseille

CREATE TABLE personne_info_deces_zone_3 AS
SELECT idp, nom, prenoms, datedeces, lieudeces
FROM personne;

CREATE TABLE personne_deces_zone_3 AS
SELECT * FROM personne_info_deces_zone_3 p
WHERE EXISTS (
    SELECT 1
    FROM commune_zone_3 c
    WHERE p.lieudeces = c.com
);
```

- `C7.` Les données sur les mairies sont nécessaires sur tous les sites. Le site de **Marseille** sera en charge des mises à jour des informations susceptibles d’être actualisées (téléphone, email...)

Voir cette contrainte dans la prochaine partie.

## C - Mise en place de la réplication logique des mairies

### Question

**1/**  
*Importer la table mairie sur ggmd3*

- Faire la foreign table
```postgresql
CREATE FOREIGN TABLE mairie(
    codeinsee character varying(10),
    cp character varying(10),
    nomorga character varying(254),
    nomcom character varying(80),
    email character varying(150),
    tel character varying(150),
    url character varying(254),
    adresse character varying(254),
    latitude character varying(150),
    longitude character varying(150),
    datemaj character varying(254)
) SERVER remote_insee_deces;
```
- Créer la table sur la base de données locale
```postgresql
CREATE TABLE all_mairie AS
SELECT *
FROM mairie;
```

---

### Configurer la base de données pour qu'elle utilise la réplication Logique

1. Se connecter en tant que `postgres`
```bash
sudo -i -u postgres
psql -d remote_insee_deces
```

2. Définir le WAL level sur logique :
```sql
ALTER SYSTEM SET wal_level = logical;
```

3. Quitter l'instance puis déconnecter vous de l'utilisateur `postgres` et redémarrer le deamon `postgresql`
```bash
sudo systemctl restart postgresql
```
3. 1 Reconnectez-vous en tant que `etum2`
```bash
psql -h localhost -p 5432 -U etum2 -d remote_insee_deces
```
3. 2 Checkez que vous avez le niveau `logical` pour WAL
```sql
SHOW wal_level;
```

---

### Question

**2/**  
*Créer une 'publication' des mairies locales*

- Se reconnecter en tant que `etum2`
```bash
psql -h localhost -p 5432 -U etum2 -d remote_insee_deces
```
- Définir la base en tant que Publisher
```postgresql
-- ggmd3 / Marseille

CREATE PUBLICATION marseille_mairie_publisher
FOR TABLE ONLY all_mairie;
```

**3/**  
*Créer les subscriptions nécessaires sur ggmd1 et ggmd2 pour disposer d'une réplication logique des mairies sur ces sites.*

- Se connecter en `postgres`
- Sur `ggmd3`, créer un utilisateur avec le rôle Replication. (user=`replication_user` - password=`replication`)
- Lancer ces commandes
```postgresql
-- ggmd1 / Paris

CREATE SUBSCRIPTION paris_mairie_subscriber
CONNECTION 'host=192.168.246.227 dbname=postgres port=5432 user=replication_user password=replication'
PUBLICATION marseille_mairie_publisher;
```
```postgresql
-- ggmd2 / Lyon

CREATE SUBSCRIPTION lyon_mairie_subscriber
CONNECTION 'host=192.168.246.227 dbname=postgres port=5432 user=replication_user password=replication'
PUBLICATION marseille_mairie_publisher;
```

**4/**  
*Depuis ggmd3 modifier la date de mise à jour associée à la mairie de Villeurbanne et tester si la synchronisation s'est bien opérée sur tous les sites.*

- Récupérer la date de mise à jour de la mairie de Villeurbanne
```postgresql
-- ggmd3 / Marseille

SELECT datemaj
FROM all_mairie 
WHERE codeInsee = '69266';
```
- Mettre à jour la date de mise à jour
```postgresql
-- ggmd3 / Marseille

UPDATE all_mairie
SET datemaj = CURRENT_DATE
WHERE codeInsee = '69266';
```
- Vérifier sur _ggmd1_ et _ggmd2_ que la date est mise à jour

## D - Interrogation des fragments

### Mise en place pour l'interrogation
- Se connecter en postgres sur *ggmd1*, **tout est à faire sur `ggmd1 / Paris`**
```
sudo -i -u postgres

psql
```
- Créer les serveurs pour les différentes bases de données
```postgresql
CREATE SERVER small_server
FOREIGN DATA WRAPPER postgres_fdw
OPTIONS (
    host '192.168.246.150',
    dbname 'postgres',
    port'5432'
);
```
```postgresql
CREATE SERVER medium_server
FOREIGN DATA WRAPPER postgres_fdw
OPTIONS (
    host '192.168.246.146', 
    dbname 'postgres', 
    port'5432'
);
```
```postgresql
CREATE SERVER large_server
FOREIGN DATA WRAPPER postgres_fdw
OPTIONS (
    host '192.168.246.227',
    dbname 'postgres',
    port'5432'
);
```
- Créer le mapping entre `postgres` de la base de données de Paris vers celles de Lyon et Marseille. Cela permettra de créer des foreign tables qui nous aideront à requêter les données stockées dans les fragments.
```postgresql
CREATE USER MAPPING FOR postgres
SERVER small_server
OPTIONS (password_required 'false');
```
```postgresql
CREATE USER MAPPING FOR postgres
SERVER medium_server
OPTIONS (password_required 'false');
```
```postgresql
CREATE USER MAPPING FOR postgres
SERVER large_server
OPTIONS (password_required 'false'); 
```
```postgresql
-- Donne les privilèges à postgres sur ces serveurs

GRANT ALL PRIVILEGES ON FOREIGN SERVER small_server TO postgres;
GRANT ALL PRIVILEGES ON FOREIGN SERVER medium_server TO postgres;
GRANT ALL PRIVILEGES ON FOREIGN SERVER large_server TO postgres;
```
- Importer les différentes foreign tables
```postgresql
CREATE SCHEMA small_schema;
IMPORT FOREIGN SCHEMA public
FROM SERVER small_server INTO small_schema;
```
```postgresql
CREATE SCHEMA medium_schema;
IMPORT FOREIGN SCHEMA public
FROM SERVER medium_server INTO medium_schema;
```
```postgresql
CREATE SCHEMA large_schema;
IMPORT FOREIGN SCHEMA public
FROM SERVER large_server INTO large_schema; 
```

### Question

*Réécrire les requêtes Q1, Q2 et Q3 à partir des fragments dont vous disposez et de manière à exécuter les requêtes depuis ggmd1.*

- `Q1.` Donner les personnes (idp, nom, prenom) nées dans la région ’Auvergne-RhôneAlpes’ et décédées dans la région ’Pays de la Loire’

```postgresql
SELECT p.idp, p.nom, p.prenoms
FROM large_schema.personne_naissance_zone_3 p
JOIN large_schema.commune_zone_3 c ON p.lieunaiss = c.com
JOIN large_schema.departement_zone_3 d ON c.dep = d.dep
JOIN large_schema.marseille r ON d.reg = r.reg
JOIN medium_schema.personne_deces_zone_2 pd ON p.idp = pd.idp
JOIN medium_schema.commune_zone_2 cd ON pd.lieudeces = cd.com
JOIN medium_schema.departement_zone_2 dd ON cd.dep = dd.dep
JOIN medium_schema.lyon rd ON dd.reg = rd.reg
WHERE r.nom = 'Auvergne-Rhône-Alpes'
AND rd.nom = 'Pays de la Loire';
```

Résultat : 8449

## E - Création des vues globales

### Questions

**1/**  
*À partir des expressions algébriques de l'exercice 3, définir les vues globales permettant d'accéder à l'ensemble des données réparties sur vos 3 VM et ce, selon le schéma initial.*

- **Table `region`**

```postgresql
-- ggmd1 / Paris

CREATE VIEW global_region AS
SELECT * FROM paris
UNION ALL
SELECT * FROM medium_schema.lyon
UNION ALL
SELECT * FROM large_schema.marseille;

-- ggmd2 / Lyon

CREATE VIEW global_region AS
SELECT * FROM lyon
UNION ALL
SELECT * FROM small_schema.paris
UNION ALL
SELECT * FROM large_schema.marseille;

-- ggmd3 / Marseille

CREATE VIEW global_region AS
SELECT * FROM marseille
UNION ALL
SELECT * FROM small_schema.paris
UNION ALL
SELECT * FROM medium_schema.lyon;
```

- **Table `departement`**

```postgresql
-- ggmd1 / Paris

CREATE VIEW global_departement AS
SELECT * FROM departement_zone_1
UNION ALL
SELECT * FROM medium_schema.departement_zone_2
UNION ALL
SELECT * FROM large_schema.departement_zone_3;

-- ggmd2 / Lyon

CREATE VIEW global_departement AS
SELECT * FROM departement_zone_2
UNION ALL
SELECT * FROM small_schema.departement_zone_1
UNION ALL
SELECT * FROM large_schema.departement_zone_3;

-- ggmd3 / Marseille

CREATE VIEW global_departement AS
SELECT * FROM departement_zone_3
UNION ALL
SELECT * FROM medium_schema.departement_zone_2
UNION ALL
SELECT * FROM small_schema.departement_zone_1;
```

- **Table `commune`**

```postgresql
-- ggmd1 / Paris

CREATE VIEW global_commune AS
SELECT * FROM commune_zone_1
UNION ALL
SELECT * FROM medium_schema.commune_zone_2
UNION ALL
SELECT * FROM large_schema.commune_zone_3;

-- ggmd2 / Lyon

CREATE VIEW global_commune AS
SELECT * FROM commune_zone_2
UNION ALL
SELECT * FROM small_schema.commune_zone_1
UNION ALL
SELECT * FROM large_schema.commune_zone_3;

-- ggmd3 / Marseille

CREATE VIEW global_commune AS
SELECT * FROM commune_zone_3
UNION ALL
SELECT * FROM medium_schema.commune_zone_2
UNION ALL
SELECT * FROM small_schema.commune_zone_1;
```

- **Table `mairie`**

```postgresql
CREATE VIEW global_mairie AS
SELECT * FROM all_mairie;
```

- **Table `personne`**

```postgresql
-- ggmd1 / Paris

CREATE VIEW global_personne_naissance AS
SELECT * FROM personne_naissance_zone_1
UNION ALL
SELECT * FROM medium_schema.personne_naissance_zone_2
UNION ALL
SELECT * FROM large_schema.personne_naissance_zone_3;

CREATE VIEW global_personne_deces AS
SELECT * FROM personne_deces_zone_1
UNION ALL
SELECT * FROM medium_schema.personne_deces_zone_2
UNION ALL
SELECT * FROM large_schema.personne_deces_zone_3;

CREATE VIEW global_personne AS
SELECT n.idp, n.nom, n.prenoms, n.datenaiss, n.lieunaiss, d.datedeces, d.lieudeces, a.age
FROM global_personne_naissance n
JOIN global_personne_deces d ON n.idp = d.idp
JOIN age_personne a ON n.idp = a.idp;
```
```postgresql
-- ggmd2 / Lyon

CREATE VIEW global_personne_naissance AS
SELECT * FROM personne_naissance_zone_2
UNION ALL
SELECT * FROM small_schema.personne_naissance_zone_1
UNION ALL
SELECT * FROM large_schema.personne_naissance_zone_3;

CREATE VIEW global_personne_deces AS
SELECT * FROM personne_deces_zone_2
UNION ALL
SELECT * FROM small_schema.personne_deces_zone_1
UNION ALL
SELECT * FROM large_schema.personne_deces_zone_3;

CREATE VIEW global_personne AS
SELECT n.idp, n.nom, n.prenoms, n.datenaiss, n.lieunaiss, d.datedeces, d.lieudeces, a.age
FROM global_personne_naissance n
JOIN global_personne_deces d ON n.idp = d.idp
JOIN small_schema.age_personne a ON n.idp = a.idp;
```
```postgresql
-- ggmd3 / Marseille

CREATE VIEW global_personne_naissance AS
SELECT * FROM personne_naissance_zone_3
UNION ALL
SELECT * FROM small_schema.personne_naissance_zone_1
UNION ALL
SELECT * FROM medium_schema.personne_naissance_zone_2;

CREATE VIEW global_personne_deces AS
SELECT * FROM personne_deces_zone_3
UNION ALL
SELECT * FROM small_schema.personne_deces_zone_1
UNION ALL
SELECT * FROM medium_schema.personne_deces_zone_2;

CREATE VIEW global_personne AS
SELECT n.idp, n.nom, n.prenoms, n.datenaiss, n.lieunaiss, d.datedeces, d.lieudeces, a.age
FROM global_personne_naissance n
JOIN global_personne_deces d ON n.idp = d.idp
JOIN small_schema.age_personne a ON n.idp = a.idp;
```

**2/**  
*Tester Q1, Q2 et Q3.*

```postgresql
SELECT p.idp, p.nom, p.prenoms
FROM global_personne p
JOIN global_commune naissance ON p.lieunaiss = naissance.com
JOIN global_departement dep_naissance ON naissance.dep = dep_naissance.dep
JOIN global_region reg_naissance ON dep_naissance.reg = reg_naissance.reg
JOIN global_commune deces ON p.lieudeces = deces.com
JOIN global_departement dep_deces ON deces.dep = dep_deces.dep
JOIN global_region reg_deces ON dep_deces.reg = reg_deces.reg
WHERE reg_naissance.nom = 'Auvergne-Rhône-Alpes'
AND reg_deces.nom = 'Pays de la Loire';
```

Résultat : 8449