package aquarius0715.infinitystack.events

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPickupItemEvent
import org.bukkit.inventory.ItemStack

class PlayerItemPickUpEvent(private val plugin: InfinityStack): Listener {

    @EventHandler

    fun onPickUp(event: PlayerPickupItemEvent) {

        if (!plugin.stackStats[event.player.uniqueId]!!) return

        val key = plugin.convertItems.itemToBase64(ItemStack(event.item.itemStack.type))

        if (plugin.itemMap.containsKey(key)) {

            plugin.mySQLUpDate.updateItems(event.player, "${plugin.itemMap[key]}", event.item.itemStack.amount, event)

        }

    }

}