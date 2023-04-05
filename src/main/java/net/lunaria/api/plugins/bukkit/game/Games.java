package net.lunaria.api.plugins.bukkit.game;

import lombok.Getter;
import net.lunaria.api.plugins.bukkit.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Games {
    COOKIE("§6§lCOOKIE", "cookie",
            new ItemBuilder(Material.COOKIE).setName("§f§k|§e§k|§r §6§lCOOKIE §e§k|§f§k|").setLore(
                    " ", "§e§l❘ §fType: §aMulti§f,§6 Combat", "§e§l❘ §fMode(s): §aSolo§f, §eDuo§f, §2Trio§f, §3Squad", " ", "§6§l❘ §fConcept:", "§f  A rédiger les gars ok ?", " ", "§e§l❘ §60 joueur(s)§f en jeu", "§e§l❘ §cAucun booster lancé", " ","§6» §fClic gauche pour jouer"
                    )
                    .toItemStack()
    ),
    VAMPIRE("§c§lVAMPIRE", "vampire", new ItemBuilder(Material.NETHER_STAR).setName("§f§k|§c§k|§r §c§lVAMPIRE §c§k|§f§k|").setLore(
                    " ", "§c§l❘ §fType: §3Stratégie§f,§9 Réfléxion", "§c§l❘ §fMode(s): §cClassique§f, §aPlus de rôles", " ", "§4§l❘ §fConcept:", "§f  A rédiger les gars ok ?", " ", "§c§l❘ §c0 joueur(s)§f en jeu", "§c§l❘ §cAucun booster lancé", " ","§4» §fClic gauche pour jouer"
            )
            .toItemStack()),
    DUNGEONS("§2§lDONJONS", "donjons", new ItemBuilder(Material.ROTTEN_FLESH).setName("§f§k|§a§k|§r §2§lDONJONS §a§k|§f§k|").setLore(
                    " ", "§a§l❘ §fType: §6Aventure§f, §2Solo", "§e§l❘ §fMode(s): §aSolo Facile§f, §bSolo Normal§f, §cSolo difficile", " ", "§2§l❘ §fConcept:", "§f  A rédiger les gars ok ?", " ", "§a§l❘ §20 joueur(s)§f en jeu", "§a§l❘ §cAucun booster lancé", " ","§2» §fClic gauche pour jouer"
            )
            .toItemStack()),
    BEDWARS("§3§lBEDWARS", "bedwars", new ItemBuilder(Material.BED).setName("§f§k|§e§k|§r §6§lCOOKIE §e§k|§f§k|").setLore(
                    " ", "§e§l❘ §fType: §aMulti§f,§6 Combat", "§e§l❘ §fMode(s): §aSolo, §eDuo, §2Trio, §3Squad", " ", "§6§l❘ §fConcept:", "§f  A rédiger les gars ok ?", " ", "§e§l❘ §60 joueur(s)§f en jeu", "§e§l❘ §cAucun booster lancé", " ","§6» §fClic gauche pour jouer"
            )
            .toItemStack()),
    SKYFAST("§b§lSKYFAST", "skyfast", new ItemBuilder(Material.FEATHER).setName("§f§k|§e§k|§r §6§lCOOKIE §e§k|§f§k|").setLore(
                    " ", "§e§l❘ §fType: §aMulti§f,§6 Combat", "§e§l❘ §fMode(s): §aSolo, §eDuo, §2Trio, §3Squad", " ", "§6§l❘ §fConcept:", "§f  A rédiger les gars ok ?", " ", "§e§l❘ §60 joueur(s)§f en jeu", "§e§l❘ §cAucun booster lancé", " ","§6» §fClic gauche pour jouer"
            )
            .toItemStack());

    private final @Getter String embeddedName;
    private final @Getter String template;
    private final @Getter ItemStack mainMenuItem;
    Games(String embeddedName, String template, ItemStack mainMenuItem) {
        this.embeddedName = embeddedName;
        this.template = template;
        this.mainMenuItem = mainMenuItem;
    }
}
