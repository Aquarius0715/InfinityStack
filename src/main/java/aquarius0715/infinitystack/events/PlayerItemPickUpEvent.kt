package aquarius0715.infinitystack.events

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.Bukkit
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

        for (itemData in plugin.itemData) {

            if (itemData.itemStack == item) {

                Bukkit.broadcastMessage("同じアイテム")

                plugin.mySQLUpDate.addItems(event.player, itemData.columnName, amount, event)

            }

        }

    }

}