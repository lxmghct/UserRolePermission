
<template>
  <div>
    <!-- <el-select v-model="value" placeholder="请选择月份">
      <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      />
    </el-select> -->
    <div id="line-chart" :class="className" :style="{height:height,width:width}" />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import resize from './mixins/resize'

export default {
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
    autoResize: {
      type: Boolean,
      default: true
    },
    activeName: {
      type: String,
      default: 'first'
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
    actualData: {
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
    this.getNewUser()
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
    getNewUser() {
      const url = '/manage/home/getNewUser'
      this.$http.post(url).then((res) => {
        if (res.status === 200) {
          console.log(res)
          res.data.data.forEach(item => {
            this.actualData.push(item.count)
            this.date.push(item.Date)
          })
          console.log('date', this.date)
          console.log('user', this.actualData)
        } else {
          this.$message({
            type: 'error',
            message: '获取失败!'
          })
        }
      })
    },
    initChart() {
      this.chart = echarts.init(document.getElementById('line-chart'))
      //   this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions()
    },
    setOptions() {
      const maxPeopleVal = Number(Math.max(...this.actualData))
      const minPeopleVal = Number(Math.min(...this.actualData))
      const intervalPeople = Math.ceil((maxPeopleVal - minPeopleVal) / 5)
      this.chart.setOption({
        xAxis: {
          name: '时间',
          data: this.date
        //   boundaryGap: true
        },
        grid: {
          top: '10%',
          left: '5%',
          right: '5%',
          bottom: '3%',
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        yAxis: {
          name: '新建用户数（人）',
          axisTick: {
            show: false
          },
          interval: intervalPeople
        },
        legend: {
          data: ['新增用户']
        },
        dataZoom: [
          {
            type: 'inside',
            start: 0,
            end: 20
          },
          {
            start: 0,
            end: 20
          }
        ],
        series: [
          {
            name: '新增用户',
            smooth: true,
            type: 'line',
            itemStyle: {
              normal: {
                color: '#3888fa',
                lineStyle: {
                  color: '#3888fa',
                  width: 2
                },
                areaStyle: {
                  color: '#f3f8ff'
                }
              }
            },
            data: this.actualData,
            animationDuration: 2800,
            animationEasing: 'quadraticOut'
          }]
      })
    }
  }
}
</script>

