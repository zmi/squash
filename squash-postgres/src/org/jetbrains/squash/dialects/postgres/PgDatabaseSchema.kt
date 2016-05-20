package org.jetbrains.squash.dialects.postgres

import org.jetbrains.squash.dialect.*
import org.jetbrains.squash.drivers.*
import org.jetbrains.squash.schema.*
import java.sql.*

class PgDatabaseSchema(dialect: SQLDialect, jdbcConnection: Connection) : JDBCDatabaseSchema(dialect, jdbcConnection) {
    override fun tables(): Sequence<DatabaseSchema.SchemaTable> {
        val schema = currentSchema()
        val tableTypes = if (schema.startsWith("pg_temp_")) arrayOf("TEMPORARY TABLE") else arrayOf("TABLE")
        val resultSet = metadata.getTables(catalogue, currentSchema(), null, tableTypes)
        return JDBCResponse(resultSet).rows.map { SchemaTable(it.get<String>("TABLE_NAME"), this) }
    }
}