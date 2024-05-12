# EnableTaskOperation - изменения статуса общедоступности задачи

## Параметры

| Название | Тип     | Обязательность | Описание        |
|----------|---------|----------------|-----------------|
| TaskId   | path    | +              | Id задачи       |
| UserId   | JWT     | +              | Id пользователя |
| enable   | boolean | +              | Статус          |

## Алгоритм

1. Обогатится задачей из **tasks** (или в кеше) по id = test.taskId
   1. Если не найдено, выбросить ошибку **TaskNotFound (422)**
   2. Если **tasks.userId != userId из JWT** , выбросить ошибку **Forbidden (403)**
2. Если **enable = false**
   1. Обновить tasks. **is_enabled = enable** по **id = taskId**
3. **Иначе** 
   1. Обогатится успешными решеними из **attempts** по **task_id = taskId**
      2. **Если** в полученных решениях есть все языки из task.languages обновить **is_enabled = enable** по **id = taskId**
      3. **Иначе** выбросить **EnableTaskError(422)**
4. Отправить событие об инвалидции кеша в топик **codest.cache.invalidate** в cоответствии с [контрактом](../events/CacheInvalidateEvent.md)