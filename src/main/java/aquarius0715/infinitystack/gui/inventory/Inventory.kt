package aquarius0715.infinitystack.gui.inventory

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Inventory(private val plugin: InfinityStack) {

    val inventory = Bukkit.createInventory(null, 54, "${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}InfinityStack")

    fun createInventory(player: Player) {

        val space = ItemStack(Material.GRAY_STAINED_GLASS_PANE)
        val spaceMeta = space.itemMeta
        spaceMeta.setDisplayName(" ")

        space.itemMeta = spaceMeta

        val closeButton = ItemStack(Material.RED_STAINED_GLASS_PANE)
        val closeButtonMeta = closeButton.itemMeta
        closeButtonMeta.setDisplayName("${ChatColor.RED}${ChatColor.BOLD}閉じる")

        closeButton.itemMeta = closeButtonMeta

        val changePageButton = ItemStack(Material.LIME_STAINED_GLASS_PANE)
        val changePageButtonMeta = changePageButton.itemMeta
        changePageButtonMeta.setDisplayName("${ChatColor.GREEN}${ChatColor.BOLD}ページを変える")

        changePageButton.itemMeta = changePageButtonMeta

        for (count in 0..53) {

            inventory.setItem(count, space)

        }

        for (count in 48..50) {

            inventory.setItem(count, closeButton)

        }

        inventory.setItem(45, changePageButton)
        inventory.setItem(53, changePageButton)

        plugin.mySQLSelect.setItem(player, inventory)

        player.openInventory(inventory)

    }

}