package projet;

import java.io.*;

public class caseDamier  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String occupePar ;
	private int X;
	private int Y;
	
	public caseDamier () {
		occupePar= " ";
		X=0;
		Y=0;
	} // Fin Construct

	public void setValeurX (int k){
		X=k;
	}
	public void setValeurY (int k){
		Y=k;
	}
	public String getValeur (){
		return occupePar;
	}
	public int getValeurX (){
		return X;
	}
	public int getValeurY (){
		return Y;
	}
	public void setValeur (String o){
		 occupePar=o;
	}
}

