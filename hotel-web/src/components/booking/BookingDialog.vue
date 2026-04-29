NEW_FILE_CODE
3️⃣ BookingDialog.vue - 弹窗表单
职责： 显示新增/修改订单的表单

<template>
  <el-dialog
      v-model="visible"
      :title="isEdit ? '修改订单信息' : '新客入住登记'"
      width="450px"
      @close="handleClose"
  >
    <el-form :model="form" label-width="100px">
      <el-form-item label="姓名" required>
        <el-input v-model="form.guestName" placeholder="入住人姓名" />
      </el-form-item>
      <el-form-item label="房间号" required>
        <el-input-number v-model="form.roomNumber" :min="1" />
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="form.phone" placeholder="联系方式" />
      </el-form-item>
      <el-form-item label="入住时间">
        <el-date-picker
            v-model="form.checkInDate"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="选择具体时间"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确认提交</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  isEdit: {
    type: Boolean,
    default: false
  },
  formData: {
    type: Object,
    default: () => ({
      guestName: '',
      roomNumber: null,
      phone: '',
      checkInDate: '',
      status: 1,
      totalAmount: 0
    })
  }
})

const emit = defineEmits(['update:modelValue', 'submit'])

const visible = ref(false)
const form = ref({ ...props.formData })

watch(() => props.modelValue, (val) => {
  visible.value = val
  if (val) {
    form.value = { ...props.formData }
  }
})

watch(visible, (val) => {
  emit('update:modelValue', val)
})

const handleClose = () => {
  visible.value = false
}

const handleSubmit = () => {
  emit('submit', { ...form.value })
}
</script>
