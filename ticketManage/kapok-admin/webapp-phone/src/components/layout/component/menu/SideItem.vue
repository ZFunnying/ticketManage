<script>
import { createVNode, toRef } from 'vue'
// import { createFromIconfontCN } from '@ant-design/icons-vue';
import Icon from '@ant-design/icons-vue';
import icon from '@/utils/icon.js';

// const IconFont = createFromIconfontCN({
//   scriptUrl: '//at.alicdn.com/t/font_2990892_4b6k1oq19p7.js',
// });

export default {
  name: 'SideItem',
  components: { Icon },
  props: {
    key: {
      type: String,
      require: true
    },
    item: {
      type: Object,
      require: true
    },
    basePath: {
      type: String,
      default: ''
    }
  },

  setup(props) {
    const propsKey = toRef(props, 'key')

    return {
      propsKey,
      icon
    }
  },

  methods: {
    hasChildren(children = []) {
      if(children.length>0) {
        return true
      } else {
        return false
      }
    },
    renderIcon(icon) {
      if(icon) {
        return createVNode(icon)
      }
    }
  }
}
</script>

<template>
  <div v-if="!item.hidden">
    <template v-if="!hasChildren(item.children)">
      <router-link :to="item.path">
        <a-menu-item :key="item.path">
          <span v-if="item.meta">{{ item.meta.title }}</span>
        </a-menu-item>
      </router-link>
    </template>

    <template v-else-if="item.children.length == 1">
      <router-link :to="item.children[0].path">
        <a-menu-item :key="item.children[0].path">
          <span v-if="item.children[0].meta">{{ item.children[0].meta.title }}</span>
        </a-menu-item>
      </router-link>
    </template>

    <a-sub-menu v-else :key="propsKey">
      <template #title v-if="item.meta">{{ item.meta.title }}</template>
      <side-item v-for="child in item.children" :key="child.path" :item="child" :basePath="child.path" />
    </a-sub-menu>
  </div>
</template>

<style>
</style>
