package aquarius0715.infinitystack.mysql

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.entity.Player

class MySQLSelect(private val plugin: InfinityStack) {

    fun isExistRecord(player: Player): Boolean {

        if (!plugin.mySQLManager.sqlConnectSafely()) return false

        return checkResultSet("select PlayerName from InfinityStackTable where UUID = '${player.uniqueId}';")

    }

    fun checkExistColumn() {

        if (!plugin.mySQLManager.sqlConnectSafely()) return

        for (columnName in plugin.loadConfig.columnNameList) {

            if (checkResultSet("describe InfinityStackTable $columnName;")) {

                return

            } else {

                plugin.mySQLManager.execute("alter table InfinityStackTable add $columnName int;")

                plugin.mySQLUpDate.setNullColumn(columnName)

            }

        }

    }

    fun checkStackStats (player: Player): Boolean {

        if (!plugin.mySQLManager.sqlConnectSafely()) return false

        val sql = "select StackStats from InfinityStackTable where UUID = '${player.uniqueId}'"

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