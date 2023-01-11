<template>
  <el-select
    :clearable="clearable"
    :placeholder="placeholder"
    :value="selectedValue"
    :disabled="disabled"
    @input="handleInput($event)"
  >
    <el-option
      v-for="item in dictData"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    />
  </el-select>
</template>
<script>
/* eslint-disable */
  export default {
  name: 'dictSelect',
  props: {
    value: {
      required: true
    },
    placeholder: {
      type: String,
      default: '请选择'
    },
    clearable: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    },
    groupCode: {
      type: String,
      required: true
    }
  },
  data() {
    return {}
  },
  computed: {
    selectedValue() {
      return typeof this.value === 'number' ? this.value + '' : this.value
    },
    dictData() {
      var reDict = []
      this.$store.getters.dictMap.map(e=>{
        if(e.groupCode === this.groupCode)reDict.push(e)
      })
      return reDict
    }
  },
  methods: {
    handleInput(newValue) {
      this.$emit('input', newValue) // 触发 input 事件，并传入新值
    }
  }
}
</script>
