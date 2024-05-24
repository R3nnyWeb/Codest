<template>
  <div v-html="markdown.render(source)" />
</template>

<script setup>
import MarkdownIt from "markdown-it";

const markdown = new MarkdownIt()

defineProps({
  source: {
    type: String,
    default: ""
  }
});
</script>

<style scoped>
div {
  border: 2px solid var(--color-border);
  border-radius: 5px;
  padding: 10px;
}
</style>