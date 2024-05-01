package r3nny.codest.api.builder.mapper

import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.Type
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.database.jdbc.mapper.parameter.JdbcParameterColumnMapper
import ru.tinkoff.kora.database.jdbc.mapper.result.JdbcResultColumnMapper
import java.sql.PreparedStatement
import java.sql.ResultSet

@Component
class ListResultColumnMapper: JdbcResultColumnMapper<List<String>> {
    override fun apply(row: ResultSet?, index: Int): List<String> {
        return (row?.getArray(index)!!.array as Array<String>).toList()
    }
}

@Component
class SetLanguageResultColumnMapper: JdbcResultColumnMapper<Set<Language>> {
    override fun apply(row: ResultSet?, index: Int): Set<Language> {
        return (row?.getArray(index)!!.array as Array<String>).map { Language.fromString(it.uppercase()) }.toSet()
    }
}

@Component
class ListTypeResultColumnMapper: JdbcResultColumnMapper<List<Type>> {
    override fun apply(row: ResultSet?, index: Int): List<Type> {
        return (row?.getArray(index)!!.array as Array<String>).map { Type.fromString(it.uppercase()) }.toList()
    }
}

@Component
class SetLanguageParameterColumnMapper: JdbcParameterColumnMapper<Set<Language>> {
    override fun set(stmt: PreparedStatement?, index: Int, value: Set<Language>?) {
        stmt!!.setArray(index, stmt.connection.createArrayOf("varchar", value?.map { it.name }?.toTypedArray()))
    }
}

@Component
class ListTypeParameterColumnMapper: JdbcParameterColumnMapper<List<Type>> {
    override fun set(stmt: PreparedStatement?, index: Int, value: List<Type>?) {
        stmt!!.setArray(index, stmt.connection.createArrayOf("varchar", value?.map { it.name }?.toTypedArray()))
    }
}

@Component
class ListStringParameterColumnMapper: JdbcParameterColumnMapper<List<String>> {
    override fun set(stmt: PreparedStatement?, index: Int, value: List<String>?) {
        stmt!!.setArray(index, stmt.connection.createArrayOf("varchar", value?.toTypedArray()))
    }
}