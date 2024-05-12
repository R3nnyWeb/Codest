# GetCompetitionById - получение соревнования по id

## Параметры

| Название      | Тип  | Обязательность | Описание        |
|---------------|------|----------------|-----------------|
| CompetitionId | Path | +              | Id соревноваия  |
| UserId        | JWT  | +              | Id пользователя |

## Алгоритм

1. Получить соревнование из **competitions** (или из кеша) по **id = competitionId**
   1. Если не найдено, выбросить **CompetitionNotFound(422)**
2. Вернуть Маппинг:

   | Название      | Значение                                                                                         | 
   |---------------|--------------------------------------------------------------------------------------------------|
   | id            | competitions.id                                                                                  |
   | title         | competitions.title                                                                               |
   | description   | competitions.description                                                                         |
   | startAt       | competitions.startAt                                                                             |
   | endAt         | competitions.endAt                                                                               |
   | isPrivate     | competitions.isPrivate                                                                           |
   | isAuthor      | competitions.userId = userId из JWT                                                              |
   | isParticipate | в competitions_users есть запись competitionId = competitions.id и userId =  competitions.userId |
   | tasks         | Задачи из **/v1/tasks{taskId}**                                                                  |
