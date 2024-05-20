# GetOwnCompetitions - получение соревнования созданных пользователем

## Параметры

| Название      | Тип  | Обязательность | Описание        |
|---------------|------|----------------|-----------------|
| UserId        | JWT  | +              | Id пользователя |

## Алгоритм

1. Получить соревнования из **competitions** по **user_id = userId**
      
   | Название      | Значение                                                                                         | 
   |---------------|--------------------------------------------------------------------------------------------------|
   | id            | competitions.id                                                                                  |
   | title         | competitions.title                                                                               |
   | startAt       | competitions.startAt                                                                             |
   | endAt         | competitions.endAt                                                                               |
   | isPrivate     | competitions.isPrivate                                                                           |
   | isParticipate | в competitions_users есть запись competitionId = competitions.id и userId =  competitions.userId |