package aquarius0715.infinitystack.mysql

import org.bukkit.Bukkit
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.logging.Level

/**
 * Created by takatronix on 2017/03/05.
 */

class MySQLFunc(host: String?, db: String?, user: String?, pass: String?, port: String?) {

    private var host: String? = null
    private var db: String? = null
    private var user: String? = null
    private var pass: String? = null
    private var port: String? = null
    var con: Connection? = null

    fun open(): Connection? {

        try {

            Class.forName("com.mysql.jdbc.Driver")

            con = DriverManager.getConnection("jdbc:mysql://$host:$port/$db?useSSL=false", user, pass)

            return con

        } catch (var2: SQLException) {

            Bukkit.getLogger().log(Level.SEVERE, "Could not connect to MySQL server, error code: " + var2.errorCode)

        } catch (var3: ClassNotFoundException) {

            Bukkit.getLogger().log(Level.SEVERE, "JDBC driver was not found in this machine.")

        }

        return con

    }

    fun checkConnection(): Boolean {

        return con != null

    }

    fun close(c: Connection?) {

        c?.close()

    }

    init {

        this.host = host
        this.db = db
        this.user = user
        this.pass = pass
        this.port = port

    }

}