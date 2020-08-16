package aquarius0715.infinitystack.mysql

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class MySQLSelect(private val plugin: InfinityStack) {

    fun isExistRecord(player: Player): Boolean {

        if (!plugin.mySQLManager.sqlConnectSafely()) return false

        return checkResultSet("SELECT PLAYER_NAME FROM InfinityStackTable WHERE UUID = '${player.uniqueId}';")

    }

    fun setItem(player: Player, inventory: Inventory) {

        val list: ArrayList<String> = arrayListOf()

        val changeStackStatsButton = ItemStack(Material.COMPASS)
        val changeStackStatsButtonMeta = changeStackStatsButton.itemMeta

        if (!plugin.mySQLManager.sqlConnectSafely()) return

        val resultSet = plugin.mySQLManager.query("SELECT * FROM InfinityStackTable WHERE UUID = '${player.uniqueId}';")

        resultSet!!.next()

            for ((count, columnName) in plugin.loadConfig.columnNameList.withIndex()) {

                list.clear()

                val material = plugin.loadConfig.itemStackList[count].type
                val button = ItemStack(material)
                val buttonMeta = button.itemMeta

                list.add("${ChatColor.YELLOW}${ChatColor.BOLD}${resultSet.getInt(columnName)}個")

                if (resultSet.getBoolean("${columnName}_STATS")) {

                    list.add("${ChatColor.WHITE}${ChatColor.BOLD}${ChatColor.UNDERLINE}自動回収: ${ChatColor.GREEN}${ChatColor.BOLD}${ChatColor.UNDERLINE}有効")

                } else {

                    list.add("${ChatColor.WHITE}${ChatColor.BOLD}${ChatColor.UNDERLINE}自動回収: ${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}無効")

                }

                list.add("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}左クリック${ChatColor.WHITE}${ChatColor.BOLD}で1スタック取り出し")

                list.add("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}右クリック${ChatColor.WHITE}${ChatColor.BOLD}で1個取り出し")

                list.add("${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}${ChatColor.UNDERLINE}シフト+左クリック${ChatColor.WHITE}${ChatColor.BOLD}で自動回収切り替え")

                    buttonMeta.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}${plugin.loadConfig.displayNameList[count]}")

                    buttonMeta.lore = list

                    button.itemMeta = buttonMeta

                    inventory.setItem(count, button)

                list.clear()

        }

        changeStackStatsButtonMeta.setDisplayName("${ChatColor.YELLOW}${ChatColor.BOLD}${ChatColor.UNDERLINE}ここをクリックして全体の自動回収を切り替える。")


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

    fun getItemOneStack(player: Player, slot: Int): ItemStack {

        if (!plugin.mySQLManager.sqlConnectSafely()) return ItemStack(Material.AIR)

        val columnName = plugin.loadConfig.columnNameList[slot]

        val resultSet = plugin.mySQLManager.query("SELECT $columnName FROM InfinityStackTable WHERE UUID = '${player.uniqueId}';")

        resultSet!!.next()

        val amount = resultSet.getInt(columnName)

        if (amount <= 0) {

            player.playSound(player.location, Sound.BLOCK_STONE_BREAK, 8.0F, -2.0F)

            return ItemStack(Material.AIR)

        }

        return if (amount < 64) {

            plugin.mySQLUpDate.removeItems(player, columnName, amount)

            resultSet.close()

            plugin.mySQLManager.close()

            plugin.inventory.createCheckStackInventory(player)

            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 8.0F, 0.0F)

            ItemStack(plugin.loadConfig.itemStackList[slot].type, amount)


        } else {

            plugin.mySQLUpDate.removeItems(player, columnName, 64)

            resultSet.close()

            plugin.mySQLManager.close()

            plugin.inventory.createCheckStackInventory(player)

            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 8.0F, 0.0F)

            ItemStack(plugin.loadConfig.itemStackList[slot].type, 64)

        }

    }

    fun getItemOne(player: Player, slot: Int): ItemStack {

        val columnName = plugin.loadConfig.columnNameList[slot]

        if (!plugin.mySQLManager.sqlConnectSafely()) return ItemStack(Material.AIR)

        val sql = "SELECT $columnName FROM InfinityStackTable WHERE UUID = '${player.uniqueId}';"

        val resultSet = plugin.mySQLManager.query(sql)

        resultSet!!.next()

        val amount = resultSet.getInt(columnName)

        resultSet.close()

        plugin.mySQLManager.close()

        if (amount <= 0) {

            player.playSound(player.location, Sound.BLOCK_STONE_BREAK, 8.0F, -2.0F)

            return ItemStack(Material.AIR)

        }

        plugin.mySQLUpDate.removeItems(player, columnName, 1)

        plugin.inventory.createCheckStackInventory(player)

        player.playSound(player.location, Sound.UI_BUTTON_CLICK, 8.0F, 0.0F)

        return ItemStack(plugin.loadConfig.itemStackList[slot].type, 1)

    }

    fun checkExistColumn() {

        if (!plugin.mySQLManager.sqlConnectSafely()) return

        for ((count, columnName) in plugin.loadConfig.columnNameList.withIndex()) {

            if (checkResultSet("DESCRIBE InfinityStackTable ${columnName};")) {

                return

            } else {

              plugin.mySQLManager.execute("ALTER TABLE InfinityStackTable ADD $columnName INT AFTER ${plugin.loadConfig.columnNameList[count - 1]};")
              plugin.mySQLManager.execute("ALTER TABLE InfinityStackTable ADD ${columnName}_STATS BOOLEAN ${columnName};")

                plugin.mySQLUpDate.setNullColumn(columnName)

            }

        }

    }

    fun checkStackStats (player: Player): Boolean {

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

    fun checkStackStats (player: Player, columnName: String): Boolean {

        if (!plugin.mySQLManager.sqlConnectSafely()) return false

        val sql = "SELECT ${columnName}_STATS FROM InfinityStackTable WHERE UUID = '${player.uniqueId}';"

        val resultSet = plugin.mySQLManager.query(sql)

        resultSet!!.next()

        val stats = resultSet.getBoolean("${columnName}_STATS")

        resultSet.close()

        plugin.mySQLManager.close()

        return stats

    }

    fun getStackStats(player: Player, columnName: String): Boolean {

        if (!plugin.mySQLManager.sqlConnectSafely()) return false

        val sql = "SELECT ${columnName}_STATS, STACK_STATS FROM InfinityStackTable WHERE UUID = '${player.uniqueId}';"

        val resultSet = plugin.mySQLManager.query(sql)

        resultSet!!.next()

        if (!resultSet.getBoolean("STACK_STATS") || !resultSet.getBoolean("${columnName}_STATS")) return false

        resultSet.close()

        plugin.mySQLManager.close()

        return true

    }

    private fun checkResultSet(sql: String): Boolean {

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