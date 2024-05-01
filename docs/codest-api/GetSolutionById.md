# GetTaskFull - получение полного описания задачи

Параметры

| Имя        | Тип  | Обязательность |
|------------|------|----------------|
| solutionId | path | +              |

## Алгоритм
1. Обогатится попыткой из **attempts** (или в кеше)
   1. Если не найдено, выбросить ошибку **AttemptNotFound (422)**
2. Если **status = 'pending'** и **createdAt - now() > 1 минута**, то
   1. обновить запись в **attempts** по **id = solutionId**

      | Поле     | Значение                    |
          |----------|-----------------------------|
      | status   | internal_error              |
      | error    | Ошибка. id = **solutionId** |
   2. Отправить событие об инвалидции кеша в топик **codest.cache.invalidate** в cоответсвии с [контрактом](../events/CacheInvalidateEvent.md)
4. Вернуть ответ в соответсвии мапингу из [openApi](codest-api-openapi.yaml)