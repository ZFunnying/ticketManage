<template>
  <div>
    <el-upload
      class="upload-demo"
      ref="upload"
      action
      :http-request="handleUpload"
      :before-remove="beforeRemove"
      multiple
      :limit="limit"
      :accept="accept"
      :on-exceed="handleExceed"
      :on-remove="onRemove"
      :on-preview="handlePreview"
      :file-list="fileList">
      <el-button size="mini" type="primary">点击上传</el-button>
      <div slot="tip" v-if="accept" class="el-upload__tip">只能上传{{accept}}文件</div>
    </el-upload>
  </div>
</template>

<script>
import {Message} from 'element-ui'
import s3Client from '@/utils/s3Client'
export default {
  name: "fileUpload",
  props:{
    fileList: {
      type: Array,
      require: true
    },
    limit: {
      type: Number,
      require: false
    },
    accept: {
      type: String,
      require: true,
      default: ''
    },
    handleUpload: Function,
  },
  methods: {
    handlePreview(file) {
      //下载文件
      const a = document.createElement('a');
      const event = new MouseEvent('click');
      a.download = file.name;
      a.href = file.url;
      a.dispatchEvent(event);
    },
    handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 ${this.limit} 个文件，本次选择了 ${
          files.length
        } 个文件，共 ${files.length + fileList.length} 个文件`
      );
    },
    onRemove(file) {
      const index = this.fileList.indexOf(file)
      this.fileList.splice(index, 1)
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    }
  }
}
</script>

<style scoped>

</style>
