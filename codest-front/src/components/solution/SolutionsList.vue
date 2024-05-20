<template>
  <div v-if="loading" style="margin-top: 100px; display: flex; justify-content: center; width: 100%;">
    <app-loader/>
  </div>
  <div v-else style="margin-top: 40px;">
    <div v-for="(solution, n) in solutions" :key="n" @click="onShow(solution.id)">
      <lite-solution-item :is-odd="n % 2 === 0" :solution="solution"/>
    </div>
  </div>

  <app-modal v-if="isShowAttempt" @close="isShowAttempt = false" :max-width-container="'1400px'" :max-width-content="'1360px'" >
    <template #header>
      Решение
    </template>
    <template #body>
      <div v-if="loading" style="margin-top: 100px; display: flex; justify-content: center; width: 100%;">
        <app-loader/>
      </div>
      <div v-else>
        <codemirror
            v-model="showing.code"
            :autofocus="false"
            :disabled="true"
            :extensions="extentios"
            :indent-with-tab="true"
            :style="{ width: '100%', 'min-height': '200px' }"
            :tab-size="4"
            placeholder="Ваше решение..."
        />
        <result-display :attempt="showing"/>
      </div>
    </template>
  </app-modal>
</template>

<script setup>
import LiteSolutionItem from "@/components/solution/LiteSolutionItem.vue";
import {computed, reactive, ref, watch} from "vue";
import attemptApi from "@/api/attemptApi";
import AppLoader from "@/components/ui/AppLoader.vue";
import AppModal from "@/components/ui/AppModal.vue";
import ResultDisplay from "@/components/solution/ResultDisplay.vue";
import {Codemirror} from "vue-codemirror";
import helper from '@/helper'
import {oneDark} from "@codemirror/theme-one-dark";

const isShowAttempt = ref(false)
const showing = reactive({})
const loading = ref(false)
const solutions = reactive([])
const props = defineProps({
  taskId: {
    type: String,
    required: true
  },
  isActive: {
    type: Boolean,
    required: true
  }
})

const onShow = ((attemptId) => {
  isShowAttempt.value = true
  loading.value = true
  attemptApi.getAttemptById(attemptId)
      .then((r) => {
        Object.assign(showing, r.data)
      })
      .catch((e) => console.log(e))
      .finally(() => {
        loading.value = false
      })
})
const extentios = computed(() => {
  const res = []
  if (helper.isDarkTheme())
    res.push(oneDark)

  res.push(helper.getLanguageExtension(showing.language))
  return res
})

watch(() => props.isActive, (newV) => {
  if (newV) {
    loading.value = true
    attemptApi.getAttemptsByTaskId(props.taskId)
        .then((r) => {
          Object.assign(solutions, r.data)
        })
        .catch((e) => console.log(e))
        .finally(() => {
          loading.value = false
        })
  }
});
</script>

<style scoped>

</style>