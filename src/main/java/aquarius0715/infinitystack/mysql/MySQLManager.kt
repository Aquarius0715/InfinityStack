package aquarius0715.infinitystack.mysql

import aquarius0715.infinitystack.main.InfinityStack
import org.bukkit.Bukkit
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.logging.Level

/**
 * Created by takatronix on 2017/03/05.
 *
 * Refactored by Aquarius0715 on 2020/08/14
 */
class MySQLManager(private val plugin: InfinityStack, private val conName: String) {
    var debugMode = false
    private var host: String? = null
    private var db: String? = null
    private var user: String? = null
    private var pass: String? = null
    private var port: String? = null
    private var connected = false
    private var st: Statement? = null
    private var con: Connection? = null
    private var mysql: MySQLFunc? = null

    /////////////////////////////////
    //       設定ファイル読み込み
    /////////////////////////////////
    fun loadConfig() {
        //   plugin.getLogger().info("MYSQL Config loading");
        plugin.reloadConfig()
        host = plugin.config.getString("mysql.host")
        user = plugin.config.getString("mysql.user")
        pass = plugin.config.getString("mysql.pass")
        port = plugin.config.getString("mysql.port")
        db = plugin.config.getString("mysql.db")
        //  plugin.getLogger().info("Config loaded");
    }

    fun commit() {
        try {
            con!!.commit()
        } catch (e: Exception) {
        }
    }

    ////////////////////////////////
    //       接続
    ////////////////////////////////
    fun Connect(host: String?, db: String?, user: String?, pass: String?, port: String?): Boolean {
        this.host = host
        this.db = db
        this.user = user
        this.pass = pass
        mysql = MySQLFunc(host, db, user, pass, port)
        con = mysql!!.open()
        if (con == null) {
            Bukkit.getLogger().info("failed to open MYSQL")
            return false
        }
        try {
            st = con!!.createStatement()
            connected = true
            plugin.logger.info("[$conName] Connected to the database.")
        } catch (var6: SQLException) {
            connected = false
            plugin.logger.info("[$conName] Could not connect to the database.")
        }
        mysql!!.close(con)
        return connected
    }

    //////////////////////////////////////////
    //         接続確認
    //////////////////////////////////////////
    fun connectCheck(): Boolean {
        return Connect(host, db, user, pass, port)
    }

    ////////////////////////////////
    //     行数を数える
    ////////////////////////////////
    fun countRows(table: String): Int {
        var count = 0
        val set = query(String.format("SELECT * FROM %s", table))
        try {
            while (set!!.next()) {
                ++count
            }
        } catch (var5: SQLException) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not select all rows from table: " + table + ", error: " + var5.errorCode)
        }
        return count
    }

    ////////////////////////////////
    //     レコード数
    ////////////////////////////////
    fun count(table: String): Int {
        val count: Int
        val set = query(String.format("SELECT count(*) from %s", table))
        count = try {
            set!!.getInt("count(*)")
        } catch (var5: SQLException) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not select all rows from table: " + table + ", error: " + var5.errorCode)
            return -1
        }
        return count
    }

    ////////////////////////////////
    //      実行
    ////////////////////////////////
    fun execute(query: String): Boolean {
        mysql = MySQLFunc(host, db, user, pass, port)
        con = mysql!!.open()
        if (con == null) {
            Bukkit.getLogger().info("failed to open MYSQL")
            return false
        }
        var ret = true
        if (debugMode) {
            plugin.logger.info("query:$query")
        }
        try {
            st = con!!.createStatement()
            st!!.execute(query)
        } catch (var3: SQLException) {
            plugin.logger.info("[" + conName + "] Error executing statement: " + var3.errorCode + ":" + var3.localizedMessage)
            plugin.logger.info(query)
            ret = false
        }
        close()
        return ret
    }

    ////////////////////////////////
    //      クエリ
    ////////////////////////////////
    fun query(query: String): ResultSet? {
        mysql = MySQLFunc(host, db, user, pass, port)
        con = mysql!!.open()
        var rs: ResultSet? = null
        if (con == null) {
            Bukkit.getLogger().info("failed to open MYSQL")
            return rs
        }
        if (debugMode) {
            plugin.logger.info("[DEBUG] query:$query")
        }
        try {
            st = con!!.createStatement()
            rs = st!!.executeQuery(query)
        } catch (var4: SQLException) {
            plugin.logger.info("[" + conName + "] Error executing query: " + var4.errorCode)
            plugin.logger.info(query)
        }

//        this.close();
        return rs
    }

    fun close() {
        try {
            st!!.close()
            con!!.close()
            mysql!!.close(con)
        } catch (var4: SQLException) {
        }
    }

    ////////////////////////////////
    //      コンストラクタ
    ////////////////////////////////
    init {
        connected = false
        loadConfig()
        connected = Connect(host, db, user, pass, port)
        if (!connected) {
            plugin.logger.info("Unable to establish a MySQL connection.")
        }

        execute("create table if not exists InfinityStackTable (" +
                "PlayerName VARCHAR(16), " +
                "UUID VARCHAR(36) not null primary key, " +
                "StackStats BOOLEAN" +
                ");")

    }

    /////////////////////////////////
    //接続確認
    /////////////////////////////////

    fun sqlConnectSafely(): Boolean {
        if (!connectCheck()) {
            Bukkit.broadcastMessage("${plugin.prefix}DB接続に失敗したためプラグインを停止します。")
            return false
        }
        return true
    }
}