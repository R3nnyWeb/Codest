# CreateCompetitionOperation - создание соревнования

## Параметры

| Название | Тип  | Обязательность | Описание        |
|----------|------|----------------|-----------------|
| request  | body | +              | тело запроса    |
| UserId   | JWT  | +              | Id пользователя |

## Алгоритм

1. Для каждой **taskId** из **request.tasks** выполнить запрос на **/v1/tasks{taskId}**
   1. Если найдена задача с **isPrivate = true и isAuthor = false**, то выбросить **TaskIsPrivate(422)**
2. Если **startAt < now** или **endAt < startAt** выбросить исключение **PeriodIncorrect(422)**
3. Сохранить попытку в competitions
4. Сохранить задачи в competitions_tasks
5. Вернуть **id**