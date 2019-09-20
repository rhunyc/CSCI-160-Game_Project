/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import GameEntities.Coworker;
import GameEntities.Enemy;
import GameEntities.Player;
import HelperClasses.DescriptionHelper;
import HelperClasses.MenuHelper;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Brian
 */
public class Game {

    Scanner scan = new Scanner(System.in);
    Coworker coworker;
    Player player;
    Random random = new Random();

    DecimalFormat timeFormat = new DecimalFormat("00.00");
    private int floorsToSurface = 10;
    private int day = 1;
    private int radiationLevel = 1;
    final int RADIATIONLEVELMAX = 100;
    private int difficultyModifier = 1;
    private String room;
    private String radiationOutput;
    private double time = 06;
    private boolean roomClear = true;
    private ArrayList<Enemy> enemyList;
    private boolean canRun = false;
    private boolean bothAlive = true;
    private boolean cameFromRun = false;
    private boolean brokenElevator = false;

    int score = 0;
    int searchedCounter = 0;
    int enemiesDefeated = 0;
    int enemiesEncountered = 0;
    int totalBulletsFired = 0;
    int roomsTraveled = 0;
    int floorsTraveled = 0;

    public Game(int difficulty, Coworker coworker, Player player) {
        this.difficultyModifier = difficulty;
        this.coworker = coworker;
        this.player = player;

        enemyList = new ArrayList<Enemy>();

    }

    public int getDifficultyModifier() {
        return difficultyModifier;
    }

    public void setDifficultyModifier(int value) {
        difficultyModifier = value;
    }

    private String listEnemies() {
        if (enemyList.isEmpty()) {
            return "None.";
        }
        String enemies = "";

        for (Enemy enemy : enemyList) {
            enemies += enemy.getName() + " (" + enemy.getHealth() + " hp left) ";
        }
        return enemies;
    }

    private void getNewRoom() {
        int roll = random.nextInt(15);
        switch (roll) {
            case 0:
            case 1:
                room = "Janitor's Closet";
                break;

            case 2:
            case 3:
                room = "Hallway";
                break;

            case 4:
            case 5:
            case 6:
                room = "Office";
                break;

            case 7:
            case 8:
            case 9:
                room = "Testing Lab";
                break;

            case 10:
                room = "Elevator";
                break;

            case 11:
            case 12:
                room = "Breakroom";
                break;

            case 13:
                room = "Medbay";
                break;

            case 14:
                room = "Security Room";
                break;

            case 15:
                room = "Waiting Room";
                break;

        }
    }

    private void getNewRoomEnemies() {
        int roll = random.nextInt(16);
        if (roll == 9) {
            Enemy headCrab1 = new Enemy("Headcrab 1", 5, 1, 50, 25);
            Enemy headCrab2 = new Enemy("Headcrab 2", 5, 1, 50, 25);
            Enemy soldier = new Enemy("Soldier", 10, 3, 70, 50);
            enemyList.add(soldier);
            enemyList.add(headCrab1);
            enemyList.add(headCrab2);
            roomClear = false;
            System.out.println("Much to your dismay, you find a couple headcrabs and a soldier in the room!");
        } else if (roll == 8) {
            Enemy headCrab1 = new Enemy("Headcrab 1", 5, 1, 50, 25);
            Enemy headCrab2 = new Enemy("Headcrab 2", 5, 1, 50, 25);
            enemyList.add(headCrab2);
            enemyList.add(headCrab1);
            roomClear = false;
            System.out.println("Uh oh, you've encountered two headcrabs!");
        } else if (roll == 7) {
            Enemy headCrab = new Enemy("Headcrab", 5, 1, 50, 25);
            enemyList.add(headCrab);
            roomClear = false;
            System.out.println("You are not alone, there is a headcrab here.");
        } else if (roll == 6) {
            Enemy soldier = new Enemy("Soldier", 10, 3, 70, 50);
            enemyList.add(soldier);
            roomClear = false;
            System.out.println("You hear the radio chatter and realize that there is also an enemy soldier in the room.");
        } else if (roll == 1) {
            Enemy soldier = new Enemy("Soldier 1", 10, 3, 70, 50);
            enemyList.add(soldier);
            Enemy soldier1 = new Enemy("Soldier 2", 10, 3, 70, 50);
            enemyList.add(soldier1);
            roomClear = false;
            System.out.println("As you enter the room, you see two soldiers in the room with you as well.");
        }

    }

