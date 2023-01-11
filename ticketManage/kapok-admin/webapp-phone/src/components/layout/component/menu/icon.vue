<script>
import { createVNode } from 'vue'
import * as AIcon from '@ant-design/icons-vue'

export default {
  props: {
    icon: {
      type: String,
      reqiure: true
    }
  },

  components: {
    AIcon
  },

  render() {
    const props = {...this.$props}
    console.log('props', AIcon)
   

    return (
      createVNode('svg', {  })
    )
  }
}

</script>