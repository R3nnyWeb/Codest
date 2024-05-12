# GetParticipatingCompetitions - получение соревнования в которых участвовал пользователь

## Параметры

| Название      | Тип  | Обязательность | Описание        |
|---------------|------|----------------|-----------------|
| UserId        | JWT  | +              | Id пользователя |

## Алгоритм

1. Получить id соревнований из **competitions_users** по **competitions_users.user_id** = userId из JWT
2. Вернуть. Маппинг:
   
   | Название      | Значение               | 
   |---------------|------------------------|
   | id            | competitions.id        |
   | title         | competitions.title     |
   | startAt       | competitions.startAt   |
   | endAt         | competitions.endAt     |
   | isPrivate     | competitions.isPrivate |
   | isParticipate | true                   |