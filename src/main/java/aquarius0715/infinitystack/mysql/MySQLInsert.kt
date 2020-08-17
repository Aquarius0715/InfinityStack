package aquarius0715.infinitystack.mysql

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class MySQLInsert(private val plugin: InfinityStack): Thread() {

    fun insertDefaultTable(player: Player) {

        Bukkit.getScheduler().runTask(plugin, this)

        run {

            if (!plugin.mySQLManager.sqlConnectSafely()) return

            if (plugin.mySQLSelect.isExistRecord(player)) return

            var sql = "INSERT INTO InfinityStackTable " +
                    "(PLAYER_NAME, UUID, STACK_STATS"

            for (columnName in plugin.loadConfig.columnNameList) {

                sql += ", ${columnName}, ${columnName}_STATS"

            }

            sql += ") VALUE ('${player.name}', '${player.uniqueId}', TRUE"

            for (count in plugin.loadConfig.columnNameList) {

                sql += ", 0, TRUE"

            }

            sql += ");"

            plugin.mySQLManager.execute(sql)

        }

    }

}