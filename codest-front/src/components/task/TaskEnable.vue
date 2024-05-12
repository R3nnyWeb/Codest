<template>
  <app-loader v-if="loading" style="max-width: 50px"/>
  <div v-else style="display:flex;">
    <div >
      <p v-if="!task.isEnabled">
        Задача недоступна для соревнований {{ task.isPrivate ? '' : 'и в общего списка' }}
      </p>
      <p v-else>Задача доступна для добавления в сореванования {{ task.isPrivate ? '' : 'и в общем списке' }}</p>
    </div>
    <a @click="changeTaskStatus"> Переключить</a>
  </div>
  <p class="error" v-if="errorMessage">
    {{errorMessage}}
  </p>

</template>
<script setup>
import {ref} from "vue";
import taskApi from "@/api/taskApi";
import AppLoader from "@/components/ui/AppLoader.vue";

const props = defineProps({
  task: {
    type: Object,
    required: true
  }
})
const loading = ref(false)
const errorMessage = ref(null)
const changeTaskStatus = () => {
  loading.value = true
  errorMessage.value = null
  taskApi.changeTaskStatus(props.task.id, !props.task.isEnabled)
      .then(() => {
        // eslint-disable-next-line vue/no-mutating-props
        props.task.isEnabled = !props.task.isEnabled
      })
      .catch((e) => {
        console.log(e)
        errorMessage.value = e.response.data.errorMessage
      }).finally(() => {
        loading.value = false
      }
  )
}

</script>
<style lang="scss" scoped>
a {
  color: var(--color-main);
  cursor: pointer;
  text-decoration: underline;
  margin-left: 10px;
}
.error {
  color: var(--color--text-error);
}
</style>