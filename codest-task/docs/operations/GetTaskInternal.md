# GetTaskInternal - получение описания задачи для выполнения

Параметры

| Имя      | Тип   | Обязательность |
|----------|-------|----------------|
| taskId   | path  | +              |
| language | query | +              |

## Алгоритм
1. Обогатится задачей из **tasks** (или в кеше)
   1. Если не найдено, выбросить ошибку **TaskNotFound (422)**
   2. Если **language** из запроса не содержится в **languages** из **tasks** выбросить исключение **InternalNotExist**
2. Обогатится тестами из **tests** (или в кеше)
3. Вернуть ответ в соответсвии мапингу из [openApi](../../src/main/resources/openapi/server/codest-task-openapi.yaml)