package nl.timvandijkhuizen.spigotutils.config.types;

import java.util.function.Consumer;

import org.bukkit.configuration.Configuration;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import nl.timvandijkhuizen.spigotutils.PluginBase;
import nl.timvandijkhuizen.spigotutils.config.ConfigOption;
import nl.timvandijkhuizen.spigotutils.config.ConfigType;
import nl.timvandijkhuizen.spigotutils.ui.UI;

public class ConfigTypeInteger implements ConfigType<Integer> {

    @Override
    public Integer getValue(Configuration config, ConfigOption<Integer> option) {
        return config.getInt(option.getPath());
    }

    @Override
    public void setValue(Configuration config, ConfigOption<Integer> option, Integer value) {
        config.set(option.getPath(), value);
    }

    @Override
    public String getValueLore(Configuration config, ConfigOption<Integer> option) {
        return "" + getValue(config, option);
    }
    
    @Override
    public boolean isValueEmpty(Configuration config, ConfigOption<Integer> option) {
        return getValue(config, option) == null;
    }

    @Override
    public void getValueInput(Player player, Consumer<Integer> callback) {
        ConversationFactory factory = new ConversationFactory(PluginBase.getInstance());

        Conversation conversation = factory.withFirstPrompt(new NumericPrompt() {
            @Override
            public String getPromptText(ConversationContext context) {
                return UI.color("What should be the new value?", UI.PRIMARY_COLOR);
            }

            @Override
            protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
                callback.accept(input.intValue());
                return null;
            }
        }).withLocalEcho(false).buildConversation(player);

        player.closeInventory();
        conversation.begin();
    }

}