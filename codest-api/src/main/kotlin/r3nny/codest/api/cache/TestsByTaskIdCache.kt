package r3nny.codest.api.cache

import r3nny.codest.shared.domain.TestCase
import ru.tinkoff.kora.cache.annotation.Cache
import ru.tinkoff.kora.cache.caffeine.CaffeineCache
import java.util.*

@Cache("cache.testsByTaskId")
interface TestsByTaskIdCache : CaffeineCache<UUID, List<TestCase>>
