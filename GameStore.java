import java.util.concurrent.ThreadLocalRandom;

public class GameStore {
    Games[] shelves = new Games[12];
    double cashRegister;
    int addedFundsToRegister;
    int day;

    public void emptyRegister(){
        cashRegister += 1000.00;
        addedFundsToRegister += 1;
    }

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
        // randomly select cashier and set cashierName
        // can use getRandomInt()
    }
    void arrive(){

    }
    void count(){
        
    }
    void vacuum(){

    }
    void stack(){

    }
    void open(){

    }
    void order(){

    }
    void close(){
        System.out.println("Store closed. " + cashierName + " is leaving.");
    }
    public static void main(String[] args) {
        GameStore store = new GameStore();
        EmployeeTasks tasks = new EmployeeTasks();
        store.fillShelves();
        while(store.day<30){
            /* simulate game for 30 days */
            tasks.pickCashier();
            tasks.arrive();
            tasks.count();
            tasks.vacuum();
            tasks.stack();
            tasks.open();
            tasks.order();
            tasks.close();
            store.day++;
        }

        /* once 30 days are over */
        for(int i = 0; i < 12; i++){
            System.out.println(store.shelves[i].name);
            System.out.println("   inevntory:" +store.shelves[i].inventory);
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
