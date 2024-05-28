# GetTaskList - получение списка задач

Параметры

| Имя        | Тип   | Обязательность | По умолчанию |
|------------|-------|----------------|--------------|
| offset     | query | -              | 0            |
| limit      | query | -              | 10           |
| level      | query | +              | -            |
| search     | query | +              | -            |

## Алгоритм

1. Обогатится задачами из **tasks**.

   | Параметр   | Значение                       |
   |------------|--------------------------------|
   | offset     | **offset**                     |
   | limit      | **limit**                      |
   | level      | **level**, если передано       |
   | name       | **LIKE %query%** если передано |
   | is_enabled | true                           |
   | is_private | false                          |
2. Обогатится количеством страниц и базы с limit = **limit**
3. Вернуть ответ в соответсвии мапингу из [openApi](codest-task-openapi.yaml)