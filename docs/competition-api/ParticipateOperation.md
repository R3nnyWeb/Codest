# ParticipateOperation - Принять участие

## Параметры

| Название      | Тип   | Обязательность | Описание            |
|---------------|-------|---------------|---------------------|
| CompetitionId | Path  | +             | Id соревноваия      |
| UserId        | JWT   | +             | Id пользователя JWT |

## Алгоритм

1. Получить соревнование из **competitions** по **id = competitionId**
    1. Если не найдено, выбросить **CompetitionNotFound(422)**
    2. Если соревнование началось выбросить **CompetitionAlreadyStarted(422)**
2. Вставить запись в **competitions_users**                                                            