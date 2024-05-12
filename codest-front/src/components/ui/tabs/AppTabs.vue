<template>
  <div>
    <div class="tabs">
      <ul>
        <li v-for="tab in tabs" :key="tab" :class="{ 'is-active': tab.isActive }">
          <a :href="tab.href" @click="selectTab(tab)">{{ tab.name }}</a>
        </li>
      </ul>
    </div>

    <div class="tabs-details">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {tabs: []};
  },
  methods: {
    selectTab(selectedTab) {
      this.tabs.forEach(tab => {
        tab.isActive = (tab.name == selectedTab.name);
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.tabs {
  margin-top: 20px;
}

ul {
  display: flex;
  list-style-type: none;
  padding-left: 0;
}

li {
  text-transform: none;
  text-decoration: none;
  font-size: 18px;
  margin-right: 5px;
}

a {
  text-decoration: none;
  color: var(--color-text);
  padding: 10px;
  border-bottom: 1px var(--color-border) solid;

  &:hover {
    border-color: var(--color-text);
    background: var(--color-background-mute);
    transition: all 0.2s linear;
  }
}

.tabs-details {
  margin-top: 20px;
}

.is-active {
  & a {
    border-color: var(--color-text);
    background: var(--color-background-mute);
  }

}

</style>