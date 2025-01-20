# GGMD - TP2 : Performances et tuning pour le traitement de Masses de Données

### Sujet du TP
[Lien vers sujet TP2 - Performances](https://forge.univ-lyon1.fr/ggmd/ggmd-tps-etudiants/-/blob/main/GGMD-TP2_Performances.md)

### Liens utiles
- [Sujet CM1 Partie BDR](https://perso.univ-lyon1.fr/nicolas.lumineau/ue/ggmd/cm/GGMD-CM-PartieBDR.pdf)

## Informations VM

- Fichier `ggmd-etus.key` sur Tomuss à mettre dans le répertoire home.
- Mettre les droits 600 avec *chmod*.

| IP ggmd1           | IP ggmd2           | IP ggmd3          |
|--------------------|--------------------|-------------------|
| `192.168.246.150`  | `192.168.246.146`  | `192.168.246.227` |

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

### Se connecter à la BDD insee_deces
```
psql -h 192.168.246.247 -U etum2 insee_deces
```

## A - Les requêtes SQL

### A1 - Focus sur les anomalies

- **Requête Qa**
```postgresql
SELECT nomprenom, datenaiss, lieunaiss, datedeces, lieudeces, count(*) as nb 
FROM personne_insee 
GROUP BY nomprenom, datenaiss, lieunaiss, datedeces, lieudeces 
ORDER BY nb DESC LIMIT 10;
```

**1/**  
*Sans exécuter Qa, expliquer en français ce que retourne la requête Qa.*

Retourne toutes les personnes sans doublon.

**2/**  
*Définir Qb à partir de Qa pour obtenir le plan d'exécution de celle-ci. Exécuter Q0b, puis analyser le résultat retourné.*

- **Requête Qb**
```postgresql
EXPLAIN (analyse, verbose, costs, buffers, timing, summary)
SELECT nomprenom, datenaiss, lieunaiss, datedeces, lieudeces, count(*) as nb
FROM personne_insee
GROUP BY nomprenom, datenaiss, lieunaiss, datedeces, lieudeces
ORDER BY nb DESC LIMIT 10;
```

Ordre d'exécution des opérations :
1. Limit
2. Sort
3. Finalize GroupAggregate
4. Gather Merge
5. Partial GroupAggregate
6. Sort => *prend le plus de temps*
7. Parallel Seq Scan

**3/**  
*Définir le prédicat is_date qui prend en argument une chaîne de caractères et qui retourne true si l'argument correspond à une date valide, et false sinon.*

- Version basique
```postgresql
CREATE OR REPLACE FUNCTION is_date(date_string TEXT) RETURNS BOOLEAN AS $$
DECLARE
    year_part INTEGER;
    month_part INTEGER;
    day_part INTEGER;
BEGIN
    -- Vérifie si la chaîne de date est non nulle
    IF date_string IS NULL OR date_string = '' OR LENGTH(date_string) != 8 THEN
        RETURN false;
    END IF;

    -- Extrait les parties de l'année, du mois et du jour de la chaîne de date
    year_part := CAST(SUBSTRING(date_string FROM 1 FOR 4) AS INTEGER);
    month_part := CAST(SUBSTRING(date_string FROM 5 FOR 2) AS INTEGER);
    day_part := CAST(SUBSTRING(date_string FROM 7 FOR 2) AS INTEGER);

    -- Vérifie que l'année, le mois et le jour sont valides
    IF year_part < 1 OR year_part > 9999 THEN
        RETURN FALSE;
    END IF;

    IF month_part < 1 OR month_part > 12 THEN
        RETURN FALSE;
    END IF;

    IF day_part < 1 OR day_part > 31 THEN
        RETURN FALSE;
    END IF;

    -- Essaye de convertir la chaîne en une date au format 'YYYYMMDD'
    BEGIN
        -- Utilisez la fonction to_date pour essayer de convertir la chaîne en date
        PERFORM to_date(date_string, 'YYYYMMDD');
        -- Si la conversion réussit, retournez true
        RETURN TRUE;
    EXCEPTION
        WHEN others THEN
            -- En cas d'erreur de conversion, retournez false
            RETURN FALSE;
    END;
END;
$$ LANGUAGE plpgsql;
```

- Version immutable
```postgresql
CREATE OR REPLACE FUNCTION is_date_immutable(date_string TEXT) RETURNS BOOLEAN AS $$
DECLARE
    year_part INTEGER;
    month_part INTEGER;
    day_part INTEGER;
BEGIN
    -- Vérifie si la chaîne de date est non nulle
    IF date_string IS NULL OR date_string = '' OR LENGTH(date_string) != 8 THEN
        RETURN false;
    END IF;

    -- Extrait les parties de l'année, du mois et du jour de la chaîne de date
    year_part := CAST(SUBSTRING(date_string FROM 1 FOR 4) AS INTEGER);
    month_part := CAST(SUBSTRING(date_string FROM 5 FOR 2) AS INTEGER);
    day_part := CAST(SUBSTRING(date_string FROM 7 FOR 2) AS INTEGER);

    -- Vérifie que l'année, le mois et le jour sont valides
    IF year_part < 1 OR year_part > 9999 THEN
        RETURN FALSE;
    END IF;

    IF month_part < 1 OR month_part > 12 THEN
        RETURN FALSE;
    END IF;

    IF day_part < 1 OR day_part > 31 THEN
        RETURN FALSE;
    END IF;

    -- Essaye de convertir la chaîne en une date au format 'YYYYMMDD'
    BEGIN
        -- Utilisez la fonction to_date pour essayer de convertir la chaîne en date
        PERFORM to_date(date_string, 'YYYYMMDD');
        -- Si la conversion réussit, retournez true
        RETURN TRUE;
    EXCEPTION
        WHEN others THEN
            -- En cas d'erreur de conversion, retournez false
            RETURN FALSE;
    END;
END;
$$ LANGUAGE plpgsql IMMUTABLE;
```

**4/**  
*Définir la requête Qc qui retourne le triplet (nbT, nbDNE, ratio) où :*  
– *nbT correspond au nombre total de tuples de la table personne_insee,*  
– *nbDNE correspond au nombre de tuples avec une Date de Naissance Erronée*  
– *ratio correspond à la proportion de tuples ayant une date de naissance erronée.*


- **Requête Qc**
```postgresql
-- ggmd1 / Paris

SELECT
    COUNT(*) AS nbT,
    SUM(CASE WHEN is_date(datenaiss) = false THEN 1 ELSE 0 END) AS nbDNE,
    (SUM(CASE WHEN is_date(datenaiss) = false THEN 1 ELSE 0 END)::float / COUNT(*)) AS ratio
FROM personne_insee_small;
```

- Résultat obtenu

| nbt      | nbdne | ratio                 |
|----------|-------|-----------------------|
| 24916669 | 89199 | 0.0035798926413478462 |


**5/**  
*Définir la requête Qd qui retourne le nombre de personnes sans nom de famille.*

- **Requête Qd**
```postgresql
-- ggmd1 / Paris

SELECT COUNT(*) AS nombre_personnes_sans_nom
FROM personne_insee_small
WHERE split_part(nomprenom, '*', 1) = '';
```

- Résultat obtenu : 95

### A2 - Autres requêtes SQL

**Q1/**  
*Pour chaque département de décès, donner le nombre de personnes ayant au moins un doublon de déclaration de décès.*

```postgresql
-- ggmd3 / Marseille

SELECT CASE WHEN lieudeces LIKE '97%' THEN SUBSTRING(lieudeces, 0, 4) ELSE SUBSTRING(lieudeces, 0, 3) END as numdep, COUNT(*) AS nbD FROM
    (
        SELECT nomprenom, datenaiss, lieunaiss, datedeces, lieudeces, COUNT(*) as nbD
        FROM personne_insee_large P
        GROUP BY nomprenom, datenaiss, lieunaiss, datedeces, lieudeces
        HAVING COUNT(*) > 1
    ) t
GROUP BY numdep
ORDER BY COUNT(*) DESC;
```

**Q2/**  
*Pour chaque décennie, donnez le top 10 des noms de famille les plus portés*

```postgresql
-- ggm3 / Marseille

WITH SS1 AS (
  SELECT
    generate_series('1970-01-01'::date, CURRENT_DATE, '1 decade'::interval) AS decades_series
),
SS2 AS (
  SELECT
    split_part(nomprenom, '*', 1) AS nom,
    EXTRACT(YEAR FROM TO_DATE(datenaiss, 'YYYYMMDD'))::integer AS annee_naissance,
    EXTRACT(YEAR FROM TO_DATE(datedeces, 'YYYYMMDD'))::integer AS annee_deces
  FROM
    personne_insee_large
  WHERE
    is_date_immutable(datenaiss) AND is_date_immutable(datedeces) 
),
RankedNames AS (
  SELECT
    EXTRACT(YEAR FROM SS1.decades_series) AS decades,
    SS2.nom,
    COUNT(*) AS occurrences,
    ROW_NUMBER() OVER (PARTITION BY EXTRACT(YEAR FROM SS1.decades_series) ORDER BY COUNT(*) DESC) AS rank
  FROM SS1
  JOIN SS2 ON
    EXTRACT(YEAR FROM SS1.decades_series) BETWEEN SS2.annee_naissance AND SS2.annee_deces
  GROUP BY SS1.decades_series, SS2.nom
)
SELECT
  decades,
  STRING_AGG(rank || '. ' || nom || ' (' || occurrences || ')', ' ; ') AS classement
FROM RankedNames
WHERE rank <= 10
GROUP BY decades
ORDER BY decades;
```

**Q3/**  
*Quel est la durée de vie moyenne des personnes selon leur département de naissance ?*

```postgresql
-- ggm3 / Marseille

WITH Personnes AS (
    SELECT
        nomprenom,
        CASE
            WHEN SUBSTRING(lieunaiss FROM 1 FOR 2) = '97' THEN SUBSTRING(lieunaiss FROM 1 FOR 3)
            ELSE SUBSTRING(lieunaiss FROM 1 FOR 2)
            END AS dep_naissance,
        CASE
            WHEN SUBSTRING(lieudeces FROM 1 FOR 2) = '97' THEN SUBSTRING(lieudeces FROM 1 FOR 3)
            ELSE SUBSTRING(lieudeces FROM 1 FOR 2)
            END AS dep_deces,
        (TO_DATE(datedeces, 'YYYYMMDD') - TO_DATE(datenaiss, 'YYYYMMDD')) AS duree_vie
    FROM
        personne_insee_large
    WHERE
        is_date_immutable(datenaiss) AND is_date_immutable(datedeces)
)
SELECT
    d.dep,
    d.nom,
    COUNT(*) AS nombre_personnes_deces,
    justify_interval( (AVG(duree_vie) || ' days')::interval ) AS esperance_vie_moyenne
FROM Personnes p
         JOIN departement d ON p.dep_naissance = d.dep
GROUP BY d.dep, d.nom
ORDER BY esperance_vie_moyenne DESC, d.dep;
```

## B - Traitement des requêtes sur grp-XX-small

### Questions

**1/**  
*Identifier les requêtes qui peuvent être traitées sur cette VM. Editer le plan d'exécution desdites requêtes.*

- Quelles requêtes fonctionnent sur quelles VMs ?

|       | Q1  | Q2  | Q3  |
|-------|-----|-----|-----|
| ggmd1 | X   | X   | OK  |
| ggmd2 | OK  | OK  | OK  |
| ggmd3 | OK  | OK  | OK  |

- Est-ce que les plans d'exécution ont pu être faites sur *ggmd1* ?

|       | Q1  | Q2  |
|-------|-----|-----|
| ggmd1 | X   | X   |

**2/**  
*Pour les plans d'exécution qui n'ont pas pu aboutir, expliquer en quelques lignes votre compréhension du problème rencontré.*

**3/**  
*Proposer un protocole de résolution pour chaque problème.
Appliquer vos protocoles. Quels impacts sur le traitement des requêtes ?*

**4/**  
*Editer un graphique G1, de type histogramme empilé, avec en abscisse les requêtes et en ordonnée les temps d'optimisation et temps d'exécution.*

Voir Google Spreadsheets.

**5/**  
*Editer un graphique G2, de type histogramme, avec en abscisse les requêtes et en ordonnée le coût de la requête (Pour rappel, le coût se base sur les accès page et les accès tuples)*

Voir Google Spreadsheets.

**6/**  
*Ajouter les index qui vous semblent pertinents.*

```postgresql

```

**7/**  
*Réévaluer les requêtes avec les index. Editer les plan d'exécution. Compléter les graphiques G1 et G2.*

Voir Google Spreadsheets.

## C - Traitement des requêtes sur grp-XX-medium

Fait par Kévin.

## D - Traitement des requêtes sur grp-XX-large

### Questions

**1/**  
*Evaluer les requêtes sans les index. Editer les plan d'exécution. Compléter les graphiques G1 et G2.*

Voir Google Spreadsheets.

**2/**  
*Créer les index.*

```postgresql
CREATE INDEX index_nomprenom ON personne_insee_large (nomprenom);

CREATE INDEX index_datedeces ON personne_insee_large (datedeces);

CREATE INDEX index_datenaiss ON personne_insee_large (datenaiss);

CREATE INDEX index_q2 ON personne_insee_medium(datenaiss, datedeces) WHERE is_date_immutable(datenaiss) AND is_date_immutable(datedeces);
```
```postgresql

```

**3/**  
*Evaluer les requêtes avec les index. Editer les plan d'exécution. Compléter les graphiques G1 et G2.*

Voir Google Spreadsheets.