import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		final int NUMTRAN = 4;
//		final int NUMPLAZAS = 6;
		String fileMatrizI = "red.txt";
		int[][] I = readMatrix(fileMatrizI);
		
		List<Integer> transiciones_hilo_1 = new MyLinkedList<Integer>();
		List<Integer> transiciones_hilo_2 = new MyLinkedList<Integer>();

		Collections.addAll(transiciones_hilo_1, 0,1);
		Collections.addAll(transiciones_hilo_2, 3,2);
		
		GestorMonitor monitor = new GestorMonitor(NUMTRAN, I);
		
		Hilo hilo1 = new Hilo(transiciones_hilo_1,monitor);
		Hilo hilo2 = new Hilo(transiciones_hilo_2,monitor);
		Thread thread1 = new Thread(hilo1);
		Thread thread2 = new Thread(hilo2);
		
		thread2.start();
		thread1.start();
	}
	
	private static int[][] readMatrix(String file){
		// read in the data
		ArrayList<ArrayList<Integer>> matriz = new ArrayList<ArrayList<Integer>>();
		Scanner input = null;
		Scanner colReader = null;
		try {
			input = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(input.hasNextLine())
		{
		    colReader = new Scanner(input.nextLine());
		    colReader.next();
		    ArrayList<Integer> col = new ArrayList<Integer>();
		    while(colReader.hasNextInt())
		    {
		        col.add(colReader.nextInt());
		    }
		    matriz.add(col);
		}
		input.close();
		colReader.close();
		//Convierto de arrayList a array
		int[][] I=new int[matriz.size()][matriz.get(0).size()];
		for (int i=0;i<matriz.size();i++){
			I[i] = convertIntegers(matriz.get(i));
		}
		return  I;
	}
	

	public static int[] convertIntegers(List<Integer> integers)
	{
	    int[] ret = new int[integers.size()];
	    for (int i=0; i < ret.length; i++)
	    {
	        ret[i] = integers.get(i).intValue();
	    }
	    return ret;
	}
}
