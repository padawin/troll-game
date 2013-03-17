--fonction de recherche d'un troll a partir de son id qui retourne son nom.
DROP FUNCTION recherche_troll(integer);

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

--fonction qui insere un troll dans la BD avec insertion pr?alable des coordonn?es dans la BD si elles n'existaient pas d?j?.
DROP FUNCTION init_troll(integer,varchar(20),integer,integer,integer,integer,integer,integer);

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

--insertion d'un objet dans la BD avec insertion de ses coord si elles n'existaient d?j? pas.
DROP FUNCTION init_objet(varchar(20) , varchar(20) , integer , integer , varchar(20) , integer);

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

--fonction qui vide entierement toutes les tables de la BD.
DROP FUNCTION clear();

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

--fonction qui d�pace un troll en changeant ses coord
DROP FUNCTION deplacer_troll(integer,integer,integer);

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

--fonction qui renvoie le nombre de PA restant de l'id d'un troll
DROP FUNCTION pa_restants(integer);

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

--fonction qui retourne vraie si la case de d�part �tait vide (aucun autre troll)
DROP FUNCTION case_vide(integer,integer);

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

--fonction permet d'attaquer le troll adverse
DROP FUNCTION decrementer_vie(integer,integer);

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

DROP FUNCTION nb_des_att(integer);

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

DROP FUNCTION nb_des_esq(integer);

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

DROP FUNCTION nb_des_deg(integer);

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

DROP FUNCTION init_pa(integer,integer);
