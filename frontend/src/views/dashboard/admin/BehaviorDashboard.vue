<template>
  <div>
    <div class="dashboard-editor-container">
      <el-row class="row">
        <div class="chart-title">用户访问统计TOP10</div>
        <CrossBarChart :active-name="activeName" />
      </el-row>
      <el-row class="row">
        <div class="chart-title">用户行为统计折线图</div>
        <BehaviorLineChart :active-name="activeName" />
      </el-row>
      <el-row class="row">
        <div class="chart-title">用户检索词TOP50</div>
        <word-cloud :id="wordId" :active-name="activeName" />
      </el-row>
    </div>
  </div>
</template>

<script>
import BehaviorLineChart from './components/BehaviorLineChart.vue'
import WordCloud from './components/WordCloud.vue'
import CrossBarChart from './components/CrossBarChart.vue'
export default {
  name: 'BehaviorDashboard',
  components: { BehaviorLineChart, WordCloud, CrossBarChart },
  props: {
    activeName: {
      type: String,
      default: 'first'
    }
  },
  data() {
    return {
      wordId: 'visit'
    }
  }
}
</script>

<style scoped>
.chart-title{
    text-align: center;
    font-size:1.5em;
    font-weight: bold;
    padding: 25px 10px;
}
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;
}
.row{
    background:#fff;
    padding:16px 16px 0;
    margin-bottom:32px;
}
</style>
