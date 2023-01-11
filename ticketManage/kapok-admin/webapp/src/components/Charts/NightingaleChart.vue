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
      const option = Object.assign({}, {
        title: {
          text: this.title
        },
        legend: {
          top: 'bottom'
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
            name: 'Nightingale Chart',
            type: 'pie',
            radius: [15, 95],
            center: ['50%', '38%'],
            roseType: 'area',
            itemStyle: {
              borderRadius: 8
            },
            data: this.chartData,
            animationDuration: animationDuration
          }
        ]
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
