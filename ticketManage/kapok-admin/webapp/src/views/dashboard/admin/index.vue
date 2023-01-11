<template>
  <div class="dashboard-editor-container">

    <!--    <panel-group @handleSetLineChartData="handleSetLineChartData" />-->

    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="6">
        <div class="item-wrapper">
          <div class="chart-wrapper">
            <funnel-chart :chart-data="funnelChartData" :chart-option="funnelChartOption" />
          </div>
          <div class="label-wrapper">漏斗图</div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="6">
        <div class="item-wrapper">
          <div class="chart-wrapper">
            <tangential-polar-bar-label-position :chart-data="tangentialChartData" :chart-option="tangentialChartOption" />
          </div>
          <div class="label-wrapper">极坐标系柱状图</div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="6">
        <div class="item-wrapper">
          <div class="chart-wrapper">
            <nightingale-chart :chart-data="nightingaleChartData" :chart-option="nightingaleChartOption" />
          </div>
          <div class="label-wrapper">南丁格尔图</div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="6">
        <div class="item-wrapper">
          <div class="chart-wrapper">
            <pie-chart :chart-data="pieChartData" :chart-option="this.pieChartOption" />
          </div>
          <div class="label-wrapper">饼图</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="6">
        <div class="item-wrapper">
          <div class="chart-wrapper">
            <radar-chart :chart-data="radarChartData" :chart-option="radarChartOption" />
          </div>
          <div class="label-wrapper">雷达图</div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="6">
        <div class="item-wrapper">
          <div class="chart-wrapper">
            <radar-chart2 :chart-data="radarChartData" :chart-option="radarChartOption" />
          </div>
          <div class="label-wrapper">雷达图2</div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="6">
        <div class="item-wrapper">
          <div class="chart-wrapper">
            <radar-chart3 :chart-data="radarChartData" :chart-option="radarChartOption" />
          </div>
          <div class="label-wrapper">雷达图3</div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="6">
        <div class="item-wrapper">
          <div class="heatmap-chart-wrapper">
            <heatmap-chart :chart-data="heatmapChartData" :chart-option="heatmapChartOption" />
          </div>
          <div class="label-wrapper">热力图</div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="6">
        <div class="item-wrapper">
          <div class="chart-wrapper">
            <bar-chart :chart-data="lineChartData" :chart-option="lineChartOption" />
          </div>
          <div class="label-wrapper">堆叠柱状图</div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="18">
        <div class="item-wrapper">
          <div class="chart-wrapper">
            <mix-bar-chart :chart-data="mixBarChartData" :chart-option="mixBarChartOption" />
          </div>
          <div class="label-wrapper">混合柱状图</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="8">
      <el-col
        :xs="{span: 24}"
        :sm="{span: 24}"
        :md="{span: 24}"
        :lg="{span: 12}"
        :xl="{span: 12}"
        style="padding-right:8px;"
      >
        <div class="item-wrapper">
          <div class="chart-wrapper">
            <treemap-chart :chart-data="treemapChartData" :chart-option="treemapChartOption" />
          </div>
          <div class="label-wrapper">矩形树图/旭日图</div>
        </div>
      </el-col>
      <el-col
        :xs="{span: 24}"
        :sm="{span: 12}"
        :md="{span: 12}"
        :lg="{span: 6}"
        :xl="{span: 6}"
      >
        <todo-list />
      </el-col>
      <el-col
        :xs="{span: 24}"
        :sm="{span: 12}"
        :md="{span: 12}"
        :lg="{span: 6}"
        :xl="{span: 6}"
      >
        <box-card />
      </el-col>
    </el-row>

    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="item-wrapper">
          <div class="chart-wrapper">
            <line-chart :chart-data="lineChartData" :chart-option="lineChartOption" />
          </div>
          <div class="label-wrapper">折线图</div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="item-wrapper">
          <div class="chart-wrapper">
            <stacked-area-chart :chart-data="lineChartData" :chart-option="lineChartOption" />
          </div>
          <div class="label-wrapper">堆叠面积图</div>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <div class="item-wrapper">
        <div class="chart-wrapper">
          <tree-chart :chart-data="treeChartData" :chart-option="treeChartOption" />
        </div>
        <div class="label-wrapper">树图</div>
      </div>
    </el-row>
  </div>
</template>

