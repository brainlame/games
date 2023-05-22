package Games;

import java.util.*;
public class Explorer {
    //boss mob
    //magical artifacts * special abilities
    private static int attack;
    private static int health;
    private static int speed;
    private static int defensePercent;
    private static ArrayList<String> weapons = new ArrayList<>();
    private static ArrayList<String> armors = new ArrayList<>();
    private static int e_attack;
    private static int e_health;
    private static int e_speed;
    private static HashMap<String,Integer> potions = new HashMap<>();
    private static ArrayList<String> potionEffects = new ArrayList<>();
    private static int fightsWon;
    private static int turnNum;

    static void welcome() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n\n\n\n\nWELCOME to your amazing adventure, soldier.\nIn your adventure, enemies will come at you from all directions.\nBe prepared to choose to either fight your enemy head on or run away.\nAcquiring potions and weapons will assist you along your path.\nGood luck.\n");
        System.out.println("~~~~~Choose a base kit~~~~~: \n1 - warrior\n2 - tank\n3 - archer\n4 - glass cannon\n5 - sumo\n6 - track star");
        int kit = sc.nextInt();
        initializeSpecialItems(kit);
        setBaseStats(kit);
        delay();
    }
    static void initializeSpecialItems(int kit) {
        //potions
        potions.put("health",0);
        potions.put("speed",0);
        potions.put("strength",0);

        //weapons
        weapons.add("Wooden");
        weapons.add("Stone");
        weapons.add("Iron");
        weapons.add("Diamond");

        //armor
        armors.add("Leather");
        armors.add("Iron");
        armors.add("Diamond");
    }
    static void printHealth() {
        System.out.println("Your Health: "+health+"\nEnemy Health: "+e_health);
    }
    static void setBaseStats(int kit) {
        kit--;
        // Warrior, Tank, Archer, Glass, Sumo, Runner
        int[] attacks = {100, 50   ,200,400,200 ,50};
        int[] healths = {1000,10000,500,100,2000,200};
        int[] speeds =  {300, 50   ,150,200,-999,1000};
        attack = attacks[kit];
        health = healths[kit];
        speed = speeds[kit];
        System.out.println("Your Attack: "+attack+"\nYour Health: "+health+"\nYour Speed: "+speed);
    }
    static String encounterEnemy() {
        //enemy attribute lists
        String[] enemies = {"Lvl 1 Crook","Fat Boy","Skinny Powerlifter","Bodybuilder","Usain Bolt","Glass Cannon","Free Loot Delivery"};
        int[] e_attacks = {100,50  ,300,150,150 ,500,10};
        int[] e_healths = {300,1000,150,750,300 ,100,10};
        int[] e_speeds =  {200,20  ,100,50 ,1000,100,10};

        //init enemy stats
        int random = (int)(Math.random()*7);
        String enemy = enemies[random];
        e_attack = e_attacks[random];
        e_health = e_healths[random];
        e_speed = e_speeds[random];
        
        System.out.println("You encounter "+enemy+"!\nAttack: "+e_attack+"\nHealth: "+e_health+"\nSpeed: "+e_speed);
        delay();
        return enemy;
    }
    static void delay() {
        System.out.println();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
    static void performAction(int action) {
        System.out.println();
        switch (action) {
            case 1: fight(); break;
            case 2: run(); break;
            case 3: drinkPotion(); break;
            case 4:
                System.out.println("---YOUR STATS---\nYour Attack: "+attack+"\nYour Health: "+health+"\nYour Speed: "+speed+"\nYour Damage Reduction %: "+defensePercent);
                for (String effect : potionEffects) {
                    System.out.println("*** "+effect + " potion ***");
                }
                delay();
                break;
            default:
                System.out.println("Not a valid option.");
        }
    }
    static void fight() {
        System.out.println("\033[31mFIGHTING... \033[0m");
        delay();
        int rounds = 0;
        while (health > 0) {
            printHealth();
            delay();
            rounds++;
            
            //user hit
            System.out.println("You dealt "+attack+" damage!");
            e_health -= attack;
            if (e_health <= 0) break; //enemy died
            
            //enemy hit
            int e_damage = (int)(e_attack * (1 - (double)defensePercent / 100)); //calculate damage received
            System.out.println("Enemy dealt "+e_damage+" damage!");
            health -= e_damage;
            if (health <= 0) return; //you died
        }
        System.out.println("\n\033[32mEnemy defeated! \033[0m("+rounds+" round(s))");
        fightsWon++;

        //remove strength effect
        if (potionEffects.contains("strength")) {
            attack /= 2;
            potionEffects.remove("strength");
        }
        delay();
        getLoot();
    }
    static void run() {
        int lostSpeed = e_speed / 10;
        if (speed > e_speed) {
            System.out.println("You ran away successfully! You lost "+lostSpeed+" speed due to fatigue.");
        } else {
            System.out.println("You weren't able to get away, losing "+lostSpeed+" speed in the process.");
            fight();
        }
        speed -= lostSpeed;
        if (potionEffects.contains("speed")) {
            speed /= 2;
            potionEffects.remove("speed");
        }

    }
    static void drinkPotion() {
        Scanner sc = new Scanner(System.in);
        System.out.println("---YOUR POTIONS---\n1 - Health Potion   (Quantity: "+potions.get("health")+") (+200 Health)");
        System.out.println("2 - Speed Potion    (Quantity: "+potions.get("speed")+") (x2 speed, one use)");
        System.out.println("3 - Strength Potion (Quantity: "+potions.get("strength")+") (x2 attack, one use)");
        System.out.println("4 - Exit Potion Menu");
        switch(sc.nextInt()) {
            case 1: //health
                if (potions.get("health") > 0) {
                    potions.put("health",potions.get("health")-1);
                    System.out.println("Regenerated 200 health!");
                    health += 200;
                } else {
                    System.out.println("No health potions in stock.");
                    drinkPotion();
                }
            break;
            case 2: //speed
                if (potions.get("speed") > 0) {
                    if (!potionEffects.contains("speed")) {
                        potions.put("speed",potions.get("speed")-1);
                        System.out.println("Speed doubled for your next escape!");
                        potionEffects.add("speed");
                        speed *= 2;
                    } else {
                        System.out.println("Speed potion already activated!");
                        drinkPotion();
                    }
                } else {
                    System.out.println("No speed potions in stock.");
                    drinkPotion();
                }
            break;
            case 3: //strength
                if (potions.get("strength") > 0) {
                    if (!potionEffects.contains("strength")) {
                        potions.put("strength",potions.get("strength")-1);
                        System.out.println("Attack doubled for the next fight!");
                        potionEffects.add("strength");
                        attack *= 2;
                    } else {
                        System.out.println("Strength potion already activated.");
                        drinkPotion();
                    }
                } else {
                    System.out.println("No strength potions in stock.");
                    drinkPotion();
                }
                break;
            case 4:
                return;
            default:
                System.out.println("Not a valid potion.");
                drinkPotion();
        }
        delay();
    }
    static String upgradeWeapon() {
        String weapon = "";
        if (weapons.size() > 0) {
            weapon = weapons.get(0) + " Sword";
            weapons.remove(0);
            attack *= 1.2;
        } else {
            weapon = "Max weapon already obtained! +100 Health";
            health += 100;
        }
        return weapon;
    }
    static String upgradeArmor() {
        String armor = "";
        if (armors.size() > 0) {
            armor = armors.get(0) + " Armor";
            armors.remove(0);
            defensePercent += 25;
        } else {
            armor = "Max armor obtained! +100 Health";
            health += 100;
        }
        return armor;
    }
    static void getLoot() {
        int item = (int)(Math.random()*7+1);
        switch (item) {
            case 1:case 2:case 3: //health potion
                System.out.println("You received a \033[92mhealth\033[0m potion! (+200 Health when used)");
                potions.put("health",potions.get("health")+1);
                break;
            case 4:
                System.out.println("You received a one-time \033[36mspeed\033[0m potion! (x2 Speed when used)");
                potions.put("speed",potions.get("speed")+1);
                break;
            case 5:
                System.out.println("You received a one-time \033[91mstrength\033[0m potion! (x2 Attack when used)");
                potions.put("strength",potions.get("strength")+1);
                break;
            case 6:
                System.out.println("You received a \033[35mweapon upgrade!\033[0m ("+upgradeWeapon()+")");
                break;
            case 7:
                System.out.println("You received an \033[35marmor upgrade!\033[0m ("+upgradeArmor()+")");
                break;
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        welcome();
        
        while (health > 0) {
            System.out.println("~~~~~TYPE ANYTHING TO PROCEED~~~~~");
            sc.next();
            turnNum++;

            //encounter new enemy
            String enemy = encounterEnemy();

            //user action
            int action;
            do {
                System.out.println("Would you like to:\n1 - Fight "+enemy+"\n2 - Run\n3 - Drink Potions\n4 - See Your Stats");
                action = sc.nextInt();
                performAction(action);
            } while (action != 1 && action != 2); // while player hasn't fought or ran
            delay();
        }
        //when no health
        System.out.println("\033[31m~~~~~YOU DIED~~~~~\033[0m");
        System.out.println("Enemies encountered: "+turnNum);
        System.out.println("Fights won: "+fightsWon);
    }
}