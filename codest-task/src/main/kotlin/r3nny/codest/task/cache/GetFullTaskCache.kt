package r3nny.codest.task.cache

import r3nny.codest.task.dto.dao.TaskDto
import ru.tinkoff.kora.cache.annotation.Cache
import ru.tinkoff.kora.cache.caffeine.CaffeineCache
import java.util.UUID

@Cache("cache.getFullTask")
interface GetFullTaskCache : CaffeineCache<UUID, TaskDto?>