    private String getRadiationLevels() {
        if (radiationLevel >= 80) {
            radiationOutput = "Hazardous radiation levels detected. Please evacuate immediately.";
        } else if (radiationLevel < 80 && radiationLevel >= 40) {
            radiationOutput = "Moderate radiation levels detected. Use caution.";
        } else {
            radiationOutput = "Minimum radiation levels detected.";
        }
        return radiationOutput;
    }

    private void addRadiation(int value) {
        if (difficultyModifier == 2) {
            radiationLevel += 3;
        } else if (difficultyModifier == 3) {
            radiationLevel += 5;
        }

        radiationLevel += value;
        if (radiationLevel >= 100) {
            this.processLossDueToRadiation();
        }
    }

    private double getNewTime() {
        time += 1;
        if (time > 24) {
            double leftOverTime = time - 24;
            time = 0 + leftOverTime;
            day += 1;
        }

        return time;
    }

    private void outputScoreInformation() {
        System.out.println("-------------Stats-------------");
        System.out.println("Rooms traveled:            " + roomsTraveled);
        System.out.println("Floors traveled:           " + floorsTraveled);
        System.out.println("Total enemies encountered: " + enemiesEncountered);
        System.out.println("Total enemies defeated:    " + enemiesDefeated);
        System.out.println("Total bullets fired:       " + totalBulletsFired);
        System.out.println("Total Score:               " + (score + (difficultyModifier * 50)));
    }

    private void outputTurnInformation() {
        System.out.println("\nDay:   " + day + "            Floors to surface: " + floorsToSurface + "    Radiation levels: " + this.getRadiationLevels());
        System.out.println("Time: " + timeFormat.format(time) + "         Location: " + room + "   Enemies present: " + this.listEnemies());
        System.out.print(DescriptionHelper.getStatus(player));
        System.out.print(DescriptionHelper.getStatus(coworker));
    }

    private void processRoomElevator() {
        if (roomClear) {
            int menuChoice = MenuHelper.displayMenu(" 1)Rest\n 2) Ride the elevator\n 3)Move stealthily\n 4)Move normally\n 5)Run\n", 1, 6);
            switch (menuChoice) {
                case 1:
                    this.processRest();
                    break;
                case 2:
                    this.processElevator();
                    break;
                case 3:
                    this.processMoveStealthily();
                    break;
                case 4:
                    this.processMoveNormally();
                    break;
                case 5:
                    this.processRun();
                    break;

            }
        } else {
            int menuChoice = MenuHelper.displayMenu(" 1) Fight\n 2) Run", 1, 2);
            switch (menuChoice) {
                case 1:
                    this.processFight();
                    break;
                case 2:
                    int roll = random.nextInt(10);
                    if (roll > 7) {
                        this.processRun();
                        System.out.println("You successfully managed to escape.");
                    } else {
                        System.out.println("You were unable to escape.");
                        cameFromRun = true;
                        this.processFight();
                    }

            }
        }

    }

    private void processRoomClearNormal() {
        int menuChoice = MenuHelper.displayMenu(" 1) Rest\n 2) Search for supplies\n 3) Move stealthily\n 4) Move normally\n 5) Run\n", 1, 5);
        switch (menuChoice) {
            case 1:
                this.processRest();
                break;
            case 2:
                this.processSuppliesSearch();
                break;
            case 3:
                brokenElevator = false;
                this.processMoveStealthily();
                break;
            case 4:
                brokenElevator = false;
                this.processMoveNormally();
                break;
            case 5:
                brokenElevator = false;
                this.processRun();
                break;

        }
    }

