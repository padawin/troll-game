package projet;

import java.sql.*;
import java.util.Hashtable;

public class Menu {

	conn connection = new conn();
	Connection conn = connection.Connection();
	Statement stmt = connection.Statement();
	CallableStatement proc;
	int tour;

	int tailleEchiquier = 10;
	int nbTrolls = 2;
	troll troll1;
	troll troll2;

	troll[] tabTrolls = new troll[3];

	Damier dam = new Damier(troll1, troll2, tailleEchiquier);

	Hashtable<String, objet> tabObjets = new Hashtable<String, objet>();
	Hashtable<String, objet> tabPopo = new Hashtable<String, objet>();
	Hashtable<String, objet> tabObjetsAffichage = new Hashtable<String, objet>();
	Hashtable<String, objet> tabPopoAffichage = new Hashtable<String, objet>();
	Hashtable<String, objet> tabTemp = new Hashtable<String, objet>();

	public Menu() {
		int choix;
		do {
			System.out.println();
			System.out.println();
			System.out.println("      **************************************");
			System.out.println("      *             TrollFight             *");
			System.out.println("      **************************************");
			System.out.println("      * 1- Commencer une partie            *");
			System.out.println("      *                                    *");
			System.out.println("      * 0- Quitter                         *");
			System.out.println("      **************************************");
			System.out.println();
			System.out.print("      Votre Choix : ");
			choix = IO.lireEntier();

			switch (choix) {
			case 1: {
				try {
					System.out
							.println("Création et placement des objets sur la map en cours");
					stmt.executeUpdate("delete from carac");
					stmt.executeUpdate("delete from bonus");
					stmt.executeUpdate("delete from equipement");
					stmt.executeUpdate("delete from objet");
					objet ob1 = new objet("masse", "arme", "attaque", 3,
							tailleEchiquier);
					tabObjets.put(ob1.idObjet(), ob1);
					tabObjetsAffichage.put(ob1.idObjet(), ob1);
					objet ob2 = new objet("epee", "arme", "degats", 2,
							tailleEchiquier);
					tabObjets.put(ob2.idObjet(), ob2);
					tabObjetsAffichage.put(ob2.idObjet(), ob2);
					objet ob3 = new objet("hache", "arme", "attaque", 5,
							tailleEchiquier);
					tabObjets.put(ob3.idObjet(), ob3);
					tabObjetsAffichage.put(ob3.idObjet(), ob3);
					objet ob4 = new objet("couteau", "arme", "degats", 1,
							tailleEchiquier);
					tabObjets.put(ob4.idObjet(), ob4);
					tabObjetsAffichage.put(ob4.idObjet(), ob4);
					objet ob5 = new objet("bouclier", "armure", "esquive", 3,
							tailleEchiquier);
					tabObjets.put(ob5.idObjet(), ob5);
					tabObjetsAffichage.put(ob5.idObjet(), ob5);
					objet ob6 = new objet("casque", "armure", "vie", 10,
							tailleEchiquier);
					tabObjets.put(ob6.idObjet(), ob6);
					tabObjetsAffichage.put(ob6.idObjet(), ob6);
					objet ob7 = new objet("popoDeg", "potion", "degats", 2,
							tailleEchiquier);
					tabPopo.put(ob7.idObjet(), ob7);
					tabPopoAffichage.put(ob7.idObjet(), ob7);
					objet ob8 = new objet("popoEsq", "potion", "esquive", 2,
							tailleEchiquier);
					tabPopo.put(ob8.idObjet(), ob8);
					tabPopoAffichage.put(ob8.idObjet(), ob8);

					System.out.println("Création Terminée");
					this.MenuCreationTroll(1);
					this.MenuCreationTroll(2);

					tabTrolls[1] = troll1;
					tabTrolls[2] = troll2;

					System.out.println();
					System.out.println();
					System.out
							.println("*** Tirage au sort du troll qui commence ****");
					System.out.println();
					tour = (int) Math.floor(Math.random() * (nbTrolls)) + 1;
					proc = conn.prepareCall("{? = call recherche_troll(?)}");
					proc.registerOutParameter(1, Types.VARCHAR);
					proc.setInt(2, tour);
					proc.executeUpdate();
					String nomTroll = proc.getString(1);
					proc.close();
					System.out.println("Le troll n°" + tour + " (" + nomTroll
							+ ") commence la partie !");
					System.out.println();
					dam.lierTrolls(troll1, troll2);
					this.menuAction();
					break;
				} catch (SQLException E) {
					System.err.println("SQLException: " + E.getMessage());
					System.err.println("SQLState:     " + E.getSQLState());
				}
			}
			default:
				break;
			} // switch
			System.out.println();
		} while (choix != 0);
	}

