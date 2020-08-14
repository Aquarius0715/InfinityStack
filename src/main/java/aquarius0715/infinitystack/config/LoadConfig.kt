package aquarius0715.infinitystack.config

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.Bukkit

class LoadConfig(private val plugin: InfinityStack) {

    val columnNameList: MutableList<String> = mutableListOf()

    private val displayNameList: MutableList<String?> = mutableListOf()

    private val base64List: MutableList<String?> = mutableListOf()

    fun loadConfig() {

        val config = plugin.config

        plugin.reloadConfig()

        for (columnName in config.getConfigurationSection("itemData")!!.getKeys(false)) {

            columnNameList.add(columnName)

            val path = "itemData.$columnName.base64"

            base64List.add(config.getString(path))

            plugin.itemMap[config.getString(path)] = columnName.toString()

            Bukkit.broadcastMessage("${plugin.itemMap[config.getString("itemData.$columnName.base64")]}")

            displayNameList.add(config.getString("itemData.$columnName.displayName"))

        }

        plugin.mySQLSelect.checkExistColumn()

    }

}