import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GestorMonitor {
	
	final Semaphore entrada_monitor = new Semaphore(1);
	private boolean k;
	private RedPetri red = new RedPetri();
	private Semaphore colas[];
	private List<Integer> sensibilizadas = new ArrayList<Integer>();
	private List<Integer> quienesEnCola = new ArrayList<Integer>();
	
	public GestorMonitor(int cantidadTransiciones){
		for (int i=0;i<cantidadTransiciones;i++){
			colas[i] = new Semaphore(1);
		}
	}
	
	public void dispararTransicion(int transicion){
		try {
			entrada_monitor.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		k = true;
		while(k == true){
			k = red.disparar();
			
			if (k==true){
				sensibilizadas = red.get_sensibilizadas();
				//Actualizo quienes estan en la cola
				this.quienesEnCola(); 
				List<Integer> listasParaDisparar = null; //Aca hay que hacer el and de sensibilizidas y listas para disparar
				if (listasParaDisparar.contains(1)){
					//A falta de politica despierto a los hilos de la primera transicion disponible
					int indiceDespertar = listasParaDisparar.indexOf(1);
					//Despierto a un hilo que esta esperando por esa transicion
					colas[indiceDespertar].release();
					//Salgo del monitor
					entrada_monitor.release();
					return;
				}
				else{
					k = false;
					//Me salgo del while
					return;
				}
				
			}
			
			else{
				entrada_monitor.release();
				try {
					colas[transicion].acquire();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					colas[transicion].acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return;
		}
	}
	
	private void quienesEnCola(){
		for (int i=0;i<colas.length;i++){
			//Lleno quienesEnCola con "1" donde hay hilos esperando y "0" donde no los hay
			int hayHilos = colas[i].availablePermits() ^ 1;
			quienesEnCola.set(i, hayHilos);
		}
		return;
	}
}
