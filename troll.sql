CREATE TABLE coord (
	x				integer,
    y				integer,
	PRIMARY KEY (x,y)
);

CREATE TABLE bonus (
	caracteristique	varchar(20),
    valeur			integer,
	PRIMARY KEY (caracteristique,valeur)
);

CREATE TABLE objet (
	idObjet			varchar(20),
    typeObjet		varchar(20),
	positionX		integer,
	positionY		integer,
	poids			float,
	PRIMARY KEY (idObjet),
	foreign key (positionX,positionY) references coord (X,Y)
);

CREATE TABLE carac (
	idObjet			varchar(20),
	caracteristique	varchar(20),
    valeur			integer,
	foreign KEY (idObjet) references objet (idObjet),
	foreign KEY (caracteristique,valeur) references bonus (caracteristique,valeur)
);

CREATE TABLE troll (
	idTroll			integer,
	nomTroll		varchar(20),
    attaque			integer,
    degats			integer,
	esquive			integer,
	vie				integer,
	paRestants		integer,
	positionX		integer,
	positionY		integer,
	PRIMARY KEY (idTroll),
	foreign key (positionX,positionY) references coord (X,Y)
);

CREATE TABLE equipement (
	idObjet			varchar(20),
	idTroll			varchar(20),
	estEquipe		boolean,
	foreign key (idObjet) references objet (idObjet)
);