package aquarius0715.infinitystack.config

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class LoadConfig(private val plugin: InfinityStack) {

    val stackItemItemStackAndColumnNameMap: MutableMap<ItemStack, String> = mutableMapOf()
    val stackItemItemStackAndDisplayName: MutableMap<ItemStack, String> = mutableMapOf()
    val stackItemItemStackList: MutableList<ItemStack> = mutableListOf()
    val stackItemColumnNameList: MutableList<String> = mutableListOf()
    val stackItemDisplayNameList: MutableList<String> = mutableListOf()

    val categoryNameList: MutableList<String> = mutableListOf()
    val categoryDisplayNameList: MutableList<String> = mutableListOf()
    val categoryItemStackList: MutableList<ItemStack> = mutableListOf()

    fun loadConfig() {

        val config = plugin.config

        plugin.reloadConfig()

        stackItemItemStackAndColumnNameMap.clear()
        stackItemItemStackAndDisplayName.clear()
        stackItemItemStackList.clear()
        stackItemColumnNameList.clear()
        stackItemDisplayNameList.clear()

        categoryNameList.clear()
        categoryDisplayNameList.clear()
        categoryItemStackList.clear()

        for (categoryName in config.getConfigurationSection("INFINITY_STACK")!!.getKeys(false)) {

            categoryNameList.add(categoryName)

            categoryDisplayNameList.add(config.getString("INFINITY_STACK.$categoryName.CATEGORY_DISPLAY_NAME")!!)

            categoryItemStackList.add(ItemStack(Material.matchMaterial(config.getString("INFINITY_STACK.$categoryName.CATEGORY_ITEM")!!)!!))

            for (columnName in config.getConfigurationSection("INFINITY_STACK.$categoryName.CATEGORY_NAME")!!.getKeys(false)) {

                val itemStack = plugin.convertItems.itemFromBase64(config.getString("INFINITY_STACK.$categoryName.CATEGORY_NAME.$columnName.BASE64"))!!
                itemStack.amount = 1

                stackItemColumnNameList.add(columnName)
                stackItemDisplayNameList.add(config.getString("INFINITY_STACK.$categoryName.CATEGORY_NAME.$columnName.DISPLAY_NAME")!!)
                stackItemItemStackList.add(itemStack)

            }

        }

    }

}