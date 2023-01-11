<script>
import { defineComponent, reactive, ref, watch, nextTick } from 'vue';
import { useRouter, useRoute } from 'vue-router'
import { useStore } from 'vuex'

export default defineComponent({

  setup() {
    const router = useRouter()
    const route = useRoute()
    const store = useStore()
    const activePage = ref('')
    const multipage = ref(true)
    const linkList = ref([])
    const pageList = ref([])
    const reloadFlag = ref(true)

    const indexKey = '/dashboard'

    const routeReload = () => {
      reloadFlag.value = false
      let ToggleMultipage = "app/ToggleMultipage"
      store.dispatch(ToggleMultipage, false)
      nextTick(() => {
        store.dispatch(ToggleMultipage, true)
        reloadFlag.value = true
      })
    }

    const changePage = (key) => {
      activePage.value = key
    }

    const onEdit = (targetKey, action) => {
      if (action === 'remove') {
        remove(targetKey);
      }
    }

    const remove = (key) => {
      if (key == indexKey) {
        message.warning('首页不能关闭!')
        return
      }
      if (pageList.value.length === 1) {
        message.warning('这是最后一页，不能再关闭了啦')
        return
      }
      // console.log("pageList ", pageList.value);
      let removeRoute = pageList.value.filter(item => item.fullPath == key)
      pageList.value = pageList.value.filter(item => item.fullPath !== key)
      let index = linkList.value.indexOf(key)
      linkList.value = linkList.value.filter(item => item !== key)
      index = index >= linkList.value.length ? linkList.value.length - 1 : index
      activePage.value = linkList.value[index]

      //update-begin--Author:scott  Date:20201015 for：路由缓存问题，关闭了tab页时再打开就不刷新 #842
      //关闭页面则从缓存cache_included_routes中删除路由，下次点击菜单会重新加载页面
      // let cacheRouterArray = Vue.ls.get(CACHE_INCLUDED_ROUTES) || []
      // if (removeRoute && removeRoute[0]) {
      //   let componentName = removeRoute[0].meta.componentName
      //   console.log("key: ", key);
      //   console.log("componentName: ", componentName);
      //   if(cacheRouterArray.includes(componentName)){
      //     cacheRouterArray.splice(cacheRouterArray.findIndex(item => item === componentName), 1)
      //     Vue.ls.set(CACHE_INCLUDED_ROUTES, cacheRouterArray)
      //   }
      //   this.emitPageClosed(removeRoute[0])
      // }
      //update-end--Author:scott  Date:20201015 for：路由缓存问题，关闭了tab页时再打开就不刷新 #842

    }

    return {
      route,
      activePage,
      multipage,
      linkList,
      pageList,
      reloadFlag,
      indexKey,
      routeReload,
      changePage,
      onEdit
    }
  },

  created() {
    if (this.$route.path != this.indexKey) {
      this.addIndexToFirst()
    }
    // 复制一个route对象出来，不能影响原route
    let currentRoute = Object.assign({}, this.$route)
    currentRoute.meta = Object.assign({}, currentRoute.meta)
    this.pageList.push(currentRoute)
    this.linkList.push(currentRoute.fullPath)
    this.activePage = currentRoute.fullPath
  },

  watch: {
    '$route': function (route) {
      this.activePage = route.fullPath
      if (!this.multipage) {
        this.linkList = [route.fullPath]
        this.pageList = [Object.assign({}, route)]
        // update-begin-author:taoyan date:20200211 for: TASK #3368 【路由缓存】首页的缓存设置有问题，需要根据后台的路由配置来实现是否缓存
      } else if (this.indexKey == route.fullPath) {
        //首页时 判断是否缓存 没有缓存 刷新之
        if (route.meta.noCache === true) {
          this.routeReload()
        }
        // update-end-author:taoyan date:20200211 for: TASK #3368 【路由缓存】首页的缓存设置有问题，需要根据后台的路由配置来实现是否缓存
      } else if (this.linkList.indexOf(route.fullPath) < 0 && route.meta.breadcrumb) {
        this.linkList.push(route.fullPath)
        this.pageList.push(Object.assign({}, route))
        //// update-begin-author:sunjianlei date:20200103 for: 如果新增的页面配置了缓存路由，那么就强制刷新一遍 #842
        // if (newRoute.meta.keepAlive) {
        //   this.routeReload()
        // }
        //// update-end-author:sunjianlei date:20200103 for: 如果新增的页面配置了缓存路由，那么就强制刷新一遍 #842
      } else if (this.linkList.indexOf(route.fullPath) >= 0) {
        let oldIndex = this.linkList.indexOf(route.fullPath)
        let oldPositionRoute = this.pageList[oldIndex]
        this.pageList.splice(oldIndex, 1, Object.assign({}, route, { meta: oldPositionRoute.meta }))
      }
    },
    'activePage': function (key) {
      let index = this.linkList.lastIndexOf(key)
      if (index >= 0) {
        let waitRouter = this.pageList[index]
        // console.log('wait', waitRouter)
        // 【TESTA-523】修复：不允许重复跳转路由异常
        if (waitRouter.fullPath !== this.$route.fullPath) {
          this.$router.push(Object.assign({}, waitRouter))
        }
      }
    },
    'multipage': function (newVal) {
      if (this.reloadFlag) {
        if (!newVal) {
          this.linkList = [this.$route.fullPath]
          this.pageList = [this.$route]
        }
      }
    },
  },

  methods: {
    addIndexToFirst() {
      this.pageList.splice(0, 0, {
        name: 'Dashboard',
        path: this.indexKey,
        fullPath: this.indexKey,
        meta: {
          icon: 'icon-shouye',
          title: '首页'
        }
      })
      this.linkList.splice(0, 0, this.indexKey)
    },
  }
})
</script>

<template>
  <!-- <a-tabs
    v-model:activeKey="activePage"
    class="tab-layout-tabs"
    hideAdd
    type="editable-card"
    @change="changePage"
    @edit="onEdit"
  >
    <a-tab-pane
      v-for="page in pageList"
      :id="page.fullPath"
      :key="page.fullPath"
      :closable="!(page.meta.title == '首页')"
    >{{ page.meta.title }}</a-tab-pane>
  </a-tabs>-->

  <a-tabs
    v-model:activeKey="activePage"
    class="tab-layout-tabs"
    style="height:40px"
    type="editable-card"
    hideAdd
    @edit="onEdit"
    @tabClick="tabClick"
    @change="changePage"
  >
    <a-tab-pane
      v-for="page in pageList"
      :key="page.fullPath"
      :tab="page.meta.title"
      :closable="!(page.meta.title == '首页')"
    ></a-tab-pane>
  </a-tabs>
</template>

<style scoped>
.tab-layout-tabs.ant-tabs {
  border-bottom: 1px solid #ccc;
  border-left: 1px solid #ccc;
  background-color: white;
  /* padding: 0 20px; */
  display: flex;
  justify-content: left;
}
deep .ant-tabs-bar {
  margin: 4px 0 0 !important;
  border: none !important;
}
/* 
.tab-layout-tabs.ant-tabs.ant-tabs-card,
.tab-layout-tabs.ant-tabs.ant-tabs-tab {
  padding: 0 24px !important;
  background-color: white !important;
  margin-right: 10px !important;
}

.tab-layout-tabs.ant-tabs.ant-tabs-tab .ant-tabs-close-x {
  width: 12px !important;
  height: 12px !important;
  opacity: 0 !important;
  cursor: pointer !important;
  font-size: 12px !important;
  margin: 0 !important;
  position: absolute;
  top: 36%;
  right: 6px;
} */
</style>