package me.lokka30.levelledmobs.nametag;

import me.lokka30.levelledmobs.LevelledMobs;
import me.lokka30.levelledmobs.result.NametagResult;
import me.lokka30.levelledmobs.wrappers.LivingEntityWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

/**
 * @author PenalBuffalo (aka stumper66)
 */
public class KyoriNametags {

    public static @NotNull Object generateComponent(
            final @NotNull LivingEntity livingEntity,
            final @NotNull NametagResult nametagResult
    ) {
        final String nametag = nametagResult.getNametagNonNull();
        final Definitions def = LevelledMobs.getInstance().getDefinitions();

        // This component holds the component of the mob name and will show the translated name on clients
        net.kyori.adventure.text.Component mobNameComponent;
        if (nametagResult.overriddenName == null) {
            String entityStr = livingEntity.getType().getKey().getKey();
            String entityNameToBeDone = formatEntityName(entityStr);
            mobNameComponent = net.kyori.adventure.text.Component.text(entityNameToBeDone);
        } else {
            mobNameComponent = LegacyComponentSerializer
                    .legacyAmpersand()
                    .deserialize(nametagResult.overriddenName);
        }

        // Replace placeholders and set the new death message
        Component result;
        if (def.getUseLegacySerializer()) {
            result = LegacyComponentSerializer
                    .legacyAmpersand()
                    .deserialize(nametag)
                    .replaceText(
                            TextReplacementConfig.builder()
                                    .matchLiteral("{DisplayName}")
                                    .replacement(mobNameComponent).build()
                    );
        } else {
            result = def.mm
                    .deserialize(nametag)
                    .replaceText(
                            TextReplacementConfig.builder()
                                    .matchLiteral("{DisplayName}")
                                    .replacement(mobNameComponent).build()
                    );
        }

        try {
            return def.method_AsVanilla.invoke(def.clazz_PaperAdventure, result);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }

        return ComponentUtils.getEmptyComponent();
    }

    // Add a helper method to format entity names with capitalization
    private static String formatEntityName(String entityStr) {
        String[] parts = entityStr.split("_");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = Character.toUpperCase(parts[i].charAt(0)) + parts[i].substring(1);
        }
        return String.join(" ", parts);
    }

    public static @NotNull Component generateDeathMessage(
            final @NotNull String mobKey,
            final @NotNull NametagResult nametagResult
    ) {
        final LivingEntityWrapper lmEntity = LivingEntityWrapper.getInstance(nametagResult.killerMob, LevelledMobs.getInstance());
        final String nametag = LevelledMobs.getInstance().levelManager.replaceStringPlaceholders(
                nametagResult.getcustomDeathMessage(), lmEntity, false, null, true);
        lmEntity.free();

        final Definitions def = LevelledMobs.getInstance().getDefinitions();

        // This component holds the component of the mob name and will show the translated name on clients
        net.kyori.adventure.text.Component mobNameComponent;
        if (nametagResult.overriddenName == null) {
            mobNameComponent = net.kyori.adventure.text.Component.translatable(mobKey);
        } else {
            mobNameComponent = LegacyComponentSerializer
                    .legacyAmpersand()
                    .deserialize(nametagResult.overriddenName);
        }

        // Replace placeholders and set the new death message
        Component result;
        if (def.getUseLegacySerializer()) {
            result = LegacyComponentSerializer
                    .legacyAmpersand()
                    .deserialize(nametag)
                    .replaceText(
                            TextReplacementConfig.builder()
                                    .matchLiteral("{DisplayName}")
                                    .replacement(mobNameComponent).build()
                    );
        } else {
            result = def.mm
                    .deserialize(nametag)
                    .replaceText(
                            TextReplacementConfig.builder()
                                    .matchLiteral("{DisplayName}")
                                    .replacement(mobNameComponent).build()
                    );
        }

        return result;
    }
}
