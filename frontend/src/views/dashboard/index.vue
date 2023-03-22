<template>
  <div class="dashboard-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="数据统计" name="first">1</el-tab-pane>
      <el-tab-pane label="用户访问统计" name="second">2</el-tab-pane>
      <el-tab-pane label="用户行为统计" name="third">3</el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
// import { mapGetters } from 'vuex'
export default {
  name: 'Dashboard',
  data() {
    return {
      activeName: 'first'
    }
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event)
    }
  }
}
</script>
<style scoped>
.dashboard-container >>>.el-tabs__header{
    padding: 15px;
    margin: 0 15px;
}
</style>
