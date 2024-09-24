<template>
  <el-dialog
    model-value="dialogVisible"
    :title="dialogTitle"
    width="30%"
    @close="handleClose"
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="大类名称：" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="大类描述：" prop="remark">
        <el-input v-model="form.remark" type="textarea" :rows="4" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleConfirm">确认</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script setup>
import { defineEmits, defineProps, ref, watch } from 'vue'
import axios from '@/util/axios'
import { ElMessage } from 'element-plus'
const props = defineProps({
  id: {
    type: Number,
    default: -1,
    required: true
  },
  dialogTitle: {
    type: String,
    default: '',
    required: true
  }
})
const form = ref({
  id: -1,
  name: '',
  remark: ''
})
const rules = ref({
  name: [
    {
      required: true,
      message: '请输入商品大类名称！'
    }
  ],
  remark: [
    {
      required: true,
      message: '请输入商品大类描述！'
    }
  ]
})
const formRef = ref(null)
const initFormData = async (id) => {
  const res = await axios.get('admin/bigType/' + id)
  form.value = res.data.bigType
}
watch(
  () => props.id,
  () => {
    const id = props.id
    // const mode = props.dialogTitle === '商品大类修改'
    if (id !== -1) {
      console.log('hhhhh')
      initFormData(id)
    } else {
      form.value = {
        id: -1,
        name: '',
        remark: ''
      }
    }
  }
)
// 定义父组件事件
const emits = defineEmits(['update:modelValue', 'initBigTypeList'])
const handleClose = () => {
  console.log('xxx')
  // 调用执行
  emits('update:modelValue', false)
}
const handleConfirm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      const result = await axios.post('admin/bigType/save', form.value)
      const data = result.data
      if (data.code === 0) {
        ElMessage.success('执行成功！')
        formRef.value.resetFields()
        emits('initBigTypeList')
        handleClose()
      } else {
        ElMessage.error(data.msg)
      }
    } else {
      console.log('fail')
      return false
    }
  })
}
</script>
<style scoped></style>
