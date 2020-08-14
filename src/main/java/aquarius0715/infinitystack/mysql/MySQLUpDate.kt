package aquarius0715.infinitystack.mysql

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerPickupItemEvent

class MySQLUpDate(private val plugin: InfinityStack) {

    fun updateItems(player: Player, columnName: String, amount: Int, event: PlayerPickupItemEvent) {

        if (!plugin.mySQLManager.sqlConnectSafely()) return

        val sql = "update InfinityStackTable set $columnName = $columnName + $amount where UUID = '${player.uniqueId}';"

        plugin.mySQLManager.execute(sql)

        event.item.remove()

        event.isCancelled = true

        event.item.remove()

    }

    fun setNullColumn(columnName: String) {

        if (!plugin.mySQLManager.sqlConnectSafely()) return

        plugin.mySQLManager.execute("update InfinityStackTable set $columnName = 0 WHERE $columnName IS NULL;")

    }

}