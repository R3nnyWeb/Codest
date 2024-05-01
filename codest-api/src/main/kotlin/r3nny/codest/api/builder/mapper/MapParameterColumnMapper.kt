package r3nny.codest.api.builder.mapper

import r3nny.codest.shared.domain.Language
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.database.jdbc.mapper.parameter.JdbcParameterColumnMapper
import ru.tinkoff.kora.json.common.JsonWriter
import java.sql.PreparedStatement

@Component
class MapParameterColumnMapper(
    private val writer: JsonWriter<Map<String, String>>,
): JdbcParameterColumnMapper<Map<Language, String>> {
    override fun set(stmt: PreparedStatement?, index: Int, value: Map<Language, String>?) {
        val valueMapped = value!!.mapKeys { it.key.name }
        stmt?.setString(index, writer.toString(valueMapped))
    }

}
