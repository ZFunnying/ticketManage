<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts';
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'
import { getRandomColor } from '@/utils'
const animationDuration = 3000
export default {
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '440px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      required: true
    },
    chartOption: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      chart: null,
      sidebarElm: null
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
    }
  },
  mounted() {
    this.initChart()
    if (this.autoResize) {
      this.__resizeHandler = debounce(() => {
        if (this.chart) {
          this.chart.resize()
        }
      }, 100)
      window.addEventListener('resize', this.__resizeHandler)
    }

    // 监听侧边栏的变化
    this.sidebarElm = document.getElementsByClassName('sidebar-container')[0]
    this.sidebarElm && this.sidebarElm.addEventListener('transitionend', this.sidebarResizeHandler)
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    if (this.autoResize) {
      window.removeEventListener('resize', this.__resizeHandler)
    }

    this.sidebarElm && this.sidebarElm.removeEventListener('transitionend', this.sidebarResizeHandler)

    this.chart.dispose()
    this.chart = null
  },
  methods: {
    sidebarResizeHandler(e) {
      if (e.propertyName === 'width') {
        this.__resizeHandler()
      }
    },
    setOptions() {
      const treemapOption = {
        title: {
          text: ''
        },
        toolbox: {
          orient: 'vertical',
          feature: {
            dataView: { readOnly: false },
            restore: {},
            saveAsImage: {}
          }
        },
        series: [
          {
            type: 'treemap',
            id: 'echarts-package-size',
            animationDurationUpdate: 1000,
            roam: false,
            nodeClick: undefined,
            data: this.chartData.children,
            universalTransition: true,
            label: {
              show: true
            },
            breadcrumb: {
              show: false
            }
          }
        ]
      };
      const sunburstOption = {
        title: {
          text: ''
        },
        toolbox: {
          dataView: {},
          feature: {
            saveAsImage: {}
          },
        },
        series: [
          {
            type: 'sunburst',
            id: 'echarts-package-size',
            radius: ['20%', '90%'],
            animationDurationUpdate: 1000,
            animationDuration: animationDuration,
            nodeClick: undefined,
            data: this.chartData.children,
            universalTransition: true,
            itemStyle: {
              borderWidth: 1,
              borderColor: 'rgba(255,255,255,.5)'
            },
            label: {
              show: false
            }
          }
        ]
      };
      let currentOption = treemapOption;
      this.chart.setOption(currentOption)
      let _this = this
      setInterval(function () {
        currentOption =
          currentOption === treemapOption ? sunburstOption : treemapOption;
        _this.chart.setOption(currentOption);
      }, 3000);

    },
    initChart() {
      console.log(echarts)
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions()
    }
  }
}
</script>
