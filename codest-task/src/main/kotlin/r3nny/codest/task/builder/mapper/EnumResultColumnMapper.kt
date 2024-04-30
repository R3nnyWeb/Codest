package r3nny.codest.task.builder.mapper

import r3nny.codest.task.dto.common.Level
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.database.jdbc.mapper.result.JdbcResultColumnMapper
import java.sql.ResultSet
import r3nny.codest.shared.domain.Type
import ru.tinkoff.kora.database.jdbc.mapper.parameter.JdbcParameterColumnMapper
import java.sql.PreparedStatement

@Component
class EnumLevelResultColumnMapper: JdbcResultColumnMapper<Level> {
    override fun apply(row: ResultSet?, index: Int): Level {
        return Level.fromString(row!!.getString(index).uppercase())
    }
}

@Component
class EnumLevelParameterColumnMapper: JdbcParameterColumnMapper<Level> {

    override fun set(stmt: PreparedStatement?, index: Int, value: Level?) {
        stmt!!.setString(index, value!!.name)
    }
}

@Component
class EnumTypeResultColumnMapper: JdbcResultColumnMapper<Type> {
    override fun apply(row: ResultSet?, index: Int): Type {
        return Type.fromString(row!!.getString(index).uppercase())
    }
}

@Component
class EnumTypeParameterColumnMapper: JdbcParameterColumnMapper<Type> {

    override fun set(stmt: PreparedStatement?, index: Int, value: Type?) {
        stmt!!.setString(index, value!!.name)
    }
}
