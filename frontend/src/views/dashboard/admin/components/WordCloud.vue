<template>
  <div>
    <div :id="id" :ref="id" :style="{height:height,width:width}" /></div>
</template>

<script>
import * as echarts from 'echarts'
import 'echarts-wordcloud/dist/echarts-wordcloud'
import 'echarts-wordcloud/dist/echarts-wordcloud.min.js'
import resize from './mixins/resize'

export default {
  name: 'WordCloud',
  mixins: [resize],
  props: {
    id: {
      type: String,
      required: true
    }, // 图形ID
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
    // autoResize: {
    //   type: Boolean,
    //   default: true
    // },
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
      wordData: [],
      chart: null
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
    this.getWord()
  },
  //   beforeDestroy() {
  //     if (!this.chart) {
  //       return
  //     }
  //     this.chart.dispose()
  //     this.chart = null
  //   },
  methods: {
    getWord() {
      const url = '/manage/home/getWord'
      this.$http.post(url).then((res) => {
        if (res.status === 200) {
          this.wordData = res.data.data.worddata
          this.initCharts()
        } else {
          this.$message({
            type: 'error',
            message: '获取失败!'
          })
        }
      })
    },
    initCharts() {
      this.chart = echarts.init(document.getElementById(this.id))
      this.chart.setOption({
        // title: {
        //   text: '用户检索词TOP50',
        //   x: '20',
        //   top: '20',
        //   textStyle: {
        //     color: '#000',
        //     fontSize: '22'
        //   }
        // },
        backgroundColor: '#FFF',
        tooltip: {},
        series: [
          {
            type: 'wordCloud',
            // 用来调整词之间的距离
            gridSize: 10,
            // //用来调整字的大小范围

            sizeRange: [14, 60],
            // //用来调整词的旋转方向，，[0,0]--代表着没有角度，也就是词为水平方向，需要设置角度参考注释内容
            rotationRange: [-90, 90],
            // rotationRange: [ 0,90],
            // rotationRange: [0, 0],
            // 随机生成字体颜色
            textStyle: {
              fontFamily: 'sans-serif',
              fontWeight: 'bold',
              // Color can be a callback function or a color string
              color: function() {
                // Random color
                return (
                  'rgb(' +
                  [
                    Math.round(Math.random() * 160),
                    Math.round(Math.random() * 160),
                    Math.round(Math.random() * 160)
                  ].join(',') +
                  ')'
                )
              }
            },
            emphasis: {
              focus: 'self',

              textStyle: {
                shadowBlur: 10,
                shadowColor: '#333'
              }
            },
            // 位置相关设置
            left: 'center',
            top: 'center',
            right: null,
            bottom: null,
            width: '100%',
            height: '100%',
            // 数据
            data: this.wordData
          }
        ]
      })
      // 点击事件
      this.chart.on('click', function(params) {
        // alert(params.name)
      })
    } }
}
</script>

<!-- <style scoped>
.chart-container{
  /* position: relative; */
  width: 100%;
  /* height: 100%; */
  height: calc(100vh - 84px);
}
</style> -->
