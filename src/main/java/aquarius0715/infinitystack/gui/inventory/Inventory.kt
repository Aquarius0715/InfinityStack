package aquarius0715.infinitystack.gui.inventory

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class Inventory(private val plugin: InfinityStack) {

    fun createInventory() {

        val inventory = Bukkit.createInventory(null, 54, "[InfinityStack]")

        val space = ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1)
        val spaceMeta = space.itemMeta
        spaceMeta.setDisplayName(" ")

        val closeButton = ItemStack(Material.RED_STAINED_GLASS_PANE, 1)
        val closeButtonMeta = closeButton.itemMeta
        closeButtonMeta.setDisplayName("${ChatColor.RED}${ChatColor.BOLD}閉じる")

        val changePageButton = ItemStack(Material.LIME_STAINED_GLASS_PANE)
        val changePageButtonMeta = changePageButton.itemMeta
        changePageButtonMeta.setDisplayName("ページを変える")

        for (count in 45..48) {

            inventory.setItem(count, space)

        }

        for (count in 48..50) {

            inventory.setItem(count, closeButton)

        }

        inventory.setItem(45, changePageButton)
        inventory.setItem(48, changePageButton)

    }

}