<template>
  <div style="margin-top: 40px;">
    <h2>Создание задачи</h2>
    <div class="form">
      <div class="form-item">
        <app-input v-model="v$.name.$model" :errors="v$.name.$errors" :label="'Название задачи'"
                   :placeholder="'Введите название задачи'" :type="'text'"
        />
      </div>
      <div class="form-item">
        <app-input v-model="v$.methodName.$model" :errors="v$.methodName.$errors" :label="'Название функции'"
                   :placeholder="'Введите название функции'" :type="'text'"
        />
      </div>
      <div class="form-item custom-input">
        <label for="level">Уровень сложности</label>
        <select id="level">
          <option v-for="level in levels" :key="level" :value="level">{{ level.toUpperCase() }}</option>
        </select>
        <label v-for="error in v$.level.$errors" :key="error.$uid"
               style="color: var(--color--text-error)">{{ error.$message }}</label>
      </div>

      <div class="form-item">
        <label>Количество входных типов</label>
        <input type="number" min="1" max="10">
      </div>

      <div class="form-item ">
        <label for="level">Описание задачи</label>
        <div class="description">
          <textarea v-model="v$.description.$model" placeholder="Описание задачи в markdown"></textarea>
        </div>
        <markdown-renderer :source="v$.description.$model"/>
      </div>
    </div>
  </div>
</template>

<script setup>

import {reactive, ref} from "vue";
import {helpers, maxLength, minLength, required} from "@vuelidate/validators";
import useVuelidate from "@vuelidate/core";
import AppInput from "@/components/ui/AppInput.vue";
import MarkdownRenderer from "@/components/markdown/MarkdownRenderer.vue";

const form = reactive(
    {
      name: '',
      level: '',
      description: '',
      languages: reactive(['java', 'python']),
      startCodes: reactive([]),
      isPrivate: false,
      methodName: '',
      tests: reactive([])
    }
)

const levels = ['easy', 'medium', 'hard']

const inputTypesCount = ref(0)

const mustContainsAllLanugages = (value) => {
  return value.keys.includes(this.languages)
}

const rules = {
  name: {
    required: helpers.withMessage("Введите название задачи", required),
    minLength: helpers.withMessage("Название задачи минимум 2 символа", minLength(2)),
    maxLength: helpers.withMessage("Название задачи максимум 50 символов", maxLength(50))
  },
  level: {
    required: helpers.withMessage("Выберите уровень", required),
  },
  description: {
    required: helpers.withMessage("Введите описание задачи", required),
    minLength: helpers.withMessage("Описание задачи минимум 2 символа", minLength(2)),
    maxLength: helpers.withMessage("Описание задачи максимум 5000 символов", maxLength(5000))
  },
  methodName: {
    required: helpers.withMessage("Введите название метода", required),
    minLength: helpers.withMessage("Название метода минимум 2 символа", minLength(2)),
    maxLength: helpers.withMessage("Название метода максимум 50 символов", maxLength(50))
  },
  tests: {
    minLength: helpers.withMessage("Минимум 2 теста", minLength(2))
  },
  startCodes: {
    mustContainsAllLanugages
  }
}

const v$ = useVuelidate(rules, form);

</script>

<style lang="scss" scoped>
.custom-input {
  display: flex;
  flex-direction: column;
}

.form-item {
  margin-top: 20px;
}

.form {
  max-width: 500px;
}

.description {
  margin: 20px 0;
  textarea {
    width: 100%;
    height: 200px;
  }
}

select {
  max-width: 100px;
  border: none;
  outline: none;
  border-bottom: 2px solid var(--color-border);
  background: transparent;
  padding: 11px 0;
  font-size: 14px;
  color: var(--color-text);
}

label {
  font-size: 12px;
}

input {
  border: none;
  outline: none;
  border-bottom: 2px solid var(--color-border);
  background: transparent;
  padding: 11px 0;
  font-size: 14px;
  transition: all .2s ease-in;
  color: var(--color-text);

  &::placeholder {
    color: var(--color-text);
    opacity: .2;
  }

  &:focus {
    border-bottom: 2px solid var(--color-border-hover);
  }
}
</style>