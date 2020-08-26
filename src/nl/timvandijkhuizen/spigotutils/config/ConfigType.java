package nl.timvandijkhuizen.spigotutils.config;

import java.util.function.Consumer;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

public interface ConfigType<T> {
    
    /**
     * Deserializes and returns the value.
     * 
     * @param config
     * @param path
     * @return
     * @throws ConfigurationException
     */
    T getValue(Configuration config, ConfigOption<T> option);
    
    /**
     * Serializes and sets the value.
     * 
     * @param config
     * @param path
     * @param value
     */
    void setValue(Configuration config, ConfigOption<T> option, T value);
    
    /**
     * Returns the lore lines used
     * to display the current value.
     * 
     * @param config
     * @param option
     * @return
     */
    String getValueLore(Configuration config, ConfigOption<T> option);
    
    /**
     * Returns whether the value is empty.
     * 
     * @param config
     * @param option
     * @return
     */
    boolean isValueEmpty(Configuration config, ConfigOption<T> option);
    
    /**
     * Returns the input value for this option.
     * 
     * @param config
     * @param option
     * @return
     */
    void getValueInput(Player player, Consumer<T> callback);

}