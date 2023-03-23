
<template>
  <div class="app-container">
    <!-- 搜索框 -->
    <el-form :model="userSearchForm" size="small" :inline="true" label-width="68px">
      <el-form-item label="用户名称">
        <el-input
          v-model="userSearchForm.username"
          placeholder="请输入用户名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleClickSearch"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="userSearchForm.startTime"
          style="width: 140px"
          value-format="yyyy-MM-dd"
          type="date"
          placeholder="起始日期"
        />
        <el-date-picker
          v-model="userSearchForm.endTime"
          style="width: 140px; margin-left: 10px;"
          value-format="yyyy-MM-dd"
          type="date"
          placeholder="截止日期"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleClickSearch">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="handleClickReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <div>
      <el-button
        type="primary"
        plain
        icon="el-icon-plus"
        size="mini"
        @click="handleClickCreateUser"
      >新增</el-button>
      <el-button
        type="danger"
        plain
        icon="el-icon-delete"
        size="mini"
        @click="handleClickDeleteUserRows(tableData.selectedRows)"
      >删除</el-button>
    </div>

    <!-- 用户表格 -->
    <el-table
      v-loading="tableData.loading"
      :data="tableData.userList"
      highlight-current-row
      @selection-change="val => (tableData.selectedRows = val)"
    > <el-table-column
        type="selection"
        width="55"
      />
      <el-table-column prop="username" label="用户名" align="center" />
      <el-table-column prop="userClass" label="类别" align="center" />
      <el-table-column prop="roleString" label="角色" align="center" />
      <el-table-column prop="createTime" label="创建时间" width="150px" align="center" />
      <el-table-column align="center" prop="statuss" label="状态" sortable min-width="100">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            data-warden-title="用户状态更改"
            :active-value="1"
            :inactive-value="0"
            active-text="激活"
            inactive-text="禁用"
            @change="handleChangeUserStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="330" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            @click="handleClickAssignRole(scope.row)"
          >分配角色</el-button>
          <el-button size="mini" type="danger" @click="handleClickDeleteUserRows([scope.row])">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页器 -->
    <el-pagination
      align="center"
      :current-page="tableData.pageNum"
      :page-sizes="[5,10,20]"
      :page-size="tableData.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="tableData.total"
      @size-change="handleTablePageSizeChange"
      @current-change="handleTableCurrentPageChange"
    />

    <!-- 分配用户角色dialog -->
    <el-dialog title="用户角色分配" :visible.sync="roleAssignForm.dialogShow" :center="true" width="800px">
      <el-form ref="dataForm" :model="roleAssignForm" label-position="left" label-width="80px" style="width: 550px; margin-left:50px;">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="roleAssignForm.username" disabled />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="roleAssignForm.roleIdList" multiple class="filter-item" placeholder="请选择">
            <el-option v-for="item in roleData.roleList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="roleAssignForm.dialogShow = false"> 取消 </el-button>
        <el-button type="primary" @click="assignRole"> 确认 </el-button>
      </div>
    </el-dialog>

    <!-- 添加用户信息dialog -->
    <el-dialog :title="userEditDialogData.title" :visible.sync="userEditDialogData.dialogShow" :center="true" width="800px">
      <h3>基本信息：</h3>
      <el-form ref="userEditForm" :model="userEditForm" :rules="userEditFormRules" label-position="left" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userEditForm.username" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="truename">
          <el-input v-model="userEditForm.truename" />
        </el-form-item>
        <el-form-item v-if="userEditDialogData.dialogType==='create_user'" label="密码" prop="password">
          <el-input v-model="userEditForm.password" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="userEditForm.address" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="userEditForm.phone" />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="userEditForm.sex" class="filter-item" placeholder="请选择">
            <el-option v-for="item in userEditDialogData.genderOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="分配角色" prop="roleIdList">
          <el-select v-model="userEditForm.roleIdList" multiple class="filter-item" placeholder="请选择">
            <el-option v-for="item in roleData.roleList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="userEditDialogData.dialogShow = false"> 取消 </el-button>
        <el-button type="primary" @click="handleConfirmUserEditForm"> 确认 </el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>

