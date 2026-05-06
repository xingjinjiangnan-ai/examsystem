<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { listSubjects, createSubject, updateSubject, deleteSubject } from '@/api/modules/system'
import type { Subject } from '@/types/subject'

const subjects = ref<Subject[]>([])
const loading = ref(false)
const dialogOpen = ref(false)
const editingSubject = ref<Subject | null>(null)
const formName = ref('')
const submitLoading = ref(false)

async function loadData() {
  loading.value = true
  try {
    subjects.value = await listSubjects() || []
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editingSubject.value = null
  formName.value = ''
  dialogOpen.value = true
}

function openEdit(s: Subject) {
  editingSubject.value = s
  formName.value = s.name
  dialogOpen.value = true
}

async function handleSubmit() {
  if (!formName.value.trim()) return
  submitLoading.value = true
  try {
    if (editingSubject.value) {
      await updateSubject(editingSubject.value.id, { name: formName.value.trim() })
    } else {
      await createSubject({ name: formName.value.trim() })
    }
    dialogOpen.value = false
    await loadData()
  } finally {
    submitLoading.value = false
  }
}

async function handleDelete(id: number) {
  if (!confirm('确定要删除该科目吗？')) return
  await deleteSubject(id)
  await loadData()
}

onMounted(loadData)
</script>

<template>
  <div class="space-y-4 max-w-screen-2xl mx-auto">
    <div class="flex items-center justify-between">
      <h2 class="text-lg font-medium text-base-content">科目列表</h2>
      <button class="btn btn-primary btn-sm rounded-lg" @click="openCreate">
        新建科目
      </button>
    </div>

    <div class="surface overflow-hidden">
      <table class="table table-zebra w-full">
        <thead>
          <tr class="bg-base-200/60 text-xs uppercase tracking-wider">
            <th class="h-12">ID</th>
            <th class="h-12">科目名称</th>
            <th class="h-12 text-right">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="loading">
            <td colspan="3" class="h-32 text-center text-base-content/40">
              <span class="loading loading-spinner loading-sm" />
              加载中...
            </td>
          </tr>
          <tr v-else-if="!subjects.length">
            <td colspan="3" class="h-32 text-center text-base-content/40">
              暂无科目数据
            </td>
          </tr>
          <tr v-for="s in subjects" :key="s.id" class="h-12">
            <td>{{ s.id }}</td>
            <td>{{ s.name }}</td>
            <td class="text-right">
              <button class="btn btn-ghost btn-sm" @click="openEdit(s)">编辑</button>
              <button class="btn btn-ghost btn-sm text-error" @click="handleDelete(s.id)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal -->
    <div class="modal" :class="{ 'modal-open': dialogOpen }">
      <div class="modal-box max-w-lg rounded-2xl shadow-xl">
        <h3 class="text-lg font-medium mb-4">
          {{ editingSubject ? '编辑科目' : '新建科目' }}
        </h3>
        <div class="form-control">
          <label class="label"><span class="label-text">科目名称</span></label>
          <input v-model="formName" class="input input-bordered w-full rounded-lg" placeholder="请输入科目名称" />
        </div>
        <div class="modal-action">
          <button class="btn btn-ghost rounded-lg" @click="dialogOpen = false">取消</button>
          <button
            class="btn btn-primary rounded-lg"
            :class="{ loading: submitLoading }"
            :disabled="submitLoading || !formName.trim()"
            @click="handleSubmit"
          >
            保存
          </button>
        </div>
      </div>
      <form class="modal-backdrop" @click.prevent="dialogOpen = false">
        <button>close</button>
      </form>
    </div>
  </div>
</template>