<script>
// import PanelGroup from '@/components/Charts/PanelGroup'
import LineChart from '@/components/Charts/LineChart'
import RadarChart from '@/components/Charts/RadarChart'
import RadarChart2 from '@/components/Charts/RadarChart2'
import RadarChart3 from '@/components/Charts/RadarChart3'
import StackedAreaChart from '@/components/Charts/StackedAreaChart'
import TangentialPolarBarLabelPosition from '@/components/Charts/TangentialPolarBarLabelPosition'
import MixBarChart from '@/components/Charts/MixBarChart'
import TreeChart from '@/components/Charts/TreeChart'
import NightingaleChart from '@/components/Charts/NightingaleChart'
import FunnelChart from '@/components/Charts/FunnelChart'
import TreemapChart from '@/components/Charts/TreemapChart'
import PieChart from '@/components/Charts/PieChart'
import BarChart from '@/components/Charts/BarChart'
import TodoList from '@/components/Charts/TodoList'
import BoxCard from '@/components/Charts/BoxCard'
import HeatmapChart from '@/components/Charts/HeatmapChart'

export default {
  name: 'DashboardAdmin',
  components: {
    // PanelGroup,
    LineChart,
    RadarChart,
    RadarChart2,
    RadarChart3,
    PieChart,
    BarChart,
    TodoList,
    BoxCard,
    StackedAreaChart,
    TangentialPolarBarLabelPosition,
    MixBarChart,
    NightingaleChart,
    TreeChart,
    FunnelChart,
    TreemapChart,
    HeatmapChart
  },
  data() {
    return {
      // 折线图
      lineChartData: {
        'Email': [120, 132, 101, 134, 90, 230, 210],
        'Union Ads': [220, 182, 191, 234, 290, 330, 310],
        'Video Ads': [150, 232, 201, 154, 190, 330, 410],
        'Direct': [320, 332, 301, 334, 390, 330, 320],
        'Search Engine': [820, 932, 901, 934, 1290, 1330, 1320]
      },
      lineChartOption: {
        xAxis: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        series: ['Email', 'Union Ads', 'Video Ads', 'Direct', 'Search Engine']
      },
      // 雷达图
      radarChartData: {
        'Allocated': [5000, 7000, 12000, 11000, 15000, 14000],
        'Expected': [4000, 9000, 15000, 15000, 13000, 11000],
        'Actual': [5500, 11000, 12000, 15000, 12000, 12000]
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
        series: ['Allocated', 'Expected', 'Actual']
      },
      // 极坐标柱状图标签
      tangentialChartOption: {
        series: ['a', 'b', 'c', 'd']
      },
      tangentialChartData: [2, 1.2, 2.4, 3.6],

      // 柱状图
      mixBarChartOption: {
        xAxis: ['A0', 'A1', 'A2', 'A3', 'A4', 'A5', 'A6', 'A7', 'A8', 'A9', 'A10', 'A11', 'A12', 'A13', 'A14', 'A15', 'A16', 'A17', 'A18', 'A19', 'A20', 'A21', 'A22', 'A23', 'A24', 'A25', 'A26', 'A27', 'A28', 'A29', 'A30', 'A31', 'A32', 'A33', 'A34', 'A35', 'A36', 'A37', 'A38', 'A39', 'A40', 'A41', 'A42', 'A43', 'A44', 'A45', 'A46', 'A47', 'A48', 'A49', 'A50', 'A51', 'A52', 'A53', 'A54', 'A55', 'A56', 'A57', 'A58', 'A59', 'A60', 'A61', 'A62', 'A63', 'A64', 'A65', 'A66', 'A67', 'A68', 'A69', 'A70', 'A71', 'A72', 'A73', 'A74', 'A75', 'A76', 'A77', 'A78', 'A79', 'A80', 'A81', 'A82', 'A83', 'A84', 'A85', 'A86', 'A87', 'A88', 'A89', 'A90', 'A91', 'A92', 'A93', 'A94', 'A95', 'A96', 'A97', 'A98', 'A99'],
        series: ['index1', 'index2']
      },
      mixBarChartData: {
        'index1': [0, -8.901463875624668, -17.025413764148556, -24.038196249566663, -29.66504684804471, -33.699527649688676, -36.00971978255796, -36.541005056170455, -35.31542466107655, -32.427752866005996, -28.038563739693934, -22.364693082297347, -15.667600860943732, -8.240217424060843, -0.3929067389459173, 7.560799717904647, 15.318054209871054, 22.599523033552096, 29.16065418543528, 34.800927952557615, 39.37074152590451, 42.77569739999406, 44.97819140223978, 45.99632376477021, 45.900279829731865, 44.806440199911805, 42.86957840395034, 40.2735832137877, 37.22119936652441, 33.92331243435557, 30.588309963978517, 27.412031986865767, 24.56878097935778, 22.203796820272576, 20.427519715115604, 19.311867685884827, 18.888649906111855, 19.150128087782186, 20.051630602288828, 21.516023200879346, 23.439750867099516, 25.700091656548704, 28.163208735293757, 30.692553648214542, 33.1571635093161, 35.439407573791215, 37.44177367693234, 39.09234039030659, 40.34865356244595, 41.19981246258526, 41.66666666666667, 41.80012531240646, 41.67768039516203, 41.39834040182826, 41.07625507973403, 40.833382300579814, 40.79160029175877, 41.06470032034727, 41.75070457358366, 42.924940903672564, 44.63427081999565, 46.89281122872821, 49.679416561286956, 52.93709961387478, 56.574470884754874, 60.46917221906629, 64.47317623531558, 68.41972346252496, 72.1315793340836, 75.43021771943799, 78.14548044723074, 80.12522637371026, 81.24447108408411, 81.41353029256493, 80.58471628367427, 78.75719600392792, 75.97969924353211, 72.35086229880064, 68.01710226438443, 63.16803467673056, 58.029567166714706, 52.854918421647554, 47.91391949819902, 43.48104807503482, 39.82272085822884, 37.18442111754884, 35.778264289169215, 35.77160292258658, 37.27724241244461, 40.345781666728996, 44.96051012913295, 51.035187614675685, 58.41491053964701, 66.8801325453253, 76.15376513468516, 85.91114110149952, 95.79248672571518, 105.41742429574506, 114.40092042993717, 122.37001313784816],
        'index2': [-50, -47.18992898088751, -42.54426104547181, -36.290773900754886, -28.71517529663627, -20.146937097399626, -10.94374119697364, -1.4752538113770308, 7.893046603320797, 16.81528588241657, 24.979206795219028, 32.11821023962515, 38.02096119056733, 42.53821720798438, 45.58667093073836, 47.14973738101559, 47.275355710354944, 46.07100702178889, 43.6962693226927, 40.35333240268025, 36.275975292575026, 31.71756381888028, 26.938653692729076, 22.194784893913152, 17.725026430574392, 13.741778696752679, 10.422266555457615, 7.902063853319403, 6.270884006107842, 5.570756810898967, 5.796594266992678, 6.899033489892203, 8.7893381290192, 11.346045936704996, 14.42297348773613, 17.858132851517098, 21.483081596548438, 25.132218074866262, 28.651548555679597, 31.906490373810854, 34.788333671419466, 37.21906041552118, 39.154309232933485, 40.58437366457342, 41.5332247510366, 42.05565130942339, 42.23270781895, 42.165745792772285, 41.969375711588256, 41.76375960543808, 41.66666666666667, 41.7857343479728, 42.21136481847887, 43.01065209435119, 44.22268037417866, 45.855461823273586, 47.88469584957917, 50.25443606443524, 52.879650371477126, 55.650558977584225, 58.43853958732492, 61.10330341815434, 63.500974294013034, 65.49264961151306, 66.95298925309743, 67.77836838841961, 67.89414332224722, 67.26061575374229, 65.87733853082335, 63.785482681031894, 61.068077697490004, 57.84804048526095, 54.284018163297375, 50.564180830851214, 46.89820707575337, 43.50780217852947, 40.616171775045245, 38.4369379107128, 37.16302649485318, 36.95607267600796, 37.93688225696513, 40.17745279877072, 43.694998595987045, 48.44834150353593, 54.33692802801367, 61.20261650152743, 68.83425165632042, 76.97491319735354, 85.33159602026458, 93.58695857541488, 101.4126683297632, 108.48378461530217, 114.49355390682695, 119.16795429637915, 122.27931702317058, 123.65837448506679, 123.20413594805603, 120.89107255501017, 116.7731992576505, 110.98476877890735]
      },

      // 南丁格尔玫瑰图
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

      // 树图
      treeChartData: require('@/views/dashboard/admin/data/tree.json'),
      treeChartOption: {},

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

      // Treemap
      treemapChartData: require('@/views/dashboard/admin/data/treemap.json'),
      treemapChartOption: {},

      // heatmap
      heatmapChartData: require('@/views/dashboard/admin/data/heatmap.json'),
      heatmapChartOption: {
        center: [120.13066322374, 30.240018034923]
      }

    }
  },
  methods: {
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    -webkit-box-shadow:0 2px 12px 0 rgba(0,0,0,0.1);
    box-shadow:0 2px 12px 0 rgba(0,0,0,0.1);
    border-radius: 5px;
  }
  .item-wrapper{
    margin-bottom: 32px;
  }
  .label-wrapper{
    color: #293c55;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    padding: 10px 10px 2px 10px;
    margin: 0;
    display: block;
    font-size: 14px;
    text-align: left;
  }
  .heatmap-chart-wrapper {
    background: #fff;
    padding: 8px 8px;
    -webkit-box-shadow:0 2px 12px 0 rgba(0,0,0,0.1);
    box-shadow:0 2px 12px 0 rgba(0,0,0,0.1);
    border-radius: 5px;
  }
}
</style>
