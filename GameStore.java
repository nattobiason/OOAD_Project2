import java.util.concurrent.ThreadLocalRandom;

public class GameStore {
    static int numberOfShelves = 12;
    Shelves[] shelves = new Shelves[numberOfShelves]; 
    double cashRegister; 
    int addedFundsToRegister;
    int day;
    int numOfCashiers = 3;
    Cashier[] cashierArr = new Cashier[numOfCashiers];
    int cashierOfDay; // to keep track of which index in cashierArr is active that day
    Announcer announcer;
    Banker cookies = new Banker();

    public static int getRandomInt(int min, int max){ 
        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
        return randomNum;
    }
    void pickCashier(){ // randomly assigns cashier for the day
        cashierOfDay = getRandomInt(0, numOfCashiers-1);
        arrive();
    }
    void arrive(){
        announcer.announce(cashierArr[cashierOfDay].name + " has arrived");
        for(int i = 0; i < numberOfShelves; i++){
            if(shelves[i].game.delivered){
                shelves[i].game.inventory = 3;
                announcer.announce("3 " + shelves[i].game.name+" were delivered");
                shelves[i].game.delivered = false;
            }
        }
        count();
    }
    void count(){
        if(cashRegister < 100.00){
            cashRegister+=1000.00;
            addedFundsToRegister++;
            announcer.announce("added $1000 to register");
            /* announce how much is in the register and that $1000 has been added to the register */
        }
        else{
            announcer.announce("$" + cashRegister + " in the register");
        }
        cookies.gongerArrives();
        vacuum();
    }

    public void vacuum(){
        if(getRandomInt(1, cashierArr[cashierOfDay].breakChance) == 1){ // chance of employee breaking game
            int r = getRandomInt(0,11);
            shelves[r].game.damaged++;
            shelves[r].game.inventory--;
            announcer.announce(cashierArr[cashierOfDay].name + " broke " + shelves[r].game.name);
        }
        stack();
    }
    public void stack(){
        shelves = cashierArr[cashierOfDay].executeStack(shelves);
        open();
    }
    public void open(){
        announcer.announce("The store has opened! Day: "+day);
        int numCustomers = getRandomInt(0, 4);
        int dailyCookieCount=0;
        for(int i=1;i<=numCustomers;i++){
            if(getRandomInt(1, 100) == 1){ // if customer is cookie monster
                cookieMonster();
                break;
            } 

            int x = 1;
            int cookiesWant = getRandomInt(1,3); // how many cookies customer wants to buy
            int cookiesBought = cookies.buyCookies(cookiesWant, i);
            dailyCookieCount=dailyCookieCount+cookiesBought;
            if(cookiesBought>0){
                cashRegister-=(cookiesBought*(cookies.cookiePrice));
                x = 5;
            }
            else x = (-10);

            int numGamesPurchased = 0;
            for(int j = 0; j<numberOfShelves; j++){
                if(numGamesPurchased == 2) break;
                int pr = shelves[j].probability+x;
                if(pr<0) pr = 0;
                if(getRandomInt(0, pr) == 1 && shelves[j].game.inventory>0){
                    announcer.announce("Customer "+i+" bought "+shelves[j].game.name);
                    numGamesPurchased++;
                    cashRegister+=shelves[j].game.price;
                    shelves[j].game.inventory--;
                    shelves[j].game.sold++;
                    int add = 0;
                    if(getRandomInt(0, shelves[j].game.probability) == 1){ // if customer wants to add special parts to purchase
                        add = getRandomInt(1, shelves[j].game.numAddedParts); // number of pieces to add
                        double p = add*(shelves[j].game.price)/8; // price of each special piece
                        cashRegister-=(add*p);
                        announcer.announce("Customer "+i+" bought "+add+" special pieces");
                    }
                }
            }
            if(numGamesPurchased == 0){
                announcer.announce("Customer "+i+" bought no games");
            }
        }
        cookies.saveDay(Integer.toString(dailyCookieCount));
        //save daily cookie count
        order();
    }
    void cookieMonster(){
        if(cookies.cookieInventory > 0){
            announcer.announce("COOKIE MONSTER ARRIVED AND ATE ALL THE COOKIES");
            cookies.cookiemonster=cookies.cookiemonster+cookies.cookieInventory;
            cookies.cookieInventory = 0;
            int damaged = getRandomInt(1, 6);
            for(int i = 0; i < damaged; i++){
                int r = getRandomInt(0,11);
                shelves[r].game.damaged++;
                shelves[r].game.inventory--;
                announcer.announce("cookie monster damaged "+shelves[r].game.name);
            }
        }
        else announcer.announce("Cookie Monster ate no cookies and damaged no games :)");

    }
    void order(){
        for (int i = 0; i < numberOfShelves; i++) {
            if(shelves[i].game.inventory==0){
                shelves[i].game.delivered=true;
                cashRegister -= (((shelves[i].game.price)/2)*3);
                announcer.announce(cashierArr[cashierOfDay].name+" ordered 3 "+shelves[i].game.name);
            }
          }
          close();
    }
    void close(){
        announcer.announce("Store is closing :(");
        cashRegister = Math.floor(cashRegister * 100) / 100;
        day++;
        if(cookies.cookieInventory == 0){
            cookies.numOfCookiePackages++;
        }
        else if(cookies.numOfCookiePackages > 1) cookies.numOfCookiePackages--;
        if(day <= 30) pickCashier();
        else endOfThirtyDays();
    }

