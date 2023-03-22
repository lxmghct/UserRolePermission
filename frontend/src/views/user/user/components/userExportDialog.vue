<template>
  <el-button
    v-hasPermi="['system:user:export']"
    type="success"
    plain
    icon="el-icon-download"
    size="mini"
    @click="handleDownload"
  >导出</el-button>
</template>
<script>

export default {
  name: 'Footer',
  data() {
    return {
      msg: '【这里是外部页面传入的消息】'
    }
  },
  mounted() {
    this.drawLine()
  },
  methods: {
    handleDownload() {
      this.downloadLoading = true
      this.$http({
        method: 'post',
        url: '/users/user/exportUser',
        responseType: 'blob' // 设置响应的数据类型为一个包含二进制数据的 Blob 对象，必须设置，否则pdf会展示空白！！！
      }).then(res => {
        // const binaryData = []
        // binaryData.push(res.data)
        // const url = window.URL.createObjectURL(
        //   new Blob(binaryData, { type: 'application/json' })
        // )
        const blob = new Blob([res.data]) // 把得到的结果用流对象转一下
        blob.download = '用户信息.xlsx' // 设置文件名
        var a = document.createElement('a') // 创建一个<a></a>标签
        a.href = URL.createObjectURL(blob) // 将流文件写入a标签的href属性值
        a.download = '用户信息.xlsx' // 设置文件名
        a.style.display = 'none' // 障眼法藏起来a标签
        document.body.appendChild(a) // 将a标签追加到文档对象中
        a.click() // 模拟点击了a标签，会触发a标签的href的读取，浏览器就会自动下载了
        a.remove() // 一次性的，用完就删除a标签
        this.$message({
          type: 'success',
          message: '导出成功!'
        })
        // window.open(url)
      }).catch(function(error) {
        console.log(error)
      })
      this.downloadLoading = false
    }
  }
}
</script>