	public void menuAction() {

		int choix;
		boolean mort = false;
		try {
			do {
				proc = conn.prepareCall("{? = call pa_restants(?)}");
				proc.registerOutParameter(1, Types.INTEGER);
				proc.setInt(2, tour);
				proc.executeUpdate();
				int paRestants = proc.getInt(1);
				proc.close();

				if (paRestants == 0) {
					tabTrolls[tour].verifTourPopo();
					tour = (tour) % 2 + 1;
					tabTrolls[tour].setPa(6);
					/*
					 * if (tour == 1) { troll1.verifTourPopo(); tour = 2;
					 * troll2.setPa(6); } else { troll2.verifTourPopo(); tour =
					 * 1; troll1.setPa(6); }
					 */

					proc = conn.prepareCall("{call init_pa(?,?)}");
					proc.setInt(1, tour);
					proc.setInt(2, 6);
					proc.executeUpdate();
					proc.close();

					proc = conn.prepareCall("{? = call recherche_troll(?)}");
					proc.registerOutParameter(1, Types.VARCHAR);
					proc.setInt(2, tour);
					proc.executeUpdate();
					String nomTroll = proc.getString(1);
					proc.close();
					System.out.println("Au tour du troll n°" + tour + " ("
							+ nomTroll + " ) de jouer !");
				}
				System.out.println();
				System.out
						.println("      ******************************************");
				System.out
						.println("      *                 Actions                *");
				System.out
						.println("      ******************************************");
				System.out
						.println("      * 1- ATTAQUER                            *");
				System.out
						.println("      * 2- SE DEPLACER                         *");
				System.out
						.println("      * 3- RAMMASSER UN OBJET                  *");
				System.out
						.println("      * 4- EQUIPER UN OBJET                    *");
				System.out
						.println("      * 5- DESEQUIPER UN OBJET                 *");
				System.out
						.println("      * 6- UTILISER UNE POTION                 *");
				System.out
						.println("      * 7- CONSULTER SES STATISTIQUES          *");
				System.out
						.println("      * 8- PASSER SON TOUR                     *");
				System.out
						.println("      *                                        *");
				System.out
						.println("      ******************************************");
				System.out.println();
				System.out.print("      Votre Choix : ");
				choix = IO.lireEntier();

				switch (choix) {
				case 1: {
					if (tabTrolls[tour].paRestant() >= 4) {
						mort = tabTrolls[tour].attaquer();
						if (mort) {
							System.out.println();
							System.out
									.println(tabTrolls[(tour) % 2 + 1]
											.nomTroll()
											+ " est mort dans d'attroces souffrances (argh...) !");
							System.out.println(tabTrolls[tour].nomTroll()
									+ " gagne donc la partie");
							tabTrolls[(tour) % 2 + 1].reinitialiserTroll();
							tabTrolls[tour].reinitialiserTroll();
							System.out.println();
							System.out.println("Retour au menu principal");
							System.out.println();
						}
					} else {
						System.out
								.print("Désolé mais vous n'avez plus assez de PA pour attaquer !");
					}

					/*
					 * if (tour==1) { if (troll1.paRestant() >= 4) { mort =
					 * troll1.attaquer(); if (mort) {
					 * System.out.println(troll2.nomTroll() +
					 * " est mort dans d'attroces souffrances (argh...) !");
					 * System.out.println(troll1.nomTroll() +
					 * " gagne donc la partie"); troll1.reinitialiserTroll();
					 * System.out.println("Retour au menu principal");
					 * System.out.println(); } } else { System.out.print(
					 * "Désolé mais vous n'avez plus assez de PA pour attaquer !"
					 * ); } } else { if (troll2.paRestant() >= 4) { mort =
					 * troll2.attaquer(); if (mort) {
					 * System.out.println(troll1.nomTroll() +
					 * " est mort dans d'attroces souffrances (argh...) !");
					 * System.out.println(troll2.nomTroll() +
					 * " gagne donc la partie"); troll2.reinitialiserTroll();
					 * System.out.println("Retour au menu principal");
					 * System.out.println(); } } else { System.out.print(
					 * "Désolé mais vous n'avez plus assez de PA pour attaquer !"
					 * ); } }
					 */
					break;
				}
				case 2: {
					dam.remplirTabObjets(tabObjetsAffichage);
					dam.remplirTabPopo(tabPopoAffichage);
					dam.dessinerDam();
					tabTrolls[tour].deplacer(tailleEchiquier);
					/*
					 * if (tour==1) { troll1.deplacer(tailleEchiquier); } else {
					 * troll2.deplacer(tailleEchiquier); }
					 */
					break;
				}
				case 3: {
					tabTrolls[tour].ramasserObjet();
					/*
					 * if (tour==1) { troll1.ramasserObjet(); } else {
					 * troll2.ramasserObjet(); }
					 */
					break;
				}
				case 4: {
					tabTrolls[tour].tesObjets();
					System.out.println("Quel objet voulez vous equiper ?");
					String idObj = IO.lireChaine();
					ResultSet rset = stmt
							.executeQuery("select estEquipe from equipement where idTroll="
									+ tour + " and idObjet='" + idObj + "'");
					boolean vide = true;
					boolean estEquipe = false;
					while (rset.next()) {
						vide = false;
						estEquipe = rset.getBoolean("estEquipe");
					}
					if (!vide) {
						if (!estEquipe) {
							tabTrolls[tour].equiperObjet(idObj);
						} else {
							System.out.println("Cet objet est déjà équipé !");
						}
					} else {
						System.out
								.println("Cet objet ne fait pas parti de votre équipement !");
					}

					/*
					 * if (tour==1) { troll1.tesObjets();
					 * System.out.println("Quel objet voulez vous equiper ?");
					 * String idObj = IO.lireChaine(); ResultSet rset =
					 * stmt.executeQuery(
					 * "select estEquipe from equipement where idTroll=1 and idObjet='"
					 * +idObj+"'"); boolean vide=true; boolean estEquipe=false;
					 * while (rset.next()) { vide = false; estEquipe =
					 * rset.getBoolean("estEquipe"); } if (!vide) { if
					 * (!estEquipe) { troll1.equiperObjet(idObj); } else {
					 * System.out.println("Cet objet est déjà équipé !"); } }
					 * else { System.out.println(
					 * "Cet objet ne fait pas parti de votre équipement !"); } }
					 * else { troll2.tesObjets();
					 * System.out.println("Quel objet voulez vous equiper ?");
					 * String idObj = IO.lireChaine(); ResultSet rset =
					 * stmt.executeQuery(
					 * "select estEquipe from equipement where idTroll=2 and idObjet='"
					 * +idObj+"'"); boolean vide=true; boolean estEquipe=false;
					 * while (rset.next()) { vide = false; estEquipe =
					 * rset.getBoolean("estEquipe"); } if (!vide) { if
					 * (!estEquipe) { troll2.equiperObjet(idObj); } else {
					 * System.out.println("Cet objet est déjà équipé !"); } }
					 * else { System.out.println(
					 * "Cet objet ne fait pas parti de votre équipement !"); } }
					 */
					break;
				}
				case 5: {
					ResultSet rset = stmt
							.executeQuery("select idObjet from equipement where idTroll="
									+ tour + " and estEquipe=true");
					if (rset.next() == true) {
						while (rset.next()) {
							System.out.println(rset.getString("idObjet"));
						}
						System.out
								.println("Quel objet voulez vous desequiper ?");
						String idObj = IO.lireChaine();
						tabTrolls[tour].desequiperObjet(idObj);
					} else {
						System.out
								.println("Cet objet ne fait pas parti de votre équipement ou n'est pas equipe !");
					}

					/*
					 * if (tour==1) { ResultSet rset = stmt.executeQuery(
					 * "select idObjet from equipement where idTroll=1 and estEquipe=true"
					 * ); if (rset.next()==true) { while (rset.next()) {
					 * System.out.println(rset.getString("idObjet")); }
					 * System.out
					 * .println("Quel objet voulez vous desequiper ?"); String
					 * idObj = IO.lireChaine(); troll1.desequiperObjet(idObj); }
					 * else { System.out.println(
					 * "Cet objet ne fait pas parti de votre équipement ou n'est pas equipe !"
					 * ); } } else { ResultSet rset = stmt.executeQuery(
					 * "select idObjet from equipement where idTroll=2 and estEquipe=true"
					 * ); if (rset.next()==true) { while (rset.next()) {
					 * System.out.println(rset.getString("idObjet")); }
					 * System.out
					 * .println("Quel objet voulez vous desequiper ?"); String
					 * idObj = IO.lireChaine(); troll2.desequiperObjet(idObj); }
					 * else { System.out.println(
					 * "Cet objet ne fait pas parti de votre équipement ou n'est pas equipe !"
					 * ); } }
					 */
					break;
				}
				case 6: {
					boolean vide = true;
					ResultSet rset = stmt
							.executeQuery("select o.idObjet from equipement e,objet o where e.idTroll="
									+ tour
									+ " and o.typeObjet='potion' and o.idObjet=e.idObjet");
					while (rset.next()) {
						vide = false;
						String id = rset.getString("idObjet");
						System.out.println(id);
						objet obj = (objet) tabPopo.get(id);
						if (obj == null) {
						}
						tabTemp.put(id, obj);
					}
					if (!vide) {
						System.out
								.println("Quelle potion voulez vous utiliser ?");
						String idObj = IO.lireChaine();
						if (tabTemp.containsKey(idObj)) {
							objet popo = (objet) tabTemp.get(idObj);
							tabTrolls[tour].utiliserPopo(popo);
						} else {
							System.out
									.println("Cette potion ne fait pas partie de votre équipement !");
						}
					} else {
						System.out
								.println("Aucune potion ne fait partie de votre équipement !");
					}

					/*
					 * if (tour==1) { ResultSet rset = stmt.executeQuery(
					 * "select o.idObjet from equipement e,objet o where e.idTroll=1 and o.typeObjet='potion' and o.idObjet=e.idObjet"
					 * ); while (rset.next()) { vide=false; String id =
					 * rset.getString("idObjet"); System.out.println(id); objet
					 * obj = (objet)tabPopo.get(id); if (obj==null){ }
					 * tabTemp.put(id,obj); } if (!vide) {
					 * System.out.println("Quelle potion voulez vous utiliser ?"
					 * ); String idObj = IO.lireChaine(); if
					 * (tabTemp.containsKey(idObj)) { objet popo =
					 * (objet)tabTemp.get(idObj); troll1.utiliserPopo(popo); }
					 * else { System.out.println(
					 * "Cette potion ne fait pas partie de votre équipement !");
					 * } } else { System.out.println(
					 * "Aucune potion ne fait partie de votre équipement !"); }
					 * } else { ResultSet rset = stmt.executeQuery(
					 * "select o.idObjet from equipement e,objet o where e.idTroll=2 and o.typeObjet='potion' and o.idObjet=e.idObjet"
					 * ); while (rset.next()) { vide=false; String id =
					 * rset.getString("idObjet"); System.out.println(id); objet
					 * obj = (objet)tabPopo.get(id); if (obj==null){
					 * System.out.println("TEST4"); } tabTemp.put(id,obj); } if
					 * (!vide) {
					 * System.out.println("Quelle potion voulez vous utiliser ?"
					 * ); String idObj = IO.lireChaine(); if
					 * (tabTemp.containsKey(idObj)) { objet popo =
					 * (objet)tabTemp.get(idObj); troll2.utiliserPopo(popo); }
					 * else { System.out.println(
					 * "Cette potion ne fait pas partie de votre équipement !");
					 * } } else { System.out.println(
					 * "Aucune potion ne fait partie de votre équipement !"); }
					 * }
					 */
					tabTemp.clear();
					break;
				}
				case 7: {
					ResultSet rset = stmt
							.executeQuery("select attaque from troll where idTroll="
									+ tour);
					rset.first();
					int att = rset.getInt("attaque");
					rset = stmt
							.executeQuery("select degats from troll where idTroll="
									+ tour);
					rset.first();
					int deg = rset.getInt("degats");
					rset = stmt
							.executeQuery("select esquive from troll where idTroll="
									+ tour);
					rset.first();
					int esq = rset.getInt("esquive");
					rset = stmt
							.executeQuery("select vie from troll where idTroll="
									+ tour);
					rset.first();
					int vie = rset.getInt("vie");
					rset = stmt
							.executeQuery("select paRestants from troll where idTroll="
									+ tour);
					rset.first();
					int pa = rset.getInt("paRestants");
					rset = stmt
							.executeQuery("select positionX from troll where idTroll="
									+ tour);
					rset.first();
					int pX = rset.getInt("positionX");
					rset = stmt
							.executeQuery("select positionY from troll where idTroll="
									+ tour);
					rset.first();
					int pY = rset.getInt("positionY");
					System.out
							.println("      **************************************");
					System.out.println("      *           STATS TROLL " + tour
							+ "            *");
					System.out
							.println("      **************************************");
					System.out.println("      * Attaque : " + att
							+ "D3                     *");
					System.out.println("      * Degats : " + deg
							+ "D3                      *");
					System.out.println("      * Esquive : " + esq
							+ "D3                     *");
					System.out.println("      * Pts de vie restants : " + vie
							+ "          *");
					System.out.println("      * Nombre de PA restants : " + pa
							+ "          *");
					System.out.println("      * Position X : " + pX
							+ "                     *");
					System.out.println("      * Position Y : " + pY
							+ "                     *");
					System.out
							.println("      * Potion(s) en cours :               *");
					tabTrolls[tour].afficherPopo();
					System.out
							.println("      **************************************");
					break;
				}
				case 8: {
					proc = conn.prepareCall("{call init_pa(?,?)}");
					proc.setInt(1, tour);
					proc.setInt(2, 0);
					proc.executeUpdate();
					proc.close();
					break;
				}
				default:
					break;
				} // switch
				System.out.println();
			} while ((choix != 1 || choix != 2 || choix != 3 || choix != 4
					|| choix != 5 || choix != 6 || choix != 7)
					&& mort == false);
		} catch (SQLException E) {
			System.err.println("SQLException: " + E.getMessage());
			System.err.println("SQLState:     " + E.getSQLState());
		}// catch

	} // Fin

