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

        if (event.inventory != plugin.inventory.inventory) return

        event.isCancelled = true

        if (event.currentItem == null) return

        if (event.currentItem!!.itemMeta == null) return

        if (plugin.loadConfig.itemStackList.size < event.slot + 1) return

        if (event.isLeftClick) {

            player.inventory.addItem(plugin.mySQLSelect.getItemOneStack(player, event.slot))

            plugin.inventory.createInventory(player)

            plugin.loadConfig.loadConfig()

        }

        if (event.isRightClick) {

            player.inventory.addItem(plugin.mySQLSelect.getItemOne(player, event.slot))

            plugin.inventory.createInventory(player)

            plugin.loadConfig.loadConfig()

        }

        if (event.isShiftClick && event.isLeftClick) {

            plugin.mySQLUpDate.setStackStats(player, plugin.loadConfig.columnNameList[event.slot])

            plugin.inventory.createInventory(player)

            plugin.loadConfig.loadConfig()

        }

    }

}