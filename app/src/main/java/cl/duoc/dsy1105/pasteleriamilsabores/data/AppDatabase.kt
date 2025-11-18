package cl.duoc.dsy1105.pasteleriamilsabores.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import cl.duoc.dsy1105.pasteleriamilsabores.model.CartItem
import cl.duoc.dsy1105.pasteleriamilsabores.model.Product

@Database(
    entities = [CartItem::class, Product::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS products(
                        id INTEGER NOT NULL PRIMARY KEY,
                        name TEXT NOT NULL,
                        description TEXT NOT NULL,
                        price INTEGER NOT NULL,
                        imageResId INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE products_new(
                        id INTEGER NOT NULL PRIMARY KEY,
                        name TEXT NOT NULL,
                        description TEXT NOT NULL,
                        price INTEGER NOT NULL,
                        imageUrl TEXT NOT NULL
                    )
                    """.trimIndent()
                )

                db.execSQL("DROP TABLE products")
                db.execSQL("ALTER TABLE products_new RENAME TO products")
            }
        }

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pasteleria_db_v3"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
