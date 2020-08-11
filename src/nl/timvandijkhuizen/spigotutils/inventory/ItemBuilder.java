package nl.timvandijkhuizen.spigotutils.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

@SuppressWarnings("deprecation")
public class ItemBuilder {
	
	protected ItemStack itemStack;
	
	public ItemBuilder(Material material) {
		this(material, 1);
	}
	
	public ItemBuilder(Material material, int amount) {
		itemStack = new ItemStack(material, amount);
	}
	
	public ItemBuilder(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public ItemBuilder clone() {
		return new ItemBuilder(itemStack.clone());
	}

	public ItemStack toItemStack() {
		return itemStack;
	}

	public ItemBuilder setDurability(short durability) {
		if (durability == 0) {
			return this;
		}
		
		itemStack.setDurability(durability);
		
		return this;
	}

	public ItemBuilder setType(Material material) {
		itemStack.setType(material);
		return this;
	}

	public ItemBuilder setName(String name) {
		ItemMeta meta = itemStack.getItemMeta();
		
		if (meta == null) {
			return this;
		}
		
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		itemStack.setItemMeta(meta);
		
		return this;
	}

	public ItemBuilder setAmount(int amount) {
		itemStack.setAmount(amount);
		return this;
	}

	public List<String> getLore() {
		ItemMeta im = itemStack.getItemMeta();
		
		if (im == null) {
			return new ArrayList<>();
		}
		
		return itemStack.getItemMeta().getLore();
	}

	public ItemBuilder removeLore() {
		ItemMeta meta = itemStack.getItemMeta();
		
		if (meta == null || !meta.hasLore() || meta.getLore() == null) {
			return this;
		}

		// Remove lore
		List<String> lore = new ArrayList<>(meta.getLore());
		
		lore.clear();
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
		
		return this;
	}

	public ItemBuilder hideAttributes() {
		ItemMeta meta = itemStack.getItemMeta();
		
		if (meta == null) {
			return this;
		}
		
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		itemStack.setItemMeta(meta);
		
		return this;
	}

	public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level) {
		itemStack.addUnsafeEnchantment(enchantment, level);
		return this;
	}

	public ItemBuilder removeEnchantment(Enchantment enchantment) {
		itemStack.removeEnchantment(enchantment);
		return this;
	}

	public ItemBuilder addEnchant(Enchantment enchantment, int level) {
		ItemMeta meta = itemStack.getItemMeta();
		
		if (meta == null) {
			return this;
		}
		
		meta.addEnchant(enchantment, level, true);
		itemStack.setItemMeta(meta);
		
		return this;
	}

	public ItemBuilder addEnchantGlow(Enchantment ench, int level) {
		ItemMeta meta = itemStack.getItemMeta();
		
		if (meta == null) {
			return this;
		}
		
		meta.addEnchant(ench, level, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemStack.setItemMeta(meta);
		
		return this;
	}

	public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
		itemStack.addEnchantments(enchantments);
		return this;
	}

	public ItemBuilder setInfinityDurability() {
		itemStack.setDurability(Short.MAX_VALUE);
		return this;
	}

	public ItemBuilder setLore(String... lore) {
		return setLore(Arrays.asList(lore));
	}

	public ItemBuilder setLore(List<String> lore) {
		ItemMeta meta = itemStack.getItemMeta();
		
		if (meta == null) {
			return this;
		}
		
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
		
		return this;
	}

	public ItemBuilder addLoreLines(String... lines) {
		return addLoreLines(Arrays.asList(lines));
	}

	public ItemBuilder addLoreLines(List<String> line) {
		ItemMeta meta = itemStack.getItemMeta();
		
		if (meta == null || !meta.hasLore() || meta.getLore() == null) {
			return this;
		}
		
		// Add lore
		List<String> lore = meta.getLore();
		
		for(String s : line) {
			lore.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
		
		return this;
	}

	public ItemBuilder setUnbreakable(boolean value) {
		ItemMeta meta = itemStack.getItemMeta();
		
		if (meta == null) {
			return this;
		}
		
		meta.setUnbreakable(value);
		itemStack.setItemMeta(meta);
		
		return this;
	}

	public ItemBuilder removeLoreLine(String line) {
		ItemMeta meta = itemStack.getItemMeta();
		List<String> lore = meta.getLore();
		
		if (!lore.contains(line)) {
			return this;
		}
		
		lore.remove(line);
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
		
		return this;
	}

	public ItemBuilder removeLoreLine(int index) {
		ItemMeta meta = itemStack.getItemMeta();
		List<String> lore = meta.getLore();
		
		if (index < 0 || index > lore.size()) {
			return this;
		}
		
		lore.remove(index);
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
		
		return this;
	}

	public ItemBuilder addLoreLine(String line) {
		ItemMeta meta = itemStack.getItemMeta();
		List<String> lore = new ArrayList<>();
		
		if (meta.hasLore()) {
			lore.addAll(meta.getLore());
		}
		
		lore.add(line);
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
		
		return this;
	}

	public ItemBuilder addLoreLine(String line, int pos) {
		ItemMeta meta = itemStack.getItemMeta();
		List<String> lore = meta.getLore();
		
		lore.set(pos, line);
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
		
		return this;
	}

	public ItemBuilder setLeatherArmorColor(Color color) {
		try {
			LeatherArmorMeta im = (LeatherArmorMeta) itemStack.getItemMeta();
			
			im.setColor(color);
			itemStack.setItemMeta(im);
		} catch (ClassCastException expected) {
			throw new RuntimeException("Item is not leather armor");
		}
		
		return this;
	}

	public static class SkullData {
		
		private final String texture;
		private final SkullDataType type;
		
		public SkullData(String texture, SkullDataType type) {
			this.texture = texture;
			this.type = type;
		}
		
		public String getTexture() {
			return texture;
		}
		
		public SkullDataType getType() {
			return type;
		}
	}
	
	public enum SkullDataType {
		NAME,
		URL,
		TEXTURE,
	}
	
}