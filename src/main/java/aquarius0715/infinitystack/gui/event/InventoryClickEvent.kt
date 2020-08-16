package aquarius0715.infinitystack.gui.event

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

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

            }

        }

        if (plugin.loadConfig.itemStackList.size < event.slot + 1) return

        if (event.isLeftClick &&
                !(event.isRightClick && event.isLeftClick) &&
                !(event.isLeftClick && event.isShiftClick)) {

            player.inventory.addItem(plugin.mySQLSelect.getItemOneStack(player, event.slot))

        }

        if (event.isRightClick &&
                !(event.isRightClick && event.isLeftClick) &&
                !(event.isRightClick && event.isShiftClick)) {

            player.inventory.addItem(plugin.mySQLSelect.getItemOne(player, event.slot))

        }

        if (event.isShiftClick && event.isLeftClick) {

            plugin.mySQLUpDate.setStackStats(player, plugin.loadConfig.columnNameList[event.slot])

            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 8.0F, 0.0F)

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

                var sql = "UPDATE InfinityStackTable SET "

                val itemList: MutableList<ItemStack> = mutableListOf()

                for (count in 0..44) {

                    if (event.inventory.getItem(count) == null) continue

                    val itemStack = event.inventory.getItem(count)

                    val amount = itemStack!!.amount

                    itemList.add(itemStack)

                    itemStack.amount = 1

                    if (count == 0) {

                        sql += "${plugin.loadConfig.itemStackAndColumnNameMap[itemStack]} = ${plugin.loadConfig.itemStackAndColumnNameMap[itemStack]} + $amount "

                    }

                    if (plugin.loadConfig.itemStackList.contains(itemStack)) {

                        sql += ", ${plugin.loadConfig.itemStackAndColumnNameMap[itemStack]} = ${plugin.loadConfig.itemStackAndColumnNameMap[itemStack]} + $amount"

                        event.inventory.setItem(count, ItemStack(Material.AIR))

                    } else {

                        itemStack.amount = amount

                        player.inventory.addItem(itemStack)

                    }

                }

                if (itemList.size == 0) {

                    plugin.inventory.createCheckStackInventory(player)

                    return

                }

                sql += " WHERE UUID = '${player.uniqueId}';"

                plugin.mySQLManager.execute(sql)

                player.sendMessage("${plugin.prefix}アイテムを登録しました。")

                plugin.inventory.createCheckStackInventory(player)

            }

        }

    }

}