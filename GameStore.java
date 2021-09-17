import java.util.ArrayList;
import java.util.*;


class GameStore{

    List<Game> shelves = new ArrayList<Game>();
    List<Game> damagedGames = new ArrayList<Game>();
    List<Game> deliveredGames = new ArrayList<Game>();
    List<Customer> activeCustomers = new ArrayList<Customer>();
    float cashRegister;
    int day;


}


class Employees extends GameStore {

    String cashierName;
    void arrive(){

    }
    void count(){

    }
    void vacuum(){
        
    }
    void stack(){

    }
    void order(){

    }
    void open(){

    }
    void close(){

    }


}

class Customer extends GameStore {

    void browseStore(){
        // customer does customer things 
    }


}

class Game extends GameStore { 

    String name;
    String category;
    float price;
    int dimensions[]; // l x w x h
    int sold;
    int shelfPosition;

}
