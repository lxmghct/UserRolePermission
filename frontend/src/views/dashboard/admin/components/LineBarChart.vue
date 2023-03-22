
<template>
  <div>
    <div :id="id" :class="className" :style="{height:height,width:width}" />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import resize from './mixins/resize'

export default {
  mixins: [resize],
  props: {
    color: {
      type: Array,
      required: true
    },
    id: {
      type: String,
      required: true
    },
    xData: {
      type: Array,
      required: true
    },
    bookNum: {
      type: Array,
      required: true
    },
    peopleNum: {
      type: Array,
      required: true
    },
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
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    activeName: {
      type: String,
      default: 'first'
    }
  },
  data() {
    return {
      chart: null,
      value: ''
    }
  },
  watch: {
    bookNum: {
      deep: true,
      handler(val) {
        this.setOptions(val)
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
      //   this.chart = echarts.init(this.$el, 'macarons')

      this.setOptions()
    },
    setOptions() {
    //   const colors = ['#c0aca1', '#ff8d57']
      //   const colors = ['#91CC75', '#EE6666'] ['#F9CA9C', '#EF7A1C']
      const maxBookVal = Number(Math.max(...this.bookNum))
      const minBookVal = Number(Math.min(...this.bookNum))
      const intervalBook = Math.ceil((maxBookVal - minBookVal) / 5)
      const maxPeopleVal = Number(Math.max(...this.peopleNum))
      const minPeopleVal = Number(Math.min(...this.peopleNum))
      const intervalPeople = Math.ceil((maxPeopleVal - minPeopleVal) / 5)
      this.chart.setOption({
        color: this.color,
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999'
            }
          }
        },
        toolbox: {
          feature: {
            // dataView: { show: true, readOnly: false },
            magicType: { show: true, type: ['line', 'bar'] },
            restore: { show: true }
            // saveAsImage: { show: true }
          }
        },
        legend: {
          data: ['古籍数量', '查阅数量']
        },
        xAxis: [
          {
            type: 'category',
            data: this.xData,
            axisPointer: {
              type: 'shadow'
            },
            axisLabel: { interval: 0, rotate: 30 }
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '古籍数量（本）',
            // min: 0,
            // max: 50,
            interval: intervalBook,
            // scale: true,
            axisLabel: {
              formatter: '{value} '
            }
          },
          {
            type: 'value',
            name: '查阅数量（次）',
            // min: 0,
            // max: 5000,
            interval: intervalPeople,
            axisLabel: {
              formatter: '{value} '
            }
          }
        ],
        series: [
          {
            name: '古籍数量',
            type: 'bar',
            tooltip: {
              valueFormatter: function(value) {
                return value + ' 本'
              }
            },
            data: this.bookNum
          },
          //   {
          //     name: 'Precipitation',
          //     type: 'bar',
          //     tooltip: {
          //       valueFormatter: function(value) {
          //         return value + ' ml'
          //       }
          //     },
          //     data: [
          //       2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3
          //     ]
          //   },
          {
            name: '查阅数量',
            type: 'line',
            yAxisIndex: 1,
            tooltip: {
              valueFormatter: function(value) {
                return value + '次'
              }
            },
            data: this.peopleNum
          }
        ]
      }, true)
    }
  }
}
</script>