    private void processSuppliesSearch() {
        this.getNewTime();
        ++searchedCounter;
        int roll = random.nextInt(101) + 1;
        if (roll > 90) {
            System.out.println("You found a MedKit Station!");
            if (player.getHealth() < player.getMaxHP()) {
                player.adjustHealth(50);
                System.out.println(player.getType() + " " + player.getName() + " was able to restore a good portion of health!");
                if (player.getHealth() > player.getMaxHP()) {
                    player.setHealth(player.getMaxHP());
                }
            } else {
                System.out.println(player.getType() + " " + player.getName() + " was at full health and did not need to heal.");
            }
            if (coworker.getHealth() < coworker.getMaxHP()) {
                coworker.adjustHealth(50);
                System.out.println(coworker.getType() + " " + coworker.getName() + " was able to restore a good portion of health!");
                if (coworker.getHealth() > coworker.getMaxHP()) {
                    coworker.setHealth(player.getMaxHP());
                }
            } else {
                System.out.println(coworker.getType() + " " + coworker.getName() + " was at full health and did not need to heal.");
            }

        } else if (roll <= 90 && roll > 60) {
            System.out.println("You found a MedKit!");
            if (player.getHealth() < player.getMaxHP()) {
                player.adjustHealth(25);
                System.out.println(player.getType() + " " + player.getName() + " was able to restore some health!");
                if (player.getHealth() > player.getMaxHP()) {
                    player.setHealth(player.getMaxHP());
                }
            } else {
                System.out.println(player.getType() + " " + player.getName() + " was at full health and did not need to heal.");
            }
            if (coworker.getHealth() < coworker.getMaxHP()) {
                coworker.adjustHealth(25);
                System.out.println(coworker.getType() + " " + coworker.getName() + " was able to restore some health!");
                if (coworker.getHealth() > coworker.getMaxHP()) {
                    coworker.setHealth(player.getMaxHP());
                }
            } else {
                System.out.println(coworker.getType() + " " + coworker.getName() + " was at full health and did not need to heal.");
            }

        } else if (roll <= 60 && roll > 30) {
            System.out.println("You found some bullets!");
            if (player.getAmmo() < player.getMaxAmmo()) {
                player.adjustAmmo(6);
                System.out.println(player.getType() + " " + player.getName() + " has replenished some ammo!");
            } else {
                System.out.println(player.getType() + " " + player.getName() + " can't carry any more ammo.");
            }
            if (coworker.getAmmo() < coworker.getMaxAmmo()) {
                coworker.adjustAmmo(6);
                System.out.println(coworker.getType() + " " + coworker.getName() + " has replenished some ammo!");
            } else {
                System.out.println(coworker.getType() + " " + coworker.getName() + " can't carry any more ammo.");
            }
        } else if (roll <= 30 && roll > 20) {
            System.out.println("You found a box of bullets!");
            if (player.getAmmo() < player.getMaxAmmo()) {
                player.adjustAmmo(12);
                System.out.println(player.getType() + " " + player.getName() + " has replenished some ammo!");
            } else {
                System.out.println(player.getType() + " " + player.getName() + " can't carry any more ammo.");
            }
            if (coworker.getAmmo() < coworker.getMaxAmmo()) {
                coworker.adjustAmmo(12);
                System.out.println(coworker.getType() + " " + coworker.getName() + " has replenished some ammo!");
            } else {
                System.out.println(coworker.getType() + " " + coworker.getName() + " can't carry any more ammo.");
            }
        } else {
            System.out.println("You searched but were unable to find any supplies.");
        }

        this.processRoomHasSearched();

    }

    private void processRoomHasSearched() {
        this.outputTurnInformation();
        int menuChoice = MenuHelper.displayMenu(" 1) Rest\n 2) Move stealthily\n 3) Move normally\n 4) Run\n", 1, 4);
        switch (menuChoice) {
            case 1:
                this.processRest();
                break;
            case 2:
                brokenElevator = false;
                this.processMoveStealthily();
                break;
            case 3:
                brokenElevator = false;
                this.processMoveNormally();
                break;
            case 4:
                brokenElevator = false;
                this.processRun();
                break;

        }
    }

