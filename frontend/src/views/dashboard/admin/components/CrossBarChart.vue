<template>
  <div>
    <div id="crossBar" :class="className" :style="{height:height,width:width}" />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import resize from './mixins/resize'

export default {
  name: 'CrossBarChart',
  mixins: [resize],
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
      default: '450px'
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
      totalNum: [970, 800, 744, 700, 666, 512, 489, 234, 230, 203],
      todayNum: [99, 95, 90, 88, 80, 70, 60, 55, 40, 35]
    }
  },
  watch: {
    activeName(val) {
      if (val === 'third') {
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
  methods: {
    initChart() {
      this.chart = echarts.init(document.getElementById('crossBar'))
      //   this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions()
    },
    setOptions() {
    //   const maxPeopleVal = Number(Math.max(...this.actualData))
    //   const minPeopleVal = Number(Math.min(...this.actualData))
    //   const intervalPeople = Math.ceil((maxPeopleVal - minPeopleVal) / 5)
    //   this.chart.setOption({
    //     tooltip: {
    //       trigger: 'axis',
    //       axisPointer: {
    //         type: 'shadow'
    //       }
    //     },
    //     legend: {},
    //     grid: {
    //       left: '3%',
    //       right: '4%',
    //       bottom: '3%',
    //       containLabel: true
    //     },
    //     xAxis: {
    //       type: 'value',
    //       boundaryGap: [0, 0.01]
    //     },
    //     yAxis: {
    //       type: 'category',
    //       data: ['用户1', '用户2', '用户3', '用户4', '用户5', '用户6', '用户7', '用户8', '用户9', '用户10']
    //     },
    //     series: [
    //       {
    //         name: '累计访问量',
    //         type: 'bar',
    //         data: [203, 230, 234, 489, 512, 666, 700, 744, 800, 970]
    //       },
    //       {
    //         name: '当日访问量',
    //         type: 'bar',
    //         data: [35, 40, 55, 60, 70, 80, 88, 90, 95, 99]
    //       }
    //     ]
    //   })
      this.chart.setOption({
        // color: this.color,
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
        grid: {
          top: '15%',
          left: '5%',
          right: '5%',
          bottom: '3%',
          containLabel: true
        },
        legend: {
          data: ['累计访问量', '当日访问量']
        },
        xAxis: [
          {
            name: '用户',
            type: 'category',
            data: ['用户1', '用户2', '用户3', '用户4', '用户5', '用户6', '用户7', '用户8', '用户9', '用户10'],
            axisPointer: {
              type: 'shadow'
            },
            axisLabel: { interval: 0, rotate: 30 }
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '累计访问量'
            // min: 0,
            // max: 50,
            // interval: intervalBook,
            // scale: true,
            // axisLabel: {
            //   formatter: '{value} '
            // }
          },
          {
            type: 'value',
            name: '当日访问量'
            // min: 0,
            // max: 5000,
            // interval: intervalPeople,
            // axisLabel: {
            //   formatter: '{value} '
            // }
          }
        ],
        series: [
          {
            name: '累计访问量',
            type: 'bar',
            tooltip: {
              valueFormatter: function(value) {
                return value + '次'
              }
            },
            data: this.totalNum
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
            name: '当日访问量',
            type: 'line',
            yAxisIndex: 1,
            tooltip: {
              valueFormatter: function(value) {
                return value + '次'
              }
            },
            data: this.todayNum
          }
        ]
      }, true)
    }
  }

}
</script>

<style scoped>

</style>
