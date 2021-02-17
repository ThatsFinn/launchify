package me.finn.launchify.utils;

import me.finn.launchify.game.DeathReason;

import java.util.ArrayList;
import java.util.Random;

public class MessagesUtil {

    public ArrayList<String> plm = new ArrayList<>();
    public ArrayList<String> vom = new ArrayList<>();
    public ArrayList<String> vkm = new ArrayList<>();

    public MessagesUtil() {
        // PLAYER MESSAGES
        plm.add(" was killed by ");
        plm.add(" has been killed by ");
        plm.add(" was owned by ");
        plm.add(" met their end to ");
        plm.add(" was doomed by ");
        plm.add(" got trickshotted on by ");
        plm.add(" lost a fight to ");
        plm.add(" was sniped by ");
        plm.add(" was powerless against ");
        plm.add(" took the L to ");
        plm.add(" got nani'd by ");
        plm.add(" got bad'd by ");
        plm.add(" got thunderstruck by ");
        plm.add(" got hit or misssed by ");
        plm.add(" got outbid by ");
        plm.add(" lost a bet to ");
        plm.add(" was sent into the shadow realm by ");
        plm.add(" got mlg 360 triggle inclined jitter bridge no scoped by ");
        plm.add(" got deported by ");
        plm.add(" got dora the explorer'd by ");
        plm.add(" got maccas double whopper premium deluxe edition'd 50% off t's, c's and fees apply made by red rooster sponsored by kfc spoken by Victoria Banks'd by ");

        // VOID MESSAGES
        vom.add(" slipped off the edge");
        vom.add(" fell into the void");
        vom.add(" forgot to press shift");
        vom.add(" under-estimated the launcher");
        vom.add(" was welcomed by the void");
        vom.add(" didn't have their inner health plus today!");
        vom.add(" thought they could fly..");
        vom.add(" missed a parkour jump");
        vom.add(" was sent into the shadow realm.");
        vom.add(" got pushed onto the spherical side of the northern pole located at the left side of a pink elephant weighing in at about 20 papers.");

        // VOID KNOCK MESSAGES
        vkm.add(" was hit into the void by ");
        vkm.add(" got knocked into the void by ");
    }

    public String getRandomMessage(DeathReason reason) {
        Integer index;
        Random rand = new Random();
        if (reason == DeathReason.BOW) {
            index = rand.nextInt(plm.size());
            return plm.get(index);
        }
        if (reason == DeathReason.VOID) {
            index = rand.nextInt(vom.size());
            return vom.get(index);
        }
        if (reason == DeathReason.KNOCK_VOID) {
            index = rand.nextInt(vkm.size());
            return vkm.get(index);
        }
        return null;
    }

}
