package nl.timvandijkhuizen.spigotutils.menu;

import org.bukkit.entity.Player;

import nl.timvandijkhuizen.spigotutils.data.DataArguments;

public interface PredefinedMenu {

    Menu create(Player player, DataArguments args);

}
