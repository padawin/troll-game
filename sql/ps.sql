--fonction de recherche d'un troll � partir de son id qui retourne son nom.
CREATE OR REPLACE FUNCTION recherche_troll(integer) RETURNS varchar(20) AS $$
DECLARE num ALIAS FOR $1;
DECLARE res varchar(20);

BEGIN
	SELECT nomTroll into res FROM troll where idTroll=num;
	RETURN (res);
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

--fonction qui insere un troll dans la BD avec insertion pr�alable des coordonn�es dans la BD si elles n'existaient pas d�j�.
CREATE OR REPLACE FUNCTION init_troll(integer,varchar(20),integer,integer,integer,integer,integer,integer) RETURNS void AS $$
DECLARE num ALIAS FOR $1;
DECLARE nom ALIAS FOR $2;
DECLARE att ALIAS FOR $3;
DECLARE deg ALIAS FOR $4;
DECLARE esq ALIAS FOR $5;
DECLARE v ALIAS FOR $6;
DECLARE pX ALIAS FOR $7;
DECLARE pY ALIAS FOR $8;

BEGIN
	if ((select count (*) from coord where x=pX and y=pY)=0) then
		INSERT INTO coord (x,y) values (pX,pY);
	end if;
	INSERT INTO troll (idTroll,nomTroll,attaque,degats,esquive,vie,paRestants,positionX,positionY) VALUES (num,nom,att,deg,esq,v,6,pX,pY);
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

--insertion d'un objet dans la BD avec insertion de ses coord si elles n'existaient d�j� pas.
CREATE OR REPLACE FUNCTION init_objet(varchar(20) , varchar(20) , integer , integer , varchar(20) , integer) RETURNS void AS $$

DECLARE obj_idObjet ALIAS FOR $1;
DECLARE obj_typeObjet ALIAS FOR $2;
DECLARE obj_positionX ALIAS FOR $3;
DECLARE obj_positionY ALIAS FOR $4;
DECLARE obj_poids float;

DECLARE bonus_carac ALIAS FOR $5;
DECLARE bonus_val ALIAS FOR $6;

BEGIN
	if ((select count (*) from objet where idObjet=obj_idObjet)=0) then
		if ((select count (*) from coord where x=obj_positionX and y=obj_positionY)=0) then
			INSERT INTO coord (x,y) values (obj_positionX,obj_positionY);
		end if;
			if (obj_typeObjet='arme') then
				obj_poids:=10.2;
			elsif (obj_typeObjet='armure') then
				obj_poids:=25.5;
			else
				obj_poids:=5;
			end if;
		INSERT INTO objet (idObjet,typeObjet,positionX,positionY,poids) VALUES (obj_idObjet,obj_typeObjet,obj_positionX,obj_positionY,obj_poids);
	end if;

	if ((select count (*) from bonus where caracteristique=bonus_carac and valeur=bonus_val)=0) then
		INSERT INTO bonus (caracteristique,valeur) VALUES (bonus_carac,bonus_val);
	end if;

	if ((select count (*) from carac where idObjet=obj_idObjet and caracteristique=bonus_carac and valeur=bonus_val)=0) then
		INSERT INTO carac (idObjet,caracteristique,valeur) VALUES (obj_idObjet,bonus_carac,bonus_val);
	end if;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

--fonction qui vide entierement toutes les tables de la BD.
CREATE OR REPLACE FUNCTION clear() RETURNS void AS $$

BEGIN
	delete from carac;
	delete from bonus;
	delete from troll;
	delete from equipement;
	delete from objet;
	delete from coord;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

--fonction qui dépace un troll en changeant ses coord
CREATE OR REPLACE FUNCTION deplacer_troll(integer,integer,integer) RETURNS varchar(200) AS $$

DECLARE id ALIAS FOR $1;
DECLARE nouvCoordX ALIAS FOR $2;
DECLARE nouvCoordY ALIAS FOR $3;
DECLARE nbObjets integer;
DECLARE nbTrolls integer;
DECLARE ancCoordX integer;
DECLARE ancCoordY integer;
DECLARE pa integer;
DECLARE mess varchar(200);
DECLARE ok boolean;

BEGIN
	mess = null;
	ok := true;
	SELECT into pa paRestants FROM troll where idTroll=id;

	select into ancCoordX positionX  from troll where idTroll=id;
	select into ancCoordY positionY from troll where idTroll=id;
	SELECT into nbTrolls count (*) FROM troll where positionX=ancCoordX and positionY=ancCoordY;

	if (pa=0) then
		mess := 'Déplacement impossible : vous n''avez plus de PA';
		ok := false;
	elsif (pa<4 and nbTrolls=2) then
		mess := 'Déplacement impossible : vous n''avez plus assez de PA pour vous enfuir !';
		ok := false;
	elsif (pa>=4 and nbTrolls=2) then
		UPDATE troll SET paRestants=paRestants-4 where idTroll=id;
	elsif (pa>0 and nbTrolls=1) then
		UPDATE troll SET paRestants=paRestants-1 where idTroll=id;
	else
		mess := 'Erreur inconnue';
		ok := false;
	end if;

	if (ok = true) then
		if ((select count (*) from coord where x=nouvCoordX and y=nouvCoordY)=0) then
			insert into coord values (nouvCoordX,nouvCoordY);
		end if;
		UPDATE troll SET positionX=nouvCoordX, positionY=nouvCoordY where idTroll=id;

		select into nbObjets count (*) from objet where positionX=ancCoordX and positionY=ancCoordY;
		select into nbTrolls count (*) from troll where positionX=ancCoordX and positionY=ancCoordY;
		if (nbObjets = 0 and nbTrolls = 0) then
			DELETE FROM coord where x=ancCoordX and y=ancCoordY;
		end if;
	end if;
	return (mess);
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

