<template>
  <div v-show="isActive">
    <slot :isActive="isActive"></slot>
  </div>
</template>

<script>
export default {
  props: {
    name: {required: true},
    selected: {default: false}
  },
  data() {
    return {
      isActive: false
    };
  },
  computed: {
    href() {
      return '#' + this.name.toLowerCase().replace(/ /g, '-');
    }
  },

  created() {
    this.$parent.tabs.push(this);
  },
  mounted() {
    this.isActive = this.selected;
  }
}
</script>

<style scoped>

</style>