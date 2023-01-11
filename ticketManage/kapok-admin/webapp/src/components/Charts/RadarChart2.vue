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
    getColor(index) {
      var colorarrays = ['#2F9323', '#D9B63A', '#2E2AA4', '#9F2E61', '#4D670C', '#BF675F', '#1F814A', '#357F88', '#673509', '#310937', '#1B9637', '#F7393C']
      if (index > colorarrays.length) {
        return getRandomColor()
      }
      return colorarrays[index]
    },
    initOption() {
      const indicator = this.chartOption.indicator
      const legend = this.chartOption.series
      const yAxis = []
      for (let i = 0; i < legend.length; i++) {
        const color = this.getColor(i)
        const item = {
          value: this.chartData[legend[i]],
          name: legend[i],
          itemStyle: {
            normal: {
              color: color,
              lineStyle: {
                color: color,
                width: 2
              }
            }
          },
        }
        yAxis.push(item)
      }

      const option = Object.assign({}, {
        title: [
          {
            text: this.title
          }
        ],
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        toolbox: {
          feature: {
            dataView: { readOnly: false },
            restore: {},
            saveAsImage: {}
          }
        },
        radar: {
          radius: '66%',
          center: ['50%', '42%'],
          startAngle: 90,
          splitNumber: 4,
          shape: 'circle',
          axisName: {
            formatter: '{value}',
            color: '#428BD4'
          },
          splitArea: {
            areaStyle: {
              color: ['#77EADF', '#26C3BE', '#64AFE9', '#428BD4'],
              shadowColor: 'rgba(0, 0, 0, 0.2)',
              shadowBlur: 10
            }
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(211, 253, 250, 0.8)'
            }
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(211, 253, 250, 0.8)'
            }
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
          emphasis: {
            lineStyle: {
              width: 4
            }
          },
          animationDuration: animationDuration,
          data: yAxis
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
