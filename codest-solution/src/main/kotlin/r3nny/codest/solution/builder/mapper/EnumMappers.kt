package r3nny.codest.solution.builder.mapper

import r3nny.codest.shared.domain.Language
import r3nny.codest.solution.dto.dao.StatusDto
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.database.jdbc.mapper.parameter.JdbcParameterColumnMapper
import ru.tinkoff.kora.database.jdbc.mapper.result.JdbcResultColumnMapper
import java.sql.PreparedStatement
import java.sql.ResultSet

@Component
class EnumLanguageResultColumnMapper : JdbcResultColumnMapper<Language> {
    override fun apply(row: ResultSet?, index: Int): Language {
        return Language.fromString(row!!.getString(index).uppercase())
    }
}

@Component
class EnumLanguageParameterColumnMapper : JdbcParameterColumnMapper<Language> {

    override fun set(stmt: PreparedStatement?, index: Int, value: Language?) {
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