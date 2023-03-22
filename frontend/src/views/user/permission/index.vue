<!--
 * @FileDescription: 权限
 * @Author: 庞志扬
 * @Date: 2022/11/14
 * @LastEditors: 庞志扬
 * @LastEditTime: 2022/11/14
 -->
<template>
  <div class="app-container">
    <el-select
      v-model="roleForm.currentRoleName"
      filterable
      placeholder="请选择"
      remote
      :remote-method="query => {roleForm.searchName = query; getRoles()}"
      @change="handleCurrentRoleChange"
    >
      <el-option
        v-for="item in roleForm.roleList"
        :key="item.id"
        :label="item.name"
        :value="item.name"
      />
      <div style="height: 20px" />
      <div style="position: absolute; bottom: 0; background-color: #fff;">
        <el-pagination
          :current-page="roleForm.pageNum"
          :total="roleForm.total"
          layout="total, prev, pager, next"
          :page-size="roleForm.pageSize"
          @current-change="getRoles"
        />
      </div>
    </el-select>
    <!--重置、保存-->
    <el-button type="primary" style="margin-left: 50px" @click="resetPermission">重置</el-button>
    <el-button type="primary" @click="savePermission">保存</el-button>
    <div style="height: 20px" />
    <el-table
      :data="permissionTree"
      style="margin-bottom: 20px;"
      row-key="id"
      border
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column label="序号" type="index" width="60" />
      <el-table-column label="权限名称" width="300">
        <template slot-scope="scope">
          <template v-if="scope.row.classify === 'system'">
            <span style="font-weight: bold;">{{ scope.row.name }}</span>
          </template>
          <template v-else>
            <el-checkbox
              v-if="scope.row.classify !== '模块权限'"
              v-model="scope.row.checked"
              style="margin-left: 5px; margin-right: 10px;"
              @change="handleCheckChange(scope.row)"
            />
            <span>{{ scope.row.name }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="权限类别" prop="classify" width="200">
        <template slot-scope="scope">
          <span> {{ scope.row.classify === 'system' ? '' : scope.row.classify }} </span>
        </template>
      </el-table-column>
      <el-table-column label="描述" prop="description" width="300" />
      <el-table-column label="创建时间" prop="createTime" width="200" />
    </el-table>

  </div>
</template>
<script>
export default {
  data() {
    return {
      roleForm: {
        searchName: '',
        roleList: [],
        currentRoleName: '',
        currentRole: {},
        pageSize: 10,
        pageNum: 1,
        loading: false,
        total: 0
      },
      roleList: [],
      permissionTree: [],
      permissionIdList: []
    }
  },
  mounted() {
    this.getRoles()
    this.getAllPermission()
  },
  methods: {

    buildTreeByList(list, idField = 'id', pidField = 'parentId') {
      const listData = Array.from(list)
      const tree = []
      const idTreeMap = {}
      for (let i = 0; i < listData.length; i++) {
        const item = listData[i]
        if (!item[pidField] || listData.findIndex(it => it[idField] === item[pidField]) === -1) {
          tree.push(item)
          idTreeMap[item[idField]] = item
        }
      }
      // 删除根节点
      for (let i = 0; i < listData.length; i++) {
        if (idTreeMap[listData[i][idField]]) {
          listData.splice(i, 1)
          i--
        }
      }
      let oldLength = listData.length + 1
      while (listData.length > 0) {
        for (let i = 0; i < listData.length; i++) {
          const item = listData[i]
          const parent = idTreeMap[item[pidField]]
          if (parent) {
            if (!parent.children) {
              parent.children = []
            }
            parent.children.push(item)
            idTreeMap[item[idField]] = item
            listData.splice(i, 1)
            i--
          }
        }
        if (oldLength === listData.length) {
          break
        }
        oldLength = listData.length
      }
      return tree
    },

    getAllPermission() {
      const url = '/users/permission/getAllPermissions'
      this.$http.get(url).then(res => {
        const data = res.data.data
        data.forEach(item => { item.checked = false })
        // 根据id和parentId构建树形结构
        const processData = data.filter(item => item.system === '数据加工系统')
        const manageData = data.filter(item => item.system === '数据管理系统')
        const showData = data.filter(item => item.system === '数据展示平台')
        this.permissionTree = [
          { id: -1, name: '数据加工系统', classify: 'system', children: this.buildTreeByList(processData) },
          { id: -2, name: '数据管理系统', classify: 'system', children: this.buildTreeByList(manageData) },
          { id: -3, name: '数据展示平台', classify: 'system', children: this.buildTreeByList(showData) }
        ]
        if (this.permissionIdList.length > 0) {
          this.changePermissionCheckStatus(this.permissionIdList)
        }
      })
    },

    traverseTree(tree, callback) {
      for (let i = 0; i < tree.length; i++) {
        const item = tree[i]
        callback(item)
        if (item.children) {
          this.traverseTree(item.children, callback)
        }
      }
    },

    changePermissionCheckStatus(permissionList) {
      this.traverseTree(this.permissionTree, item => {
        if (permissionList.includes(item.id)) {
          item.checked = true
        } else {
          item.checked = false
        }
      })
    },
    getPermissionOfRole(roleId) {
      const params = new URLSearchParams()
      const url = '/users/role/getPermissionsByRoleId'
      params.append('roleId', roleId)
      this.$http.post(url, params).then(res => {
        const permissionList = res.data.data
        this.permissionIdList = permissionList.map(item => item.id)
        this.changePermissionCheckStatus(this.permissionIdList)
      })
    },
    resetPermission() {
      this.changePermissionCheckStatus(this.permissionIdList)
    },
    getRoles() {
      this.roleForm.loading = true
      const params = new URLSearchParams()
      const url = '/users/role/getRoleList'
      params.append('pageNum', this.roleForm.pageNum)
      params.append('pageSize', this.roleForm.pageSize)
      params.append('name', this.roleForm.searchName)
      this.$http.post(url, params).then((res) => {
        this.roleForm.roleList = res.data.data.roleList
        this.roleForm.total = res.data.data.total
        // 从页面的params中获取roleId
        const roleId = this.$route.params.roleId
        if (roleId) {
          this.getPermissionOfRole(roleId)
          this.roleForm.currentRole = this.roleForm.roleList.find(item => item.id === roleId)
          this.roleForm.currentRoleName = this.roleForm.currentRole.name
          this.$route.params.roleId = undefined
        }
      }).finally(() => {
        this.roleForm.loading = false
      })
    },

    findTreeNodeByNode(node, id) {
      if (node.id === id) {
        return node
      }
      if (node.children) {
        for (let i = 0; i < node.children.length; i++) {
          const result = this.findTreeNodeByNode(node.children[i], id)
          if (result) {
            return result
          }
        }
      }
      return null
    },

    findTreeNode(id) {
      for (let i = 0; i < this.permissionTree.length; i++) {
        const result = this.findTreeNodeByNode(this.permissionTree[i], id)
        if (result) {
          return result
        }
      }
      return null
    },

    checkAllChildren(node, checked) {
      if (node.children) {
        node.children.forEach(item => {
          item.checked = checked
          this.checkAllChildren(item, checked)
        })
      }
    },

    checkParentNode(node, checked) {
      if (node.parentId && checked) {
        const parentNode = this.findTreeNode(node.parentId)
        if (parentNode) {
          parentNode.checked = true
          this.checkParentNode(parentNode, checked)
        }
      }
    },

    handleCheckChange(row) {
      // 子级全选/全不选
      this.checkAllChildren(row, row.checked)
      // 父级检查是否全选
      this.checkParentNode(row, row.checked)
    },

    handleCurrentRoleChange() {
      this.roleForm.currentRole = this.roleForm.roleList.find(item => item.name === this.roleForm.currentRoleName)
      this.getPermissionOfRole(this.roleForm.currentRole.id)
    },

    getPermissionListOfTree() {
      const permissionList = []
      this.traverseTree(this.permissionTree, item => {
        if (item.checked && item.id > 0 && item.classify !== '模块权限') {
          permissionList.push(item.id)
        }
      })
      return permissionList
    },

    savePermission() {
      if (!this.roleForm.currentRole) {
        this.$message({
          message: '请选择角色',
          type: 'warning'
        })
        return
      }
      const params = new URLSearchParams()
      const url = '/users/role/updateRolePermissions'
      params.append('roleId', this.roleForm.currentRole.id)
      params.append('permissionIds', this.getPermissionListOfTree())
      this.$http.put(url, params).then(res => {
        if (res.data.status === 200) {
          this.$message({
            message: '修改成功',
            type: 'success'
          })
          this.getPermissionOfRole(this.roleForm.currentRole.id)
        } else {
          this.$message({
            message: '修改失败',
            type: 'error'
          })
        }
      })
    }
  }
}
</script>
<style scoped>
</style>
