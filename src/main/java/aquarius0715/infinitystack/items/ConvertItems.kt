package aquarius0715.infinitystack.items

import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.jvm.Throws

class ConvertItems {

    fun itemFromBase64(data: String?): ItemStack? {

        return try {

            val inputStream = ByteArrayInputStream(Base64Coder.decodeLines(data))

            val dataInput = BukkitObjectInputStream(inputStream)

            val items = arrayOfNulls<ItemStack>(dataInput.readInt())

            // Read the serialized inventory

            for (i in items.indices) {

                items[i] = dataInput.readObject() as ItemStack

            }

            dataInput.close()

            items[0]

        } catch (e: Exception) {

            null

        }

    }

    @Throws(IllegalStateException::class)

    fun itemToBase64(item: ItemStack?): String {

        return try {

            val outputStream = ByteArrayOutputStream()

            val dataOutput = BukkitObjectOutputStream(outputStream)

            val items = arrayOfNulls<ItemStack>(1)

            items[0] = item

            dataOutput.writeInt(items.size)

            for (i in items.indices) {

                dataOutput.writeObject(items[i])

            }

            dataOutput.close()

            Base64Coder.encodeLines(outputStream.toByteArray())

        } catch (e: Exception) {

            throw IllegalStateException("Unable to save item stacks.", e)

        }
    }
}