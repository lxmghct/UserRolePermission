<template>
  <div>
    <el-button
      v-hasPermi="['system:user:import']"
      data-warden-title="导入用户"
      type="info"
      plain
      icon="el-icon-upload2"
      size="mini"
      @click="handleImport"
    >导入</el-button>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="80px" class="demo-ruleForm">
        <el-form-item label="选择角色">
          <el-select v-model="roleStr" class="filter-item" placeholder="请选择" style="margin-bottom: 0;margin-left: 10px">
            <el-option v-for="item in roleList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-upload
            :limit="1"
            class="filter-item"
            style="margin-bottom: 0;margin-left: 10px"
            action=""
            :before-upload="updateFiles"
          >
            <el-button type="primary" icon="el-icon-upload2">
              导入用户文件
            </el-button>
          <!-- <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div> -->
          </el-upload>
        </el-form-item>
        <el-form-item label="">
          <el-button class="filter-item" type="primary" style="margin-bottom: 0;margin-left: 10px" icon="el-icon-download" @click="downloadExample">
            下载示例文件
          </el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <!-- <el-button type="primary" @click="submitFileForm">确 定</el-button> -->
        <el-button @click="upload.open = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import XLSX from 'xlsx'
export default {
  name: 'Footer',
  data() {
    return {
      roleStr: [],
      roleList: [],
      total: 0,
      msg: '【这里是外部页面传入的消息】',
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
      }
    }
  },
  mounted() {
  },
  methods: {
    handleImport() {
      this.getRoleList()
      this.upload.title = '用户导入'
      this.upload.open = true
    },
    // 获取角色
    getRoleList() {
      this.listLoading = true
      const params = new URLSearchParams()
      const url = '/users/role/getRoleList'
      params.append('pageNum', 1)
      params.append('pageSize', 100)
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
        // const fileString = e
        // 接下来可对文件内容进行处理
        // console.log(fileString)
          const data = e.target.result
          const workbook = XLSX.read(data, { type: 'binary' })
          const worksheet = workbook.Sheets[workbook.SheetNames[0]]
          const fields = ['userName', 'trueName', 'sex', 'phone', 'address', 'note']
          const excelData = XLSX.utils.sheet_to_json(worksheet, { header: fields })
          excelData.shift()
          console.log('excel', excelData)
          console.log('json数据', JSON.stringify(excelData))
          console.log('roleId', this.roleStr)
          // const treeData = this.totree(excelData)
          // console.log('excel2', treeData)
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
    }
  }
}
</script>
