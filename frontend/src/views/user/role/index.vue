
<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="roleSearchForm.roleName" placeholder="输入角色名称" style="width: 200px" @keyup.enter.native="handleClickSearchRole" />
      <el-button class="btn" type="primary" icon="el-icon-search" @click="handleClickSearchRole">
        搜索
      </el-button>
      <el-button class="btn" type="primary" icon="el-icon-plus" @click="handleClickCreateRole">
        添加角色
      </el-button>
    </div>

    <el-table
      v-loading="roleData.loading"
      :data="roleData.roleList"
      border
      fit
      highlight-current-row
    >
      <el-table-column prop="name" label="角色名称" min-width="50px" align="center" />
      <el-table-column prop="description" label="角色描述" align="center" />
      <el-table-column prop="createTime" label="创建时间" align="center" />
      <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="success" class="el-dropdown-link" @click="handleClickAssignPermission(scope.row)">
            分配权限
          </el-button>
          <el-button size="mini" type="danger" @click="handleClickDeleteRole(scope.row,scope.$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      align="center"
      :current-page="roleData.pageNum"
      :page-sizes="[5,10,20]"
      :page-size="roleData.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="roleData.total"
      @size-change="handleTablePageSizeChange"
      @current-change="handleTableCurrentPageChange"
    />

    <el-dialog :title="roleEditDialogData.title" :visible.sync="roleEditDialogData.dialogShow" :center="true" width="500px" append-to-body>
      <el-form ref="roleEditForm" :rules="roleEditFormRules" :model="roleEditForm" label-position="left" label-width="80px">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="roleEditForm.name" />
        </el-form-item>
        <el-form-item label="角色描述">
          <el-input v-model="roleEditForm.description" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="roleEditForm.dialogShow = false">
          取消
        </el-button>
        <el-button type="primary" @click="handleConfirmRoleEditForm">
          确认
        </el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { nextTick } from 'process'
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
      roleSearchForm: {
        roleName: ''
      },
      roleData: {
        roleList: [],
        pageNum: 1,
        pageSize: 10,
        total: 0,
        loading: false
      },
      roleEditForm: {
        id: null,
        name: '',
        description: ''
      },
      roleEditFormRules: {
        name: [{ required: true, message: '角色名称不能为空', trigger: 'blur' }],
        desc: [{ required: true, message: '角色描述不能为空', trigger: 'blur' }]
      },
      roleEditDialogData: {
        title: '',
        dialogShow: false,
        dialogType: ''
      },
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
      deleteId: undefined
    }
  },
  created() {
    this.getRoleSearchResult()
  },
  methods: {
    deleteRole(roleId) {
      const params = { params: { id: roleId }}
      const url = '/users/role/deleteRole'
      this.$http.delete(url, params).then((res) => {
        if (res.status === 200) {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
          this.getRoleSearchResult()
        }
      })
    },
    handleClickDeleteRole(row) {
      this.$confirm('此操作将永久删除该角色, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.deleteRole(row.id)
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    },
    getRoleSearchResult() {
      this.roleData.loading = true
      const params = new URLSearchParams()
      const url = '/users/role/getRoleList'
      params.append('pageNum', this.roleData.pageNum)
      params.append('pageSize', this.roleData.pageSize)
      this.$http.post(url, params).then((res) => {
        this.roleData.roleList = res.data.data.roleList
        this.roleData.total = res.data.data.total
      }).finally(() => {
        this.roleData.loading = false
      })
    },
    addRole() {
      const params = new URLSearchParams()
      const url = '/users/role/addRole'
      params.append('name', this.roleEditForm.name)
      params.append('description', this.roleEditForm.description)
      this.$http.post(url, params).then((res) => {
        if (res.status === 200) {
          this.$message({
            type: 'success',
            message: '添加成功!'
          })
          this.getRoleSearchResult()
          this.roleEditDialogData.dialogShow = false
        }
      })
    },
    updateRole() {
      // TODO: 更新角色
      this.$message({
        type: 'warning',
        message: '该功能待开发!'
      })
    },
    handleConfirmRoleEditForm() {
      this.$refs.roleEditForm.validate(valid => {
        if (valid) {
          if (this.roleEditDialogData.dialogType === 'create_role') {
            this.addRole()
          } else if (this.roleEditDialogData.dialogType === 'update_role') {
            this.updateRole()
          }
        }
      })
    },
    handleClickSearchRole() {
      this.roleData.pageNum = 1
      this.getRoleSearchResult()
    },
    handleClickCreateRole() {
      this.roleEditForm = {
        id: null,
        name: '',
        description: ''
      }
      this.roleEditDialogData.title = '添加角色'
      this.roleEditDialogData.dialogShow = true
      this.roleEditDialogData.dialogType = 'create_role'
      nextTick(() => {
        this.$refs.roleEditForm.clearValidate()
      })
    },
    handleTablePageSizeChange(val) {
      this.roleData.pageNum = 1
      this.roleData.pageSize = val
      this.getRoleSearchResult()
    },
    handleTableCurrentPageChange(val) {
      this.roleData.pageNum = val
      this.getRoleSearchResult()
    },
    handleClickAssignPermission(row) {
      this.$router.push({
        name: 'Permission',
        params: {
          roleId: row.id
        }
      })
    }
  }
}
</script>
<style scoped>
.btn {
  margin-left: 10px;
}
.el-dropdown{
    margin-left: 10px;
    margin-right: 10px;
  }
</style>
