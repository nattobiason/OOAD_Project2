import java.util.concurrent.ThreadLocalRandom;
import java.lang.Math;

public class GameStore {    /* ABSTRACTION */ 
    Games[] shelves = new Games[12];
    double cashRegister;
    int addedFundsToRegister;
    int day;

    public void fillShelves(){
        shelves[0] = new Games("Monopoly", "Family", 19.99, 15, 10, 2);
        shelves[1] = new Games("Clue", "Family", 18.99, 10, 10, 2);
        shelves[2] = new Games("Life", "Family", 19.99, 16, 11, 3);

        shelves[3] = new Games("Mousetrap", "Kids", 24.89, 16, 10, 4);
        shelves[4] = new Games("Candyland", "Kids", 12.99, 8, 11, 2);
        shelves[5] = new Games("Connect Four", "Kids", 11.99, 10, 10, 2);

        shelves[6] = new Games("Magic", "Card", 39.99, 3, 2, 2);
        shelves[7] = new Games("Pokemon", "Card", 3.99, 3, 2, 1);
        shelves[8] = new Games("Netrunner", "Card", 10.99, 3, 2, 1);

        shelves[9] = new Games("Catan", "Board", 41.92, 9, 7, 2);
        shelves[10] = new Games("Risk", "Board", 31.49, 16, 11, 2);
        shelves[11] = new Games("Gloomhaven", "Board", 99.99, 16, 12, 8);
    }

    int getRandomInt(int min, int max){
        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
        return randomNum;
    }

    public GameStore(){
        cashRegister = 0.0;
        day = 1;
        addedFundsToRegister = 0;
    }
}

class EmployeeTasks extends GameStore{ /* INHERITANCE */ 
    private String cashierName; /* ENCAPSULATION */
    void pickCashier(){
        int randomNum = getRandomInt(0,1);
        if(randomNum==0){
            cashierName="Burt";
        }
        else{
            cashierName="Ernie";
        }
    }
    void arrive(){
        System.out.println(cashierName + " the Cashier has arrived at the store on Day "+day);
        for(int i=0;i<12;i++){
            if(shelves[i].delivered){
                shelves[i].inventory=3;
                System.out.println("3 "+shelves[i].name + " have been delivered ");
                shelves[i].delivered=false;
            }
        }
    }
    void count(){
        if(cashRegister<100.00){
            cashRegister=cashRegister+1000.00;
            addedFundsToRegister=addedFundsToRegister+1;
            System.out.println("There is $"+cashRegister +" in the cash register");
            System.out.println("Added  $1000 to the register on day "+day);
        }
        else{
            System.out.println("There is $"+cashRegister +" in the cash register");
        }
    }
    void vacuum(){
        System.out.println(cashierName+" is vacuuming ");
        int randomNum = getRandomInt(0,20);
        int lim=1;
        if(cashierName=="Burt"){
            lim=lim+1;
        }
        if(randomNum<lim){
            int r = getRandomInt(0,11);
            shelves[r].damaged=shelves[r].damaged+1;
            shelves[r].inventory=shelves[r].inventory-1;
            System.out.println(cashierName+" damaged "+shelves[r].name);
        }
    }
    void stack(){
        if(cashierName == "Ernie"){
            for (int i = 0; i < 11; i++)
            {
                int min_idx = i;
                for (int j = i+1; j < 12; j++){
                    if ((shelves[j].inventory*shelves[j].dimensions[2]) < (shelves[min_idx].inventory*shelves[min_idx].dimensions[2])){
                        min_idx = j;
                    }
                }
                Games temp = shelves[min_idx];
                shelves[min_idx] = shelves[i];
                shelves[i] = temp;
                System.out.println("Ernie stacks "+ shelves[i].inventory + " " + shelves[i].name + " games in shelf position " + i + " (pile height = "+ shelves[i].inventory*shelves[i].dimensions[2]+ "in)");
            }
        }

        else if(cashierName == "Burt"){ /* POLYMORPHISM */
            for (int i = 11; i >= 0; i--)
            {
                int max_idx = i;
                for (int j = 0; j < i; j++){
                    if ((shelves[j].dimensions[1]) > (shelves[max_idx].dimensions[1])){
                        max_idx = j;
                    }
                }
                Games temp = shelves[max_idx];
                shelves[max_idx] = shelves[i];
                shelves[i] = temp;
                System.out.println("Burt stacks "+ shelves[i].inventory + " " + shelves[i].name + " games in shelf position " + i + " (pile width = "+ shelves[i].dimensions[1]+ "in)");
            }
        }
    }
    void open(){
        System.out.println(cashierName + " has opened the store!");
        int randomNum = getRandomInt(1, 5);
        for(int i=1;i<randomNum;i++){
            int prob=20;
            int bought=0;
            for(int j=0;j<12;j++){
                int a = getRandomInt(0, 100);
                if(bought==2){
                    break;
                }
                if(a<=prob){
                    if(shelves[j].inventory!=0){
                        bought=bought+1;
                        cashRegister=cashRegister+shelves[j].price;
                        shelves[j].inventory=shelves[j].inventory-1;
                        shelves[j].sold=shelves[j].sold+1;
                        System.out.println(cashierName + " sold a "+shelves[j].name + " to customer " + i + " for "+ shelves[j].price);
                    }
                }
                prob=prob-2;
            }
            if(bought==0){
                System.out.println(cashierName + " did not sell anything to customer " + i);
            }
        }
    
    }
    void order(){
        for (int i = 0; i < 12; i++) {
            if(shelves[i].inventory==0){
                shelves[i].delivered=true;
                cashRegister -= (((shelves[i].price)/2)*3);
                System.out.println(cashierName + " ordered "+shelves[i].name);
            }
          }
    }
    void close(){
        System.out.println("Store closed. " + cashierName + " is leaving.");
        System.out.println(" ");
        cashRegister = Math.floor(cashRegister * 100) / 100;
    }
    public static void main(String[] args) {
        EmployeeTasks store = new EmployeeTasks(); /* IDENTITY */
        store.fillShelves();
        while(store.day<=30){
            store.pickCashier();
            store.arrive();
            store.count();
            store.vacuum();
            store.stack();
            store.open();
            store.order();
            store.close();
            store.day++;
        }

        /* once 30 days are over */
        for(int i = 0; i < 12; i++){
            Games temp = store.shelves[i];
            
            System.out.println(temp.name);
            System.out.println("   inevntory: " + temp.inventory);
            System.out.println("   number sold: " + temp.sold);
            double sales = temp.sold * temp.price;
            sales = Math.floor(sales * 100) / 100;
            System.out.println("   total sales: " + sales);
        }
        System.out.println("Damaged Games:");
        for(int i = 0; i < 12; i++){
            Games temp = store.shelves[i];
            if(temp.damaged > 0){
                System.out.println(temp.name + ": " + temp.damaged);
            }
        }
        System.out.println("Cash Register: $" + store.cashRegister);
        System.out.println("Number of times money had to be added to register: " + store.addedFundsToRegister);
        store = null;
    }
}

class Games { /* HIGH COHESION - Games class only exists to give info about each game (well defined job) */

    String name;
    String category;
    double price;
    int dimensions[] = new int[3]; // l x w x h
    int sold;
    int inventory;
    int damaged; 
    boolean delivered; 

    public Games(String n, String c, double p, int l, int w, int h){
        name = n;
        category = c;
        price = p;
        dimensions = new int[]{l, w, h};
        sold = 0;
        inventory = 3;
        damaged = 0;
        delivered = false;
    }
}
