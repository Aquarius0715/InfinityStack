package aquarius0715.infinitystack.mysql

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerPickupItemEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class MySQLUpDate(private val plugin: InfinityStack): Thread() {

    fun addItems(player: Player, columnName: String, amount: Int, event: PlayerPickupItemEvent) {

        Bukkit.getScheduler().runTask(plugin, this)

        run {

            if (!plugin.mySQLManager.sqlConnectSafely()) return

            val sql = "UPDATE InfinityStackTable SET $columnName = $columnName + $amount WHERE UUID = '${player.uniqueId}';"

            plugin.mySQLManager.execute(sql)

            event.item.remove()

            event.isCancelled = true

            event.item.remove()

        }

    }

    fun addItemsLocal(player: Player, inventory: Inventory) {

        Bukkit.getScheduler().runTask(plugin, this)

        val displayMap: MutableMap<ItemStack, Int> = mutableMapOf()

        run {

            var sql = "UPDATE InfinityStackTable SET "

            val itemList: MutableList<ItemStack> = mutableListOf()

            for (count in 0..44) {

                if (inventory.getItem(count) == null) continue

                val itemStack = inventory.getItem(count)

                val amount = itemStack!!.amount

                itemStack.amount = 1

                if (!displayMap.keys.contains(itemStack)) {

                    displayMap[itemStack] = 0

                }

                displayMap[itemStack] = displayMap[itemStack]?.plus(amount)!!

                if (!plugin.loadConfig.itemStackList.contains(itemStack)) {

                    itemStack.amount = amount

                    player.inventory.addItem(itemStack)

                    continue

                }

                itemList.add(itemStack)

            }


            var sqlCount = 0

            for (itemStack in displayMap.keys) {

                if (!plugin.loadConfig.itemStackList.contains(itemStack)) {

                    displayMap.remove(itemStack)

                    continue

                }

                if (sqlCount == 0) {

                    sql += "${plugin.loadConfig.itemStackAndColumnNameMap[itemStack]} = ${plugin.loadConfig.itemStackAndColumnNameMap[itemStack]} + ${displayMap[itemStack]} "

                    player.sendMessage("${plugin.prefix}${plugin.loadConfig.itemStackAndDisplayName[itemStack]}を${displayMap[itemStack]}個登録しました。")

                    sqlCount++

                    continue

                }

                sql += ", ${plugin.loadConfig.itemStackAndColumnNameMap[itemStack]} = ${plugin.loadConfig.itemStackAndColumnNameMap[itemStack]} + ${displayMap[itemStack]}"

                player.sendMessage("${plugin.prefix}${plugin.loadConfig.itemStackAndDisplayName[itemStack]}を${displayMap[itemStack]}個登録しました。")

                itemStack.amount = displayMap[itemStack]!!

                sqlCount++

                inventory.remove(itemStack)

            }

            if (displayMap.isEmpty()) {

                plugin.inventory.createCheckStackInventory(player)

                player.playSound(player.location, Sound.BLOCK_CHEST_CLOSE, 8.0F, 0.0F)

                return

            }

            if (sqlCount != 0) {

                sql += " WHERE UUID = '${player.uniqueId}';"

                plugin.mySQLManager.execute(sql)

            }

            player.playSound(player.location, Sound.BLOCK_CHEST_CLOSE, 8.0F, 0.0F)

            player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 8.0F, 0.0F)

            plugin.inventory.createCheckStackInventory(player)

        }

    }

    fun setStackStats(player: Player, columnName: String) {

        Bukkit.getScheduler().runTask(plugin, this)

        run {

            plugin.mySQLManager.execute("UPDATE InfinityStackTable SET ${columnName}_STATS = ${!plugin.mySQLSelect.checkStackStats(player, columnName)} WHERE UUID = '${player.uniqueId}';")

            plugin.inventory.createCheckStackInventory(player)

        }

    }

    fun setStackStats(player: Player) {

        Bukkit.getScheduler().runTask(plugin, this)

        run {

            plugin.mySQLManager.execute("UPDATE InfinityStackTable SET STACK_STATS = ${!plugin.mySQLSelect.checkStackStats(player)} WHERE UUID = '${player.uniqueId}';")

            plugin.stackStats[player.uniqueId] = plugin.mySQLSelect.checkStackStats(player)

            plugin.inventory.createCheckStackInventory(player)

        }

    }

    fun removeItems(player: Player, columnName: String, amount: Int) {

        Bukkit.getScheduler().runTask(plugin, this)

        run {

            if (!plugin.mySQLManager.sqlConnectSafely()) return

            val sql = "UPDATE InfinityStackTable SET $columnName = $columnName - $amount WHERE UUID = '${player.uniqueId}';"

            plugin.mySQLManager.execute(sql)

        }

    }

}