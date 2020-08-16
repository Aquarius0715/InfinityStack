package aquarius0715.infinitystack.gui.inventory

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*

class Inventory(private val plugin: InfinityStack) {

    val checkStackInventoryMap: MutableMap<UUID, Inventory> = mutableMapOf()

    val setItemInventoryMap: MutableMap<UUID, Inventory> = mutableMapOf()

    fun createCheckStackInventory(player: Player) {

        checkStackInventoryMap[player.uniqueId] = Bukkit.createInventory(null, 54,
                "${ChatColor.GOLD}${ChatColor.BOLD}Infinity" +
                "${ChatColor.YELLOW}${ChatColor.BOLD}Stack")

        val space = ItemStack(Material.GRAY_STAINED_GLASS_PANE)
        val spaceMeta = space.itemMeta
        spaceMeta.setDisplayName(" ")
        space.itemMeta = spaceMeta

        val closeButton = ItemStack(Material.RED_STAINED_GLASS_PANE)
        val closeButtonMeta = closeButton.itemMeta
        closeButtonMeta.setDisplayName("${ChatColor.RED}${ChatColor.BOLD}閉じる")
        closeButton.itemMeta = closeButtonMeta

        val pageDownButton = ItemStack(Material.BLUE_STAINED_GLASS_PANE)
        val pageDownButtonMeta = pageDownButton.itemMeta
        pageDownButtonMeta.setDisplayName("${ChatColor.BLUE}${ChatColor.BOLD}1ページ戻る。")
        pageDownButton.itemMeta = pageDownButtonMeta

        val pageUpButton = ItemStack(Material.GREEN_STAINED_GLASS_PANE)
        val pageUpButtonMeta = pageUpButton.itemMeta
        pageUpButtonMeta.setDisplayName("${ChatColor.GREEN}${ChatColor.BOLD}1ページ進む。")
        pageUpButton.itemMeta = pageUpButtonMeta

        val setItemLocalButton = ItemStack(Material.CHEST)
        val setItemLocalButtonMeta = setItemLocalButton.itemMeta
        setItemLocalButtonMeta.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}ここからアイテムを搬入する。")
        setItemLocalButton.itemMeta = setItemLocalButtonMeta

        for (count in 0..53) {

            checkStackInventoryMap[player.uniqueId]!!.setItem(count, space)

        }

        for (count in 48..50) {

            checkStackInventoryMap[player.uniqueId]!!.setItem(count, closeButton)

        }

        checkStackInventoryMap[player.uniqueId]!!.setItem(45, pageDownButton)
        checkStackInventoryMap[player.uniqueId]!!.setItem(53, pageUpButton)

        checkStackInventoryMap[player.uniqueId]!!.setItem(51, setItemLocalButton)

        plugin.mySQLSelect.setItem(player, checkStackInventoryMap[player.uniqueId]!!)

        player.openInventory(checkStackInventoryMap[player.uniqueId]!!)

    }

    fun createSetItemInventory(player: Player) {

        setItemInventoryMap[player.uniqueId] = Bukkit.createInventory(null, 54, "${ChatColor.BOLD}登録したいアイテムを入れてください。")

        val space = ItemStack(Material.GRAY_STAINED_GLASS_PANE)
        val spaceMeta = space.itemMeta
        spaceMeta.setDisplayName(" ")
        space.itemMeta = spaceMeta

        val closeButton = ItemStack(Material.RED_STAINED_GLASS_PANE)
        val closeButtonMeta = closeButton.itemMeta
        closeButtonMeta.setDisplayName("${ChatColor.RED}${ChatColor.BOLD}アイテムを登録")
        closeButton.itemMeta = closeButtonMeta

        for (count in 45..53) {

            setItemInventoryMap[player.uniqueId]!!.setItem(count, space)

        }

        for (count in 48..50) {

            setItemInventoryMap[player.uniqueId]!!.setItem(count, closeButton)

        }

        player.openInventory(plugin.inventory.setItemInventoryMap[player.uniqueId]!!)

        player.playSound(player.location, Sound.BLOCK_CHEST_OPEN, 8.0F, 0.0F)

    }

}