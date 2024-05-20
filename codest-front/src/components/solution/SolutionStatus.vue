<template>
  <div id="status" :class="resultClass">
    {{ value.toUpperCase() }}
  </div>
</template>

<script setup>
import {computed} from "vue";

const props = defineProps({
  value: {
    type: String,
    required: true
  }
});

const resultClass = computed(() => {
  let className = '';
  className = props.value.toLowerCase()
  return className;
});
</script>

<style scoped>
#status {
  min-width: 130px;
  border: 1px solid;
  padding: 10px 25px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 100px;
  text-align: center;
}

.pending {
  color: gray;
  border-color: gray;
}

.accepted {
  color: var(--color-success);
  border-color: var(--color-success);
}

.runtime_error {
  color: var(--color-hard);
  border-color: var(--color-hard);
}

.compile_error {
  color: var(--color-hard);
  border-color: var(--color-hard);
}

.test_error {
  color: var(--color-medium);
  border-color: var(--color-medium);
}

.timeout_error {
  color: var(--color-hard);
  border-color: var(--color-hard);
}
.internal_error {
  color: var(--color-hard);
  border-color: var(--color-hard);
}


@media (max-width: 600px) {
  #level {
    min-width: 100px;
    padding: 5px 20px;
    font-size: 14px;
  }
}
</style>