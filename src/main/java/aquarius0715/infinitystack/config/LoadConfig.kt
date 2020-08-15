package aquarius0715.infinitystack.config

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.inventory.ItemStack

class LoadConfig(private val plugin: InfinityStack) {

    private val columnNameList: MutableList<String> = mutableListOf()

    private val displayNameList: MutableList<String?> = mutableListOf()

    private val itemStackList: MutableList<ItemStack> = mutableListOf()

    private val itemMap: MutableMap<ItemStack, String> = mutableMapOf()

    fun loadConfig() {

        val config = plugin.config

        plugin.reloadConfig()

        for (columnName in config.getConfigurationSection("itemData")!!.getKeys(false)) {

            columnNameList.add(columnName)

            val base64Path = "itemData.$columnName.base64"

            config.getString("itemData.$columnName.displayName")?.let {
                InfinityStack.ItemData(it,
                        plugin.convertItems.itemFromBase64(config.getString(base64Path))!!,
                        config.getString(base64Path)!!,
                        columnName)
            }?.let { plugin.itemData.add(it) }

            itemStackList.add(plugin.convertItems.itemFromBase64(config.getString(base64Path))!!)

            itemMap[plugin.convertItems.itemFromBase64(config.getString(base64Path))!!] = columnName.toString()

            displayNameList.add(config.getString("itemData.$columnName.displayName"))

        }

        plugin.mySQLSelect.checkExistColumn()

    }

}