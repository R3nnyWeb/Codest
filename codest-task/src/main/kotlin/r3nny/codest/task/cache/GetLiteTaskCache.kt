package r3nny.codest.task.cache

import r3nny.codest.task.dto.dao.TaskLiteDto
import ru.tinkoff.kora.cache.annotation.Cache
import ru.tinkoff.kora.cache.caffeine.CaffeineCache
import java.util.UUID

@Cache("cache.getLiteTask")
interface GetLiteTaskCache : CaffeineCache<UUID, TaskLiteDto?>
