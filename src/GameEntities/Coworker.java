package GameEntities;

import java.util.ArrayList;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Brian
 */
public class Coworker {

    Random random = new Random();
    private String name;
    private int maxHP;
    private int health;
    private int stamina;
    private int ammo;
    private int maxAmmo = 24;
    private int iType;
    private int damage;
    private int hitChance;
    private int runChance;
    private String type;

    public Coworker() {

    }

    public String getName() {
        return name;
    }

    public String getType() {
        if (iType == 0) {
            type = "Scientist";
        } else {
            type = "Security Guard";
        }
        return type;
    }

    public int getHealth() {
        return health;
    }
    
    public int getMaxHP(){
        return maxHP;
    }

    public boolean hasAmmo() {
        if (ammo > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getDamage() {
        int modifiedDamage = damage;
        int roll = random.nextInt(100);
        if (roll > 80) {
            modifiedDamage += 1;
        } else if (roll <= 80 && roll > 70) {
            modifiedDamage -= 1;
        } else if (roll >= 0 && roll < 10) {
            modifiedDamage += 2;
        }

        if (!this.hasAmmo() || modifiedDamage < 1) {
            modifiedDamage = 1;
        }

        return modifiedDamage;
    }

    public int getHitChance() {
        if (this.ammo <= 0) {
            hitChance = 100;
        }
        return hitChance;
    }

    public int getRunChance() {
        return runChance;
    }

    public void adjustHealth(int value) {
        health += value;
    }
    
    public void setHealth (int value){
        health = value;
    }

    public int getStamina() {
        return stamina;
    }

    public void adjustStamina(int value) {
        stamina += value;
    }

    public int getAmmo() {
        return ammo;
    }

    public void adjustAmmo(int value) {
        if ((value + ammo) <= maxAmmo) {
        ammo += value;
        } else {
            ammo = maxAmmo;
        }
        
    }
    
     public int getMaxAmmo(){
        return maxAmmo;
    }
    
    public void setAmmo(int value){
        ammo = value;
    }

    public boolean isDead() {
        if (health > 0) {
            return false;
        } else {
            return true;
        }
    }
      public boolean isScientist() {
        if (iType == 0){
            return true;
        } else {
            return false;
        }
    }

    public boolean doesAttack() {
        int roll = random.nextInt(99) + 1;
        if (roll < hitChance) {
            return true;
        } else {
            return false;
        }
    }

    public Coworker(String name) {
        this.name = name;
        health = 100;
        stamina = 100;
        ammo = random.nextInt(6) + 1;
        int roll = random.nextInt(99) + 1;
        if (roll < 51) {
            iType = 0;
        } else {
            iType = 1;
        }
        hitChance = random.nextInt(99) + 1;
        damage = random.nextInt(5) + 1;
        runChance = random.nextInt(49) + 1;
        System.out.println(this.getName() + " is a " + this.getType() + ". Base damage: " + damage + ". Hit chance: " + hitChance + ". Run chance: " + runChance + ".");
    }

    public Coworker(String name, int iType) {
        this.name = name;
        if (iType == 0) {
            health = 100;
            stamina = 100;
            ammo = 0;
            damage = 2;
            hitChance = 50;
            runChance = 40;
            this.iType = 0;
        } else {
            health = 100;
            stamina = 100;
            ammo = 24;
            damage = 4;
            runChance = 20;
            hitChance = 75;
            this.iType = 1;
        }

    }

}
