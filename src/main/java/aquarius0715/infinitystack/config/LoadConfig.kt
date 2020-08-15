package aquarius0715.infinitystack.config

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.inventory.ItemStack
import sun.security.ec.point.ProjectivePoint

class LoadConfig(private val plugin: InfinityStack) {

    val itemStackAndColumnNameMap: MutableMap<ItemStack, String> = mutableMapOf()

    val itemStackList: MutableList<ItemStack> = mutableListOf()

    val columnNameList: MutableList<String> = mutableListOf()

    val displayNameList: MutableList<String> = mutableListOf()

    fun loadConfig() {

        val config = plugin.config

        plugin.reloadConfig()

        itemStackAndColumnNameMap.clear()

        itemStackList.clear()

        columnNameList.clear()

        displayNameList.clear()

        for (columnName in config.getConfigurationSection("itemData")!!.getKeys(false)) {

            val base64 = config.getString("itemData.$columnName.base64")!!

            val itemStack = plugin.convertItems.itemFromBase64(base64)!!

            val displayName = config.getString("itemData.$columnName.displayName")!!

            itemStack.amount = 1

            itemStackAndColumnNameMap[itemStack] = columnName

            columnNameList.add(columnName)

            displayNameList.add(displayName)

            itemStackList.add(itemStack)

        }

        plugin.mySQLSelect.checkExistColumn()

    }

}