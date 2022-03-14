/**
 * DiningServer.java
 *
 * This class contains the methods called by the  philosophers.
 *
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DiningServerImpl  implements DiningServer
{
	// initial varibles
	Philosopher[] philosophers = new Philosopher[5];
	Condition[] forkConditions = new Condition[5];
	Lock lock = new ReentrantLock();
	Philosopher p;
	Thread pThread;

	//DiningServerImpl constructor
	public DiningServerImpl(){
		//initialize and start threads
		for(int i = 0; i < 5; i++){
			p = new Philosopher(i, this);
			philosophers[i] = p;
			pThread = new Thread(philosophers[i]);
			forkConditions[i] = lock.newCondition();
			pThread.start();
		}
	}

	//test if philosopher can eat and sets action to eating if possible
	private void check(int philNumber){
		if((philosophers[(philNumber+4) % 5].getAction() != "EATING") 
			&& (philosophers[philNumber].getAction() == "WAITING")
			&& (philosophers[(philNumber + 1) % 5].getAction() != "EATING")){
				philosophers[philNumber].setAction("EATING");
				System.out.println("philosopher" + philNumber + "is eating");
				forkConditions[philNumber].signal(); // tells other philosophers something changed
			}
	}

	//philosopher takes forks to eat
	@Override
	public void takeForks(int philNumber) {
		// aquire lock
		lock.lock();
		philosophers[philNumber].setAction("WAITING");
		System.out.println("Philosopher" + philNumber + "is waiting to eat");
		//test to see if philosopher can eat
		check(philNumber);
		//keep waiting if cannot eat
		try {
			if (philosophers[philNumber].getAction() != "EATING"){
				forkConditions[philNumber].await();
			}
			
		} catch (InterruptedException e) {
			System.out.println("Exception occured");
		}
		finally{
			//release lock
			lock.unlock();
		}
		
	}

	@Override
	public void returnForks(int philNumber) {
		// aquire lock
		lock.lock();
		philosophers[philNumber].setAction("THINKING");
		System.out.println("The philosopher is done eating and will now think");

		//check if nieghbors need forks
		check((philNumber + 4) % 5);
		check((philNumber + 1) % 5);

		//release lock
		lock.unlock();
		
		
	}  
}
