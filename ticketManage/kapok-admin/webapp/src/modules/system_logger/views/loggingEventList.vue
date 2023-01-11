<template>
  <div class="container">
    <el-backtop/>
    <el-tabs tab-position="top">
      <el-tab-pane label="文件日志">
        <sticky :z-index="10">
          <el-row style="background: #ffffff;padding: 10px">
            <el-col :span="16">
              <el-select v-model="logName">
                <el-option
                  v-for="item in logNameOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
              <el-input v-model="Keyword" style="width: 100px;margin-left: 10px" @input="changeKeyword" placeholder="关键词"/>
              <el-button style="margin-left: 10px" :loading="logLoading" @click="getLogContext">查看日志</el-button>
              <el-button type="primary" @click="downloadLogFile">下载日志</el-button>
              <el-checkbox style="margin-left: 10px;margin-right: 10px;" v-model="ReverseView" @change="checkboxChange">倒序查看</el-checkbox>
              <el-checkbox v-model="DisplayAll" @change="checkboxChange1">显示全部</el-checkbox>
            </el-col>
            <el-col :span="8">
              <div style="text-align: right;margin-right: 30px">
                <el-input-number :min="0" v-model="contextRange" style="width: 100px;margin-right: 10px" placeholder="上下文行数"/>
                <el-button type="primary" @click="getMore" v-if="logContext.length > 0 && !DisplayAll && Keyword == ''" :loading="logLoading">加载更多</el-button>
              </div>
            </el-col>
          </el-row>
        </sticky>
        <div>
          <!--<el-input-->
          <!--v-model="logContext"-->
          <!--style="margin-top: 20px"-->
          <!--type="textarea"-->
          <!--:autosize="{ minRows: 30}"-->
          <!--/>-->
          <div style="margin-top: 60px">
            <div class="text-wrapper" style="font-family:Consolas;font-size: 12px;background-color: #2b2b2b;padding:0px 10px 0px 10px" v-for="(item,index) in logContext" :key="index">
              <div v-if="!item.isError">
                <span v-if="Keyword !== ''"><button @click="showContext(item.line)" >显示上下文</button></span>
                <span style='color: #b9b9b5'>{{item.time.padEnd(25,' ')}}</span>
                <span style='color: #5c952c' v-if="item.level.toUpperCase() === 'INFO'">{{item.level.padEnd(6,' ')}}</span>
                <span style='color: #a5890d' v-else-if="item.level.toUpperCase()  === 'WARN'">{{item.level.padEnd(6,' ')}}</span>
                <span style='color: #fd6b68' v-else-if="item.level.toUpperCase()  === 'ERROR'">{{item.level.padEnd(6,' ')}}</span>
                <span style='color: #b9b9b5'>{{item.codeLine.padEnd(6,' ')}}</span>
                <span style='color: #b9b9b5'>[{{item.thread.padStart(15,' ')}}]</span>
                <span style='color: #06a1a1'>{{' ' + item.method.padEnd(40,' ')}}</span>
                <span style='color: #b9b9b5'> : </span>
                <span style='color: #b9b9b5'>{{item.log}}</span>
              </div>
              <div v-else>
                <span style='color: #b9b9b5'>{{item.error}}</span>
              </div>
            </div>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="数据库日志">
        <el-card class="table-container">
          <div style="margin: 0 10px 20px 10px">
            <el-form :inline="true" :model="searchForm" class="demo-form-inline">
              <el-form-item
                label=""
                prop="timestmp"
              >
                <el-col :span="22">
                  <el-date-picker
                    v-model="searchForm.timeRange"
                    type="datetimerange"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    :picker-options="pickerOptions"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    align="right"
                  />
                </el-col>
              </el-form-item>
              <el-form-item
                label=""
                prop="formattedMessage"
              >
                <el-col :span="22">
                  <el-select v-model="searchForm.levelString" placeholder="请选择日志级别" clearable multiple>
                    <el-option
                      v-for="item in levelStringOption"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-col>
              </el-form-item>
              <el-form-item
                label=""
                prop="loggerName"
              >
                <el-col :span="22">
                  <el-input
                    v-model="searchForm.loggerName"
                    :rows="5"
                    clearable
                    type="text"
                    placeholder="类名"
                  />
                </el-col>
              </el-form-item>
              <el-form-item
                label=""
                prop="callerMethod"
              >
                <el-col :span="22">
                  <el-input
                    v-model="searchForm.callerMethod"
                    :rows="5"
                    clearable
                    type="text"
                    placeholder="调用方法"
                  />
                </el-col>
              </el-form-item>
              <el-form-item
                label=""
                prop="formattedMessage"
              >
                <el-col :span="22">
                  <el-input
                    v-model="searchForm.formattedMessage"
                    :rows="5"
                    clearable
                    type="text"
                    placeholder="日志内容"
                  />
                </el-col>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleQuery">查询</el-button>
              </el-form-item>
            </el-form>
          </div>
          <el-table
            v-loading="findLoading"
            :data="tableData.list"
            :header-row-class-name="'table-head-th'"
            row-key="id"
            fit
            highlight-current-row
          >
            <el-table-column
              key="timestmp"
              show-overflow-tooltip
              :formatter="dateFormat"
              prop="timestmp"
              label="时间戳"
              width="140px"
            />
            <el-table-column
              key="levelString"
              show-overflow-tooltip
              prop="levelString"
              width="80px"
              label="日志级别"
            >
              <template slot-scope="scope">
                <span v-if="scope.row.levelString === 'INFO'">{{ scope.row.levelString }}</span>
                <span v-else-if="scope.row.levelString === 'WARN'" style="color: darkorange">{{ scope.row.levelString }}</span>
                <span v-else-if="scope.row.levelString === 'ERROR'"><el-button style="color: red" type="text" @click="getException(scope.row.eventId)">{{ scope.row.levelString }}</el-button></span>
              </template>
            </el-table-column>
            <el-table-column
              key="loggerName"
              show-overflow-tooltip
              prop="loggerName"
              label="类名"
            />
            <el-table-column
              key="formattedMessage"
              prop="formattedMessage"
              label="日志信息"
              width="800px"
            />
            <el-table-column
              key="callerMethod"
              show-overflow-tooltip
              prop="callerMethod"
              label="调用方法"
            />
            <el-table-column
              key="callerLine"
              show-overflow-tooltip
              prop="callerLine"
              label="调用行"
            />
          </el-table>
          <!-- ��ҳ��Ϣ -->
          <div class="pagination-container">
            <el-pagination
              background
              layout="total, prev, pager, next, jumper"
              :current-page="pageNum"
              :total="tableData.total"
              @current-change="handlePageChange"
              @size-change="handleSizeChange"
            />
          </div>
        </el-card>
      </el-tab-pane>
    </el-tabs>
    <el-dialog
      title="异常详情"
      fullscreen
      :visible.sync="showExceptionDialog"
    >
      <el-scrollbar>
        <li v-for="item in exceptionList" :key="item.i">{{ item.traceLine }}</li>
      </el-scrollbar>
      <!--<div v-if="exceptionList.length>0">-->
      <!--<div-->
      <!--v-for="item in exceptionList"-->
      <!--:key="item.i">-->
      <!--<p>{{item.traceLine}}</p>-->
      <!--</div>-->
      <!--</div>-->
      <!--<div v-else>-->
      <!--暂无数据-->
      <!--</div>-->
    </el-dialog>
  </div>
