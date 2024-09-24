<template>
  <el-dialog
    model-value="dialogVisible"
    :title="dialogTitle"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="商品名称" prop="name">
        <el-input v-model="form.name" style="width: 400px" />
      </el-form-item>
      <el-form-item label="商品价格" prop="price">
        <el-input v-model="form.price" style="width: 100px" />
      </el-form-item>
      <el-form-item label="商品库存" prop="stock">
        <el-input v-model="form.stock" style="width: 100px" />
      </el-form-item>
      <el-form-item label="商品类别">
        <el-select
          v-model="bigTypeId"
          class="m-2"
          placeholder="请选择商品大类..."
          @change="handleBigTypeChange"
        >
          <el-option
            v-for="item in bigTypeSelectOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
        &nbsp;&nbsp;
        <el-select
          v-model="form.type.id"
          class="m-2"
          placeholder="请选择商品小
  类..."
        >
          <el-option
            v-for="item in smallTypeSelectOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="商品描述" prop="description">
        <el-input v-model="form.description" type="textarea" :rows="4" />
      </el-form-item>
      <el-form-item label="商品介绍"> </el-form-item>
      <QuillEditor
        v-model:content="form.productIntroImgs"
        contentType="html"
        toolbar="full"
        theme="snow"
        style="height: 200px"
      />
      <el-form-item label="商品参数"> </el-form-item>
      <QuillEditor contentType="html" ref="QuillEditor-a" class="ql-editor" />
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

const bigTypeSelectOptions = ref([])
const smallTypeSelectOptions = ref([])

const form = ref({
  id: -1,
  name: '',
  price: null,
  stock: null,
  type: {
    id: ''
  },
  description: '',
  productIntroImgs: '',
  productParaImgs: ''
})

const initSmallTypeSelectList = async (bigTypeId) => {
  form.value.type.id = ''
  const res = await axios.post('admin/smallType/listAll/' + bigTypeId)
  smallTypeSelectOptions.value = res.data.smallTypeList
}
const initBigTypeSelectList = async () => {
  const res = await axios.post('admin/bigType/listAll')
  bigTypeSelectOptions.value = res.data.bigTypeList
}
initBigTypeSelectList()
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
  },
  dialogVisible: {
    type: Boolean,
    default: false,
    required: true
  }
})
const bigTypeId = ref('')
const rules = ref({
  name: [
    {
      required: true,
      message: '请输入商品小类名称！'
    }
  ],
  price: [
    { required: true, message: '请输入商品价格!' },
    {
      type: 'number',
      message: '商品价格是数值类型！',
      transform: (value) => Number(value)
    }
  ],
  stock: [
    { required: true, message: '请输入商品库存!' },
    {
      type: 'number',
      message: '商品库存是数值类型！',
      transform: (value) => Number(value)
    }
  ],
  description: [
    {
      required: true,
      message: '请输入商品小类描述！'
    }
  ]
})
const formRef = ref(null)
const initFormData = async (id) => {
  const res = await axios.get('admin/product/' + id)
  bigTypeId.value = res.data.product.type.bigType.name
  form.value = res.data.product
}
const handleBigTypeChange = (bigTypeId) => {
  console.log('bigTypeId=' + bigTypeId)
  initSmallTypeSelectList(bigTypeId)
}
watch(
  () => props.id,
  () => {
    console.log(props.id)
    const id = props.id
    if (id !== -1) {
      console.log(id)
      initFormData(id)
    } else {
      form.value = {
        id: -1,
        name: '',
        price: null,
        stock: null,
        type: {
          id: ''
        },
        description: '',
        productIntroImgs: '',
        productParaImgs: ''
      }
    }
  }
)
const emits = defineEmits(['update:modelValue', 'initProductList'])
const handleClose = () => {
  emits('update:modelValue', false)
}
const handleConfirm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      if (form.value.type.id === '') {
        ElMessage.error('请选择商品大类')
        return
      }
      const result = await axios.post('admin/product/save', form.value)
      const data = result.data
      if (data.code === 0) {
        ElMessage.success('执行成功！')
        formRef.value.resetFields()
        emits('initProductList')
        handleClose()
      } else {
        ElMessage.error(data.msg)
      }
    } else {
      console.log('fail')
    }
  })
}
</script>
<style scoped></style>
