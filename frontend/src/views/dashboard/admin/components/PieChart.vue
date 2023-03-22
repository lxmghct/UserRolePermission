<template>
  <div :id="id" :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts'
import resize from './mixins/resize'

export default {
  mixins: [resize],
  props: {
    pieData: {
      type: Array,
      required: true
    }, // 图形数据
    dynastyData: {
      type: Array,
      required: true
    }, // 图形数据
    id: {
      type: String,
      required: true
    }, // 图形ID
    chartLabel: {
      type: String,
      required: true
    }, // 图形浮动数据名称
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
    activeName: {
      type: String,
      default: 'first'
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    pieData: {
      deep: true,
      handler(val) {
        this.initChart()
      }
    },
    dynastyData: {
      deep: true,
      handler(val) {
        this.initChart()
      }
    },
    activeName(val) {
      if (val === 'first') {
        this.$nextTick(() => {
          this.chart.resize()
        })
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(document.getElementById(this.id))

      this.chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          type: 'scroll',
          orient: 'vertical',
          left: 35,
          top: 20,
          bottom: 20,
          data: this.dynastyData
        },
        series: [
          {
            name: this.chartLabel,
            type: 'pie',
            roseType: 'radius',
            radius: [15, 95],
            center: ['50%', '45%'],
            data: this.pieData,
            animationEasing: 'cubicInOut',
            animationDuration: 2600
          }
        ]
      })
    }
  }
}
</script>
