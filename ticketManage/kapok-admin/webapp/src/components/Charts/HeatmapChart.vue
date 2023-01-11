<template>
  <div :class="className" :style="{height:height,width:width,position: relative}" />
</template>

<script>
import * as echarts from 'echarts';
require('echarts/theme/macarons') // echarts theme
import 'echarts/extension/bmap/bmap';
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
    title: {
      type: String,
      default: ''
    },
    relative: {
      type: String,
      default: 'relative'
    },
    height: {
      type: String,
      default: '300px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Array,
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
      const center = this.chartOption.center
      const points = [].concat.apply(
        [],
        this.chartData.map(function (track) {
          return track.map(function (seg) {
            return seg.coord.concat([1]);
          });
        })
      );
      const option = {
        title: {
          text: this.title
        },
        animation: false,
        bmap: {
          center: center,
          zoom: 14,
          roam: true
        },
        visualMap: {
          show: false,
          top: 'top',
          min: 0,
          max: 3,
          seriesIndex: 0,
          calculable: true,
          inRange: {
            color: ['blue', 'blue', 'green', 'yellow', 'red']
          }
        },
        series: [
          {
            type: 'heatmap',
            coordinateSystem: 'bmap',
            data: points,
            pointSize: 5,
            blurSize: 6,
            animationDuration: animationDuration
          }
        ]
      };
      this.chart.setOption(option)
      // 添加百度地图插件
      var bmap = this.chart.getModel().getComponent('bmap').getBMap();
      bmap.addControl(new BMap.MapTypeControl());
    },
    initChart() {
      console.log(echarts)
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions()
    }
  }
}
</script>
