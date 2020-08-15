package aquarius0715.infinitystack.gui.event

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class InventoryClickEvent(val plugin: InfinityStack): Listener {

    @EventHandler

    fun onClick(event: InventoryClickEvent) {

        val player = event.whoClicked as Player

        event.isCancelled = true

        if (event.inventory != plugin.inventory.inventory) return

        if (event.currentItem == null) return

        if (event.currentItem!!.itemMeta == null) return

        if (plugin.itemData.size < event.slot + 1) return

        if (!plugin.mySQLSelect.checkStackStats(player, plugin.itemData[event.slot].columnName)) return

        if (!plugin.mySQLSelect.checkStackStats(player)) return

        if (event.isLeftClick) {

            player.inventory.addItem(plugin.mySQLSelect.getItemOneStack(player, event.slot))

            plugin.inventory.createInventory(player)

        }

        if (event.isRightClick) {

            player.inventory.addItem(plugin.mySQLSelect.getItemOne(player, event.slot))

            plugin.inventory.createInventory(player)


        }

        if (event.isShiftClick) {

            plugin.mySQLUpDate.setStackStats(player, plugin.itemData[event.slot].columnName)

            plugin.inventory.createInventory(player)


        }

    }

}