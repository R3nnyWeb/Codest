package r3nny.codest.api.builder.mapper

import r3nny.codest.api.dto.common.Level
import r3nny.codest.api.dto.dao.StatusDto
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.Type
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.database.jdbc.mapper.parameter.JdbcParameterColumnMapper
import ru.tinkoff.kora.database.jdbc.mapper.result.JdbcResultColumnMapper
import java.sql.PreparedStatement
import java.sql.ResultSet

@Component
class EnumLevelResultColumnMapper: JdbcResultColumnMapper<Level> {
    override fun apply(row: ResultSet?, index: Int): Level {
        return Level.fromString(row!!.getString(index).uppercase())
    }
}

@Component
class EnumLevelParameterColumnMapper: JdbcParameterColumnMapper<Level> {

    override fun set(stmt: PreparedStatement?, index: Int, value: Level?) {
        stmt!!.setString(index, value?.name)
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

@Component
class EnumStatusResultColumnMapper : JdbcResultColumnMapper<StatusDto> {
    override fun apply(row: ResultSet?, index: Int): StatusDto {
        return StatusDto.fromDb(row!!.getString(index))
    }
}

@Component
class EnumStatusParameterColumnMapper : JdbcParameterColumnMapper<StatusDto> {

    override fun set(stmt: PreparedStatement?, index: Int, value: StatusDto?) {
        stmt!!.setString(index, value!!.db)
    }
}

@Component
class EnumLanguageResultColumnMapper: JdbcResultColumnMapper<Language> {
    override fun apply(row: ResultSet?, index: Int): Language {
        return Language.fromString(row!!.getString(index).uppercase())
    }
}

@Component
class EnumLanguageParameterColumnMapper: JdbcParameterColumnMapper<Language> {

    override fun set(stmt: PreparedStatement?, index: Int, value: Language?) {
        stmt!!.setString(index, value!!.name)
    }
}