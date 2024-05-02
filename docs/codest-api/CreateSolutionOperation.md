# CreateSolutionOperation - создание решения

## Параметры

| Название | Тип    | Обязательность | Описание        |
|----------|--------|----------------|-----------------|
| TaskId   | path   | +              | Id задачи       |
| UserId   | header | +              | Id пользователя |

## Алгоритм

1. Обогатится задачей в **tasks** (или кеше)
   1. Если задача не найдена выбросить **TaskNotFound** и завершить обработку
   2. **language** из тела не в **languages** из задачи выбросить **LanguageNotAcceptable** и завершить
       обработку
2. Сохранить в **attempts**. Маппинг:

   | Поле     | Значение            |
       |----------|---------------------|
   | id       | uuid()              |
   | taskId   | taskId из path      |
   | userId   | userId из header    |
   | code     | code из запроса     |
   | language | language из запроса |
3. Отправить сообщение в **codest.runner.request** в соотвествии
   с [контрактом](../events/RunCodeRequestEvent.md) и ключем id из attempts
   Маппинг:

   | Поле     | Значение                                                                            |
       |----------|-------------------------------------------------------------------------------------|
   | tests[]  | task.tests                                                                          |
   | code     | task.driver с подставленным code по ключу solution и testsCount = task.tests.size() |
   | language | language из запроса                                                                 |