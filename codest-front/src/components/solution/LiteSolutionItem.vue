<template>
  <div :class="{odd : isOdd}" class="card">
    <div class="card-item language">{{ solution.language }}</div>
    <div class="card-item time">{{ solution.createdAt }}</div>
    <div class="card-item status">
      <solution-status :value="solution.status.toUpperCase()"/>
    </div>
  </div>
</template>

<script setup>
import SolutionStatus from "@/components/solution/SolutionStatus.vue";

defineProps({
  solution: {
    type: Object,
    required: true
  },
  isOdd: {
    type: Boolean,
    required: true
  }
})

</script>

<style lang="scss" scoped>
.status{
  display: flex;
  justify-content: end;
  align-items: center;
}
.card {
  font-size: 18px;
  font-weight: 500;
  background-color: var(--color-background-soft);
  padding: 15px 70px 15px 50px;
  width: 100%;
  display: flex;
  align-items: center;

  cursor: pointer;
  opacity: .7;
  border: 1px solid var(--color-border);
  border-left: none;
  border-right: none;
  &:hover{
    opacity: 1;
    border: 1px solid var(--color-border-hover);
  }
  &.odd {
    background-color: var(--color-background-mute);
  }

  &-item {
    width: 50%;
    text-align: center;
  }
}
.language{
  text-align: left;
}

@media (max-width: 600px) {
.card{
   font-size: 14px;
   padding: 15px 20px 15px 20px;
}
}

</style>