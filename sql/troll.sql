CREATE TABLE game (
	id SERIAL PRIMARY KEY,
	name VARCHAR(50) NOT NULL
);

CREATE TABLE coord (
	id_game INTEGER REFERENCES game(id),
	x				integer,
	y				integer,
	PRIMARY KEY (x,y)
);

CREATE TABLE bonus (
	id SERIAL PRIMARY KEY,
	caracteristique	varchar(20),
	valeur			integer
);

CREATE TABLE objet (
	idObjet			varchar(20),
	id_game INTEGER REFERENCES game(id),
	typeObjet		varchar(20),
	positionX		integer,
	positionY		integer,
	poids			float,
	PRIMARY KEY (idObjet),
	foreign key (positionX,positionY) references coord (X,Y)
);

CREATE TABLE carac (
	idObjet			varchar(20) references objet(idObjet),
	id_bonus	integer REFERENCES bonus(id)
);

CREATE TABLE troll (
	idTroll			integer,
	id_game INTEGER REFERENCES game(id),
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
	id SERIAL PRIMARY KEY,
	idObjet			varchar(20),
	idTroll			varchar(20),
	estEquipe		boolean,
	CONSTRAINT uniq_equipement_idobjet_idtroll UNIQUE (idObjet, idTroll)
);
