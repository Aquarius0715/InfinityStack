package aquarius0715.infinitystack.mysql

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class MySQLSelect(private val plugin: InfinityStack) {

    fun isExistRecord(player: Player): Boolean {

        if (!plugin.mySQLManager.sqlConnectSafely()) return false

        return checkResultSet("select PlayerName from InfinityStackTable where UUID = '${player.uniqueId}';")

    }

    fun setItem(player: Player, inventory: Inventory) {

        val list: ArrayList<String> = arrayListOf()

        if (!plugin.mySQLManager.sqlConnectSafely()) return

        val resultSet = plugin.mySQLManager.query("select * from InfinityStackTable WHERE UUID = '${player.uniqueId}';")

        resultSet!!.next()

            for ((count, columnName) in plugin.loadConfig.columnNameList.withIndex()) {

                list.add("${ChatColor.YELLOW}${ChatColor.BOLD}${resultSet.getInt(columnName)}個")

                if (resultSet.getBoolean("${columnName}Stats")) {

                    list.add("${ChatColor.WHITE}${ChatColor.BOLD}${ChatColor.UNDERLINE}自動回収は${ChatColor.GREEN}${ChatColor.BOLD}${ChatColor.UNDERLINE}有効")

                } else {

                    list.add("${ChatColor.WHITE}${ChatColor.BOLD}${ChatColor.UNDERLINE}自動回収は${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}無効")

                }

                list.add("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}左クリック${ChatColor.WHITE}${ChatColor.BOLD}で1スタック取り出し")

                list.add("${ChatColor.RED}${ChatColor.BOLD}${ChatColor.UNDERLINE}右クリック${ChatColor.WHITE}${ChatColor.BOLD}で1個取り出し")

                list.add("${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}${ChatColor.UNDERLINE}シフト+左クリック${ChatColor.WHITE}${ChatColor.BOLD}で自動回収切り替え")

                    val button = plugin.loadConfig.itemStackList[count]

                    val buttonMeta = button.itemMeta

                    buttonMeta.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}${ChatColor.UNDERLINE}${plugin.loadConfig.displayNameList[count]}")

                    buttonMeta.lore = list

                    button.itemMeta = buttonMeta

                    inventory.setItem(count, button)

                list.clear()

        }

        resultSet.close()

        plugin.mySQLManager.close()

        return

    }

    fun getItemOneStack(player: Player, slot: Int): ItemStack {

        if (!plugin.mySQLManager.sqlConnectSafely()) return ItemStack(Material.AIR)

        val columnName = plugin.loadConfig.columnNameList[slot]

        val sql = "select $columnName from InfinityStackTable where UUID = '${player.uniqueId}';"

        val resultSet = plugin.mySQLManager.query(sql)

        resultSet!!.next()

        val amount = resultSet.getInt(columnName)

        if (amount <= 0) return ItemStack(Material.AIR)

        return if (amount < 64) {

            plugin.mySQLUpDate.removeItems(player, columnName, amount)

            resultSet.close()

            plugin.mySQLManager.close()

            plugin.inventory.createInventory(player)

            ItemStack(plugin.loadConfig.itemStackList[slot].type, amount)


        } else {

            plugin.mySQLUpDate.removeItems(player, columnName, 64)

            resultSet.close()

            plugin.mySQLManager.close()

            plugin.inventory.createInventory(player)

            ItemStack(plugin.loadConfig.itemStackList[slot].type, 64)

        }

    }

    fun getItemOne(player: Player, slot: Int): ItemStack {

        val columnName = plugin.loadConfig.columnNameList[slot]

        if (!plugin.mySQLManager.sqlConnectSafely()) return ItemStack(Material.AIR)

        val sql = "select $columnName from InfinityStackTable where UUID = '${player.uniqueId}';"

        val resultSet = plugin.mySQLManager.query(sql)

        resultSet!!.next()

        val amount = resultSet.getInt(columnName)

        resultSet.close()

        plugin.mySQLManager.close()

        if (amount <= 0) return ItemStack(Material.AIR)

        plugin.inventory.createInventory(player)

        plugin.mySQLUpDate.removeItems(player, columnName, 1)

        return ItemStack(plugin.loadConfig.itemStackList[slot].type, 1)

    }

    fun checkExistColumn() {

        if (!plugin.mySQLManager.sqlConnectSafely()) return

        for (columnName in plugin.loadConfig.columnNameList) {

            if (checkResultSet("describe InfinityStackTable ${columnName};")) {

                return

            } else {

              plugin.mySQLManager.execute("alter table InfinityStackTable add $columnName int;")
              plugin.mySQLManager.execute("alter table InfinityStackTable add ${columnName}Stats boolean;")

                plugin.mySQLUpDate.setNullColumn(columnName)

            }

        }

    }

    fun checkStackStats (player: Player): Boolean {

        if (!plugin.mySQLManager.sqlConnectSafely()) return false

        val sql = "select StackStats from InfinityStackTable where UUID = '${player.uniqueId}';"

        val resultSet = plugin.mySQLManager.query(sql)

        resultSet!!.next()

        if (resultSet.getBoolean("StackStats")) {

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

        val sql = "select ${columnName}Stats from InfinityStackTable where UUID = '${player.uniqueId}';"

        val resultSet = plugin.mySQLManager.query(sql)

        resultSet!!.next()

        val stats = resultSet.getBoolean("${columnName}Stats")

        resultSet.close()

        plugin.mySQLManager.close()

        return stats

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