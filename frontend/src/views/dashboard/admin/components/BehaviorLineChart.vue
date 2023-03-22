<template>
  <div>
    <div id="behavior-chart" :class="className" :style="{height:height,width:width}" />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import resize from './mixins/resize'
export default {
  name: 'BehaviorLineChart',
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
      default: '350px'
    },
    activeName: {
      type: String,
      default: 'first'
    },
    autoResize: {
      type: Boolean,
      default: true
    }
    // chartData: {
    //   type: Object,
    //   required: true
    // }
  },
  data() {
    return {
      chart: null,
      options: [{
        value: '选项1',
        label: '1月'
      }, {
        value: '选项2',
        label: '2月'
      }, {
        value: '选项3',
        label: '3月'
      }, {
        value: '选项4',
        label: '4月'
      }, {
        value: '选项5',
        label: '5月'
      }, {
        value: '选项6',
        label: '6月'
      }, {
        value: '选项7',
        label: '7月'
      }, {
        value: '选项8',
        label: '8月'
      }, {
        value: '选项9',
        label: '9月'
      }, {
        value: '选项10',
        label: '10月'
      }, {
        value: '选项11',
        label: '11月'
      }, {
        value: '选项12',
        label: '12月'
      }],
      date: [],
      actualData: [],
      value: ''
    }
  },
  watch: {
    activeName(val) {
      if (val === 'third') {
        console.log('thisrd')
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
      this.chart = echarts.init(document.getElementById('behavior-chart'))
      //   this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions()
    },
    setOptions() {
    //   const maxPeopleVal = Number(Math.max(...this.actualData))
    //   const minPeopleVal = Number(Math.min(...this.actualData))
    //   const intervalPeople = Math.ceil((maxPeopleVal - minPeopleVal) / 5)
      this.chart.setOption({
        xAxis: {
          name: '用户',
          nameLocation: 'end',
          data: ['用户1', '用户2', '用户3', '用户4', '用户5', '用户6', '用户7', '用户8', '用户9', '用户10'],
          boundaryGap: true
        },
        grid: {
          top: '10%',
          left: '5%',
          right: '5%',
          bottom: '3%',
          containLabel: true
        },
        // grid: {
        //   left: 10,
        //   right: 10,
        //   bottom: 20,
        //   top: 30,
        //   containLabel: true
        // },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        yAxis: {
          name: '次数',
          type: 'value'
        //   axisTick: {
        //     show: false
        //   },
        //   interval: intervalPeople
        },
        legend: {
          data: ['检索结果', '显示文章内容', '用户登录', '修改密码', '进行用户管理']
        },
        // dataZoom: [
        //   {
        //     type: 'inside',
        //     start: 0,
        //     end: 20
        //   },
        //   {
        //     start: 0,
        //     end: 20
        //   }
        // ],
        // series: [
        //   {
        //     name: '新增用户',
        //     smooth: true,
        //     type: 'line',
        //     itemStyle: {
        //       normal: {
        //         color: '#3888fa',
        //         lineStyle: {
        //           color: '#3888fa',
        //           width: 2
        //         },
        //         areaStyle: {
        //           color: '#f3f8ff'
        //         }
        //       }
        //     },
        //     data: this.actualData,
        //     animationDuration: 2800,
        //     animationEasing: 'quadraticOut'
        //   }]
        series: [
          {
            name: '检索结果',
            type: 'line',
            stack: 'Total',
            data: [12, 13, 10, 13, 9, 23, 20, 10, 13, 9],
            animationDuration: 2800,
            animationEasing: 'quadraticOut'
          },
          {
            name: '显示文章内容',
            type: 'line',
            stack: 'Total',
            data: [22, 18, 19, 23, 29, 33, 31, 19, 23, 29],
            animationDuration: 2800,
            animationEasing: 'quadraticOut'
          },
          {
            name: '用户登录',
            type: 'line',
            stack: 'Total',
            data: [15, 23, 20, 15, 19, 33, 41, 20, 15, 19],
            animationDuration: 2800,
            animationEasing: 'quadraticOut'
          },
          {
            name: '修改密码',
            type: 'line',
            stack: 'Total',
            data: [1, 0, 2, 0, 0, 0, 1, 0, 0, 3],
            animationDuration: 2800,
            animationEasing: 'quadraticOut'
          },
          {
            name: '进行用户管理',
            type: 'line',
            stack: 'Total',
            data: [50, 32, 42, 34, 12, 30, 20, 15, 24, 12],
            animationDuration: 2800,
            animationEasing: 'quadraticOut'
          }
        ]
      })
    }
  }
}
</script>

<style>

</style>
