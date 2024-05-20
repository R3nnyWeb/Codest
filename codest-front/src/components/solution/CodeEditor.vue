<template>
  <div id="code-editor">
    <app-dropdown-select v-model="solution.language" :items="languagesMapped" style="margin-bottom: 20px;"/>
    <codemirror
        v-if="!loading"
        v-model="solution.code"
        :autofocus="true"
        :disabled="$props.pending"
        :extensions="extensions"
        :indent-with-tab="true"
        :style="{ width: '100%', 'min-height': '200px' }"
        :tab-size="4"
        placeholder="Ваше решение..."
    />
    <button :disabled="$props.pending" @click="sendSolution">
      <span v-if="!$props.pending"> Отправить на проверку</span>
    </button>
  </div>

</template>

<script>
import {defineComponent, reactive, ref, shallowRef, watch} from "vue";
import {Codemirror} from "vue-codemirror";
import {java} from "@codemirror/lang-java";
import {useRoute} from "vue-router";
import {oneDark} from "@codemirror/theme-one-dark";
import AppDropdownSelect from "@/components/ui/AppDropdownSelect.vue";
import helper from "@/helper";


export default defineComponent({
  components: {AppDropdownSelect, Codemirror},
  props: {
    languages: Array,
    pending: Boolean,
    templates: Object
  },

  setup(props) {
    const loading = shallowRef(false)
    const route = useRoute()

    const extensions = ref([java()])
    const languagesMapped = props.languages.map((language) => {
      return {
        title: `${language.toUpperCase()}`,
        value: language
      }
    })

    checkForDarkTheme();

    const view = shallowRef()
    const handleReady = (payload) => {
      view.value = payload.view
    }
    const solution = reactive({
      code: props.templates.java,
      language: "java",
      taskId: route.params.id
    })


    function sendSolution() {
      this.$emit('sendAttempt', solution)
    }

    function checkForDarkTheme() {
      if (helper.isDarkTheme())
        extensions.value.push(oneDark)
    }

    watch(
        () => solution.language,
        (lang) => {
          loading.value = true;
          // eslint-disable-next-line vue/no-mutating-props
          props.templates.lang = solution.code
          extensions.value = helper.getLanguageExtension(lang)

          solution.code = props.templates[lang];
          checkForDarkTheme();
          setTimeout(() => loading.value = false, 200)

        }
    )

    return {
      languagesMapped,
      loading,
      sendSolution,
      solution,
      extensions,
      handleReady,
      log: console.log
    }
  }
})
</script>

<style lang="scss" scoped>
button {
  margin-top: 40px;
  background: var(--color-main);
  color: var(--color-text);
  padding: 10px 40px;
  border-radius: 100px;
  box-shadow: 0px 5px 15px 0px rgba(14, 26, 57, 0.20);
  border: none;
  font-size: 16px;
  cursor: pointer;
  transition: all .2s linear;

  &:hover {
    transform: scale(102%);
  }
}

select {

  margin-bottom: 35px;
  font-size: 16px;
  padding: 10px 50px;
  border-radius: 100px;
  border: none;
  color: var(--color-text);
  background-color: var(--color-background-soft);
}


@media (max-width: 650px) {
  #code-editor {
    margin-top: 40px;
    margin-bottom: 40px;
  }
}
</style>