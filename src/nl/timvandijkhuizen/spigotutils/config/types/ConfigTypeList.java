package nl.timvandijkhuizen.spigotutils.config.types;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import nl.timvandijkhuizen.spigotutils.config.ConfigObject;
import nl.timvandijkhuizen.spigotutils.config.ConfigOption;
import nl.timvandijkhuizen.spigotutils.config.ConfigType;
import nl.timvandijkhuizen.spigotutils.config.OptionConfig;
import nl.timvandijkhuizen.spigotutils.menu.items.MenuItemBuilder;
import nl.timvandijkhuizen.spigotutils.menu.items.MenuItems;
import nl.timvandijkhuizen.spigotutils.menu.types.PagedMenu;
import nl.timvandijkhuizen.spigotutils.ui.UI;

public class ConfigTypeList<T extends ConfigObject> implements ConfigType<List<T>> {

    private Class<T> clazz;
    private String menuTitle;
    private Material menuIcon;
    
    public ConfigTypeList(Class<T> clazz, String menuTitle, Material menuIcon) {
        this.clazz = clazz;
        this.menuTitle = menuTitle;
        this.menuIcon = menuIcon;
    }
    
    public ConfigTypeList(Class<T> clazz, String menuTitle) {
        this(clazz, menuTitle, Material.PAPER);
    }
    
    @Override
    public List<T> getValue(OptionConfig config, ConfigOption<List<T>> option) {
        List<T> output = new ArrayList<>();
        
        for(String rawObject : config.getStringList(option.getPath())) {
            byte[] decodedBytes = Base64.getDecoder().decode(rawObject);
            ByteArrayDataInput stream = ByteStreams.newDataInput(decodedBytes);
            T object;
            
            try {
                object = clazz.newInstance();
                object.deserialize(stream);
            } catch(Exception e) {
                continue;
            }
            
            output.add(object);
        }
        
        return output;
    }

    @Override
    public void setValue(OptionConfig config, ConfigOption<List<T>> option, List<T> value) {
        List<String> output = new ArrayList<>();
        
        for(T object : value) {
            ByteArrayDataOutput stream = ByteStreams.newDataOutput();
            
            object.serialize(stream);
            output.add(Base64.getEncoder().encodeToString(stream.toByteArray()));
        }
        
        config.set(option.getPath(), output);
    }

    @Override
    public String getValueLore(List<T> value) {
        String[] items = value.stream().map(i -> i.getItemName()).toArray(String[]::new);
        return String.join(", ", items);
    }

    @Override
    public boolean isValueEmpty(List<T> value) {
        return value == null || value.isEmpty();
    }

    @Override
    public void getValueInput(Player player, List<T> value, Consumer<List<T>> callback) {
        PagedMenu menu = new PagedMenu(menuTitle, 3, 7, 1, 1);
        List<T> objects = new ArrayList<>(value);

        // Add command buttons
        for (T object : objects) {
            addObjectButton(player, menu, objects, object);
        }

        // Cancel button
        // ===========================
        MenuItemBuilder cancelButton = MenuItems.CANCEL.clone();

        cancelButton.setClickListener(event -> {
            UI.playSound(player, UI.SOUND_CLICK);
            callback.accept(value);
        });

        menu.setButton(cancelButton, menu.getSize().getSlots() - 9 + 3);

        // Create button
        // ===========================
        MenuItemBuilder createButton = new MenuItemBuilder(Material.NETHER_STAR);

        createButton.setName(UI.color("Create", UI.COLOR_SECONDARY, ChatColor.BOLD));

        createButton.setClickListener(event -> {
            try {
                T object = clazz.newInstance();
                
                UI.playSound(player, UI.SOUND_CLICK);
                player.closeInventory();
                
                object.getInput(player, () -> {
                    addObjectButton(player, menu, objects, object);
                    objects.add(object);
                    
                    UI.playSound(player, UI.SOUND_CLICK);
                    menu.open(player);
                });
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        menu.setButton(createButton, menu.getSize().getSlots() - 9 + 4);
        
        // Save button
        // ===========================
        MenuItemBuilder saveButton = MenuItems.SAVE.clone();

        saveButton.setClickListener(event -> {
            UI.playSound(player, UI.SOUND_SUCCESS);
            callback.accept(objects);
        });

        menu.setButton(saveButton, menu.getSize().getSlots() - 9 + 5);
        
        // Open menu
        menu.open(player);
    }
    
    private void addObjectButton(Player player, PagedMenu menu, List<T> objects, T object) {
        MenuItemBuilder item = new MenuItemBuilder(menuIcon);

        item.setName(UI.color(object.getItemName(), UI.COLOR_PRIMARY, ChatColor.BOLD));
        item.setLore(object.getItemLore());
        
        item.addLore("", UI.color("Use left-click to edit.", UI.COLOR_SECONDARY, ChatColor.ITALIC));
        item.addLore(UI.color("Use right-click to delete.", UI.COLOR_SECONDARY, ChatColor.ITALIC));

        item.setClickListener(event -> {
            ClickType clickType = event.getClickType();

            if (clickType == ClickType.RIGHT) {
                UI.playSound(player, UI.SOUND_DELETE);
                
                objects.remove(object);
                
                menu.removePagedButton(item);
                menu.refresh();
            } else {
                UI.playSound(player, UI.SOUND_CLICK);
                player.closeInventory();
                
                object.getInput(player, () -> {
                    item.setName(UI.color(object.getItemName(), UI.COLOR_PRIMARY, ChatColor.BOLD));
                    item.setLore(object.getItemLore());
                    
                    UI.playSound(player, UI.SOUND_CLICK);
                    menu.open(player);
                });
            }
        });

        menu.addPagedButton(item);
    }

}
