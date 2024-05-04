<template>
  <app-loader v-if="isLoading" style="display: flex; align-items: center;"/>
  <div v-show="!isLoading">
    <div v-if="!isSuccess">
      <div class="form-item">
        <app-input v-model="v$.username.$model" :errors="v$.username.$errors" :label="'Username'"
                   :placeholder="'Введите username'"
                   :type="'email'"/>
      </div>
      <div class="form-item">
        <app-input v-model="v$.password.$model" :errors="v$.password.$errors" :label="'Пароль'"
                   :placeholder="'Введите пароль'" :type="'password'"/>
      </div>
      <p v-if="isError" style="margin-top: 10px;text-align: center; color: var(--color--text-error)">Неправильный email
        или пароль</p>
      <div class="form-item">
        <app-button :disabled="v$.$errors.length !== 0" :full-width="true" @click="send">Войти</app-button>
      </div>
      <h5>или <span style="color: var(--color-main)" @click="$emit('register')">Зарегистрироваться</span></h5>
    </div>
    <div v-else>
      Успешно
    </div>
  </div>
</template>

<script setup>

import {computed, reactive} from "vue";
import AppButton from "@/components/ui/AppButton.vue";
import AppInput from "@/components/ui/AppInput.vue";
import useVuelidate from "@vuelidate/core";
import {helpers, required} from "@vuelidate/validators";
import {useStore} from "vuex";
import AppLoader from "@/components/ui/AppLoader.vue";

const form = reactive(
    {
      username: '',
      password: ''
    }
)
const rules = {

  username: {
    required: helpers.withMessage("Введите Username", required),
  },
  password: {required: helpers.withMessage("Введите пароль", required)},
}
const v$ = useVuelidate(rules, form);
const store = useStore();
const isError = computed(() => store.getters['user/isLoginError'])
const isSuccess = computed(() => store.getters['user/isAuth'])
const isLoading = computed(() => store.getters['user/isLoading'])
const send = async () => {
  const result = await v$.value.$validate()
  if (result) {
    await store.dispatch('user/login', {
      username: form.username,
      password: form.password
    })
  }
}

</script>

<style scoped>
.form-item {
  margin-top: 20px;
}

h5 {
  margin-top: 10px;
  font-size: 14px;
  text-align: center;
}

span {
  cursor: pointer;
}

span:hover {
  opacity: .7;
}
</style>