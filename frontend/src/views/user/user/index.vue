<!--
 * @FileDescription: 用户管理
 * @Author: 庞志扬
 * @Date: 2022/11/14
 * @LastEditors: 庞志扬
 * @LastEditTime: 2022/11/14
 -->
<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item label="用户名称" prop="username">
        <el-input
          v-model="name"
          placeholder="请输入用户名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <div style="display:inline-block;">
          <div class="block" style="display:inline-block;">
            <el-date-picker
              v-model="dateRange[0]"
              style="width: 130px"
              value-format="yyyy-MM-dd"
              type="date"
              placeholder="起始日期"
            />
          </div>
          <div class="block" style="display:inline-block;margin-left:10px;">
            <el-date-picker
              v-model="dateRange[1]"
              style="width: 130px"
              value-format="yyyy-MM-dd"
              type="date"
              placeholder="截止日期"
            />
          </div>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleCreate"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :data-warden-title="'删除用户${multipleSelection}'"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          @click="checkChange"
        >删除</el-button>
      </el-col>
      <!-- <right-toolbar :show-search.sync="showSearch" :columns="columns" @queryTable="getList" /> -->
    </el-row>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="userList"
      highlight-current-row
      style=""
      @sort-change="sortChange"
      @selection-change="handleSelectionChange"
    > <el-table-column
        type="selection"
        width="55"
      />
      <el-table-column prop="username" label="用户名" align="center" />
      <el-table-column prop="userClass" label="类别" align="center" />
      <el-table-column prop="roleStr" label="角色" align="center" />
      <el-table-column label="创建时间" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime | createTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="statuss" label="状态" sortable min-width="100">
        <template slot-scope="row">
          <el-switch
            v-model="row.row.status"
            data-warden-title="用户状态更改"
            active-value="1"
            inactive-value="0"
            active-text="激活"
            inactive-text="禁用"
            @change="changeShow(row.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="330" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button
            type="primary"
            size="mini"
            style="margin-left: 10px"
            @click="handleAssignRole(row)"
          >分配角色</el-button>
          <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="checkChange(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页器 -->
    <el-pagination align="center" :current-page="pageNum" :page-sizes="[5,10,20]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />

    <!-- 分配用户角色dialog -->
    <el-dialog title="用户角色分配" :visible.sync="dialogFormVisible" :center="true" width="800px">
      <el-form ref="dataForm" :model="roleAssignForm" label-position="left" label-width="80px" style="width: 550px; margin-left:50px;">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="roleAssignForm.username" disabled />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="roleAssignForm.roleList" multiple class="filter-item" placeholder="请选择">
            <el-option
              v-for="item in roleList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="assignRole">
          确认
        </el-button>
      </div>
    </el-dialog>

    <!-- 添加用户信息dialog -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible2" :center="true" width="800px">
      <h3>基本信息：</h3>
      <el-form ref="dataForm" :model="temp" :rules="rules" label-position="left" label-width="80px" style="width: 550px; margin-left:50px;">
        <el-row>
          <el-col :span="20">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="temp.username" />
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="真实姓名" prop="truename">
              <el-input v-model="temp.truename" />
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item v-if="dialogStatus=='create'" label="密码" prop="password">
              <el-input v-model="temp.password" />
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="地址" prop="address">
              <el-input v-model="temp.address" />
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="电话" prop="phone">
              <el-input v-model="temp.phone" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="sex">
              <el-select v-model="temp.sex" class="filter-item" placeholder="请选择">
                <el-option v-for="item in genderOptions" :key="item.key" :label="item.key" :value="item.key" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col>
            <el-form-item label="分配角色" prop="roleIdList">
              <el-select v-model="temp.roleIdList" multiple class="filter-item" placeholder="请选择">
                <el-option v-for="item in roleList" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="20" />
        </el-row></el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible2 = false">
          取消
        </el-button>
        <el-button type="primary" @click="updateUserInfo">
          确认
        </el-button>
      </div>
    </el-dialog>
    <!-- </el-scollbar> -->

    <el-dialog :visible.sync="dialogPvVisible" title="Reading statistics">
      <el-table :data="pvData" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="key" label="Channel" />
        <el-table-column prop="pv" label="Pv" />
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogPvVisible = false">Confirm</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import waves from '@/directive/waves' // waves directive
import XLSX from 'xlsx'
import md5 from 'js-md5'

