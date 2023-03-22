<template>
  <div class="dashboard-editor-container">

    <div class="dashboard-title"><span>《国医典藏》是由中国中医科学院中医药信息研究所（图书馆）研发的国家级中医古籍数字资源库，其中后台管理系统有着图书数据存储、数据统计、图书信息维护、用户角色权限管理、审核发布等强大功能。保证平台正常运营。</span></div>

    <el-row class="row">
      <el-col :span="12"><div class="chart-title">各馆的古籍数量及用户查阅数量</div><line-bar-chart :id="bookId" :active-name="activeName" :x-data="xBookData" :book-num="bookNum" :people-num="peopleNum" :color="color1" /></el-col>
      <el-col :span="12"><div class="chart-title">各馆的古籍数量及用户查阅数量</div><line-bar-chart :id="outBookId" :active-name="activeName" :x-data="xOutData" :book-num="outBookNum" :people-num="outPeopleNum" :color="color2" /></el-col>
    </el-row>
    <el-row class="row">
      <el-col :span="12"><div class="chart-title">各朝代的古籍数量</div><pie-chart :id="dynastyId" :active-name="activeName" chart-label="古籍数量" :pie-data="dynastyBookData" :dynasty-data="dynastyData1" /></el-col>
      <el-col :span="12"><div class="chart-title">各朝代的用户查阅数量</div> <pie-chart :id="dynastyPeopleId" :active-name="activeName" chart-label="查阅数量" :pie-data="dynastyPeopleData" :dynasty-data="dynastyData2" /></el-col>
    </el-row>
    <!-- <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <div class="chart-title">各省市的古籍数量及用户查阅数量</div>
      <line-bar-chart :id="countyId" :x-data="xCountyData" :book-num="countyBookNum" :people-num="countyPeopleNum" />
    </el-row> -->
    <el-row class="row">
      <div class="chart-title">新建用户</div>
      <line-chart :active-name="activeName" />
    </el-row>
    <!-- <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <mix-chart />
    </el-row> -->
    <!-- <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <div class="chart-title">用户检索词TOP50</div>
      <word-cloud :id="wordId" />
    </el-row> -->
  </div>
</template>

<script>
import LineChart from './components/LineChart'
import LineBarChart from './components/LineBarChart'
import PieChart from './components/PieChart.vue'
const lineChartData = {
  newVisitis: {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [90, 90, 91, 90, 95, 92, 93, 88, 82, 91, 89, 97, 98, 101, 105, 110, 118, 134, 126, 135, 140, 140, 136, 137, 138, 140, 140, 145, 140, 140, 150]
  }
}

export default {
  name: 'DataDashboard',
  components: {
    LineBarChart,
    LineChart,
    PieChart
  },
  props: {
    activeName: {
      type: String,
      default: 'first'
    }
  },
  data() {
    return {
      lineChartData: lineChartData.newVisitis,
      bookId: 'bookId',
      outBookId: 'outBookId',
      dynastyId: 'dynastyChart',
      dynastyPeopleId: 'dynastyPeopleChart',
      wordId: 'data',
      color1: ['#c0aca1', '#ff8d57'],
      xBookData: [],
      bookNum: [],
      peopleNum: [],
      color2: ['#CFD99D', '#8F85C2'],
      xOutData: [],
      outBookNum: [],
      outPeopleNum: [],

      dynastyData1: [],
      dynastyData2: [],
      dynastyBookData: [],
      dynastyPeopleData: [],
      countyId: 'countyId',
      xCountyData: ['北京', '天津', '上海', '重庆', '河北', '山西', '辽宁', '吉林', '黑龙江', '江苏', '浙江', '安徽', '福建', '江西', '山东', '河南', '湖北', '湖南', '广东', '四川', '贵州', '云南', '海南', '陕西', '甘肃', '青海', '台湾', '广西', '内蒙古', '西藏', '宁夏', '新疆', '香港', '澳门'],
      countyBookNum: [149, 156, 27, 134, 173, 23, 147, 145, 211, 198, 167, 90, 100, 149, 156, 27, 134, 173, 23, 147, 145, 211, 198, 167, 90, 100, 23, 147, 145, 211, 198, 167, 90, 100],
      countyPeopleNum: [1253, 2000, 3678, 4222, 2567, 3874, 4298, 2980, 1653, 1278, 3421, 4278, 2784, 1253, 2000, 3678, 4222, 2567, 3874, 4298, 2980, 1653, 1278, 3421, 4278, 2784, 3874, 4298, 2980, 1653, 1278, 3421, 4278, 2784]
    }
  },
  mounted() {
    this.getSubjectCountList()
    this.getSublibraryCountList()
    this.getDynastyUserBookCount()
    this.getDynastyBookCount()
  },
  methods: {
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type]
    },
    getSubjectCountList() {
      const url = '/manage/home/getSubjectCountList'
      this.$http.post(url).then((res) => {
        if (res.status === 200) {
          this.xOutData = res.data.data.sublibraryName
          this.outBookNum = res.data.data.bookCount
          this.outPeopleNum = res.data.data.userCount
        } else {
          this.$message({
            type: 'error',
            message: '获取失败!'
          })
        }
      })
    },
    getSublibraryCountList() {
      const url = '/manage/home/getSublibraryCountList'
      this.$http.post(url).then((res) => {
        if (res.status === 200) {
          this.xBookData = res.data.data.sublibraryName
          this.bookNum = res.data.data.bookCount
          this.peopleNum = res.data.data.userCount
        } else {
          this.$message({
            type: 'error',
            message: '获取失败!'
          })
        }
      })
    },
    getDynastyUserBookCount() {
      const url = '/manage/home/getDynastyUserBookCount'
      this.$http.post(url).then((res) => {
        if (res.status === 200) {
          this.dynastyPeopleData = res.data.data
          this.dynastyPeopleData.forEach(item => {
            this.dynastyData1.push(item.name)
          })
          console.log('dynastyData1', this.dynastyData1)
        } else {
          this.$message({
            type: 'error',
            message: '获取失败!'
          })
        }
      })
    },
    getDynastyBookCount() {
      const url = '/manage/home/getDynastyBookCount'
      this.$http.post(url).then((res) => {
        if (res.status === 200) {
          console.log('res', res)
          this.dynastyBookData = res.data.data
          this.dynastyBookData.forEach(item => {
            this.dynastyData2.push(item.name)
          })
        } else {
          this.$message({
            type: 'error',
            message: '获取失败!'
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.chart-title{
    text-align: center;
    font-size:1.5em;
    font-weight: bold;
    padding: 25px 10px;
}
.row{
    background:#fff;
    padding:16px 16px 0;
    margin-bottom:32px;
}
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .dashboard-title {
   background-color: #fff;
   height: 100px;
   padding: 20px 50px;
   margin-bottom: 32px;
  }

  .dashboard-title span {
    // margin: 8px;
    text-indent:2em;
    line-height: 150%;
  }

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
