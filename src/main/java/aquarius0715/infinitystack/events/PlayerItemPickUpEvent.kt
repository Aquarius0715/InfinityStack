package aquarius0715.infinitystack.events

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPickupItemEvent

class PlayerItemPickUpEvent(private val plugin: InfinityStack): Listener {

    @EventHandler

    fun onPickUp(event: PlayerPickupItemEvent) {

        val player = event.player

        val item = event.item.itemStack

        val amount = item.amount

        item.amount = 1

        if (!plugin.stackStats[event.player.uniqueId]!!) return

        if (!plugin.mySQLSelect.getStackStats(player, plugin.loadConfig.itemStackAndColumnNameMap[item]!!)) return

        if (plugin.loadConfig.itemStackList.contains(item)) {

            player.playSound(player.location, Sound.ENTITY_ITEM_PICKUP, 8.0F, -1.0F)

            plugin.mySQLUpDate.addItems(event.player, plugin.loadConfig.itemStackAndColumnNameMap[item]!!, amount, event)

        }

    }

}