package aquarius0715.infinitystack.config

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.inventory.ItemStack

class LoadConfig(private val plugin: InfinityStack) {

    val stackItemItemStackAndColumnNameMap: MutableMap<ItemStack, String> = mutableMapOf()
    val stackItemItemStackAndDisplayNameMap: MutableMap<ItemStack, String> = mutableMapOf()
    val stackItemItemStackList: MutableList<ItemStack> = mutableListOf()
    val stackItemColumnNameList: MutableList<String> = mutableListOf()
    val stackItemDisplayNameList: MutableList<String> = mutableListOf()

    val categoryNameList: MutableList<String> = mutableListOf()
    val categoryDisplayNameList: MutableList<String> = mutableListOf()
    val categoryItemStackList: MutableList<String> = mutableListOf() //TODO ここのItemStackだった

    fun loadConfig() {

        val config = plugin.config

        plugin.reloadConfig()

        stackItemItemStackAndColumnNameMap.clear()
        stackItemItemStackAndDisplayNameMap.clear()
        stackItemItemStackList.clear()
        stackItemColumnNameList.clear()
        stackItemDisplayNameList.clear()

        categoryNameList.clear()
        categoryDisplayNameList.clear()
        categoryItemStackList.clear()

        for (categoryName in config.getConfigurationSection("INFINITY_STACK")!!.getKeys(false)) {

            categoryNameList.add(categoryName)

            categoryDisplayNameList.add(config.getString("INFINITY_STACK.$categoryName.CATEGORY_DISPLAY_NAME")!!)

            categoryItemStackList.add(config.getString("INFINITY_STACK.$categoryName.CATEGORY_DISPLAY_NAME.CATEGORY_ITEM")!!)

            for (itemColumnName in config.getConfigurationSection("INFINITY_STACK.$categoryName.CATEGORY_DISPLAY_NAME.CATEGORY_ITEM.ITEM_DATA")!!.getKeys(false)) {

                val displayName = config.getString("INFINITY_STACK.$categoryName.CATEGORY_DISPLAY_NAME.CATEGORY_ITEM.ITEM_DATA.$itemColumnName.DISPLAY_NAME")!!

                val itemStack = plugin.convertItems.itemFromBase64(config.getString("INFINITY_STACK.$categoryName.CATEGORY_DISPLAY_NAME.CATEGORY_ITEM.ITEM_DATA.$itemColumnName.BASE64"))!!

                itemStack.amount = 1

                stackItemColumnNameList.add(itemColumnName)

                stackItemDisplayNameList.add(displayName)

                stackItemItemStackList.add(itemStack)

                stackItemItemStackAndColumnNameMap[itemStack] = itemColumnName

                stackItemItemStackAndDisplayNameMap[itemStack] = displayName


            }
        }

    }

}