    private void processRoomWithNoStaminaAndEnemies() {
        int menuChoice = MenuHelper.displayMenu(" 1) Rest\n", 1, 1);
        switch (menuChoice) {
            case 1:
                cameFromRun = true;
                this.processRest();
                break;
        }
    }

    private void processRoomWithNoStamina() {
        int menuChoice = MenuHelper.displayMenu(" 1) Rest\n", 1, 1);
        switch (menuChoice) {
            case 1:
                this.processRest();
                break;
        }
    }

    private void processRoomWithEnemies() {
        enemiesEncountered += enemyList.size();
        int menuChoice = MenuHelper.displayMenu(" 1) Fight\n 2) Run", 1, 2);
        switch (menuChoice) {
            case 1:
                this.processFight();
                break;
            case 2:
                int roll = random.nextInt(10);
                if (roll > 7) {
                    this.processRun();
                    System.out.println("You successfully managed to escape.");
                } else {
                    System.out.println("You were unable to escape.");
                    cameFromRun = true;
                    this.processFight();
                }

        }
    }

    private void processTurn() {
        if ("Elevator".equals(room) && !brokenElevator) {
            this.processRoomElevator();
        } else if (coworker.getStamina() > 0 && player.getStamina() > 0) {
            if (roomClear) {
                this.processRoomClearNormal();
            } else {
                this.processRoomWithEnemies();
            }

        } else if (coworker.getStamina() <= 0 || player.getStamina() <= 0) {
            if (roomClear) {
                this.processRoomWithNoStamina();
            } else {
                this.processRoomWithNoStaminaAndEnemies();
            }
        }
    }

    private void processElevator() {
        floorsToSurface -= random.nextInt(2) + 1;
        this.addRadiation(-100);

        if (player.isScientist() && coworker.isScientist()) {
            int roll = random.nextInt(99) + 1;

            if (roll > 50) {
                System.out.println("Using both of your credentials, you were able to go 5 floors higher than normal.");
                floorsToSurface -= 5;
                this.addRadiation(-400);
            } else if (roll <= 50 && roll > 30) {
                System.out.println("Using both of your credentials, you were able to go 4 levels higher than normal.");
                floorsToSurface -= 4;
                this.addRadiation(-400);
            }

        } else if (player.isScientist() || coworker.isScientist()) {
            int roll = random.nextInt(99) + 1;
            if (roll > 50) {
                floorsToSurface -= 3;
                this.addRadiation(-200);
                System.out.println("Since one of you are a scientist, their credentials allowed you to go 3 floors higher than normal.");
            } else if (roll <= 50 && roll > 30) {
                this.addRadiation(-100);
                System.out.println("Since one of you are a scientist, their credentials allowed you to go 2 floors higher than normal.");
                floorsToSurface -= 2;
            }

            brokenElevator = true;
            processRoomHasSearched();
            if (floorsToSurface <= 0) {
                this.processVictory();
            }
        }
    }

    private void processRest() {
        if (player.getStamina() < 100) {
            player.adjustStamina(50);
        }
        if (coworker.getStamina() < 100) {
            coworker.adjustStamina(50);
        }
        time += 3;
        this.getNewTime();
        this.addRadiation(20);
        System.out.println("Both you and " + coworker.getName() + " feel refreshed.");
        if (!roomClear) {
            cameFromRun = true;
            this.processFight();
        }
    }

