<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts';
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'

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
      default: '300px'
    },
    title: {
      type: String,
      default: ''
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
      chart: null
    }
  },
  mounted() {
    this.initChart()
    this.__resizeHandler = debounce(() => {
      if (this.chart) {
        this.chart.resize()
      }
    }, 100)
    window.addEventListener('resize', this.__resizeHandler)
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    window.removeEventListener('resize', this.__resizeHandler)
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initOption() {
      const indicator = this.chartOption.indicator
      const legend = this.chartOption.series
      const yAxis = []
      for (let i = 0; i < legend.length; i++) {
        const item = {
          value: this.chartData[legend[i]],
          name: legend[i]
        }
        yAxis.push(item)
      }

      const option = Object.assign({}, {
        title: [
          {
            text: this.title
          }
        ],
        toolbox: {
          feature: {
            dataView: { readOnly: false },
            restore: {},
            saveAsImage: {}
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        radar: {
          radius: '66%',
          center: ['50%', '42%'],
          axisName: {
            color: '#fff',
            backgroundColor: '#666',
            borderRadius: 3,
            padding: [3, 5]
          },
          indicator: indicator
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: legend
        },
        series: [{
          type: 'radar',
          symbolSize: 0,
          data: yAxis,
          animationDuration: animationDuration
        }]
      })
      return option
    },
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      const option = this.initOption()
      console.log(option)
      this.chart.setOption(option)
    }
  }
}
</script>
