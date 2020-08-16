package aquarius0715.infinitystack.mysql

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerPickupItemEvent

class MySQLUpDate(private val plugin: InfinityStack) {

    fun addItems(player: Player, columnName: String, amount: Int, event: PlayerPickupItemEvent) {

        if (!plugin.mySQLManager.sqlConnectSafely()) return

        val sql = "UPDATE InfinityStackTable SET $columnName = $columnName + $amount WHERE UUID = '${player.uniqueId}';"

        plugin.mySQLManager.execute(sql)

        event.item.remove()

        event.isCancelled = true

        event.item.remove()

    }

    fun setStackStats(player: Player, columnName: String) {

        plugin.mySQLManager.execute("UPDATE InfinityStackTable SET ${columnName}_STATS = ${!plugin.mySQLSelect.checkStackStats(player, columnName)} WHERE UUID = '${player.uniqueId}';")

        plugin.inventory.createCheckStackInventory(player)

    }

    fun setStackStats(player: Player) {

        plugin.mySQLManager.execute("UPDATE InfinityStackTable SET STACK_STATS = ${!plugin.mySQLSelect.checkStackStats(player)} WHERE UUID = '${player.uniqueId}';")

        plugin.stackStats[player.uniqueId] = plugin.mySQLSelect.checkStackStats(player)

        plugin.inventory.createCheckStackInventory(player)

    }

    fun removeItems(player: Player, columnName: String, amount: Int) {

        if (!plugin.mySQLManager.sqlConnectSafely()) return

        val sql = "UPDATE InfinityStackTable SET $columnName = $columnName - $amount WHERE UUID = '${player.uniqueId}';"

        plugin.mySQLManager.execute(sql)

    }

    fun setNullColumn(columnName: String) {

        if (!plugin.mySQLManager.sqlConnectSafely()) return

        plugin.mySQLManager.execute("UPDATE InfinityStackTable SET $columnName = 0 WHERE $columnName IS NULL;")

    }

}