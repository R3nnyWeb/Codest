# CreateTestsOperation - создание тестов

## Параметры

| Название          | Тип  | Обязательность | Описание       |
|-------------------|------|----------------|----------------|
| TaskId            | path | +              | Id задачи      |
| UserId            | JWT  | +              | Id пользователя |
| CreateTaskRequest | body | +              | Запрос         |

## Алгоритм

1. Обогатится задачей из **tasks** (или в кеше) по id = test.taskId
   1. Если не найдено, выбросить ошибку **TaskNotFound (422)**
   2. Если **tasks.userId != userId из JWT** , выбросить ошибку **Forbidden (403)**
2. Обогатиться тестами из **tests** по **taskId**
3. Убрать из тестов в запросе такие, которые уже есть или задублированы
   4. Если найдены противоречивые или количество элементов в inputData != колву в текущих тестах - выбрость **TestsNotCorrect (422)**
4. Сохранить новые тесты в **tests**
5. Отправить событие об инвалидции кеша в топик **codest.cache.invalidate** в cоответствии с [контрактом](../events/CacheInvalidateEvent.md)