    void endOfThirtyDays(){
        
        /* 
        announce:
            for each game -- num in inventory, num sold, total sales
            contents damaged game conatiner
            amount in cash register
            how many times we had to add funds to register
        */
        for(int i = 0; i < 12; i++){
            // shelves[i].game.inventory
            
            System.out.println(shelves[i].game.name);
            System.out.println("   inventory: " + shelves[i].game.inventory);
            System.out.println("   number sold: " + shelves[i].game.sold);
            double sales = shelves[i].game.sold * shelves[i].game.price;
            sales = Math.floor(sales * 100) / 100;
            System.out.println("   total sales: " + sales);
        }
        System.out.println("Damaged Games:");
        for(int i = 0; i < 12; i++){
            if(shelves[i].game.damaged > 0){
                System.out.println(shelves[i].game.name + ": " + shelves[i].game.damaged);
            }
        }
        System.out.println("cookies sold each day " + cookies.numsoldperday);

        System.out.println("cookies sold in total  " + cookies.totalsold);

        System.out.println("cookies stole by the cookie monster  " + cookies.cookiemonster);
        System.out.println("money paid to gonger  " + cookies.gongersPocket);



        System.out.println("Cash Register: $" + cashRegister);
        System.out.println("Number of times money had to be added to register: " + addedFundsToRegister);

    }


    public GameStore(){
        
        day = 1;
        addedFundsToRegister = 0;
        cashRegister = 0.0;

        shelves[0] = new Shelves(new Monopoly(), 50);
        shelves[1] = new Shelves(new FamilyGame("Clue", 18.99, 10, 10, 2, 0, 0), 5);
        shelves[2] = new Shelves(new FamilyGame("Life", 19.99, 16, 11, 3, 0, 0), 6);
        shelves[3] = new Shelves(new MouseTrap(), 7);
        shelves[4] = new Shelves(new KidGame("Candyland", 12.99, 8, 11, 2, 0, 0), 8);
        shelves[5] = new Shelves(new KidGame("Connect Four", 11.99, 10, 10, 2, 0, 0), 10);
        shelves[6] = new Shelves(new CardGame("Magic", 39.99, 3, 2, 2), 12);
        shelves[7] = new Shelves(new CardGame("Pokemon", 3.99, 3, 2, 1), 18);
        shelves[8] = new Shelves(new CardGame("Netrunner", 10.99, 3, 2, 1), 25);
        shelves[9] = new Shelves(new BoardGame("Catan", 41.92, 9, 7, 2, 0, 0), 50);
        shelves[10] = new Shelves(new BoardGame("Risk", 31.49, 16, 11, 2, 0, 0), 0);
        shelves[11] = new Shelves(new Gloomhaven(), 0);

        cashierArr[0] = new Cashier("Burt", 10, new BurtStack());
        cashierArr[1] = new Cashier("Ernie", 20, new ErnieStack());
        cashierArr[2] = new Cashier("Bart", 50, new BartStack());
        announcer = new Announcer();
        cookies = new Banker();

        pickCashier();
    }

