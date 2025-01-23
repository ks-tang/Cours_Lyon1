-- On importe les données dans la table s2016

CREATE TABLE s2016 (
	"Programme 2016" DECIMAL NOT NULL,
	"SIREN" VARCHAR NOT NULL,
	"NIC" VARCHAR,
	"Dénomination" VARCHAR NOT NULL,
	"Montant" DECIMAL NOT NULL,
	"Objet" VARCHAR NOT NULL,
	"Parlementaire" VARCHAR,
	"Réserve 2016" VARCHAR,
	"Convention 2016" VARCHAR,
	"COG : code département" VARCHAR,
	"COG : code commune" DECIMAL,
	"COG : ville ou pays" VARCHAR NOT NULL,
	"Nomenclature juridique" DECIMAL NOT NULL,
	"Code NAF" VARCHAR NOT NULL,
	"Situation SIRENE" VARCHAR,
	"RNA" VARCHAR
);


--On recupere les valeur non numerique de la colonne SIREN

SELECT *
FROM s2016 s 
WHERE NOT ( "SIREN"  IS NULL OR "SIREN"  ~ '^[0-9]+$');


-- on chnage la colonne SIREN en numerique en autorisant les NULL

ALTER TABLE s2016
ALTER COLUMN "SIREN" DROP NOT NULL;

-- on recupere les valeur non numerique de la colonne SIREN et on les transfomre en Null

UPDATE s2016
SET "SIREN" = NULL
WHERE "SIREN" !~ '^[0-9]+$';


-- On change le type de la colonne SIREN en numerique

ALTER TABLE s2016
ALTER COLUMN "SIREN" TYPE DECIMAL USING "SIREN"::numeric;

-- Modifiez la colonne "Réserve 2016" en un type booléen
UPDATE s2016
SET "Réserve 2016" = CASE
  WHEN "Réserve 2016" IS NOT NULL THEN true
  ELSE false
END;

ALTER TABLE s2016
ALTER COLUMN "Réserve 2016" TYPE BOOLEAN
  USING "Réserve 2016"::BOOLEAN;

-- Enelver 00 00 de NIC

UPDATE s2016
SET "NIC" = NULL
WHERE "NIC" = '00 00';

ALTER TABLE s2016
ALTER COLUMN "NIC" TYPE DECIMAL USING "NIC"::numeric;



/*******
 * 
 * Schéma
 * 
 * 
 */

-- montant pas la cle

/* Liste DF */
/*
 * (SIRENE,NIC) -> (Dénomination, codep, codeCom, ville/pays, Situation SIRENE, RNA, NomenclatureJ, NAF)
 * (codeDep, codeCom) -> Ville ou pays 
 * (#entrepriseId, #idProg annee, num) -> Montant, parlementaire, objet, Réserve, convention
 * (idProg) -> programme
 * 
 */

-- exemple pour verifier DF 

SELECT "COG : code département", "COG : code commune", COUNT(DISTINCT "COG : ville ou pays") AS unique_ville_pays_count
FROM s2016
GROUP BY "COG : code département", "COG : code commune"
HAVING COUNT(DISTINCT "COG : ville ou pays") > 1;

 
/*******
 * 
 * CREATION DE table 
 * 
*/

create table Lieu(
	"codeDep" VARCHAR,
	"codeCom" DECIMAL,
	"nom" VARCHAR,
	primary key("codeDep","codeCom")	
);

INSERT INTO Lieu ("codeDep", "codeCom", "nom")
SELECT DISTINCT "COG : code département", "COG : code commune", "COG : ville ou pays"
FROM s2016
WHERE "COG : code département" IS NOT NULL;



CREATE TABLE Etablissement (
	"idEtab" serial primary key,
	"SIREN" DECIMAL,
	"NIC" DECIMAL,
	"Dénomination" VARCHAR not null,
	"Situation SIRENE" VARCHAR,
	"RNA" VARCHAR,
	"Nomenclature juridique" DECIMAL NOT NULL,
	"NAF" VARCHAR NOT null,
	"codeDep" VARCHAR,
	"codeCom" DECIMAL,
    FOREIGN KEY ("codeDep","codeCom") REFERENCES Lieu("codeDep","codeCom")
);

INSERT INTO Etablissement ("SIREN", "NIC", "Dénomination", "Situation SIRENE", "RNA", "Nomenclature juridique", "NAF", "codeDep", "codeCom")
SELECT s."SIREN", s."NIC", s."Dénomination", s."Situation SIRENE", s."RNA", s."Nomenclature juridique", s."Code NAF", l."codeDep", l."codeCom"
FROM s2016 s
INNER JOIN Lieu l ON s."COG : code département" = l."codeDep" AND s."COG : code commune" = l."codeCom";

    
create table Subvention (	
	"idSub" serial primary key,
	"idEtab" INTEGER references Etablissement("idEtab"),
	"Programme" DECIMAL,
	"Montant" DECIMAL NOT NULL,
	"Objet" VARCHAR NOT NULL,
	"Parlementaire" VARCHAR,
	"Réserve 2016" BOOLEAN,
	"Convention 2016" VARCHAR
);

INSERT INTO Subvention ("idEtab", "Programme", "Montant", "Objet", "Parlementaire", "Réserve 2016", "Convention 2016")
SELECT e."idEtab", s."Programme 2016", s."Montant", s."Objet", s."Parlementaire", s."Réserve 2016", s."Convention 2016"
FROM s2016 s
JOIN Etablissement e ON s."SIREN" = e."SIREN" AND s."NIC" = e."NIC";

create table Parlementaire(
	"idParl" serial primary key,
	"nom" VARCHAR
);

INSERT INTO Parlementaire ("nom")
SELECT DISTINCT "Parlementaire"
FROM s2016
WHERE "Parlementaire" IS NOT NULL;


create table Subv_Parl(
	"idSub" Integer references Subvention("idSub"),
	"idParl" Integer references Parlementaire("idParl"),
	primary key("idSub","idParl")
);

INSERT INTO Subv_Parl ("idSub", "idParl")
SELECT s."idSub", p."idParl"
FROM Subvention s
JOIN Parlementaire p ON s."Parlementaire" = p."nom";