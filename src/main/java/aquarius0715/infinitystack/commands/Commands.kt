package aquarius0715.infinitystack.commands

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Commands(private val plugin: InfinityStack): CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {

            sender.sendMessage("${plugin.prefix}このコマンドはプレーヤーしか使うことができません。")

            return false

        }

        when (label) {

            "is" -> {

                when (args.size) {

                    0 -> {

                        plugin.inventory.createInventory(sender)

                    }

                }

            }

        }

        return false

    }


}