    public static void main(String[] args){
        GameStore store = new GameStore();
        store = null;
    }
}


class Employee {
    String name;

    public Employee(String n){
        name = n;
    }
}
class Cashier extends Employee{
    public Cashier(String n, int b, Stack s) {
        super(n);
        breakChance = b;
        stackBehavior = s;
    }
    public Shelves[] executeStack(Shelves s[]){
        return stackBehavior.stack(s);
    }
    Stack stackBehavior;
    int breakChance;
}
class Announcer extends Employee{
    public void announce(String s){
        System.out.println("Guy says: " + s);
    }
    public Announcer() {
        super("Guy");
    }
    
}
class Banker extends Employee{
    int cookieInventory;
    double cookiePrice = 2.0;
    int numOfCookiePackages;
    double gongersPocket;
    String numsoldperday="";
    int totalsold=0;
    int cookiemonster=0;

    void gongerArrives(){
        System.out.println("Gonger had arrived");
        cookieInventory=numOfCookiePackages;
    }

    void saveDay(String S){
        numsoldperday=numsoldperday+S + ",";
    }

    int buyCookies(int num, int c){
    
        if(num>cookieInventory){
            num = cookieInventory;
            totalsold=totalsold+num;
            cookieInventory=0;
        }
        else {
            cookieInventory-=num;
            totalsold=totalsold+num;
        }
        gongersPocket+=(num*cookiePrice);
        System.out.println("Customer "+c+" bought "+num+" cookies");
        return num;
    }

    public Banker() {
        super("Gonger");
        gongersPocket = 0.0;
        cookieInventory = 0;
        numOfCookiePackages = 12;
    }

}

