package me.unclickable.mgui.bukkit.utils.items;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import lombok.Builder;
import me.unclickable.mgui.bukkit.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Builder
public class ItemCreator {

    private final Material material;
    private final int amount;
    private final List<String> lore;
    private final String displayName;
    private final Set<NamespacedKeyData<?>> namespacedKeys;
    private final Map<Enchantment, Integer> enchantments; // <Enchantment, Level>
    private final Map<String, String> replacements; // <searchKey, replaceValue>
    private final String skullData;
    private final Map<String, String> placeholderLines;

    public ItemStack create() {
        if (material == null) throw new IllegalArgumentException("Material cannot be null");

        ItemStack itemStack = new ItemStack(material, amount > 0 ? amount : 1);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            if (displayName != null) itemMeta.displayName(TextUtils.getComponent(replaceKeywords(displayName)));

            if (lore != null && !lore.isEmpty()) {
                itemMeta.lore(lore.stream()
                        .map(s -> {
                            if(replacements.containsKey(s)) return s.replace(s, placeholderLines.get(s));
                            return s;
                        })
                        .filter(s -> !placeholderLines.containsKey(s))
                        .map(this::replaceKeywords)
                        .map(TextUtils::getComponent)
                        .toList());
            }

            if (enchantments != null && !enchantments.isEmpty()) {
                enchantments.forEach((enchantment, level) -> itemMeta.addEnchant(enchantment, level, false));
            }

            if(namespacedKeys != null && !namespacedKeys.isEmpty()) {
                namespacedKeys.forEach(namespacedKeyData -> addPersistentData(itemMeta, namespacedKeyData));
            }

            if (material == Material.PLAYER_HEAD && skullData != null && !skullData.isEmpty() && itemMeta instanceof SkullMeta skullMeta) {
                applySkullTexture(skullMeta, skullData);
            }

            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    private String replaceKeywords(String line) {
        if (replacements != null) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                line = line.replace(entry.getKey(), entry.getValue());
            }
        }

        return line;
    }

    private void applySkullTexture(SkullMeta skullMeta, String texture) {
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.setProperty(new ProfileProperty("textures", texture));
        skullMeta.setPlayerProfile(profile);
    }

    private <T> void addPersistentData(ItemMeta itemMeta, NamespacedKeyData<T> data) {
        itemMeta.getPersistentDataContainer().set(data.getKey(), data.getType(), data.getValue());
    }

}
