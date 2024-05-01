package r3nny.codest.solution.cache

import r3nny.codest.solution.dto.dao.AttemptDto
import ru.tinkoff.kora.cache.annotation.Cache
import ru.tinkoff.kora.cache.caffeine.CaffeineCache
import java.util.*

@Cache("config.getAttempt")
interface GetAttemptCache : CaffeineCache<UUID, AttemptDto>