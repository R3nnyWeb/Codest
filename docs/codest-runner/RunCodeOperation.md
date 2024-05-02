# RunCodeOperation - запуск кода

Предназначена для компиляции, запуска кода и проверки решения.

## Алгоритм

1. Прочитать из топика **codest.runner.request** и декодировать в соотвествии
   с [контрактом](../events/RunCodeRequestEvent.md)
2. Сохранить файл в временную папку
3. Если код необходимо скомпилировать:
    1. Скомпилировать код при помощи **commandToCompile** из настроек для **language** из запроса. При ошибке
       компиляции:
        1. Положить в топик **codest.runner.response** сообщение
           по [контракту](../../../codest-shared/docs/Runner/RunCodeResponseEvent.md) и **key = event.key**

           | Поле      | Значение                        |
                      |-----------|---------------------------------|
           | errorType | COMPILE_ERROR                   |
           | output    | Сообщение из вывода компилятора |
4. Выполнить код командой **commandToRun** из настроек для **language** из запроса, полученый в 3.i или 1.i
    1. Каждый элемент из **input[]** из события отправить в стандартный поток ввода
    2. Стандартный поток вывода записать в **result[]** по строкам
    3. Поток ошибок записать в **errors**
5. Если **errors** **не пустой**:
    1. Положить в топик **codest.runner.response** сообщение
       по [контракту](../events/RunCodeResponseEvent.md) и **key = event.key**:

       | Поле      | Значение       |
              |-----------|----------------|
       | errorType | RUNTIME_ERROR  |
       | output    | input + errors |
6. **Иначе**
    1. Сопоставить массив output и ожидаемые ответы из request. Если значения не совпадают:
        1. Положить в топик **codest.runner.response** сообщение
       по [контракту](../events/RunCodeResponseEvent.md) и **key = event.key**:

       | Поле      | Значение                  |
              |-----------|---------------------------|
       | errorType | TEST_ERROR                |
       | output    | List.of(expected, actual) |
    2. **Иначе** Положить в топик **codest.runner.response** сообщение
       по [контракту](../events/RunCodeResponseEvent.md) и **key = event.key**:

       | Поле      | Значение |
              |-----------|----------|
       | errorType | null     |
       | output    | result   |
7. Если 3 или 4 занимают более **maxTime** из настроек, то прервать выполнение и положить в топик *
   *codest.runner.response** сообщение
   по [контракту](../events/RunCodeResponseEvent.md) и **key = event.key**:

   | Поле      | Значение                   |
       |-----------|----------------------------|
   | errorType | TIME_EXCEED_ERROR          |
   | output    | "Время ожидания превышено" |

В случае любой непредвиденной ошибки при выполнении одной задачи отменить ее выполнение и:

1. Залогировать ошибку **ParseRunnerRequestError**
2. Положить в топик **codest.runner.response** сообщение
   по [контракту](../events/RunCodeResponseEvent.md) и **key = event.key**:

| Поле      | Значение                |
  |-----------|-------------------------|
| errorType | INTERNAL_ERROR          |
| output    | Сообщение из исключения |