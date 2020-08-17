package aquarius0715.infinitystack.main

import aquarius0715.infinitystack.events.PlayerItemPickUpEvent
import aquarius0715.infinitystack.events.PlayerJoinEvent
import aquarius0715.infinitystack.commands.Commands
import aquarius0715.infinitystack.config.LoadConfig
import aquarius0715.infinitystack.gui.event.InventoryClickEvent
import aquarius0715.infinitystack.gui.event.InventoryCloseEvent
import aquarius0715.infinitystack.gui.inventory.Inventory
import aquarius0715.infinitystack.items.ConvertItems
import aquarius0715.infinitystack.mysql.MySQLInsert
import aquarius0715.infinitystack.mysql.MySQLManager
import aquarius0715.infinitystack.mysql.MySQLSelect
import aquarius0715.infinitystack.mysql.MySQLUpDate
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class InfinityStack : JavaPlugin() {

    val prefix = "${ChatColor.BOLD}[" +
            "${ChatColor.GOLD}${ChatColor.BOLD}Infinity" +
            "${ChatColor.YELLOW}${ChatColor.BOLD}Stack" +
            "${ChatColor.WHITE}${ChatColor.BOLD}]"

    val convertItems = ConvertItems()

    val mySQLManager = MySQLManager(this, "InfinityStack")

    val mySQLSelect = MySQLSelect(this)

    val mySQLInsert = MySQLInsert(this)

    val mySQLUpDate = MySQLUpDate(this)

    val stackStats: MutableMap<UUID, Boolean> = mutableMapOf()

    val closeStats: MutableMap<UUID, Boolean> = mutableMapOf()

    val inventory = Inventory(this)

    val loadConfig = LoadConfig(this)

    var sqlStats = true

    var pluginStats = true

    override fun onEnable() {

        saveDefaultConfig()

        loadConfig.loadConfig()

        mySQLSelect.checkExistColumn()

        Objects.requireNonNull(getCommand("is")!!.setExecutor(Commands(this)))

        server.pluginManager.registerEvents(PlayerItemPickUpEvent(this), this)

        server.pluginManager.registerEvents(InventoryCloseEvent(this), this)

        server.pluginManager.registerEvents(PlayerJoinEvent(this), this)

        server.pluginManager.registerEvents(InventoryClickEvent(this), this)

    }

    fun reboot() {

        Bukkit.broadcastMessage("${prefix}プラグインを再起動中です。")

        pluginStats = false

        sqlStats = false

        onEnable()

        Bukkit.broadcastMessage("${prefix}プラグインの再起動が終了しました。")

        pluginStats = true

        sqlStats = true

    }

}