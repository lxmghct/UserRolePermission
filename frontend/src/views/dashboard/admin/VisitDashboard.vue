<template>
  <div>
    <div class="dashboard-editor-container">

      <el-row class="table">
        <div class="title">用户访问情况汇总</div>
        <el-table :data="userList" border center highlight-current-row style="margin:0 15px 10px 15px;" class="tableDetail" @row-click="showDetail">
          <el-table-column type="index" label="序号" align="center" :index="indexMethod" width="80px" />
          <el-table-column prop="id" label="用户Id" align="center" />
          <el-table-column prop="userName" label="用户名称" align="center" />
          <el-table-column prop="userIP" label="IP地址" align="center" />
          <el-table-column prop="count" label="访问次数" align="center" />
        </el-table>
        <el-pagination align="center" :current-page="pageNum" :page-sizes="[5,10,20]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="userList.length" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
      </el-row>
    </div>
    <el-dialog
      :title="`${user.userName}用户行为统计`"
      :visible.sync="detailDialog"
      width="1000px"
      :before-close="editdatadialogclose"
      center
    >
      <el-row :gutter="23" style="margin-left:30px;margin-right: 10px;">
        <!--用户数据-->
        <el-col :span="23" :xs="24">
          <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px" style="margin-right: 30px;">
            <el-form-item label="操作内容" prop="bookName">
              <el-input v-model="value" placeholder="请输入操作内容" size="mini" />
              <!-- <el-select v-model="value" placeholder="请选择">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  size="3"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select> -->
            </el-form-item>

            <el-form-item label="选择时间">
              <div style="display:inline-block;">
                <div class="block" style="display:inline-block;">
                  <el-date-picker
                    v-model="dateRange[0]"
                    style="width: 135px"
                    value-format="yyyy-MM-dd"
                    type="date"
                    placeholder="起始日期"
                  />
                </div>
                <div class="block" style="display:inline-block;margin-left:10px;">
                  <el-date-picker
                    v-model="dateRange[1]"
                    style="width: 135px"
                    value-format="yyyy-MM-dd"
                    type="date"
                    placeholder="截止日期"
                  />
                </div>
              </div>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini">查询</el-button>
            </el-form-item>
            <!-- <el-form-item>
              <el-button type="primary" size="mini">删除</el-button>
            </el-form-item> -->
            <!-- <el-form-item>
              <el-button type="primary" icon="el-icon-download" size="mini">导出excel</el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="mini" @click="getBookList">重置</el-button>
            </el-form-item> -->
          </el-form>
          <el-table
            :data="pages"
            border
            highlight-current-row
            style="margin-left:2px;margin-bottom: 20px;"
          >
            <el-table-column prop="id" label="序号" align="center" width="80px" />
            <el-table-column prop="userName" label="用户" align="center" />
            <el-table-column prop="userIP" label="IP地址" align="center" />
            <el-table-column prop="loginTime" label="访问时间" align="center" />
            <el-table-column prop="accessType" label="操作内容" align="center" />
            <!-- <el-table-column prop="message" label="相关信息" align="center" /> -->

          </el-table>
          <el-pagination align="center" :current-page="detailPageNum" :page-sizes="[5,10,20]" :page-size="detailPageSize" layout="total, sizes, prev, pager, next, jumper" :total="pages.length" @size-change="handleDetailSizeChange" @current-change="handleDetailCurrentChange" />
        </el-col>
      </el-row>

    </el-dialog>
  </div>
</template>

