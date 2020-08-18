package aquarius0715.infinitystack.mysql

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class MySQLSelect(private val plugin: InfinityStack): Thread() {

    fun isExistRecord(player: Player): Boolean {

        Bukkit.getScheduler().runTask(plugin, this)

        run {

            if (!plugin.mySQLManager.sqlConnectSafely()) return false

            return checkResultSet("SELECT PLAYER_NAME FROM InfinityStackTable WHERE UUID = '${player.uniqueId}';")

        }

    }

    fun setItem(player: Player, inventory: Inventory) {

        Bukkit.getScheduler().runTask(plugin, this)

        run {

            if (!plugin.mySQLManager.sqlConnectSafely()) return

            val changeStackStatsButton = ItemStack(Material.COMPASS)
            val changeStackStatsButtonMeta = changeStackStatsButton.itemMeta

            val list: ArrayList<String> = arrayListOf()

            val resultSet = plugin.mySQLManager.query(
                    "SELECT * FROM InfinityStackTable WHERE UUID = '${player.uniqueId}';")

            resultSet!!.next()

            for ((count, columnName) in plugin.loadConfig.stackItemColumnNameList.withIndex()) {

                val material = plugin.loadConfig.stackItemItemStackList[count].type
                val button = ItemStack(material)
                val buttonMeta = button.itemMeta

                list.add("${ChatColor.YELLOW}${ChatColor.BOLD}${resultSet.getInt(columnName)}個")

                if (resultSet.getBoolean("${columnName}_STATS")) {

                    list.add("${ChatColor.WHITE}${ChatColor.BOLD}${ChatColor.UNDERLINE}自動回収: " +
                            "${ChatColor.GREEN}${ChatColor.BOLD}${ChatColor.UNDERLINE}有効")

                } else {

                    list.add("${ChatColor.WHITE}${ChatColor.BOLD}${ChatColor.UNDERLINE}自動回収: " +
                            "${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}無効")

                }

                list.add("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}左クリック: " +
                        "${ChatColor.WHITE}${ChatColor.BOLD}1スタック取り出し")

                list.add("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}右クリック: " +
                        "${ChatColor.WHITE}${ChatColor.BOLD}1個取り出し")

                list.add("${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}${ChatColor.UNDERLINE}シフト+左クリック: " +
                        "${ChatColor.WHITE}${ChatColor.BOLD}自動回収切り替え")

                buttonMeta.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}${plugin.loadConfig.stackItemDisplayNameList[count]}")

                buttonMeta.lore = list

                button.itemMeta = buttonMeta

                inventory.setItem(count, button)

                list.clear()


            }

            changeStackStatsButtonMeta.setDisplayName("${ChatColor.YELLOW}${ChatColor.BOLD}${ChatColor.UNDERLINE}自動回収切り替え")


            if (resultSet.getBoolean("STACK_STATS")) {

                list.add("${ChatColor.BOLD}現在の自動回収: ${ChatColor.GREEN}${ChatColor.BOLD}${ChatColor.UNDERLINE}有効")

            } else {

                list.add("${ChatColor.BOLD}現在の自動回収: ${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}無効")

            }

            changeStackStatsButtonMeta.lore = list
            changeStackStatsButton.itemMeta = changeStackStatsButtonMeta

            inventory.setItem(52, changeStackStatsButton)

            list.clear()

            resultSet.close()

            plugin.mySQLManager.close()

            return

        }

    }

    fun getItemStack(player: Player, slot: Int, getAmount: Int): ItemStack {

        Bukkit.getScheduler().runTask(plugin, this)

        run {

            if (!plugin.mySQLManager.sqlConnectSafely()) return ItemStack(Material.AIR)

            val columnName = plugin.loadConfig.stackItemColumnNameList[slot]

            val maxStackSize = plugin.loadConfig.stackItemItemStackList[slot].maxStackSize

            val resultSet = plugin.mySQLManager.query("SELECT $columnName FROM InfinityStackTable WHERE UUID = '${player.uniqueId}';")

            resultSet!!.next()

            val amount = resultSet.getInt(columnName)

            resultSet.close()

            plugin.mySQLManager.close()

            if (amount <= 0) {

                player.playSound(player.location, Sound.BLOCK_STONE_BREAK, 8.0F, -2.0F)

                return ItemStack(Material.AIR)

            }

            if (getAmount == 1) {

                plugin.mySQLUpDate.removeItems(player, columnName, 1)

                player.playSound(player.location, Sound.UI_BUTTON_CLICK, 8.0F, 0.0F)

                return ItemStack(plugin.loadConfig.stackItemItemStackList[slot].type, 1)

            } else {

                return if (amount < maxStackSize) {

                    plugin.mySQLUpDate.removeItems(player, columnName, amount)

                    player.playSound(player.location, Sound.UI_BUTTON_CLICK, 8.0F, 0.0F)

                    ItemStack(plugin.loadConfig.stackItemItemStackList[slot].type, amount)


                } else {

                    plugin.mySQLUpDate.removeItems(player, columnName, maxStackSize)

                    player.playSound(player.location, Sound.UI_BUTTON_CLICK, 8.0F, 0.0F)

                    ItemStack(plugin.loadConfig.stackItemItemStackList[slot].type, maxStackSize)

                }

            }

        }

    }


    fun checkExistColumn() {

        Bukkit.getScheduler().runTask(plugin, this)

        run {

            if (!plugin.mySQLManager.sqlConnectSafely()) return

            var sql = "ALTER TABLE InfinityStackTable "

            var sqlCount = 0

            for ((count, columnName) in plugin.loadConfig.stackItemColumnNameList.withIndex()) {

                if (checkResultSet("DESCRIBE InfinityStackTable ${columnName};")) {

                    continue

                } else {

                    if (sqlCount == 0) {

                        sql += "ADD $columnName INT NOT NULL DEFAULT 0 AFTER ${plugin.loadConfig.stackItemColumnNameList[count - 1]}" +
                                ", ADD ${columnName}_STATS BOOLEAN NOT NULL DEFAULT TRUE AFTER ${plugin.loadConfig.stackItemColumnNameList[count - 1]}_STATS"

                        sqlCount++

                        continue

                    }

                    sql += ", ADD $columnName INT NOT NULL DEFAULT 0 AFTER ${plugin.loadConfig.stackItemColumnNameList[count - 1]}" +
                            ", ADD ${columnName}_STATS BOOLEAN NOT NULL DEFAULT TRUE AFTER ${plugin.loadConfig.stackItemColumnNameList[count - 1]}_STATS"

                }

            }

            sql += ";"

            plugin.mySQLManager.execute(sql)

        }

    }

    fun checkStackStats (player: Player): Boolean {

        Bukkit.getScheduler().runTask(plugin, this)

        run {

            if (!plugin.mySQLManager.sqlConnectSafely()) return false

            val sql = "SELECT STACK_STATS FROM InfinityStackTable WHERE UUID = '${player.uniqueId}';"

            val resultSet = plugin.mySQLManager.query(sql)

            resultSet!!.next()

            if (resultSet.getBoolean("STACK_STATS")) {

                resultSet.close()

                plugin.mySQLManager.close()

                return true

            } else {

                resultSet.close()

                plugin.mySQLManager.close()

                return false

            }

        }

    }

    fun checkStackStats (player: Player, columnName: String): Boolean {

        Bukkit.getScheduler().runTask(plugin, this)

        run {

            if (!plugin.mySQLManager.sqlConnectSafely()) return false

            val sql = "SELECT ${columnName}_STATS FROM InfinityStackTable WHERE UUID = '${player.uniqueId}';"

            val resultSet = plugin.mySQLManager.query(sql)

            resultSet!!.next()

            val stats = resultSet.getBoolean("${columnName}_STATS")

            resultSet.close()

            plugin.mySQLManager.close()

            return stats

        }

    }

    fun getStackStats(player: Player, columnName: String): Boolean {

        Bukkit.getScheduler().runTask(plugin, this)

        run {

            if (!plugin.mySQLManager.sqlConnectSafely()) return false

            val sql = "SELECT ${columnName}_STATS, STACK_STATS FROM InfinityStackTable WHERE UUID = '${player.uniqueId}';"

            val resultSet = plugin.mySQLManager.query(sql)

            resultSet!!.next()

            if (!resultSet.getBoolean("STACK_STATS") || !resultSet.getBoolean("${columnName}_STATS")) return false

            resultSet.close()

            plugin.mySQLManager.close()

            return true

        }

    }

    private fun checkResultSet(sql: String): Boolean {

        Bukkit.getScheduler().runTask(plugin, this)

        run {

            val resultSet = plugin.mySQLManager.query(sql)

            if (resultSet!!.next()) {

                resultSet.close()

                plugin.mySQLManager.close()

                return true

            } else {

                resultSet.close()

                plugin.mySQLManager.close()

                return false


            }

        }

    }

}