import 'echarts-wordcloud/dist/echarts-wordcloud'
import 'echarts-wordcloud/dist/echarts-wordcloud.min.js'
export default {
  name: 'ComplexTable',
  filters: {
    statusFilter(status) {
      const statusMap = {
        激活: 'success',
        禁用: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  directives: { waves },
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
    chartData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      deleteId: undefined,
      value: '',
      msg: 'Welcome to Your Vue.js App',
      dateRange: ['', ''],
      roleStr: [],
      roleList: [],
      genderOptions: [{
        displayName: '选项1',
        key: '男'
      },
      {
        displayName: '选项2',
        key: '女'
      }],
      tableData: [],
      bookPageNum: 1,
      bookPageSize: 5,
      bookName: '',
      bookClassify: '',
      bookOrder: '',
      bookOrderMethod: '',
      multipleSelection: [],
      multipleSourceSelection: [],
      usersList: [],
      addBookList: [],
      userQuery: {
        pageSize: 1,
        pageNum: 5,
        name: '',
        startDate: '',
        endDate: ''
      },
      searchContent: '',
      userArrayList: [],
      userList: [],
      userLists: [],
      pageNum: 1,
      pageSize: 10,
      name: '',
      subjectId: '',
      startDate: '',
      endDate: '',
      value1: '',
      checkedCities: [],
      isIndeterminate: true,
      dicts: [],
      deptName: undefined,
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      bookListLoading: true,
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: '',
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        // headers: { Authorization: 'Bearer ' + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + '/system/user/importData'
      },
      pages: [
      ],
      listQuery: {
        page: 1,
        limit: 20,
        importance: undefined,
        title: undefined,
        type: undefined,
        sort: '+id'
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        username: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined
      },
      importanceOptions: [1, 2, 3],
      temp: {
        id: undefined,
        roleIdList: [],
        roleStr: [],
        username: '',
        truename: '',
        sex: '',
        avatar: '',
        status: '',
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        type: '',
        checkedCities: [],
        phone: '',
        address: '',
        note: ''
      },
      wordData: [],
      chart: null,
      dialogFormVisible: false,
      dialogFormVisible2: false,
      dialogStatus: '',
      textMap: {
        update: '用户信息编辑',
        create: '用户新建'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        roleIdList: [{ required: true, message: '角色为必选项', trigger: 'change' }],
        roleStr: [{ required: true, message: '角色为必选项', trigger: 'change' }],
        title: [{ required: true, message: 'title is required', trigger: 'blur' }],
        username: [{ required: true, message: '用户名为必填项', trigger: 'blur' }],
        password: [{ required: true, message: '密码为必填项', trigger: 'blur' }],
        truename: [{ required: true, message: '真实姓名为必填项', trigger: 'blur' }],
        sex: [{ required: true, message: '性别为必选项', trigger: 'change' }],
        phone: [{ required: true, message: '电话号码为必填项', trigger: 'blur' },
          { pattern: /^([1][3-9][0-9]{9})$/, message: '输入正确的手机号码', trigger: 'blur' }],
        address: [{ required: true, message: '地址为必填项', trigger: 'blur' }]
      },
      downloadLoading: false,
      sourceTotal: 0,
      roleAssignForm: {
        username: '',
        roleList: [],
        userId: undefined
      }
    }
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.getRoles()
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    checkChange(row) {
      if (row.id !== undefined) {
        this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.handleDeleteUsers(row.id)
        }).catch((e) => {
          console.error(e)
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      } else {
        if (this.multipleSelection.length === 0) {
          this.$message({
            type: 'error',
            message: '请选择要删除的信息'
          })
        }
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 用户是否放到禁用
    changeShow(row) {
      this.$message({
        type: 'warning',
        message: '暂不支持修改'
      })
      row.status = row.status === '1' ? '0' : '1'
    },
    // 获取用户列表
    getRoles() {
      this.listLoading = true
      // Just to simulate the time of the request
      const params = new URLSearchParams()
      const url = '/users/user/searchUserList'
      params.append('pageNum', this.pageNum)
      params.append('pageSize', this.pageSize)
      params.append('name', this.name)
      params.append('startDate', this.dateRange[0])
      params.append('endDate', this.dateRange[1])
      this.$http.post(url, params).then((res) => {
        this.userList = res.data.data.userList

        this.total = res.data.data.total
        if (this.total === undefined) {
          this.total = 0
        }
        this.userList.forEach((item, i) => {
          item.status = String(item.status)
          this.$set(item, 'userClass', '个人用户')
          // this.changeUrl(item)
          this.$set(item, 'roleStr', item.roleNameList.join(',') || '无')
        })
      })
      setTimeout(() => {
        this.listLoading = false
      }, 1.5 * 1000)
    },
    // 获取图片
    changeUrl(item) {
      var userId = item.id

      this.$http({
        method: 'get',
        url: '/users/user/getAvatar?userId=' + userId,
        // 例:{uploadUrl:uploadUrl},传入ofd格式的下载文件url
        responseType: 'blob' // 设置响应的数据类型为一个包含二进制数据的 Blob 对象，必须设置，否则pdf会展示空白！！！
      }).then(res => {
        const binaryData = []
        binaryData.push(res.data)
        const url = window.URL.createObjectURL(
          new Blob(binaryData, { type: 'application/jpg' })
        )
        this.$set(item, 'url', url)
        this.viewVisible = true // 当前页弹框打开
        // 新页面打开
        //   window.open(url)
        this.$forceUpdate()
      })
    },
    handleImport() {
      this.getRoleList()
      this.upload.title = '用户导入'
      this.upload.open = true
    },
    // 树查找
    filterNode(value, data) {
      if (!value) return true
      return data.name.indexOf(value) !== -1
    },

    handleNodeClick(data) {
    //   this.userList.groupInfo = data.id
      this.sourceTotal = 0
      this.getRoles()
    },
    handleFilter() {
      this.listQuery.page = 1
    //   this.getList()
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作Success',
        type: 'success'
      })
      row.status = status
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'id') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+id'
      } else {
        this.listQuery.sort = '-id'
      }
      this.handleFilter()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        status: 'published',
        type: ''
      }
    },
    handleCreate() {
      this.getRoleList()
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible2 = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    handleUpdate(row) {
      this.getRoleList()
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible2 = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    handleAssignRole(row) {
      this.roleAssignForm.userId = row.id
      this.roleAssignForm.username = row.username
      this.roleAssignForm.roleList = []
      this.dialogFormVisible = true
      this.getRoleList()
    },

    assignRole() {
      if (this.roleAssignForm.roleList.length === 0 || !this.roleAssignForm.userId) {
        this.$message({
          type: 'error',
          message: '请选择角色'
        })
        return
      }
      const params = new URLSearchParams()
      params.append('userId', this.roleAssignForm.userId)
      params.append('roleIdList', this.roleAssignForm.roleList)
      this.$http.put('/users/user/updateUserRoles', params).then((res) => {
        if (res.status === 200) {
          this.$message({
            type: 'success',
            message: '分配角色成功!'
          })
          this.dialogFormVisible = false
          this.handleQuery()
        }
      })
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.pageNum = 1
      this.getRoles()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = ['', '']
      this.name = ''
      this.handleQuery()
    },
    // 删除用户
    handleDeleteUsers(id) {
      if (id !== undefined) {
        const params = { params: { userId: id }}
        const url = '/users/user/deleteUser'
        this.$http.delete(url, params).then((res) => {
          if (res.status === 200) {
            this.$message({
              type: 'success',
              message: '删除成功!'

            })
            this.getRoles()
          }
        })
      } else {
        for (let i = 0; i < this.multipleSelection.length; i++) {
          this.usersList[i] = this.multipleSelection[i].id
        }
        const params = new URLSearchParams()
        console.log('ssss', this.usersList)
        const url = '/users/user/deleteUsers'
        params.append('userIds', this.usersList)
        this.$http.post(url, params).then((res) => {
          if (res.status === 200) {
            this.$message({
              type: 'success',
              message: '删除成功!'

            })
            this.getRoles()
          } else {
            this.$message({
              type: 'error',
              message: '删除失败!'
            })
          }
        })
      }
    },
    // 更新用户信息
    updateUserInfo() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          if (this.temp.id === undefined) {
            const params = new URLSearchParams()
            const url = '/users/user/addUser'
            params.append('password', md5(this.temp.password))
            params.append('username', this.temp.username)
            params.append('truename', this.temp.truename)
            params.append('sex', this.temp.sex)
            params.append('phone', this.temp.phone)
            params.append('address', this.temp.address)
            params.append('roleIdList', this.temp.roleIdList)
            const responseType = 'blob'
            this.$http.post(url, params, responseType).then((res) => {
              if (res.status === 200) {
                this.$message({
                  type: 'success',
                  message: '添加成功!'
                })
                this.dialogFormVisible2 = false
                this.getRoles()
              } else {
                this.$message({
                  type: 'error',
                  message: '添加失败!'
                })
              }
            })
          } else {
            const params = new URLSearchParams()
            const url = '/users/user/updateUserAdmin'
            params.append('userId', this.temp.id)
            params.append('username', this.temp.username)
            params.append('truename', this.temp.truename)
            params.append('sex', this.temp.sex)
            params.append('phone', this.temp.phone)
            params.append('address', this.temp.address)
            params.append('roleIdList', this.temp.roleIdList)
            const responseType = 'blob'
            this.$http.post(url, params, responseType).then((res) => {
              if (res.data.status === 200) {
                this.$message({
                  type: 'success',
                  message: '修改成功!'
                })
                this.dialogFormVisible2 = false
                this.getRoles()
              } else {
                this.$message({
                  type: 'error',
                  message: '修改失败!'
                })
              }
            })
          }
        }
      })
    },
    // 获取角色
    getRoleList() {
      this.listLoading = true
      const params = new URLSearchParams()
      const url = '/users/role/getRoleList'
      params.append('pageNum', 1)
      params.append('pageSize', 20)
      params.append('name', '')
      this.$http.post(url, params).then((res) => {
        this.roleList = res.data.data.roleList
        this.total = res.data.data.total
        console.log('roleList', this.list)
      })
      this.listLoading = false
    },
    // 格式化导入文件
    updateFiles(file) {
      console.log(file)
      // 解析上传的文件
      if (this.roleStr === '') {
        this.$message({
          type: 'error',
          message: '导入失败，请选择角色后再导入文件!'
        })
      } else {
        const reader = new FileReader()
        reader.readAsBinaryString(file)
        reader.onload = (e) => {
        // 读取文件内容
          const data = e.target.result
          const workbook = XLSX.read(data, { type: 'binary' })
          const worksheet = workbook.Sheets[workbook.SheetNames[0]]
          const fields = ['username', 'truename', 'sex', 'phone', 'address', 'note']
          const excelData = XLSX.utils.sheet_to_json(worksheet, { header: fields })
          excelData.shift()
          console.log('excel', excelData)
          console.log('json数据', JSON.stringify(excelData))
          console.log('roleId', this.roleStr)
          const params = new URLSearchParams()
          const url = '/users/user/importUser'
          // params.append('bookId', this.bookId)
          params.append('userStr', JSON.stringify(excelData))
          params.append('roleId', this.roleStr)
          this.$http.post(url, params).then((res) => {
            if (res.status === 200) {
              this.$message({
                type: 'success',
                message: '导入成功!'
              })
              this.getRoles()
              this.roleStr = ''
            } else {
              this.$message({
                type: 'error',
                message: '导入失败!'
              })
            }
          })
        }
      }
    },
    // 下载示例文件
    downloadExample() {
      this.$axios.get('用户信息示例.xlsx', { responseType: 'blob' }).then(
        response => {
          const url = window.URL.createObjectURL(new Blob([response.data]))
          const link = document.createElement('a')
          console.log('ure', url)
          link.href = url
          link.setAttribute('download', '用户信息示例.xlsx')
          document.body.appendChild(link)
          link.click()
        }
      )
    },
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    },
    // y用户列表大小
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
      this.pageNum = 1
      this.pageSize = val
      this.getRoles()
    },
    // 用户当前页
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`)
      this.pageNum = val
      this.getRoles()
    }

  }
}

</script>
<style scoped>
.el-scrollbar_wrap{
    overflow-x: hidden!important;
}
.oneLine {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.chart-title{
    text-align: center;
    font-size:1.5em;
    font-weight: bold;
    padding: 25px 10px;
}
.box-card {
    height: 600px;
  }
.box-card2 {
    height: 300px;
  }
</style>