--fonction qui renvoie le nombre de PA restant de l'id d'un troll
CREATE OR REPLACE FUNCTION pa_restants(integer) RETURNS integer AS $$

DECLARE id ALIAS FOR $1;
DECLARE pa integer;

BEGIN
	SELECT into pa paRestants FROM troll where idTroll=id;
	return (pa);
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

--fonction qui retourne vraie si la case de départ était vide (aucun autre troll)
CREATE OR REPLACE FUNCTION case_vide(integer,integer) RETURNS boolean AS $$

DECLARE x ALIAS FOR $1;
DECLARE y ALIAS FOR $2;

BEGIN
	if ((SELECT count (*) FROM troll where positionX=x and positionY=y)=1) then
		return true;
	else
		return false;
	end if;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

--fonction permet d'attaquer le troll adverse
CREATE OR REPLACE FUNCTION decrementer_vie(integer,integer) RETURNS boolean AS $$

DECLARE id ALIAS FOR $1;
DECLARE deg ALIAS FOR $2;
DECLARE vieRestante integer;

BEGIN
	SELECT into vieRestante vie from troll where idTroll=id;
	if (deg<=vieRestante) then
		vieRestante := vieRestante-deg;
	else
		vieRestante := 0;
	end if;
	UPDATE troll SET vie=vieRestante where idTroll=id;
	if (vieRestante = 0) then
		return true;
	else
		return false;
	end if;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION nb_des_att(integer) RETURNS integer AS $$

DECLARE id ALIAS FOR $1;
DECLARE des integer;

BEGIN
	SELECT into des attaque FROM troll where idTroll=id;
	return (des);
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION nb_des_esq(integer) RETURNS integer AS $$

DECLARE id ALIAS FOR $1;
DECLARE des integer;

BEGIN
	SELECT into des esquive FROM troll where idTroll=id;
	return (des);
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION nb_des_deg(integer) RETURNS integer AS $$

DECLARE id ALIAS FOR $1;
DECLARE des integer;

BEGIN
	SELECT into des degats FROM troll where idTroll=id;
	return (des);
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION init_pa(integer,integer) RETURNS void AS $$

DECLARE id ALIAS FOR $1;
DECLARE nbPa ALIAS FOR $2;

BEGIN
	if (nbPa >= 0) then
		UPDATE troll SET paRestants=nbPa where idTroll=id;
	end if;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION rammasser(integer,varchar(20)) RETURNS void AS $$

DECLARE idTroll ALIAS FOR $1;
DECLARE idObjet ALIAS FOR $2;

BEGIN
	Insert into equipement values (idObjet,idTroll,false);
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION equiper_objet (integer,varchar(20)) RETURNS void AS $$

DECLARE idTroll ALIAS FOR $1;
DECLARE idObjet ALIAS FOR $2;
DECLARE c varchar(20);
DECLARE v integer;
DECLARE val integer;

BEGIN
	update equipement SET estEquipe = true where idObjet = idObjet and idTroll=idTroll;
	select  caracteristique into c from carac where idObjet=idObjet;
	select  valeur into v from carac where idObjet=idObjet;
	if (c='attaque') then
		select attaque into val from troll where idTroll=idTroll;
		update troll SET attaque = v+val where idTroll=idTroll;
	elsif (c='degats') then
		select degats into val from troll where idTroll=idTroll;
		update troll SET degats = v+val where idTroll=idTroll;
	elsif (c='esquive') then
		select esquive into val from troll where idTroll=idTroll;
		update troll SET esquive = v+val where idTroll=idTroll;
	elsif (c='vie') then
		select vie into val from troll where idTroll=idTroll;
		update troll SET vie = v+val where idTroll=idTroll;
	end if;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION utiliser_potion (integer,varchar(20)) RETURNS void AS $$

DECLARE idTroll ALIAS FOR $1;
DECLARE idObjet ALIAS FOR $2;
DECLARE c varchar(20);
DECLARE v integer;

BEGIN

END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION get_valeur_bonus_objet (varchar(20)) RETURNS integer AS $$

DECLARE idObjet ALIAS FOR $1;
DECLARE v integer;

BEGIN
	Select valeur into v from carac where idObjet = idObjet;
	return v;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION get_type_bonus_objet (varchar(20)) RETURNS varchar(20) AS $$

DECLARE idObjet ALIAS FOR $1;
DECLARE c varchar(20);

BEGIN
	Select caracteristique into c from carac where idObjet = idObjet;
	return c;
END;
$$ LANGUAGE plpgsql;
