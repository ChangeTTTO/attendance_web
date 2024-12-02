<template>
  <el-table :data="res" style="width: 100%">
    <el-table-column prop="name" label="姓名" />
    <el-table-column prop="studentNo" label="学号"  />
    <el-table-column prop="gender" label="性别" />
    <el-table-column prop="clazz" label="班级" />
    <el-table-column prop="phone" label="联系电话" />
    <el-table-column prop="address" label="家庭住址" />
    <el-table-column label="操作">
      <el-button size="small" @click="handleEdit()">
        编辑
      </el-button>
      <el-button
          size="small"
          type="danger"
          @click="handleDelete()"
      >
        删除
      </el-button>
    </el-table-column>
  </el-table>
</template>


<script lang="js" setup>
import {onMounted, ref} from "vue";
import request from "../axios.js";
let res;
const dialogFormVisible = ref(false)
const formLabelWidth = '140px'
onMounted(async () => {
  try {
    // 发送 GET 请求
    res = (await request.get('/student')).data
    console.log(res);
  } catch (error) {
    console.error("请求错误：", error);
  }
});
const handleEdit = (index, row) => {
  console.log(index, row)
}
const handleDelete = (index, row) => {
  console.log(index, row)
}
</script>
