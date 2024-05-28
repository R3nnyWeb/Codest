<template>
  <div id="task-info">
    <div class="task-level">
       <task-level :value="task.level.toUpperCase()"/>
    </div>
    <p class="task-descr">
      <markdown-renderer :source="task.description"/>
    </p>

  </div>
</template>

<script setup>
import TaskLevel from "@/components/task/TaskLevel.vue";
import MarkdownRenderer from "@/components/markdown/MarkdownRenderer.vue";

defineProps(['task'])
</script>

<style lang="scss" scoped>
#task-info {

}
.task-level{
  margin-bottom: 40px;
}
.task-limits{
  margin-top: 20px;
}
.task-example {
  margin-top: 30px;

  &-wrapper {
    background-color: var(--color-background-soft);
    padding: 10px 30px;
    border-radius: 10px;
    font-size: 14px;
    & span{
      font-weight: 600;
    }
  }
}


code {
  font-size: 16px;
}

@media (max-width: 650px) {
code {
  font-size: 14px;
}
  h5 {
    font-size: 14px;
  }
}
</style>