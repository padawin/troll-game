package troll;

import java.util.*;

public class Damier {

	private int x;
	private int y;
	private troll tr1;
	private troll tr2;
	private Hashtable tabObjets;
	private Hashtable tabPopo;

	public Damier(troll tr1, troll tr2, int tailleEchiquier) {
		x = tailleEchiquier;
		y = tailleEchiquier;
		this.tr1 = tr1;
		this.tr2 = tr2;
	}// fin constructeur Damier

	public void dessinerDam() {
		caseDamier cased = new caseDamier();
		System.out.println();
		System.out.print("    ");
		for (int j = 1; j <= x; j++) {
			if (j < 10) {
				System.out.print(" 00" + (j));
			} else if (j < 100 && j >= 10) {
				System.out.print(" 0" + (j));
			} else if (j >= 100 && j < 1000) {
				System.out.print(" " + (j));
			}
		}
		System.out.println();
		for (int j = 1; j <= y; j++) {

			cased.setValeurY(j);
			System.out.print("    ");
			for (int e = 0; e < x; e++) {
				System.out.print(" ---");
			}
			System.out.println();
			if (j < 10) {
				System.out.print(" 00" + (j));
			} else if (j < 100 && j >= 10) {
				System.out.print(" 0" + (j));
			} else if (j >= 100 && j < 1000) {
				System.out.print(" " + (j));
			}

			for (int h = 0; h < x; h++) {
				cased.setValeur(" ");
				int k = h + 1;
				cased.setValeurX(k);
				// si les 2 trolls sont sur la même case
				if (((cased.getValeurX() == tr1.positionX()) && (cased
						.getValeurY() == tr1.positionY()))
						&& ((cased.getValeurX() == tr2.positionX()) && (cased
								.getValeurY() == tr2.positionY()))) {
					cased.setValeur("F");
					System.out.print("| " + cased.getValeur() + " ");
				}
				// si le troll 1 est sur la case
				else if ((cased.getValeurX() == tr1.positionX())
						&& (cased.getValeurY() == tr1.positionY())) {
					System.out.print("| " + tr1.getValeur() + " ");
				}
				// si le troll 2 est sur la case
				else if (((cased.getValeurX() == tr2.positionX()) && (cased
						.getValeurY() == tr2.positionY()))) {
					System.out.print("| " + tr2.getValeur() + " ");
				}
				// si un objet est sur la case
				else {
					Enumeration tab = tabObjets.elements();
					int compteur = 0;
					int compteur2 = 0;
					while (tab.hasMoreElements()) {
						objet obj = (objet) tab.nextElement();
						int x = obj.positionX();
						int y = obj.positionY();
						if ((cased.getValeurX() == x)
								&& (cased.getValeurY() == y)) {
							compteur = compteur + 1;
						}
					}
					Enumeration tab2 = tabPopo.elements();
					while (tab2.hasMoreElements()) {
						objet obj = (objet) tab2.nextElement();
						int x = obj.positionX();
						int y = obj.positionY();
						if ((cased.getValeurX() == x)
								&& (cased.getValeurY() == y)) {
							compteur2 = compteur2 + 1;
						}
					}
					if (compteur == 0 && compteur2 == 0) {
						cased.setValeur(" ");
						System.out.print("| " + cased.getValeur() + " ");
					} else if (compteur == 1 && compteur2 == 0) {
						cased.setValeur("O");
						System.out.print("| " + cased.getValeur() + " ");
					} else if (compteur2 == 1 && compteur == 0) {
						cased.setValeur("P");
						System.out.print("| " + cased.getValeur() + " ");
					} else {
						cased.setValeur((compteur + compteur2) + "");
						System.out.print("| " + cased.getValeur() + " ");
					}
				}
				cased.setValeur(" ");
			}
			System.out.print("|");
			System.out.println();
		}
		System.out.print("    ");
		for (int i = 0; i < x; i++) {
			System.out.print(" ---");
		}
		System.out.println();
	}// fin dessinerDam

	public void lierTrolls(troll tr1, troll tr2) {
		this.tr1 = tr1;
		this.tr2 = tr2;
	}

	public void remplirTabObjets(Hashtable tabObjets) {
		this.tabObjets = tabObjets;
	}

	public void remplirTabPopo(Hashtable tabPopo) {
		this.tabPopo = tabPopo;
	}

}// fin class Damier
