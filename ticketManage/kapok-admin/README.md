---
title: "Kapok框架使用教程"
author: "黎智韬"
---



[TOC]



## 快速开始

如果通过开发组Gitlab管理项目，建议通过Fork方式新建项目，这样能够同步[框架](https://git.cmitgf.com:9006/vgearen/kapok-admin.git)的更新。

### 开发环境

开发中需要具备以下环境：

- MySQL
- Redis
- Java
- Maven
- NodeJs(建议14.20 LTS)

MySQL初始化好后，需要新建一个数据库，并导入项目中的`/sql/mysql.sql`

### 配置后端工程并启动

1. 配置数据库：

   ```properties
   spring.datasource.url=jdbc:mysql://ip:port/db_name?characterEncoding=utf8
   spring.datasource.username=
   spring.datasource.password=
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   
   # 配置Redis
   # Redis数据库索引（默认为0）
   spring.redis.database=0
   # Redis服务器地址
   spring.redis.host=
   # Redis服务器连接端口
   spring.redis.port=
   # Redis服务器连接密码（默认为空）
   spring.redis.password=
   # 连接池最大连接数（使用负值表示没有限制） 默认 8
   spring.redis.lettuce.pool.max-active=50000
   # 连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1
   spring.redis.lettuce.pool.max-wait=-1
   # 连接池中的最大空闲连接 默认 8
   spring.redis.lettuce.pool.max-idle=5000
   # 连接池中的最小空闲连接 默认 0
   spring.redis.lettuce.pool.min-idle=0
   ```

2. 启动工程：

   启动文件`src/main/java/com/cmit/kapok/Application.java`，默认为8900端口

### 启动前端工程

```bash
# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org

# 启动服务
npm run dev
```

浏览器访问 http://localhost:2333

### 使用jasypt加密配置文件
* 1、使用jasypt的jar包加密字符串，或者使用[jasypt在线加密工具](https://www.devglan.com/online-tools/jasypt-online-encryption-decryption)：
  
    ```
    java -cp jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI password='加密密码' algorithm=PBEWithMD5AndDES input='要加密的字符串'
    ```
* 2、配置文件中用ENC()包含加密串
* 3、环境变量配置启动参数-Djasypt.encryptor.password=加密密码
* 4、maven打包时使用命令
  ```
  mvn jasypt:encrypt -Dspring.profiles.active=prod -Djasypt.encryptor.password="the password"
  ```
* 5、或者启动工程时添加参数
    ```
    java -jar .\base-module-system-1.0.jar --jasypt.encryptor.password=密码
    ```

## 组件

### 文件上传

> 文件上传组件路径：/src/components/Upload/fileUpload.vue

#### Minio

文件上传组件支持qiniu、aliyun、minio等文件托管服务，下面以minio为例：

1. 在s3Client.js中配置minio endpoint、accessKeyId、secretAccessKey等参数

2. 在文件中添加组件引用：

   ```javascript
   import fileUpload from '@/components/Upload/fileUpload'
   import s3Client from "@/utils/s3Client";
   ……
   
   components: {
       fileUpload
   },
   ```

3. 使用：

   ```javascript
   <file-upload  :file-list="fileList" accept=".jpg" :limit="3" :handle-upload="handleUpload"/>
   
   ……
   
   handleUpload(rawFile) {
     //判断大小
     console.log(rawFile)
     let _self = this
     return new Promise((resolve, reject) => {
       const reader = new FileReader()
       reader.onload = e => {
         const data = e.target.result
         const managedUpload = s3Client.upload({
           // minio中上传的bucket在此配置，项目中可以上传到多个bucket
           Bucket: 'cmcc-admin',
           Key: rawFile.file.name,
           Body: new Buffer(data),
         }, {}, function(err, data) {
           if (err) {
             console.log(err)
             Message.error(err.message)
             reject(err)
           }
           console.log('File uploaded successfully.',data)
           let obj = {}
           obj.fileName = rawFile.file.name
           obj.size = rawFile.file.size
           _self.fileList.push(rawFile.file)
           rawFile.onSuccess()
           resolve()
         });
         managedUpload.on('httpUploadProgress', function (progress) {
           console.log(progress.loaded / progress.total)
           rawFile.onProgress({percent: (progress.loaded / progress.total)*100})
         });
       }
       reader.readAsArrayBuffer(rawFile.file)
     })
   },
   ```

   参数说明：

   | 参数名       | 说明                                                         | 类型     | 默认值 |
   | ------------ | ------------------------------------------------------------ | -------- | ------ |
   | file-list    | 上传的文件列表, 例如: [{name: 'food.jpg', url: 'https://xxx.cdn.com/xxx.jpg'}] | array    | -      |
   | accept       | 可选上传文件类型                                             | string   | -      |
   | limit        | 上传文件数量限制                                             | number   | -      |
   | handleUpload | 自定义上传方法                                               | function | -      |

4. 文件上传后会push到fileList中，此时可以提交相关信息到后台。

#### 触达易对象存储

如果自己没有对象存储，可以直接使用触达易的对象存储服务。

1. 在触达易上新建应用，获取appkey和appsecret

2. 在应用后端编写方法生成签名，每次上传请求前先生成签名：

   ```java
   // 以下代码仅供参考
   // Controller.java
   
   @RequestMapping(value = "/getUploadUrl", method = RequestMethod.GET)                              
   public Result getUploadUrl(@RequestParam String fileName){                                        
       String body = "/uploadFile" + fileName;                                                       
       String timespan = String.valueOf(System.currentTimeMillis());                                 
       // 生成随机字符                                                                                     
       String nonce = getNonce();                                                                    
       String signature = Authentication(body, timespan, nonce);                                     
       Map reMap = new HashMap<>();                                                                  
       reMap.put("url",this.chudayiHost);                                                            
       reMap.put("timespan",timespan);                                                               
       reMap.put("nonce",nonce);                                                                     
       reMap.put("signature",signature);                                                             
       reMap.put("appkey",this.chudayiAppKey);                                                       
       return ResultGenerator.genSuccessResult(reMap);                                               
   }                                                                                                 
                                                                                                     
                                                                                                     
   private String Authentication(String body, String timespan, String nonce) {                       
       String verifySignature = SHA1Util.getSignature(body, this.chudayiAppSecret, nonce, timespan); 
       log.info("Signature:{}",verifySignature);                                                     
       return verifySignature;                                                                       
   }          
   
   // SHA1Util
   public static String getSignature(String body, String appSecret,String nonce,String timespan){
       String[] tempArr = {body, appSecret,nonce,timespan};                                      
       Arrays.sort(tempArr);                                                                     
       String strArr = String.join("",tempArr);                                                  
       String reStr = "";                                                                        
       try {                                                                                     
           reStr = SHA1Util.getSHA1HexString(strArr);                                            
       } catch (Exception e) {                                                                   
           logger.error("trans 2 SHA1 error:"+e.getMessage());                                   
       }                                                                                         
       return reStr.toLowerCase();                                                               
   }                                                                                             
   ```

3. 在前端文件中添加组件引用：

   ```javascript
   import fileUpload from '@/components/Upload/fileUpload'
   ……
   
   components: {
       fileUpload
   },
   ```

4. 使用：

   ```javascript
   <file-upload  :file-list="fileList" accept=".jpg" :limit="3" :handle-upload="handleUpload"/>
   
   ……
   
   handleUpload(rawFile) {
     getUploadUrl(rawFile.file.name).then(rspData => {                            
       if(rspData.data.code == 200){                                               
         const timespan = rspData.data.data.timespan                               
         const nonce = rspData.data.data.nonce                                     
         const appkey = rspData.data.data.appkey                                   
         const signature = rspData.data.data.signature                             
         const url = rspData.data.data.url + "/cdy/api/uploadFile?fileName="       
           + rawFile.file.name + "&signature=" + signature + "&appkey=" + appkey   
           + "&timespan=" + timespan + "&nonce=" + nonce              
         const formData = new FormData()                                           
         formData.append('file', rawFile.file, rawFile.file.name);                 
         console.log(url)                                                          
         console.log(formData)                                                     
         axios.post(url, formData,{                                                
           headers: {                                                              
             'Content-Type': 'multipart/form-data'                                 
           }                                                                       
         }).then((response) => {                                                   
           console.log("upload_success_response:", response)                       
           let obj = {}                                                            
           obj.fileName = response.data.data.objectName                            
           obj.size = rawFile.file.size                                            
           this.fileList.push(obj)                                                
           rawFile.onSuccess()                                                     
         }).catch((error) =>{                                                      
             console.log('FAILURE!!',error);                                       
           });                                                                     
       } else {                                                                    
         Message.error(rspData.data.message)                                       
       }                                                                           
     })                                                                              
   },
   ```

5. 文件服务入参及返回参数可以查看触达易通用服务文档，这里不再赘述。

### Excel导出

excel导出组件位于`@/vendor/Export2Excel`，从后端查询相关数据后，前端处理转为Excel，在导出，适用于导出小量数据的情况（10W以内），以下是使用代码：

```javascript
handleExportExcel(){
  let param = {}
  param = Object.assign({}, this.searchForm)
  TestApi.queryByCond(param, 0, 0).then(rspData => {
    let datalist = []
    datalist = rspData.data.data.list
    import('@/vendor/Export2Excel').then(excel => {
      const tHeader = [
            '编号',
            '创建人',
            '创建日期',
            '日期',
            '手机号',
            '标题'
      ]
      const filterVal = [
          'id',
          'name',
          'createTime',
          'dateTest',
          'tel',
          'title'
      ]
      const data = this.formatJson(filterVal, datalist)
      excel.export_json_to_excel({
        header: tHeader,
        data,
        filename: '导出Excel',
        autoWidth: true,
        bookType: 'xlsx'
      })
    })
  }).catch(error => {
    console.log(error)
    if (error.message !== '') {
      Message.error(error.message)
    }
  })
},
// 在这里可以对数据进行加工
formatJson(filterVal, jsonData) {
  return jsonData.map(v => filterVal.map(j => {
    return v[j]
  }))
},
```

### Excel导入

excel导入和excel导出相反，通过在前端读取excel转为json，再请求后端插入数据：

引入组件：

```javascript
import ImportExcel from '@/components/ImportExcel'

……

components: {
  ImportExcel
},
```

使用：

```javascript
<import-excel :on-success="handleImportExcel" :key-map="keyMap" :before-upload="beforeUpload" :upload-loading="importLoading" />

……

handleImportExcel({ results, header }) {
  // results返回数据的json array
  // header返回表头
  const param = Object.assign([], results)
  TestApi.importExcelData(param).then(rspData => {
    if (rspData.data.code === 200) {
      Message.success('导入成功')
      this.handleQuery()
    } else {
      // 导入失败，显示导入失败文字
      Message.error(rspData.data.message)
    }
    this.importLoading = false
  }).catch(error => {
    this.importLoading = false
    console.log(error)
    if (error.message !== '') {
      Message.error(error.message)
    }
  })
},
// 用于做文件上传前的限制    
beforeUpload(file) {
  const isLt3M = file.size / 1024 / 1024 < 3
  if (isLt3M) {
    this.importLoading = true
    this.keyMap = {
            '编号':'id',
            '创建人':'name',
            '创建日期':'createTime',
            '日期':'dateTest',
            '手机号':'tel',
            '标题':'title'
          }
    return true
  }
  this.$message({
    message: '上传的文件不能大于3m',
    type: 'warning'
  })
  return false
},
```

参数说明：

| 参数名         | 说明                          | 类型     | 默认值 |
| -------------- | ----------------------------- | -------- | ------ |
| on-success     | 获取excel数据后的后续处理函数 | function | -      |
| key-map        | 数据key的替换                 | object   | -      |
| before-upload  | 用于做文件上传前的限制        | function | -      |
| upload-loading | 文件上传loading               | boolean  | false  |

> **注意：**
>
> `keyMap`用于表头转换，如果不传`keyMap`参数，读取excel的结果为：
>
> ```json
> {
>     '编号':'1',
>     '创建人':'vgearen',
>     '创建日期':'2022-10-11 10:0:0',
>     '日期':'2022-10-11',
>     '手机号':'1380000000',
>     '标题':'测试'
> }
> ```
>
> 通过keyMap转换后：
>
> ```json
> {
>     'id':'1',
>     'name':'vgearen',
>     'createTime':'2022-10-11 10:0:0',
>     'dateTest':'2022-10-11',
>     'tel':'1380000000',
>     'title':'测试'
> }
> ```

### 数据字典

#### Form数据字典引入

引入：

```javascript
import DictionarySelect from '@/components/DictionarySelect/index'

……

components: {
      DictionarySelect
}
```

使用：

```vue
<dictionary-select v-model="YOUR MODEL"  groupCode="YOUR GROUP CODE"/>
```

#### Table数据字典字段格式化

`el-table-column`添加formatter：`:formatter="formatDict"`

```javascript
formatDict(row, column, cellValue, index) {
  for (const e of this.$store.getters.dictMap) {
    if(e.groupCode === "YOUR GROUP CODE"){
      if(e.value === cellValue){
        return e.label
      }
    }
  }
},
```

### 图表组件

![图表组件](.\img\图表组件.png)

#### 折线图

组件路径：`@/components/Charts/LineChart.vue`

引用：

```javascript
import LineChart from '@/components/Charts/LineChart'

……

components: {
      LineChart
}
```

使用：

```vue
<line-chart :chart-data="lineChartData" :chart-option="lineChartOption"/>

lineChartData:{
    expected: [130, 140, 141, 142, 145, 150, 160],
    actual: [120, 82, 91, 154, 162, 140, 130]
}
lineChartOption: {
    // x轴
    xAxis: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    series: ['expected', 'actual']
}
```



#### 雷达图

组件路径：`@/components/Charts/RadarChart.vue`

引用：

```javascript
import RadarChart from '@/components/Charts/RadarChart'

……

components: {
      RadarChart
}
```

使用：

```vue
<radar-chart :chart-data="radarChartData" :chart-option="radarChartOption" />

radarChartData: {
  'Allocated Budget': [5000, 7000, 12000, 11000, 15000, 14000],
  'Expected Spending': [4000, 9000, 15000, 15000, 13000, 11000],
  'Actual Spending': [5500, 11000, 12000, 15000, 12000, 12000]
},
radarChartOption: {
  indicator: [
    { name: 'Sales', max: 10000 },
    { name: 'Administration', max: 20000 },
    { name: 'Information Techology', max: 20000 },
    { name: 'Customer Support', max: 20000 },
    { name: 'Development', max: 20000 },
    { name: 'Marketing', max: 20000 }
  ],
  series: ['Allocated Budget', 'Expected Spending', 'Actual Spending']
}
```



#### 漏斗图

组件路径：`@/components/Charts/FunnelChart.vue`

引用：

```javascript
import FunnelChart from '@/components/Charts/FunnelChart'

……

components: {
      FunnelChart
}
```

使用：

```vue
<funnel-chart :chart-data="funnelChartData" :chart-option="funnelChartOption" />

// 漏斗图
funnelChartData: [
  { value: 60, name: 'Visit' },
  { value: 30, name: 'Inquiry' },
  { value: 10, name: 'Order' },
  { value: 80, name: 'Click' },
  { value: 100, name: 'Show' }
],
funnelChartOption: {
  xAxis: ['Show', 'Click', 'Visit', 'Inquiry', 'Order'],
  name: 'Funnel',
  sort: 'none' // descending ascending none
},
```



#### 极坐标柱状图

组件路径：`@/components/Charts/TangentialPolarBarLabelPosition.vue`

引用：

```javascript
import TangentialPolarBarLabelPosition from '@/components/Charts/TangentialPolarBarLabelPosition'

……

components: {
      TangentialPolarBarLabelPosition
}
```

使用：

```vue
<tangential-polar-bar-label-position :chart-data="tangentialChartData" :chart-option="tangentialChartOption" /> 

// 极坐标柱状图标签                                  
tangentialChartOption: {                     
  series: ['a', 'b', 'c', 'd']               
},                                           
tangentialChartData: [2, 1.2, 2.4, 3.6],
```



#### 南丁格尔图

组件路径：`@/components/Charts/NightingaleChart.vue`

引用：

```javascript
import NightingaleChart from '@/components/Charts/NightingaleChart'

……

components: {
      NightingaleChart
}
```

使用：

```vue
<nightingale-chart :chart-data="nightingaleChartData" :chart-option="nightingaleChartOption" /> 

// 极坐标柱状图标签                                  
nightingaleChartData: [          
  { value: 40, name: 'rose 1' }, 
  { value: 38, name: 'rose 2' }, 
  { value: 32, name: 'rose 3' }, 
  { value: 30, name: 'rose 4' }, 
  { value: 28, name: 'rose 5' }, 
  { value: 26, name: 'rose 6' }, 
  { value: 22, name: 'rose 7' }, 
  { value: 18, name: 'rose 8' }],
nightingaleChartOption: {},      
```



#### 饼图

组件路径：`@/components/Charts/PieChart.vue`

引用：

```javascript
import PieChart from '@/components/Charts/PieChart' 

……

components: {
      PieChart
}
```

使用：

```vue
<pie-chart :chart-data="pieChartData" :chart-option="this.pieChartOption" />

// 饼图                                                                 
pieChartData: [                                                       
  { value: 320, name: 'Industries' },                                 
  { value: 240, name: 'Technology' },                                 
  { value: 149, name: 'Forex' },                                      
  { value: 100, name: 'Gold' },                                       
  { value: 59, name: 'Forecasts' }                                    
],                                                                    
pieChartOption: {                                                     
  series: ['Industries', 'Technology', 'Forex', 'Gold', 'Forecasts'], 
  name: 'WEEKLY WRITE ARTICLES'                                       
},                                                                    
```



#### 堆叠柱状图

组件路径：`@/components/Charts/BarChart.vue`

引用：

```javascript
import BarChart from '@/components/Charts/BarChart'

……

components: {
      BarChart
}
```

使用：

```vue
<bar-chart :chart-data="barChartData" :chart-option="barChartOption" />

//柱状图
barChartData: {                                                         
  'Email': [120, 132, 101, 134, 90, 230, 210],                           
  'Union Ads': [220, 182, 191, 234, 290, 330, 310],                      
  'Video Ads': [150, 232, 201, 154, 190, 330, 410],                      
  'Direct': [320, 332, 301, 334, 390, 330, 320],                         
  'Search Engine': [820, 932, 901, 934, 1290, 1330, 1320]                
},                                                                       
barChartOption: {                                                       
  xAxis: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],              
  series: ['Email', 'Union Ads', 'Video Ads', 'Direct', 'Search Engine'] 
},           
```



#### 热力图

组件路径：`@/components/Charts/HeatmapChart.vue`

引用：

```javascript
import HeatmapChart from '@/components/Charts/HeatmapChart'   

……

components: {
      HeatmapChart
}
```

使用：

```vue
<heatmap-chart :chart-data="heatmapChartData" :chart-option="heatmapChartOption" />

// 热力图                                                              
heatmapChartData: require('@/views/dashboard/admin/data/heatmap.json'), 
heatmapChartOption: {                                                   
  center: [120.13066322374, 30.240018034923]                            
}                                                                                                 
```



#### 矩形树图/旭日图

组件路径：`@/components/Charts/TreemapChart.vue`

引用：

```javascript
import TreemapChart from '@/components/Charts/TreemapChart'   

……

components: {
      TreemapChart
}
```

使用：

```vue
<treemap-chart :chart-data="treemapChartData" :chart-option="treemapChartOption" />

// 矩形树图                                                              
treemapChartData: require('@/views/dashboard/admin/data/treemap.json'),
treemapChartOption: {},                                       
```



#### 树图

组件路径：`@/components/Charts/TreeChart.vue`

引用：

```javascript
import TreeChart from '@/components/Charts/TreeChart'   

……

components: {
      TreeChart
}
```

使用：

```vue
<tree-chart :chart-data="treeChartData" :chart-option="treeChartOption" />

// 树图
treeChartData: require('@/views/dashboard/admin/data/tree.json'),
treeChartOption: {},                                                                                  
```

