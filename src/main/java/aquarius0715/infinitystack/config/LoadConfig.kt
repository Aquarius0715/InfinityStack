package aquarius0715.infinitystack.config

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.inventory.ItemStack

class LoadConfig(private val plugin: InfinityStack) {

    val itemStackAndColumnNameMap: MutableMap<ItemStack, String> = mutableMapOf()

    val itemStackAndDisplayName: MutableMap<ItemStack, String> = mutableMapOf()

    val itemStackList: MutableList<ItemStack> = mutableListOf()

    val columnNameList: MutableList<String> = mutableListOf()

    val displayNameList: MutableList<String> = mutableListOf()

    fun loadConfig() {

        val config = plugin.config

        plugin.reloadConfig()

        itemStackAndColumnNameMap.clear()

        itemStackAndDisplayName.clear()

        itemStackList.clear()

        columnNameList.clear()

        displayNameList.clear()

        for (columnName in config.getConfigurationSection("itemData")!!.getKeys(false)) {

            val base64 = config.getString("itemData.$columnName.base64")!!

            val itemStack = plugin.convertItems.itemFromBase64(base64)!!

            val displayName = config.getString("itemData.$columnName.displayName")!!

            itemStack.amount = 1

            itemStackAndColumnNameMap[itemStack] = columnName

            itemStackAndDisplayName[itemStack] = displayName

            columnNameList.add(columnName)

            displayNameList.add(displayName)

            itemStackList.add(itemStack)

        }

    }

}