interface Stack{ /* STRATEGY PATTERN */
    public Shelves[] stack(Shelves s[]);
}
class BurtStack implements Stack{
    @Override
    public Shelves[] stack(Shelves s[]){
        int num_shelves = GameStore.numberOfShelves;
        for (int i = num_shelves - 1; i >= 0; i--)
        {
            int max_idx = i;
            for (int j = 0; j < i; j++){
                if (s[j].game.dimensions[1] > s[max_idx].game.dimensions[1]){
                    max_idx = j;
                }
            }
            Games temp = s[max_idx].game;
            s[max_idx].game = s[i].game;
            s[i].game = temp;
            Announcer a = new Announcer();
            a.announce("Burt stacks "+ s[i].game.inventory + " " + s[i].game.name + " games in shelf position " + i + " (pile width = "+ s[i].game.dimensions[1]+ "in)");
        }
        return s;
    }

}
class ErnieStack implements Stack{
    @Override
    public Shelves[] stack(Shelves s[]){
        for (int i = 0; i < GameStore.numberOfShelves; i++)
        {
            int min_idx = i;
            for (int j = i+1; j < GameStore.numberOfShelves; j++){
                if ((s[j].game.inventory*s[j].game.dimensions[2]) < (s[min_idx].game.inventory*s[min_idx].game.dimensions[2])){
                    min_idx = j;
                }
            }
            Games temp = s[min_idx].game;
            s[min_idx].game = s[i].game;
            s[i].game = temp;
            Announcer a = new Announcer();
            a.announce("Ernie stacks "+ s[i].game.inventory + " " + s[i].game.name + " games in shelf position " + i + " (pile height = "+ (s[i].game.inventory*s[i].game.dimensions[2])+ "in)");
        }
        return s;
    }
}
class BartStack implements Stack{
    @Override
    public Shelves[] stack(Shelves s[]){
        int num_shelves = GameStore.numberOfShelves;

        for (int i = num_shelves - 1; i >= 0; i--)
        {
            int max_idx = i;
            if(s[i].game.inventory <= 1){ // sort games with inventory > 1 in the back
                for(int j = num_shelves-1; j > i; j--){
                    if((s[j].game.dimensions[1] > s[max_idx].game.dimensions[1])){
                        if(s[j].game.inventory > 1 && s[max_idx].game.inventory > 1){
                            max_idx = j;
                        }
                    }
                }
                Games temp = s[max_idx].game;
                s[max_idx].game = s[i].game;
                s[i].game = temp;
            }
    
            else{
                for (int j = i+1; j < i; j++){
                    if ((s[j].game.dimensions[1] > s[max_idx].game.dimensions[1])){
                        if((s[j].game.inventory > 1) && (s[max_idx].game.inventory > 1)){
                            max_idx = j;
                        }
                    }
                }
                Games temp = s[max_idx].game;
                s[max_idx].game = s[i].game;
                s[i].game = temp;
            }

            Announcer a = new Announcer();
            a.announce("Bart stacks "+ s[i].game.inventory + " " + s[i].game.name + " games in shelf position " + i + " (pile width = "+ s[i].game.dimensions[1]+ "in)");
        }
        return s;
    }
}

class Shelves{
    Games game;
    int probability; // probability of buying from shelf position = 100/x
    public Shelves(Games g, int p){
        game = g;
        probability = p;
    }
}

class Games { 
    String name;
    String category;
    double price;
    int dimensions[] = new int[3]; // l x w x h
    int sold;
    int damaged;
    int inventory;
    Boolean delivered;
    int probability; // probability of adding to purchase -- 100/x
    int numAddedParts; // if customer wants to add to purchase, this is the max number special parts that will be added
    public Games(String n, String c, double p, int l, int w, int h, int prob, int num){
        name = n;
        category = c;
        price = p;
        dimensions = new int[]{l, w, h};
        sold = 0;
        damaged = 0;
        inventory = 3;
        delivered = false;
        probability = prob;
        numAddedParts = num;
    }
    public Games(Object category2, Object category3, double p, int l, int w, int h) {
    }
}

class CardGame extends Games{
    public CardGame(String n, double p, int l, int w, int h) {
        super(n, "Card", p, l, w, h, 5, 6);
    }
}

class FamilyGame extends Games{
    String category = "Family";
    public FamilyGame(String n, double p, int l, int w, int h, int prob, int add) {
        super(n, "Family", p, l, w, h, prob, add);
    }
}

class KidGame extends Games{
    public KidGame(String n, double p, int l, int w, int h, int prob, int add) {
        super(n, "Kids", p, l, w, h, prob, add);
    }
}

class BoardGame extends Games{
    public BoardGame(String n, double p, int l, int w, int h, int prob, int add) {
        super(n, "Board Game", p, l, w, h, prob, add);
    }
}

class Monopoly extends FamilyGame{
    public Monopoly() {
        super("Monopoly", 19.99, 15, 10, 2, 2, 1);
    }
}
class MouseTrap extends KidGame{
    public MouseTrap() {
        super("MouseTrap", 24.89, 16, 10, 4, 3, 2);
    }
}
class Gloomhaven extends BoardGame{
    public Gloomhaven() {
        super("Gloomhaven", 99.99, 16, 12, 8, 5, 4);
    }
    String name = "Gloomhaven";
}