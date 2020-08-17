package aquarius0715.infinitystack.commands

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Commands(private val plugin: InfinityStack): CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {

            sender.sendMessage("${plugin.prefix}このコマンドはプレーヤーしか使うことができません。")

            return true

        }

        if (!plugin.sqlStats) {

            sender.sendMessage("${plugin.prefix}DBが稼働停止しています。")

            return true

        }

        if (!plugin.pluginStats) {

            sender.sendMessage("${plugin.prefix}プラグインが稼働停止しています。")

            return true

        }

        when (label) {

            "is" -> {

                when (args.size) {

                    0 -> {

                        plugin.inventory.createCheckStackInventory(sender)

                        sender.playSound(sender.location, Sound.BLOCK_ENDER_CHEST_OPEN, 8.0F, 0.0F)

                        return true

                    }

                    1 -> {

                        when (args[0]) {

                            "help" -> {

                                sender.sendMessage("${plugin.prefix}</is>: InfinityStackのメインメニューを開きます。")
                                sender.sendMessage("${plugin.prefix}</is help>: この説明画面を開きます。")
                                sender.sendMessage("${plugin.prefix}</is plugin on>: このプラグインの利用を許可します。")
                                sender.sendMessage("${plugin.prefix}</is plugin off>: このプラグインの利用を禁止します。")
                                sender.sendMessage("${plugin.prefix}</is mysql on>: mysqlとの接続を許可します。")
                                sender.sendMessage("${plugin.prefix}</is mysql off>: mysqlとの接続を禁止します。")
                                sender.sendMessage("${plugin.prefix}</is mysql remove [ColumnName]>: 指定したInfinityStack情報を一括削除します。")
                                sender.sendMessage("${plugin.prefix}注意！以下のコマンドは、InfinityStackを適用させたいアイテムを一つ持ちながら実行すること。")
                                sender.sendMessage("${plugin.prefix}</is create [ColumnName] [DisplayName]>: displayNameを決めて新しいInfinityStackを作ります。")

                            }

                            "reload" -> {

                                plugin.onEnable()

                            }

                        }

                    }

                }

            }

        }

        return false

    }


}