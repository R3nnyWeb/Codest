package r3nny.codest.task.builder.mapper

import r3nny.codest.shared.domain.Language
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.database.jdbc.mapper.result.JdbcResultColumnMapper
import ru.tinkoff.kora.json.common.JsonReader
import java.sql.ResultSet

@Component
class MapLanguageResultColumnMapper(
    private val reader: JsonReader<Map<String, String>>
): JdbcResultColumnMapper<Map<Language, String>> {
    override fun apply(row: ResultSet?, index: Int): Map<Language, String> {
        val stringMap = reader.read(row?.getString(index))
        return stringMap.mapKeys { Language.fromString(it.key.uppercase()) }
    }
}
