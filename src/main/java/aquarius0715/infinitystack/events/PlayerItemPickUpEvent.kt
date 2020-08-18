package aquarius0715.infinitystack.events

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPickupItemEvent

class PlayerItemPickUpEvent(private val plugin: InfinityStack): Listener, Thread() {

    @EventHandler

    fun onPickUp(event: PlayerPickupItemEvent) {

        Bukkit.getScheduler().runTask(plugin, this)

        run {

            val player = event.player

            val item = event.item.itemStack

            val amount = item.amount

            item.amount = 1

            if (!plugin.sqlStats) return

            if (!plugin.stackStats[event.player.uniqueId]!!) return

            if (!plugin.loadConfig.stackItemItemStackList.contains(item)) return

            if (!plugin.mySQLSelect.getStackStats(player, plugin.loadConfig.stackItemItemStackAndColumnNameMap[item]!!)) return

            if (plugin.loadConfig.stackItemItemStackList.contains(item)) {

                player.playSound(player.location, Sound.ENTITY_ITEM_PICKUP, 8.0F, -1.0F)

                plugin.mySQLUpDate.addItems(event.player, plugin.loadConfig.stackItemItemStackAndColumnNameMap[item]!!, amount, event)


            }

        }

    }

}