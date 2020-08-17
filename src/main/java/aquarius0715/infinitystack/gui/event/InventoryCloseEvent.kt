package aquarius0715.infinitystack.gui.event

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent

class InventoryCloseEvent(val plugin: InfinityStack): Listener {

    @EventHandler

    fun onCloseInventoryCloseEvent(event: InventoryCloseEvent) {

        val player = event.player as Player

        if (event.inventory != plugin.inventory.setItemInventoryMap[event.player.uniqueId]) return

        if (plugin.closeStats[player.uniqueId]!!) return

        plugin.mySQLUpDate.addItemsLocal(player, event.inventory)

    }

}