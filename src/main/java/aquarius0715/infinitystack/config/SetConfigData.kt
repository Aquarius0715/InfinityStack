package aquarius0715.infinitystack.config

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack

class SetConfigData(val plugin: InfinityStack) {

    fun setConfig(columnName: String, displayName: String, itemStack: ItemStack, sender: CommandSender) {

        itemStack.amount = 1

        if (plugin.config.getConfigurationSection("itemData")!!.contains(columnName)) {

            sender.sendMessage("${plugin.prefix}カラムの名前が重複しています。")

            return

        }

            plugin.config.set("itemData.$columnName", " ")
            plugin.config.set("itemData.$columnName.displayName", displayName)
            plugin.config.set("itemData.$columnName.displayName.base64", plugin.convertItems.itemToBase64(itemStack))

            plugin.reboot()

        sender.sendMessage("${plugin.prefix}column: $columnName, displayName: $displayName でアイテムを登録しました。")

    }

}