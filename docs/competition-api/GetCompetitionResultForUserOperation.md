# GetCompetitionResultForUserOperation - получение результатов соревнований

## Параметры

| Название      | Тип   | Обязательность | Описание            |
|---------------|-------|---------------|---------------------|
| CompetitionId | Path  | +             | Id соревноваия      |
| UserId        | JWT   | +             | Id пользователя JWT |
| userId        | query | +             | Id пользователя     |

## Алгоритм

1. Получить соревнование из **competitions** по **id = competitionId**
    1. Если не найдено, выбросить **CompetitionNotFound(422)**
    2. Если соревнование не закончилось выбросить **CompetitionNotEnded(422)**
    3. Если **userId из JWT** не автор и не участник выбросить **Forbidden(403)**
2. Вернуть список. Маппинг:

   | Название          | Значение                                                                                               | 
   |-------------------|--------------------------------------------------------------------------------------------------------|
   | userId            | query.userId                                                                                           |
   | username          | users.username                                                                                         |
   | points            | Обогатиться последними успешными решениями для задач из competitions_tasks и сложить полученные points |
   | tasks[].task      | Задачи из **/v1/tasks{taskId}**  taskId из competition_tasks                                           |
   | tasks[].attemptId | Последняя успешная попытка для этой задачи и этого соревнования                                        |
   | tasks[].isSolved  | tasks[].attemptId != null                                                                              |