import { nextTick } from 'process'
export default {
  name: 'UserManage',
  data() {
    return {
      userSearchForm: {
        username: '',
        startTime: '',
        endTime: ''
      },
      tableData: {
        pageNum: 1,
        pageSize: 10,
        total: 0,
        userList: [],
        loading: false,
        selectedRows: []
      },
      userEditForm: {
        userId: null,
        username: '',
        truename: '',
        password: '',
        address: '',
        phone: '',
        sex: '',
        roleIdList: []
      },
      userEditFormRules: {
        roleIdList: [{ required: true, message: '角色为必选项', trigger: 'change' }],
        username: [{ required: true, message: '用户名为必填项', trigger: 'blur' }],
        password: [{ required: true, message: '密码为必填项', trigger: 'blur' }],
        truename: [{ required: true, message: '真实姓名为必填项', trigger: 'blur' }],
        sex: [{ required: true, message: '性别为必选项', trigger: 'change' }],
        phone: [{ required: true, message: '电话号码为必填项', trigger: 'blur' },
          { pattern: /^([1][3-9][0-9]{9})$/, message: '输入正确的手机号码', trigger: 'blur' }],
        address: [{ required: true, message: '地址为必填项', trigger: 'blur' }]
      },
      userEditDialogData: {
        title: '',
        dialogShow: false,
        dialogType: '',
        roleList: [],
        genderOptions: ['男', '女']
      },
      roleData: {
        roleList: [],
        total: 0,
        pageSize: 100,
        pageNum: 1
      },
      roleAssignForm: {
        userId: '',
        username: '',
        roleIdList: [],
        dialogShow: false
      }
    }
  },
  created() {
    this.getUserSearchResult()
  },
  methods: {
    deleteUsers(userIdList) {
      const url = '/users/user/deleteUser?' + userIdList.map(item => 'userIdList=' + item).join('&')
      this.$http.delete(url).then((res) => {
        if (res.status === 200) {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
          this.getUserSearchResult()
        }
      })
    },
    handleClickDeleteUserRows(rows) {
      const userNameList = rows.map(item => item.username)
      this.$confirm(`此操作将永久删除用户${userNameList.join('、')}，是否继续?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteUsers(rows.map(item => item.id))
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    handleChangeUserStatus(row) {
      this.$message({
        type: 'warning',
        message: '暂不支持修改'
      })
      row.status = 1 - row.status
    },
    getUserSearchResult() {
      this.tableData.loading = true
      const params = new URLSearchParams()
      const url = '/users/user/searchUserList'
      params.append('pageNum', this.tableData.pageNum)
      params.append('pageSize', this.tableData.pageSize)
      params.append('name', this.userSearchForm.name)
      params.append('startDate', this.userSearchForm.startTime)
      params.append('endDate', this.userSearchForm.endTime)
      this.$http.post(url, params).then((res) => {
        this.tableData.userList = res.data.data.userList
        this.tableData.total = res.data.data.total || 0
        this.tableData.userList.forEach((item, i) => {
          item.userClass = '个人用户'
          item.roleString = item.roleNameList.join(',') || '无'
        })
        this.tableData.selectedRows = []
      }).finally(() => {
        this.tableData.loading = false
      })
    },
    handleClickCreateUser() {
      this.getRoleList()
      this.userEditForm = {
        userId: null, username: '', truename: '',
        password: '', address: '', phone: '',
        sex: '', roleIdList: []
      }
      this.userEditDialogData.dialogShow = true
      this.userEditDialogData.dialogType = 'create_user'
      this.userEditDialogData.title = '添加新用户'
      nextTick(() => {
        this.$refs.userEditForm.clearValidate()
      })
    },

    handleClickAssignRole(row) {
      this.getRoleList()
      this.roleAssignForm.userId = row.id
      this.roleAssignForm.username = row.username
      this.roleAssignForm.roleIdList = []
      this.roleAssignForm.dialogShow = true
    },

    assignRole() {
      if (this.roleAssignForm.roleIdList.length === 0) {
        this.$message.error('请选择角色')
        return
      }
      const params = new URLSearchParams()
      params.append('userId', this.roleAssignForm.userId)
      params.append('roleIdList', this.roleAssignForm.roleIdList)
      this.$http.put('/users/user/updateUserRoles', params).then((res) => {
        if (res.status === 200) {
          this.$message.success('分配角色成功')
          this.roleAssignForm.dialogShow = false
          this.getUserSearchResult()
        }
      })
    },
    handleClickSearch() {
      this.tableData.pageNum = 1
      this.getUserSearchResult()
    },
    handleClickReset() {
      this.userSearchForm.startTime = ''
      this.userSearchForm.endTime = ''
      this.userSearchForm.username = ''
      this.handleClickSearch()
    },
    createUser() {
      const params = new URLSearchParams()
      const url = '/users/user/addUser'
      params.append('password', this.userEditForm.password)
      params.append('username', this.userEditForm.username)
      params.append('truename', this.userEditForm.truename)
      params.append('sex', this.userEditForm.sex)
      params.append('phone', this.userEditForm.phone)
      params.append('address', this.userEditForm.address)
      params.append('roleIdList', this.userEditForm.roleIdList)
      this.$http.post(url, params).then((res) => {
        if (res.status === 200) {
          this.$message({
            type: 'success',
            message: '添加成功!'
          })
          this.userEditDialogData.dialogShow = false
          this.getUserSearchResult()
        }
      })
    },
    updateUserInfo() {
      // TODO: 更新用户信息
      this.$message({
        type: 'warning',
        message: '暂不支持更新用户信息'
      })
    },
    handleConfirmUserEditForm() {
      this.$refs.userEditForm.validate(valid => {
        if (valid) {
          if (this.userEditDialogData.dialogType === 'create_user') {
            this.createUser()
          } else {
            this.updateUserInfo()
          }
        }
      })
    },
    getRoleList() {
      const params = new URLSearchParams()
      const url = '/users/role/getRoleList'
      params.append('pageNum', this.roleData.pageNum)
      params.append('pageSize', this.roleData.pageSize)
      params.append('name', '')
      this.$http.post(url, params).then((res) => {
        this.roleData.roleList = res.data.data.roleList
        this.roleData.roleTotal = res.data.data.total
      })
    },
    handleTablePageSizeChange(val) {
      this.tableData.pageNum = 1
      this.tableData.pageSize = val
      this.getUserSearchResult()
    },
    handleTableCurrentPageChange(val) {
      this.tableData.pageNum = val
      this.getUserSearchResult()
    }

  }
}
</script>
<style scoped>
</style>