<script>
// import CrossBarChart from './components/CrossBarChart.vue'
export default {
  name: 'VisitDashboard',
  //   components: { CrossBarChart },
  props: {
    activeName: {
      type: String,
      default: 'first'
    }
  },
  data() {
    return {
      wordId: 'visit',
      userList: [{
        id: 1,
        userName: 'admin',
        userIP: '127.0.0.1',
        loginTime: '2023-01-09 10:13:36',
        accessType: '检索结果列表',
        message: '数据类型=1',
        count: 156
      },
      {
        id: 2,
        userName: '用户1',
        userIP: '127.0.0.1',
        loginTime: '2023-01-09 10:13:36',
        accessType: '检索结果列表',
        message: '数据类型=1',
        count: 64
      },
      {
        id: 3,
        userName: '用户2',
        userIP: '127.0.0.1',
        loginTime: '2023-01-09 10:13:36',
        accessType: '检索结果列表',
        message: '数据类型=1',
        count: 56

      },
      {
        id: 4,
        userName: '用户3',
        userIP: '127.0.0.1',
        loginTime: '2023-01-09 10:13:36',
        accessType: '检索结果列表',
        message: '数据类型=1',
        count: 45
      },
      {
        id: 5,
        userName: '用户5',
        userIP: '127.0.0.1',
        loginTime: '2023-01-09 10:13:36',
        accessType: '检索结果列表',
        message: '数据类型=1',
        count: 45
      },
      {
        id: 6,
        userName: '用户6',
        userIP: '127.0.0.1',
        loginTime: '2023-01-09 10:13:36',
        accessType: '检索结果列表',
        message: '数据类型=1',
        count: 37
      }, {
        id: 7,
        userName: '用户7',
        userIP: '127.0.0.1',
        loginTime: '2023-01-09 10:13:36',
        accessType: '检索结果列表',
        message: '数据类型=1',
        count: 64
      },
      {
        id: 8,
        userName: '用户8',
        userIP: '127.0.0.1',
        loginTime: '2023-01-09 10:13:36',
        accessType: '检索结果列表',
        message: '数据类型=1',
        count: 56

      },
      {
        id: 9,
        userName: '用户9',
        userIP: '127.0.0.1',
        loginTime: '2023-01-09 10:13:36',
        accessType: '检索结果列表',
        message: '数据类型=1',
        count: 45
      },
      {
        id: 10,
        userName: '用户10',
        userIP: '127.0.0.1',
        loginTime: '2023-01-09 10:13:36',
        accessType: '检索结果列表',
        message: '数据类型=1',
        count: 37
      }],
      pages: [{
        id: 1,
        userName: 'admin',
        userIP: '127.0.0.1',
        loginTime: '2023-01-09 10:13:36',
        accessType: '检索结果列表',
        message: '数据类型=1'
      },
      {
        id: 2,
        userName: 'admin',
        userIP: '127.0.0.1',
        loginTime: '2023-01-09 10:13:36',
        accessType: '检索结果列表',
        message: '数据类型=1'
      },
      {
        id: 3,
        userName: 'admin',
        userIP: '127.0.0.1',
        loginTime: '2023-01-09 10:13:36',
        accessType: '检索结果列表',
        message: '数据类型=1'

      },
      {
        id: 4,
        userName: 'admin',
        userIP: '127.0.0.1',
        loginTime: '2023-01-09 10:13:36',
        accessType: '检索结果列表',
        message: '数据类型=1'
      },
      {
        id: 5,
        userName: 'admin',
        userIP: '127.0.0.1',
        loginTime: '2023-01-09 10:13:36',
        accessType: '检索结果列表',
        message: '数据类型=1'
      }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined
      },
      dateRange: ['', ''],
      pageNum: 1,
      pageSize: 10,
      detailpageNum: 1,
      detailpageSize: 5,
      user: {},
      detailDialog: false
    }
  },
  methods: {
    indexMethod(index) {
      return this.pageSize * (this.pageNum - 1) + index + 1
    },
    showDetail(row) {
      this.user = row
      this.detailDialog = true
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getRoles()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.name = ''
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // y用户列表大小
    handleSizeChange(val) {
      this.pageNum = 1
      this.pageSize = val
    //   this.getRoles()
    },
    // 用户当前页
    handleCurrentChange(val) {
      this.pageNum = val
    //   this.getRoles()
    },
    handleDetailSizeChange(val) {
      this.detailpageNum = 1
      this.detailpageSize = val
    //   this.getRoles()
    },
    // 用户当前页
    handleDetailCurrentChange(val) {
      this.detailpageNum = val
    //   this.getRoles()
    }
  }
}
</script>

<style scoped>
.table{
  background-color: #fff;
   padding: 20px 50px;
   margin-bottom: 32px;
}
.tableDetail{
    cursor: pointer;
}
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;
}
.title{
    text-align: center;
    font-size:1.5em;
    font-weight: bold;
    padding: 5px 10px 15px 10px;
}
.row{
    background:#fff;
    padding:16px 16px 0;
    margin-bottom:32px;
}
</style>
