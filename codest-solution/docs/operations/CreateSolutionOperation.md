# CreateSolutionOperation - создание решения

## Параметры

| Название | Тип    | Обязательность | Описание        |
|----------|--------|----------------|-----------------|
| TaskId   | path   | +              | Id задачи       |
| UserId   | header | +              | Id пользователя |

## Алгоритм

1. Обогатится техническими особенностями задачи в **task**. Выполнить запрос на /v1/tasks/{taskId}/internal
    1. Параметры

       | Название | Значение                 | Тип   |
               |----------|--------------------------|-------|
       | taskId   | taskId из вх. параметров | path  |
       | language | language из тела запросв | query |
    2. Если **code = 422** и **errorCode = 'TaskNotFound'** выбросить **TaskNotFound** и завершить обработку
    3. Если **code = 422** и **errorCode = 'InternalNotExist'** выбросить **LanguageNotAcceptable** и завершить
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
   с [контрактом](../../../codest-shared/docs/Runner/RunCodeRequestEvent.md)
   Маппинг:

   | Поле     | Значение                                           |
       |----------|----------------------------------------------------|
   | input[]  | Входные данные из task.tests                       |
   | code     | task.driver с подставленным code по ключу solution |
   | language | language из запроса                                |