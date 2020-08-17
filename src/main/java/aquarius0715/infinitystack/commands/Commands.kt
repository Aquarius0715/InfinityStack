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

        when (label) {

            "is" -> {

                when (args.size) {

                    0 -> {

                        if (!checkCommandStats(sender)) return false

                        plugin.inventory.createCheckStackInventory(sender)

                        sender.playSound(sender.location, Sound.BLOCK_ENDER_CHEST_OPEN, 8.0F, 0.0F)

                        return true

                    }

                    1 -> {

                        when (args[0]) {

                            "help" -> {

                                if (!checkCommandStats(sender)) return false

                                sender.sendMessage("${plugin.prefix}</is>: InfinityStackのメインメニューを開きます。")
                                sender.sendMessage("${plugin.prefix}</is help>: この説明画面を開きます。")
                                sender.sendMessage("${plugin.prefix}</is plugin on>: このプラグインの利用を許可します。")
                                sender.sendMessage("${plugin.prefix}</is plugin off>: このプラグインの利用を禁止します。")
                                sender.sendMessage("${plugin.prefix}</is mysql on>: mysqlとの接続を許可します。")
                                sender.sendMessage("${plugin.prefix}</is mysql off>: mysqlとの接続を禁止します。")

                            }

                            "reload" -> {

                                if (!checkCommandStats(sender)) return false

                                plugin.reboot()

                                return true

                            }

                        }

                    }

                    2 -> {

                        when(args[0]) {

                            "plugin" -> {

                                when (args[1]) {

                                    "on" -> {

                                        if (!checkCommandStats(sender)) return false

                                        return if (plugin.pluginStats) {

                                            sender.sendMessage("${plugin.prefix}すでにプラグインの使用は許可されています。")

                                            true

                                        } else {

                                            plugin.pluginStats = true

                                            sender.sendMessage("${plugin.prefix}プラグインの使用を禁止しました。")

                                            true

                                        }

                                    }

                                    "off" -> {

                                        if (!checkCommandStats(sender)) return false

                                        return if (!plugin.pluginStats) {

                                            sender.sendMessage("${plugin.prefix}すでにプラグインの使用は禁止されています。")

                                            true

                                        } else {

                                            plugin.pluginStats = false

                                            sender.sendMessage("${plugin.prefix}プラグインの使用を許可しました。")

                                            true

                                        }

                                    }

                                }

                            }

                            "mysql" -> {

                                when (args[1]) {

                                    "on" -> {

                                        if (!checkCommandStats(sender)) return false

                                        return if (plugin.sqlStats) {

                                            sender.sendMessage("${plugin.prefix}すでにMYSQLの使用は許可されています。")

                                            true

                                        } else {

                                            plugin.sqlStats = true

                                            sender.sendMessage("${plugin.prefix}MYSQLの使用を禁止しました。")

                                            true

                                        }

                                    }

                                    "off" -> {

                                        if (!checkCommandStats(sender)) return false

                                        return if (!plugin.sqlStats) {

                                            sender.sendMessage("${plugin.prefix}すでにMYSQLの使用は禁止されています。")

                                            true

                                        } else {

                                            plugin.sqlStats = false

                                            sender.sendMessage("${plugin.prefix}MYSQLの使用を許可しました。")

                                            true

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

        return false

    }

    private fun checkCommandStats(sender: CommandSender): Boolean {

        if (!plugin.sqlStats) {

            sender.sendMessage("${plugin.prefix}DBが稼働停止しています。")

            return false

        }

        if (!plugin.pluginStats) {

            sender.sendMessage("${plugin.prefix}プラグインが稼働停止しています。")

            return false

        }

        return true

    }

}