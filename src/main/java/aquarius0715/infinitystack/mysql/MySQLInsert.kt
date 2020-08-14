package aquarius0715.infinitystack.mysql

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.entity.Player

class MySQLInsert(private val plugin: InfinityStack) {

    fun insertDefaultTable(player: Player) {

        if (!plugin.mySQLManager.sqlConnectSafely()) return

        if (plugin.mySQLSelect.isExistRecord(player)) return

        var sql = "insert into InfinityStackTable " +
                "(PlayerName, UUID, StackStats"

        for (columnName in plugin.loadConfig.columnNameList) {

            sql += ", $columnName"

        }

        sql += ") VALUE ('${player.name}', '${player.uniqueId}', true"

        for (count in plugin.loadConfig.columnNameList) {

            sql += ", 0"

        }

        sql += ");"

        plugin.mySQLManager.execute(sql)

    }

}