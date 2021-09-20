import java.util.concurrent.ThreadLocalRandom;

public class GameStore {
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

class EmployeeTasks extends GameStore{ /* EXAMPLE OF INHERITANCE */
    String cashierName;
    void pickCashier(){
        int randomNum = getRandomInt(0,1);
        if(randomNum==0){
            cashierName="Burt";
        }
        else{
            cashierName="Ernie";
        }
        // randomly select cashier and set cashierName
        // can use getRandomInt()
    }
    void arrive(){
        System.out.println(cashierName + " the Cashier has arrived at the store on Day "+day);
        for(int i=0;i<12;i++){
            if(shelves[i].delivered==true){
                shelves[i].inventory=3;
                System.out.println("3 "+shelves[i].name + " have been delivered ");
                shelves[i].delivered=false;
                stack();
            }
        }
    }
    void count(){
        if(cashRegister<100){
            cashRegister=cashRegister+1000;
            addedFundsToRegister=addedFundsToRegister+1;
            System.out.println("There is $"+cashRegister +" in the cash register");
            System.out.println("Added  1000 to the register on day "+day);
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

    }
    void open(){
        System.out.println(cashierName + " has opened the store!");
        int randomNum = ThreadLocalRandom.current().nextInt(0, 5);
        for(int i=0;i<randomNum;i++){
            int prob=20;
            int bought=0;
            for(int j=0;j<12;j++){
                int a = ThreadLocalRandom.current().nextInt(0, 100);
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
                cashRegister=cashRegister-shelves[i].price*3;
                System.out.println(cashierName + " Bought "+shelves[i].name);
            }
          }
    }
    void close(){
        System.out.println("Store closed. " + cashierName + " is leaving.");
    }
    public static void main(String[] args) {
        EmployeeTasks store = new EmployeeTasks();
        store.fillShelves();
        while(store.day<30){
            /* simulate game for 30 days */
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
            System.out.println(store.shelves[i].name);
            System.out.println("   inventory:" +store.shelves[i].inventory);
            System.out.println("   number sold:" + store.shelves[i].sold);
            double sales = store.shelves[i].sold * store.shelves[i].price;
            System.out.println("   total sales:" + sales);
        }
        System.out.println("Damaged Games:");
        for(int i = 0; i < 12; i++){
            if(store.shelves[i].damaged > 0){
                System.out.println(store.shelves[i].name + ": " + store.shelves[i].name);
            }
        }
        System.out.println("Cash Register: $" + store.cashRegister);
        System.out.println("Number of times money had to be added to register: " + store.addedFundsToRegister);

    }
}

class Games {

    String name;
    String category;
    double price;
    int dimensions[] = new int[3]; // l x w x h
    int sold;
    int shelfPosition;
    int inventory;
    int pileHeight; // makes it easier for Ernies stack() function. When customer buys game make sure to update pile height
    int pileWidth; // makes it easier for Berts stack() function. 
    int damaged; // instead of having a whole array of damaged games, just keep track of how many get damaged
    boolean delivered; // true if employee ordered new games last night, false if not

    public Games(String n, String c, double p, int l, int w, int h){
        name = n;
        category = c;
        price = p;
        dimensions = new int[]{l, w, h};
        sold = 0;
        shelfPosition = 0;
        inventory = 3;
        pileHeight = inventory*dimensions[2]; 
        pileWidth = dimensions[1];
        damaged = 0;
        delivered = false;
    }
}
