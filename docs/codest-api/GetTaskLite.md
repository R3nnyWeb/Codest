# GetTaskLite - получение задачи для фронта

Параметры

| Имя    | Тип  | Обязательность |
|--------|------|----------------|
| taskId | path | +              |
| userId | JWT  | -              |

## Алгоритм
1. Обогатится задачей из **tasks** (или в кеше)
   1. Если не найдено, выбросить ошибку **TaskNotFound (422)**
2. Если userId передан обогатится в **attempts** по taskId и userId
2. Если userId из JWT передан и равен userId из задачи, то считать **attempts.isAuthor = true**
3. Вернуть ответ в соответсвии мапингу из [openApi](codest-task-openapi.yaml)