package r3nny.codest.api.cache

import r3nny.codest.api.dto.dao.AttemptDto
import ru.tinkoff.kora.cache.annotation.Cache
import ru.tinkoff.kora.cache.caffeine.CaffeineCache
import java.util.*

@Cache("config.getAttempt")
interface GetAttemptCache : CaffeineCache<UUID, AttemptDto>