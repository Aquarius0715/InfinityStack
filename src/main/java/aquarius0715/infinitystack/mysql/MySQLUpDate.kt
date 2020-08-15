package aquarius0715.infinitystack.mysql

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerPickupItemEvent

class MySQLUpDate(private val plugin: InfinityStack) {

    fun addItems(player: Player, columnName: String, amount: Int, event: PlayerPickupItemEvent) {

        if (!plugin.mySQLManager.sqlConnectSafely()) return

        val sql = "update InfinityStackTable set $columnName = $columnName + $amount where UUID = '${player.uniqueId}';"

        plugin.mySQLManager.execute(sql)

        event.item.remove()

        event.isCancelled = true

        event.item.remove()

    }

    fun setStackStats(player: Player, columnName: String) {

        plugin.mySQLManager.execute("update InfinityStackTable set ${columnName}Stats = ${!plugin.mySQLSelect.checkStackStats(player, columnName)} where UUID = '${player.uniqueId}';")

    }

    fun removeItems(player: Player, columnName: String, amount: Int) {

        if (!plugin.mySQLManager.sqlConnectSafely()) return

        val sql = "update InfinityStackTable set $columnName = $columnName - $amount where UUID = '${player.uniqueId}';"

        plugin.mySQLManager.execute(sql)

    }

    fun setNullColumn(columnName: String) {

        if (!plugin.mySQLManager.sqlConnectSafely()) return

        plugin.mySQLManager.execute("update InfinityStackTable set $columnName = 0 WHERE $columnName IS NULL;")

    }

}