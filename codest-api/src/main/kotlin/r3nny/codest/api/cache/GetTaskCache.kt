package r3nny.codest.api.cache

import r3nny.codest.api.dto.dao.TaskDto
import ru.tinkoff.kora.cache.annotation.Cache
import ru.tinkoff.kora.cache.caffeine.CaffeineCache
import java.util.*

@Cache("cache.getTask")
interface GetTaskCache : CaffeineCache<UUID, TaskDto>
