package aquarius0715.infinitystack.events

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPickupItemEvent

class PlayerItemPickUpEvent(private val plugin: InfinityStack): Listener {

    @EventHandler

    fun onPickUp(event: PlayerPickupItemEvent) {

        if (!plugin.stackStats[event.player.uniqueId]!!) return

        val item = event.item.itemStack

        val amount = item.amount

        item.amount = 1

            if (plugin.loadConfig.itemStackList.contains(item)) {

                plugin.mySQLUpDate.addItems(event.player, plugin.loadConfig.itemStackAndColumnNameMap[item]!!, amount, event)

        }

    }

}