package me.borawski.arena.user;

/**
 * Created by Ethan on 7/27/2017.
 */
public enum ArenaStat {

    LAST_LOGIN("last_login"),
    KILLS("kills"),
    DEATHS("deaths"),
    HIGHEST("killstreak");

    String name;

    ArenaStat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
