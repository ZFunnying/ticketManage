<script>
import { defineComponent, ref } from 'vue';
import { useStore } from 'vuex'
import Menu from './component/menu/index.vue'
import Nav from './component/nav/index.vue'
import Tab from './component/Tab/index.vue'
import { MenuUnfoldOutlined, MenuFoldOutlined } from '@ant-design/icons-vue';
import { mixin, mixinDevice } from '../../utils/mixin'

export default defineComponent({
  name: "",
  components: {
    Menu,
    Nav,
    Tab,
    MenuUnfoldOutlined,
    MenuFoldOutlined,
  },
  mixins: [mixin, mixinDevice],

  setup() {
    const store = useStore()

    return {
      store,
      collapsed: ref(false)
    };
  },

  created() {
    const device = this.store.getters.device;
    if (device === 'mobile') {
      this.collapsed = true
    } else {
      this.collapsed = false
    }

  },

  watch: {

  },

  methods: {
    onClose() {
      this.collapsed = false
    }
  }

})
</script>

<template>
  <router-view></router-view>
</template>

<style scoped>

.logo {
  line-height: 1;
  margin: 13px 16px 3px;
}
.logo-zhankai {
  height: 40px;
}
.logo-zhedie {
  width: 31px;
  height: 31px;
  margin: -10px 0px 0px 0px;
}
.layout-header {
  padding: 0;
  height: 56px;
  display: flex;
  flex-direction: row;
  align-items: center;
}
.trigger {
  margin: 0 24px;
}

</style>

// 新增样式
<style>
/* 样式1 */
/* .layout-header {
  background: #fff; 
  color: #333;
}
.ant-menu-inline.ant-menu-root .ant-menu-item > .ant-menu-title-content, .ant-menu-inline.ant-menu-root .ant-menu-submenu-title > .ant-menu-title-content {
  color: #333;
}
.anticon-setting{
  color: #333;
  margin-top: 24px;
  float: right;
} */

/* 样式2 */
/* .layout-header {
  background: rgb(189, 83, 83); 
  color: #fff;;
}
.ant-menu-inline.ant-menu-root .ant-menu-item > .ant-menu-title-content, .ant-menu-inline.ant-menu-root .ant-menu-submenu-title > .ant-menu-title-content {
  color: #fff;;
}
.anticon-setting{
  color: #fff;
  margin-top: 24px;
  float: right;
}
.ant-menu-submenu > .ant-menu-submenu-title > .ant-menu-submenu-arrow{
  color: #fff;
}
.ant-layout-sider,.ant-menu,.ant-menu-sub.ant-menu-inline{
  background: rgb(189, 83, 83); 
}

.ant-menu:not(.ant-menu-horizontal) .ant-menu-item-selected{
  background: rgb(189, 83, 83); 
}
.ant-menu-submenu:hover > .ant-menu-submenu-title > .ant-menu-submenu-expand-icon, .ant-menu-submenu:hover > .ant-menu-submenu-title > .ant-menu-submenu-arrow {
  color: #fff;;
}
.ant-menu-item:active, .ant-menu-submenu-title:active {
  background: rgba(0,0,0,.2);
}
.ant-menu-vertical .ant-menu-item::after, .ant-menu-vertical-left .ant-menu-item::after, .ant-menu-vertical-right .ant-menu-item::after, .ant-menu-inline .ant-menu-item::after {
  border-right: 3px solid rgb(255,187,102);
}
.ant-dropdown-menu{
  background: rgb(189, 83, 83); 
}
.ant-dropdown-menu-title-content a{
  color: #fff;;
}
.ant-dropdown-menu-item:hover, .ant-dropdown-menu-submenu-title:hover {
  background-color: rgba(0,0,0,.2);
}
.ant-menu-light .ant-menu-item:hover, 
.ant-menu-light .ant-menu-item-active, 
.ant-menu-light .ant-menu:not(.ant-menu-inline) 
.ant-menu-light .ant-menu-submenu-active, 
.ant-menu-light .ant-menu-submenu-title:hover{
  background-color: rgba(0,0,0,.2);
}  */

/* 样式3 */
/* .layout-header {
  background: rgb(74, 115, 153); 
  color: rgb(43, 51, 88);
}
.ant-menu-inline.ant-menu-root .ant-menu-item > .ant-menu-title-content, .ant-menu-inline.ant-menu-root .ant-menu-submenu-title > .ant-menu-title-content {
  color: #fff;
}
.ant-layout-sider,.ant-menu,.ant-menu-sub.ant-menu-inline{
  background: rgb(74, 115, 153); 
}

.ant-menu:not(.ant-menu-horizontal) .ant-menu-item-selected{
  background: rgb(74, 115, 153); 
}
.ant-menu-submenu > .ant-menu-submenu-title > .ant-menu-submenu-arrow{
  color: #fff;
}
.ant-menu-submenu:hover > .ant-menu-submenu-title > .ant-menu-submenu-expand-icon, .ant-menu-submenu:hover > .ant-menu-submenu-title > .ant-menu-submenu-arrow {
  color: #fff;
}
.ant-menu-item:active, .ant-menu-submenu-title:active {
  color: #fff;
}
.ant-menu-vertical .ant-menu-item::after, .ant-menu-vertical-left .ant-menu-item::after, .ant-menu-vertical-right .ant-menu-item::after, .ant-menu-inline .ant-menu-item::after {
  border-right: 3px solid rgb(43, 51, 88);
}
.ant-dropdown-menu{
  background: rgb(74, 115, 153); 
}
.ant-dropdown-menu-title-content a{
  color: #fff;
}
.ant-dropdown-menu-item:hover, 
.ant-dropdown-menu-submenu-title:hover {
  background-color: rgba(0,0,0,.2);
}
.ant-menu-light .ant-menu-item:hover, 
.ant-menu-light .ant-menu-item-active, 
.ant-menu-light .ant-menu:not(.ant-menu-inline) 
.ant-menu-light .ant-menu-submenu-active, 
.ant-menu-light .ant-menu-submenu-title:hover{
  background-color: rgba(0,0,0,.2);
} */



</style>