    private void processFight() {
        do {
            int rollForPlayer;
            int enemyChosen;
            int bulletsFired;
            if (!cameFromRun) {
                if (player.doesAttack() == true) {
                    enemyChosen = random.nextInt(enemyList.size());
                    bulletsFired = random.nextInt(2) + 1;
                    enemyList.get(enemyChosen).adjustHealth(-player.getDamage());
                    if (player.hasAmmo()) {
                        if (bulletsFired > player.getAmmo()) {
                            bulletsFired = player.getAmmo();
                        }
                        totalBulletsFired += bulletsFired;
                        player.adjustAmmo(-bulletsFired);
                        System.out.println(player.getType() + " " + player.getName() + " expends " + bulletsFired + " ammo and does " + player.getDamage() + " damage to " + enemyList.get(enemyChosen).getName());
                    } else {
                        if (player.getName().equals("Gordon Freeman")) {
                            System.out.println(player.getType() + " " + player.getName() + " hits " + enemyList.get(enemyChosen).getName() + " with a crowbar and deals " + player.getDamage() + " damage.");
                        } else {
                            System.out.println(player.getType() + " " + player.getName() + " hits " + enemyList.get(enemyChosen).getName() + " with a melee attack and deals " + player.getDamage() + " damage.");
                        }

                    }
                    if (enemyList.get(enemyChosen).getHealth() <= 0) {
                        System.out.println(player.getType() + " " + player.getName() + " has defeated " + enemyList.get(enemyChosen).getName());
                        score += enemyList.get(enemyChosen).getPoints();
                        enemyList.remove(enemyChosen);
                    }
                } else {
                    enemyChosen = random.nextInt(enemyList.size());
                    if (player.hasAmmo()) {
                        bulletsFired = random.nextInt(2) + 1;
                        if (bulletsFired > player.getAmmo()) {
                            bulletsFired = player.getAmmo();
                        }
                        totalBulletsFired += bulletsFired;
                        player.adjustAmmo(-bulletsFired);
                        System.out.println(player.getType() + " " + player.getName() + " expends " + bulletsFired + " ammo and misses " + enemyList.get(enemyChosen).getName() + "!");
                    } else {
                        System.out.println(enemyList.get(enemyChosen).getName() + " dodges " + player.getType() + " " + player.getName() + "'s attack!");
                    }
                }

                if (coworker.doesAttack() && !enemyList.isEmpty()) {
                    bulletsFired = random.nextInt(2) + 1;

                    enemyChosen = random.nextInt(enemyList.size());
                    enemyList.get(enemyChosen).adjustHealth(-coworker.getDamage());
                    if (coworker.hasAmmo()) {
                        if (bulletsFired > coworker.getAmmo()) {
                            bulletsFired = coworker.getAmmo();
                        }
                        coworker.adjustAmmo(-bulletsFired);
                        System.out.println(coworker.getType() + " " + coworker.getName() + " expends " + bulletsFired + " ammo and does " + coworker.getDamage() + " damage to " + enemyList.get(enemyChosen).getName());
                    } else {
                        System.out.println(coworker.getType() + " " + coworker.getName() + " hits " + enemyList.get(enemyChosen).getName() + " with a melee attack and does " + coworker.getDamage() + " damage.");
                    }
                    if (enemyList.get(enemyChosen).getHealth() <= 0) {
                        System.out.println(coworker.getType() + " " + coworker.getName() + " has defeated " + enemyList.get(enemyChosen).getName());
                        score += enemyList.get(enemyChosen).getPoints();
                        enemyList.remove(enemyChosen);
                    }
                } else if (!coworker.doesAttack() && !enemyList.isEmpty()) {
                    enemyChosen = random.nextInt(enemyList.size());
                    if (coworker.hasAmmo()) {
                        bulletsFired = random.nextInt(2) + 1;
                        if (bulletsFired > coworker.getAmmo()) {
                            bulletsFired = coworker.getAmmo();
                        }
                        coworker.adjustAmmo(-bulletsFired);
                        System.out.println(coworker.getType() + " " + coworker.getName() + " expends " + bulletsFired + " ammo and misses " + enemyList.get(enemyChosen).getName() + "!");
                    } else {
                        System.out.println(enemyList.get(enemyChosen).getName() + " dodges " + coworker.getType() + " " + coworker.getName() + "'s attack!");
                    }
                }
            }
            for (int i = 0; i < enemyList.size(); i++) {
                rollForPlayer = random.nextInt(100) + 1;
                Enemy current = enemyList.get(i);
                if (current.doesAttack()) {
                    if (rollForPlayer > 75 && rollForPlayer < 25) {
                        player.adjustHealth(-current.getDamage());
                        System.out.println(current.getName() + " hit " + player.getType() + " " + player.getName() + " for " + current.getDamage() + " damage!");
                    } else {
                        coworker.adjustHealth(-current.getDamage());
                        System.out.println(current.getName() + " hit " + coworker.getType() + " " + coworker.getName() + " for " + current.getDamage() + " damage!");
                    }

                } else {
                    if (rollForPlayer > 75 && rollForPlayer < 25) {
                        System.out.println(player.getType() + " " + player.getName() + " dodges " + current.getName() + "'s attack!");
                    } else {
                        System.out.println(coworker.getType() + " " + coworker.getName() + " dodges " + current.getName() + "'s attack!");

                    }
                }

            }
            cameFromRun = false;
            player.adjustStamina(-5);
            coworker.adjustStamina(-5);
            if (!enemyList.isEmpty()) {
                System.out.println("");
                System.out.println("Enemies: " + this.listEnemies());
                System.out.println(DescriptionHelper.getStatus(player) + DescriptionHelper.getStatus(coworker));
                if (player.getStamina() > 0 && coworker.getStamina() > 0) {
                    int option = MenuHelper.displayMenu("Select a choice.\n 1)Fight\n 2)Run", 1, 2);
                    if (option == 2) {
                        cameFromRun = true;
                        int roll = random.nextInt(100);
                        if (roll < (player.getRunChance() + coworker.getRunChance())) {
                            System.out.println("You successfully escaped");
                            canRun = true;
                            cameFromRun = false;
                            this.processRun();
                        } else {
                            System.out.println("You were unable to escape!");
                        }
                    }
                } else {
                    processRoomWithNoStaminaAndEnemies();
                }
            }
        } while (player.getHealth() > 0 && !enemyList.isEmpty() && !canRun);

        enemyList.clear();
        canRun = false;
        roomClear = true;
    }

