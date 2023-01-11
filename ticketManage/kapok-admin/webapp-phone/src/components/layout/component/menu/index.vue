<script>
import { computed, defineComponent, ref, toRef, watch } from 'vue';
import { MailOutlined, QqOutlined, AppstoreOutlined, SettingOutlined } from '@ant-design/icons-vue'
import { mapGetters } from 'vuex'
import SideItem from './SideItem.vue'

export default defineComponent({
  name: 'Menu',
  components: {
    MailOutlined,
    QqOutlined,
    AppstoreOutlined,
    SettingOutlined,
    SideItem
  },

  props: {
    collapsed: {
      type: Boolean,
      require: true
    }
  },

  setup(props) {
    const selectedKeys = ref([]);
    const openKeys = ref([]);
    const collapsed = toRef(props.collapsed)
    // const handleClick = e => {
    //   console.log('click', e);
    // };

    // const titleClick = e => {
    //   console.log('titleClick', e);
    // };

    // watch(() => openKeys, val => {
    //   console.log('openKeys', val);
    // });

    // const permission_routes = store.state.permission_routes
    return {
      // avtiveMenu,
      // permission_routes
      selectedKeys,
      openKeys,
      collapsed
      // handleClick,
      // titleClick,
    };
  },
  // data() {
  //   return {
  //     openKeys: [],
  //     selectedKeys: []
  //   }
  // },
  mounted() {
    this.init()
  },

  computed: {
    ...mapGetters([
      'permission_routes',
      'sidebar'
    ]),
  },

  watch: {
    '$route': function(route) {
      let selectedKey = route.path
      this.selectedKeys = [selectedKey]
    }
  },

  methods: {
    init() {
      let selectedKey = this.$route.path
      this.selectedKeys = [selectedKey]
      let newOpenKeys = []
      this.fullOpenKeys(this.permission_routes, selectedKey, newOpenKeys)
     
      if (newOpenKeys.length > 0) {
        this.openKeys = newOpenKeys.reverse()
      }
      // console.log('menukeys', this.selectedKeys, this.openKeys)
    },
    // 递归查找当前选中的菜单和父级菜单，填充openKeys
    fullOpenKeys(menus, selectedKey, openKeys) {
      for (let item of menus) {
        if (item.path === selectedKey) {
          openKeys.push(item.path)
          // this.$emit('updateMenuTitle', item)
          return true
        } else if (Array.isArray(item.children)) {
          if (this.fullOpenKeys(item.children, selectedKey, openKeys)) {
            openKeys.push(item.path)
            return true
          }
        }
      }
    },
  },
})
</script>

<template>
  <div>
    <a-menu v-model:selectedKeys="selectedKeys" v-model:openKeys="openKeys" class="menu" mode="inline"  :inline-collapsed="collapsed">
      <side-item
        v-for="item in permission_routes"
        :key="item.path"
        :item="item"
        :basePath="item.path"
      ></side-item>
      <!-- <a-sub-menu key="sub1" @titleClick="titleClick">
        <template #icon>
          <MailOutlined />
        </template>
        <template #title>Navigation One</template>
        <a-menu-item-group key="g1">
          <template #icon>
            <QqOutlined />
          </template>
          <template #title>Item 1</template>
          <a-menu-item key="1">Option 1</a-menu-item>
          <a-menu-item key="2">Option 2</a-menu-item>
        </a-menu-item-group>
        <a-menu-item-group key="g2" title="Item 2">
          <a-menu-item key="3">Option 3</a-menu-item>
          <a-menu-item key="4">Option 4</a-menu-item>
        </a-menu-item-group>
      </a-sub-menu>
      <a-sub-menu key="sub2" @titleClick="titleClick">
        <template #icon>
          <AppstoreOutlined />
        </template>
        <template #title>Navigation Two</template>
        <a-menu-item key="5">Option 5</a-menu-item>
        <a-menu-item key="6">Option 6</a-menu-item>
        <a-sub-menu key="sub3" title="Submenu">
          <a-menu-item key="7">Option 7</a-menu-item>
          <a-menu-item key="8">Option 8</a-menu-item>
        </a-sub-menu>
      </a-sub-menu>
      <a-sub-menu key="sub4">
        <template #icon>
          <SettingOutlined />
        </template>
        <template #title>Navigation Three</template>
        <a-menu-item key="9">Option 9</a-menu-item>
        <a-menu-item key="10">Option 10</a-menu-item>
        <a-menu-item key="11">Option 11</a-menu-item>
        <a-menu-item key="12">Option 12</a-menu-item>
      </a-sub-menu>-->
    </a-menu>
  </div>
</template>

<style scoped>
.menu {
  height: 100vh;
}

</style>

