# HandleRunCodeFinish - обработка завершения запуска кода


## Алгоритм

1. Прослушать событие из топика **codest.runner.response** и декодировать в соотвествии с [контрактом](../../../codest-shared/docs/Runner/RunCodeResponseEvent.md)
2. Обогатится попыткой в **attempts**. Если не найдено выбросить **AttemptNotFoundError** и перейти к обновлению попытки с **InternalError** и завершить обработку
3. Обогатится тестами по id задачи из **attempts**
4. Если **errorType** из события = null: 
   1. Объеденить **outputData** из **tests** и из массива. Пройти по итоговому zip массиву. При несовпадении ожидаемого результата и реального 
   обновить попытку:
   
      | Поле     | Значение                   |
          |----------|----------------------------|
      | status   | test_error                 |
      | error    | Ожидаемый и реальный ответ |
   2. Иначе   
   
      | Поле     | Значение                   |
          |----------|----------------------------|
      | status   | accepted                 |
5. Иначе выполнить обновление в соотвествии с маппингом:
   
      | errorType | status                     | error             |
          |-----------|------------------------|-------------------|
      | RUNTIME_ERROR    | runtime_error                 | output из события |
      | INTERNAL_ERROR    | internal_error                 | output из события |
      | COMPILE_ERROR    | compile_error                 | output из события |
      | TIME_EXCEED_ERROR    | timeout_error                 | output из события |
6. Отправить событие об инвалидции кеша в топик **codest.cache.invalidate** в cоответсвии с [контрактом](../../../codest-shared/docs/Runner/CacheInvalidateEvent.md)

7. При возникновении неожиданной ошибки обновить 
   
      | Поле     | Значение       |
          |----------|----------------|
      | status   | internal_error |
      | error    | Сообщение      |