	public void supprimerPopo(String idPopo) {
		// tabObjets.remove(idPopo);
		tabPopoAffichage.remove(idPopo);
	}

	public void supprimerObjet(String idObjet) {
		// objet obj = (objet)tabObjets.get(idObjet);
		// if (obj.typeObjet().equals("potion")) {
		// tabPopo.remove(idObjet);
		// }
		// else {
		tabObjetsAffichage.remove(idObjet);
		// }
	}

	public void MenuCreationTroll(int numTroll) {
		// boolean dedans=false;
		int choix;
		do {
			System.out.println();
			System.out.println();
			System.out.println("      **************************************");
			System.out.println("      *              TROLL " + numTroll
					+ "               *");
			System.out.println("      **************************************");
			System.out.println("      * 1- Charger un troll existant       *");
			System.out.println("      * 2- Creer un nouveau troll          *");
			System.out.println("      **************************************");
			System.out.println();
			System.out.print("      Votre Choix : ");
			choix = IO.lireEntier();

			switch (choix) {
			case 1: {
				ResultSet rset;
				int att = 0;
				int deg = 0;
				int esq = 0;
				if (numTroll == 1) {
					try {
						System.out.print("Nom du troll à charger : ");
						String nomTroll = IO.lireChaine();
						rset = stmt
								.executeQuery("select attaque,degats,esquive from troll where nomTroll='"
										+ nomTroll + "'");
						while (rset.next()) {
							att = rset.getInt("attaque");
							deg = rset.getInt("degats");
							esq = rset.getInt("esquive");
						}
						troll1 = new troll(numTroll, nomTroll, att, deg, esq,
								tailleEchiquier);
					} catch (SQLException E) {
						System.err.println("SQLException: " + E.getMessage());
						System.err.println("SQLState:     " + E.getSQLState());
					}
				} else {
					try {
						System.out.print("Nom du troll à charger : ");
						String nomTroll = IO.lireChaine();
						rset = stmt
								.executeQuery("select attaque,degats,esquive from troll where nomTroll='"
										+ nomTroll + "'");
						while (rset.next()) {
							att = rset.getInt("attaque");
							deg = rset.getInt("degats");
							esq = rset.getInt("esquive");
						}
						troll2 = new troll(numTroll, nomTroll, att, deg, esq,
								tailleEchiquier);
					} catch (SQLException E) {
						System.err.println("SQLException: " + E.getMessage());
						System.err.println("SQLState:     " + E.getSQLState());
					}
				}
				break;
			}
			case 2: {
				// dedans=true;
				if (numTroll == 1) {
					troll1 = new troll(1, tailleEchiquier, this);
				} else {

					troll2 = new troll(2, tailleEchiquier, this);
				}
				break;
			}
			default:
				break;
			} // switch
			System.out.println();
		} while (choix != 1 && choix != 2);
	}

}// Fin classe Menu