    private void processMoveStealthily() {
        this.getNewTime();
        this.getNewRoom();
        roomsTraveled++;
        System.out.println("You both manage to safely sneak to the next room.");
        this.addRadiation(7);
        time += 2;

    }

    private void processMoveNormally() {
        this.getNewTime();
        this.getNewRoom();
        roomsTraveled++;
        System.out.println("You each decide to move to the next room.");
        this.getNewRoomEnemies();
        player.adjustStamina(-10);
        coworker.adjustStamina(-10);
        time += 1;
        this.addRadiation(5);
    }

    private void processRun() {
        this.getNewTime();
        this.getNewRoom();
        roomsTraveled++;
        System.out.println("You both run to the next room.");
        this.getNewRoomEnemies();
        player.adjustStamina(-30);
        coworker.adjustStamina(-30);
        this.addRadiation(1);
    }

    private void processLossDueToRadiation() {
        System.out.println("You and your coworker have died due to radiation poisoning. Game over.");
        outputScoreInformation();
        System.out.println("Please close the window or type exit to leave.");
        System.out.print("> ");
        String input = scan.nextLine();
        if (input.startsWith("exit")) {
            System.exit(0);
        }
    }

    private void processVictory() {
        System.out.println("You and your coworker have made it to the surface in one piece. Congratulations.");
        outputScoreInformation();
        System.out.println("Please close the window or type exit to leave.");
        System.out.print("> ");
        String input = scan.nextLine();
        if (input.startsWith("exit")) {
            System.exit(0);
        }
    }

    public void start() {
        this.room = "Waiting Room";
        this.roomClear = true;
        while (0 < player.getHealth() && radiationLevel < 100) {
            this.outputTurnInformation();
            this.processTurn();
        }
        System.out.println("You lose.");
    }
}
