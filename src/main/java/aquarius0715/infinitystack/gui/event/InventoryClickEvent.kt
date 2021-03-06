package aquarius0715.infinitystack.gui.event

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class InventoryClickEvent(val plugin: InfinityStack): Listener {

    @EventHandler

    fun onClickCheckStackInventory(event: InventoryClickEvent) {

        val player = event.whoClicked as Player

        if (event.inventory != plugin.inventory.checkStackInventoryMap[player.uniqueId]) return

        event.isCancelled = true

        if (event.currentItem == null) return

        if (event.currentItem!!.itemMeta == null) return

        when (event.slot) {

            48, 49, 50 -> {

                player.closeInventory()

                player.playSound(player.location, Sound.BLOCK_ENDER_CHEST_CLOSE, 8.0F, 0.0F)

            }

            51 -> {

                plugin.inventory.createSetItemInventory(player)

                player.playSound(player.location, Sound.BLOCK_CHEST_OPEN, 8.0F, 0.0F)

            }

            52 -> {

                plugin.mySQLUpDate.setStackStats(player)

                player.playSound(player.location, Sound.UI_BUTTON_CLICK, 8.0F, 0.0F)

                plugin.inventory.createCheckStackInventory(player)

            }

        }

        if (plugin.loadConfig.stackItemItemStackList.size < event.slot + 1) return

        if (event.isLeftClick &&
                !(event.isRightClick && event.isLeftClick) &&
                !(event.isLeftClick && event.isShiftClick)) {

            player.inventory.addItem(plugin.mySQLSelect.getItemStack(player, event.slot, plugin.loadConfig.stackItemItemStackList[event.slot].maxStackSize))

            plugin.inventory.createCheckStackInventory(player)

        }

        if (event.isRightClick &&
                !(event.isRightClick && event.isLeftClick) &&
                !(event.isRightClick && event.isShiftClick)) {

            player.inventory.addItem(plugin.mySQLSelect.getItemStack(player, event.slot, 1))

            plugin.inventory.createCheckStackInventory(player)

        }

        if (event.isShiftClick && event.isLeftClick) {

            plugin.mySQLUpDate.setStackStats(player, plugin.loadConfig.stackItemColumnNameList[event.slot])

            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 8.0F, 0.0F)

            plugin.inventory.createCheckStackInventory(player)

        }

    }

    @EventHandler

    fun onClickSetItemInventory(event: InventoryClickEvent) {

        val player = event.whoClicked as Player

        if (event.inventory != plugin.inventory.setItemInventoryMap[player.uniqueId]) return

        when (event.slot) {

            45, 46, 47, 51, 52, 53 -> {

                event.isCancelled = true

            }

            48, 49, 50 -> {

                event.isCancelled = true

                plugin.closeStats[player.uniqueId] = true

                plugin.mySQLUpDate.addItemsLocal(player, event.inventory)

                plugin.inventory.createCheckStackInventory(player)

                plugin.closeStats[player.uniqueId] = false

            }

        }

    }

}