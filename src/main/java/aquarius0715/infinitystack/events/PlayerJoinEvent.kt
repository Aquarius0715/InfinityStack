package aquarius0715.infinitystack.events

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinEvent(private val plugin: InfinityStack): Listener {

    @EventHandler

    fun onJoin(event: PlayerJoinEvent) {

        plugin.mySQLInsert.insertDefaultTable(event.player)

        plugin.stackStats[event.player.uniqueId] = plugin.mySQLSelect.checkStackStats(event.player)

    }

}