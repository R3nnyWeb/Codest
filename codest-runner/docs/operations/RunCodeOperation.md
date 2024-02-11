# RunCodeOperation - запуск кода

Предназначена для компиляции и запуска кода.

## Алгоритм

1. Прочитать из топика **codest.runner.request** и декодировать в соотвествии с [контрактом](../../../codest-shared/docs/Runner/RunCodeRequestEvent.md)
2. Сохранить код файлом в
   `tmp/${key из события}.` + **languages[language из 1.i].extension** из [настроек](../settings.md)
3. Если **commandToCompile** из [настроек](../settings.md) != null для **language** из 1.i
    1. Скомпилировать код. При ошибке компиляции:
        1. Положить в топик **codest.runner.response** сообщение
           по [контракту](../../../codest-shared/docs/Runner/RunCodeResponseEvent.md) и **key = event.key**

           | Поле      | Значение                        |
           |-----------|---------------------------------|
           | errorType | COMPILE_ERROR                   |
           | output    | Сообщение из вывода компилятора |
4. Выполнить код командой **commandToRun** из [настроек](../settings.md) , полученый в 3.i или 1.i
    1. Каждый элемент из **input[]** из события отправить в стандартный поток ввода
    2. Стандартный поток вывода записать в **result[]** по строкам
    3. Поток ошибок записать в **errors**
5. Если **errors** **не пустой**:
    1. Положить в топик **codest.runner.response** сообщение
       по [контракту](../../../codest-shared/docs/Runner/RunCodeResponseEvent.md) и **key = event.key**:

       | Поле      | Значение      |
       |-----------|---------------|
       | errorType | RUNTIME_ERROR |
       | output    | errors        |
6. **Иначе**
    1. Положить в топик **codest.runner.response** сообщение
       по [контракту](../../../codest-shared/docs/Runner/RunCodeResponseEvent.md) и **key = event.key**:

       | Поле      | Значение |
       |-----------|----------|
       | errorType | null     |
       | output    | result   |
7. Если 3 или 4 занимают более **maxTime** из [настроек](../settings.md), то прервать выполнение и положить в топик **codest.runner.response** сообщение
   по [контракту](../../../codest-shared/docs/Runner/RunCodeResponseEvent.md) и **key = event.key**:
    
    | Поле      | Значение                  |
    |-----------|---------------------------|
    | errorType | TIME_EXCEED_ERROR         |
    | output    | "Время решения превышено" |

В случае любой непредвиденной ошибки при выполнении одной задачи отменить ее выполнение и:
1. Залогировать ошибку **ParseRunnerRequestError**
2. Положить в топик **codest.runner.response** сообщение
по [контракту](../../../codest-shared/docs/Runner/RunCodeResponseEvent.md) и **key = event.key**:

  | Поле      | Значение                |
  |-----------|-------------------------|
  | errorType | INTERNAL_ERROR          |
  | output    | Сообщение из исключения |