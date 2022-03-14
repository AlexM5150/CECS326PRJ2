/**
 * Philosopher.java
 *
 * This class represents each philosopher thread.
 * Philosophers alternate between eating and thinking.
 *
 */


public class Philosopher implements Runnable
{
    //inital variables
    private int philNumber;
    private String action;
    private DiningServerImpl table;
    
    //philosopher constructor
    public Philosopher(int philNumber, DiningServerImpl table){
        this.philNumber = philNumber;
        this.table = table;
        //set inital philisopher action
        this.action = "THINKING";
    }
    //Throw exception when thread is interrupted
    private void sleep() throws InterruptedException{
        // generate random sleep period
        long rand = (long)(Math.random()*2000) + 1000;
        Thread.sleep(rand);
    }

    //set philosophers action
    public void setAction(String action){
        this.action = action;
    }

    //get philosophers action
    public String getAction(){
       return this.action;
    }

    @Override
    public void run() {
        while(true){
            try{
                //philosopher takes forks off the table
                table.takeForks(philNumber);
                //sleep
                sleep();
                //philiosopher returns forks to think
                table.returnForks(philNumber);
            }
            catch(Exception e) {
                System.out.println("Exception occured");
            }
        }
        
    }
}