</template>

<script>
import { Message } from 'element-ui'
import LoggingEventApi from '../api/loggingEvent'
import loggingEventEdit from './loggingEventEdit'
import TextHighlight from 'vue-text-highlight';
import Sticky from '@/components/Sticky'
export default {
  name: 'loggingEventList',
  components: {
    loggingEventEdit,
    Sticky
  },
  data() {
    return {
      queries: ['ERROR'],
      logLoading: false,
      ReverseView: false,
      DisplayAll: false,
      contextRange: 100,
      Keyword: '',
      /* ������������*/
      searchForm: {
      },
      logName: '',
      logNameOptions: [],
      logContext: [],
      startLogIndex:0,
      endLogIndex:999,
      pageSize: 10,
      pageNum: 1,
      // ��������
      tableData: {
        results: []
      },
      mode: 'edit',
      // �Ƿ���ʾ����/�༭�Ի���
      showEditDialog: false,
      findLoading: false,
      // ��ǰ������
      currentRecord: {},
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      levelStringOption: [{
        value: 'INFO',
        label: 'INFO'
      }, {
        value: 'WARN',
        label: 'WARN'
      }, {
        value: 'ERROR',
        label: 'ERROR'
      }],
      showExceptionDialog: false,
      exceptionList: []
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    // ҳ���ʼ��
    init() {
      this.handleQuery()
      this.getLogFileNameOptions()
    },
    showContext(line){
      let range = 100
      if(this.contextRange){
        range = this.contextRange
      }
      this.startLogIndex = ((line - range)>0?(line - range):0)
      this.endLogIndex = line + range
      this.DisplayAll = false
      this.ReverseView = false
      this.Keyword = ''
      this.getLogContext();
    },
    getMore(){
      this.startLogIndex = this.endLogIndex + 1
      this.endLogIndex += 1000
      if (this.logName === undefined || this.logName === null || this.logName === '') {
        return
      }
      return new Promise((resolve, reject) => {
        this.logLoading = true
        LoggingEventApi
          .getLogFileContext(this.logName,this.startLogIndex,this.endLogIndex,this.ReverseView,this.DisplayAll,this.Keyword)
          .then(response => {
            if (response.data.code === 200) {
              if(this.ReverseView){
                this.logContext = response.data.data.concat(this.logContext)
              }else {
                this.logContext = this.logContext.concat(response.data.data)
              }
            } else {
              Message.error(response.data.message)
            }
            this.logLoading = false
          })
          .catch(error => {
            this.logLoading = false
            if (error.message !== '') {
              console.log(error.message)
            }
            reject(error)
          })
      })
    },
    getLogContext() {
      if (this.logName === undefined || this.logName === null || this.logName === '') {
        return
      }
      return new Promise((resolve, reject) => {
        this.logLoading = true
        LoggingEventApi
          .getLogFileContext(this.logName,this.startLogIndex,this.endLogIndex,this.ReverseView,this.DisplayAll,this.Keyword)
          .then(response => {
            if (response.data.code === 200) {
              this.logContext = response.data.data
            } else {
              Message.error(response.data.message)
            }
            this.logLoading = false
          })
          .catch(error => {
            this.logLoading = false
            if (error.message !== '') {
              console.log(error.message)
            }
            reject(error)
          })
      })
    },
    getLogFileNameOptions() {
      return new Promise((resolve, reject) => {
        LoggingEventApi
          .getLogFileNameOptions()
          .then(response => {
            const data = response.data
            if (data.code === 200) {
              this.logNameOptions = data.data
            } else {
              console.log(data.message)
            }
          })
          .catch(error => {
            if (error.message !== '') {
              console.log(error.message)
            }
            reject(error)
          })
      })
    },
    changeKeyword(){
      this.logContext = []
      this.startLogIndex = 0
      this.endLogIndex = 999
      this.DisplayAll = false
      this.ReverseView = false
    },
    checkboxChange(){
      this.logContext = []
      this.startLogIndex = 0
      this.endLogIndex = 999
      this.DisplayAll = false
      this.Keyword = ''
    },
    checkboxChange1(){
      this.logContext = []
      this.startLogIndex = 0
      this.endLogIndex = 999
      this.ReverseView = false
      this.Keyword = ''
    },
    downloadLogFile() {
      return new Promise((resolve, reject) => {
        LoggingEventApi
          .getLogFile(this.logName)
          .then(response => {
            const blob = new Blob([response.data])
            const fileName = this.logName
            if ('download' in document.createElement('a')) { // 非IE下载
              const elink = document.createElement('a')
              elink.download = fileName
              elink.style.display = 'none'
              elink.href = URL.createObjectURL(blob)
              document.body.appendChild(elink)
              elink.click()
              URL.revokeObjectURL(elink.href) // 释放URL 对象
              document.body.removeChild(elink)
            } else { // IE10+下载
              navigator.msSaveBlob(blob, fileName)
            }
          })
          .catch(error => {
            if (error.message !== '') {
              console.log(error.message)
            }
            reject(error)
          })
      })
    },
    // 时间格式化
    dateFormat(row, column, cellValue, index) {
      const daterc = row[column.property]
      var date = new Date(daterc)
      var y = date.getFullYear()
      var m = date.getMonth() + 1
      m = m < 10 ? ('0' + m) : m
      var d = date.getDate()
      d = d < 10 ? ('0' + d) : d
      var h = date.getHours()
      h = h < 10 ? ('0' + h) : h
      var minute = date.getMinutes()
      var second = date.getSeconds()
      minute = minute < 10 ? ('0' + minute) : minute
      second = second < 10 ? ('0' + second) : second
      return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second
    },
    handleQuery() {
      this.pageNum = 1
      this.query()
    },
    query() {
      return new Promise((resolve, reject) => {
        const param = Object.assign({}, this.searchForm)
        this.findLoading = true
        LoggingEventApi
          .queryByCond(param, this.pageSize, this.pageNum)
          .then(response => {
            const data = response.data
            if (data.code === 200) {
              const data = response.data.data
              this.tableData = data
              this.findLoading = false
            } else {
              this.findLoading = false
              Message.error(data.message)
              reject()
            }
          })
          .catch(error => {
            this.findLoading = false
            if (error.message !== '') {
              Message.error(error.message)
            }
            reject(error)
          })
      })
    },
    handleAdd() {
      this.mode = 'add'
      this.currentRecord = {}
      this.showEditDialog = true
    },
    handleDetail(row) {
      this.mode = 'details'
      this.currentRecord = Object.assign({}, row)
      this.showEditDialog = true
    },
    handleEdit(row) {
      this.mode = 'edit'
      this.currentRecord = Object.assign({}, row)
      this.showEditDialog = true
    },

    // ������ҳ�¼�
    handlePageChange(currentPage) {
      this.pageNum = currentPage
      this.query()
    },
    // ����ҳ���������¼�
    handleSizeChange(val) {
      this.pageSize = val
      this.query()
    },
    // �����Ի���ر��¼�
    handleDialogClose(done) {
      this.$refs.editForm.resetFields()
      done()
    },
    handleDel(row) {
      this.$confirm('ȷ��ɾ����', '����', {
        confirmButtonText: 'ȷ��',
        cancelButtonText: 'ȡ��',
        type: 'warning'
      })
        .then(() => {
          const param = Object.assign({}, row)
          LoggingEventApi
            .delete(param)
            .then(response => {
              const data = response.data
              if (data.code === 200) {
                this.$message({
                  message: 'ɾ���ɹ�',
                  type: 'success'
                })
                this.handleQuery()
              } else {
                Message.error(data.message)
              }
            })
            .catch(error => {
              console.log(error)
              if (error.message !== '') {
                Message.error(error.message)
              }
            })
        }).catch(() => {
      })
    },
    // �����༭�Ի���ر��¼�
    handleAfterEdit() {
      this.showEditDialog = false
      this.$refs.editForm.resetFields()
      this.query()
    },
    getException(eventId) {
      this.showExceptionDialog = true
      this.exceptionList = []
      LoggingEventApi
        .getException(eventId)
        .then(response => {
          const data = response.data
          if (data.code === 200) {
            this.exceptionList = data.data
          }
        })
    }
  }
}
</script>
<style>
.container {
  margin: 10px;
}
.text-wrapper {
  white-space: pre-wrap;
  line-height: 1.5;
}
.table-container {
  margin-top: 0px;
}
.pagination-container {
  margin-top: 30px;
  text-align: right;
}
.components-container {
  position: relative;
  height: 100vh;
}


</style>

