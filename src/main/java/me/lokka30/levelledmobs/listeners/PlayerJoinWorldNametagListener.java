package me.lokka30.levelledmobs.listeners;

import me.lokka30.levelledmobs.LevelledMobs;
import me.lokka30.levelledmobs.misc.LivingEntityWrapper;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Collections;

/**
 * TODO Describe...
 *
 * @author lokka30
 */
public class PlayerJoinWorldNametagListener implements Listener {

    private final LevelledMobs main;

    public PlayerJoinWorldNametagListener(final LevelledMobs main) {
        this.main = main;
    }

    private void updateNametagsInWorld(final Player player, final World world) {
        for (final Entity entity : world.getEntities()) {
            if (entity instanceof LivingEntity) {
                final LivingEntity livingEntity = (LivingEntity) entity;

                // mob must be alive
                if (!livingEntity.isValid()) continue;

                // mob must be levelled
                final LivingEntityWrapper lmEntity = new LivingEntityWrapper(livingEntity, main);
                if (!lmEntity.isLevelled()) continue;

                //Send nametag packet
                //This also must be delayed by 1 tick
                // public void updateNametagWithDelay(final LivingEntityWrapper lmEntity, final List<Player> playerList, final long delay) {
                main.levelManager.updateNametagWithDelay(lmEntity, Collections.singletonList(player), 1);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onJoin(final PlayerJoinEvent event) {
        updateNametagsInWorld(event.getPlayer(), event.getPlayer().getWorld());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onChangeWorld(final PlayerChangedWorldEvent event) {
        updateNametagsInWorld(event.getPlayer(), event.getPlayer().getWorld());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onTeleport(final PlayerTeleportEvent event) {
        if (event.getTo() != null && event.getTo().getWorld() != null)
            updateNametagsInWorld(event.getPlayer(), event.getTo().getWorld());
    }
}
