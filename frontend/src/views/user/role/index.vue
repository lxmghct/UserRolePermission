<!--
 * @FileDescription: 角色
 * @Author: 庞志扬
 * @Date: 2022/11/14
 * @LastEditors: 庞志扬
 * @LastEditTime: 2022/11/14
 -->
<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="userName" placeholder="输入角色名称" style="width: 200px;margin-right: 20px" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>{{ ip }}
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-plus" @click="handleCreate">
        添加角色
      </el-button>
    </div>

    <el-table
      ref="multipleTable"
      v-loading="listLoading"
      :data="list"
      row-key="id"
      border
      fit
      highlight-current-row
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column prop="name" label="角色名称" min-width="50px" align="center" />
      <el-table-column prop="description" label="角色描述" align="center" />
      <el-table-column prop="createTime" label="创建时间" align="center" />
      <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button size="mini" type="success" class="el-dropdown-link" @click="handlePermission(row)">
            分配权限
          </el-button>
          <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="checkChange(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页器 -->
    <el-pagination align="center" :current-page="pageNum" :page-sizes="[5,10,20]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />

    <el-dialog :title="dialogStatus" :visible.sync="dialogFormVisible" :center="true" width="500px" append-to-body>
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="角色描述">

          <el-input v-model="temp.description" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button :data-warden-title="`角色信息修改添加，角色名称${temp.name}`" type="primary" @click="addOrUpdateRole">
          确认
        </el-button>
      </div>
    </el-dialog>

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
import { parseTime } from '@/utils'
export default {
  name: 'Role',

  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },

  data() {
    return {
      permClassify: '',
      pageNum: 1,
      pageSize: 10,
      userName: '',
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        importance: undefined,
        title: undefined,
        type: undefined,
        sort: '+id'
      },
      temp: {
        id: undefined,
        name: '',
        description: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      dialogPvVisible: false,
      pvData: [],
      rules: {
        name: [{ required: true, message: '角色名称不能为空', trigger: 'blur' }],
        desc: [{ required: true, message: '角色描述不能为空', trigger: 'blur' }]
      },
      downloadLoading: false,
      form: {
        dataScope: [],
        roleId: undefined,
        roleName: undefined,
        roleKey: undefined,
        roleSort: 0,
        status: '0',
        menuIds: [],
        deptIds: [],
        menuCheckStrictly: true,
        deptCheckStrictly: true,
        remark: undefined
      },
      title: '',
      dataScopeOptions: [
        {
          value: '1',
          label: '全部权限'
        },
        {
          value: '2',
          label: '自定义权限'
        },
        {
          value: '3',
          label: '本用户组权限'
        }
      ],
      deptOptions: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      deleteId: undefined,
      checkMessage: false
    }
  },
  created() {
    this.getRoles()
  },
  methods: {
    // 确认删除
    checkChange(row) {
      this.checkMessage = true
      this.deleteId = row.id
    },
    // 权限操作选择
    handleCommand(command, row) {
      switch (command) {
        case 'handleDataScope':
          this.handleDataScope(row)
          break
        case 'handleOperationScope':
          this.handleOperationScope(row)
          break
        case 'handlePageScope':
          this.handlePageScope(row)
          break
        default:
          break
      }
    },
    // 获取全部角色
    getRoles() {
      this.listLoading = true
      const params = new URLSearchParams()
      const url = '/users/role/getRoleList'
      params.append('pageNum', this.pageNum)
      params.append('pageSize', this.pageSize)
      this.$http.post(url, params).then((res) => {
        this.list = res.data.data.roleList
        this.total = res.data.data.total
        this.list.forEach((item, i) => {
          this.$set(item, 'operateInfo', res.data.data.operateInfo[i])
          this.$set(item, 'pageInfo', res.data.data.pageInfo[i])
          this.$set(item, 'systemInfo', res.data.data.systemInfo[i])
          this.$set(item, 'dataInfo', res.data.data.dataInfo[i])
        })
        console.log('roleList', this.list)
      })
      setTimeout(() => {
        this.listLoading = false
      }, 1.5 * 1000)
    },
    // 添加角色
    addOrUpdateRole() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          if (this.temp.id === undefined) {
            const formate = new FormData()
            formate.append('name', this.temp.name)
            formate.append('description', this.temp.description)
            const url = '/users/role/addRole'
            this.$http.post(url, formate).then((res) => {
              if (res.status === 200) {
                this.$message({
                  type: 'success',
                  message: '添加成功!'
                })
                this.getRoles()
              }
            }).catch(() => {
              this.$message({
                type: 'error',
                message: '添加失败!'
              })
            })
            this.dialogFormVisible = false
          } else {
            const formate = new FormData()
            formate.append('id', this.temp.id)
            formate.append('name', this.temp.name)
            formate.append('description', this.temp.description)
            const url = '/users/role/updateRole'
            this.$http.put(url, formate).then((res) => {
              if (res.status === 200) {
                this.$message({
                  type: 'success',
                  message: '修改成功!'
                })
                this.getRoles()
              }
            }).catch(() => {
              this.$message({
                type: 'error',
                message: '修改失败!'
              })
            })
            this.dialogFormVisible = false
          }
        }
      })
    },
    handleFilter() {
      this.pageNume = 1
      this.getRoles()
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
      this.resetTemp()
      this.dialogStatus = '添加角色'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.dialogStatus = '编辑角色基本信息'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    // 删除角色
    handleDelete(row) {
      const params = new URLSearchParams()
      const url = '/users/role/deleteRole'
      params.append('id', this.deleteId)
      this.$http.post(url, params).then((res) => {
        if (res.status === 200) {
          this.$message({
            type: 'success',
            message: '删除成功!'

          })
          this.checkMessage = false
          this.getRoles()
        } else {
          this.$message({
            type: 'error',
            message: '删除失败!'
          })
        }
      })
    },
    formatJson(filterVal) {
      return this.list.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    },
    // 表格大小控制
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
      this.pageNum = 1
      this.pageSize = val
      this.getRoles()
    },
    // 当前页控制
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`)
      this.pageNum = val
      this.getRoles()
    },
    handlePermission(row) {
      this.$router.push({
        name: 'PermissionList',
        params: {
          roleId: row.id
        }
      })
    }
  }
}
</script>
<style scoped>
.el-dropdown{
    margin-left: 10px;
    margin-right: 10px;
  }
</style>
