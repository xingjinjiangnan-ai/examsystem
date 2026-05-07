<script setup lang="ts">
import { ref, onMounted } from "vue";
import {
  listQuestions,
  deleteQuestion,
  createQuestion,
  updateQuestion,
} from "@/api/modules/question";
import { listSubjects } from "@/api/modules/system";
import QuestionForm from "@/components/QuestionForm.vue";
import { useAuthStore } from "@/stores/auth";
import type {
  QuestionVO,
  QuestionType,
  QuestionCreateReq,
} from "@/types/question";
import type { Subject } from "@/types/subject";

const questions = ref<QuestionVO[]>([]);
const authStore = useAuthStore();
const canEdit = authStore.isAdmin || authStore.isTeacher;
const subjects = ref<Subject[]>([]);
const loading = ref(false);
const page = ref(0);
const totalPages = ref(0);
const totalElements = ref(0);

const filterSubjectId = ref<number | undefined>(undefined);
const filterType = ref<QuestionType | undefined>(undefined);
const filterDifficulty = ref<number | undefined>(undefined);

const dialogOpen = ref(false);
const editingId = ref<number | null>(null);
const submitLoading = ref(false);
const formRef = ref<InstanceType<typeof QuestionForm> | null>(null);

const questionTypeMap: Record<QuestionType, string> = {
  SINGLE_CHOICE: "单选题",
  MULTI_CHOICE: "多选题",
  TRUE_FALSE: "判断题",
  FILL_BLANK: "填空题",
  SUBJECTIVE: "主观题",
};

async function loadData() {
  loading.value = true;
  try {
    const res = await listQuestions({
      page: page.value,
      size: 20,
      subjectId: filterSubjectId.value,
      type: filterType.value,
      difficulty: filterDifficulty.value,
    });
    questions.value = res?.content || [];
    totalPages.value = res?.totalPages || 0;
    totalElements.value = res?.totalElements || 0;
  } finally {
    loading.value = false;
  }
}

async function loadSubjects() {
  subjects.value = (await listSubjects()) || [];
}

function applyFilters() {
  page.value = 0;
  loadData();
}

function resetFilters() {
  filterSubjectId.value = undefined;
  filterType.value = undefined;
  filterDifficulty.value = undefined;
  page.value = 0;
  loadData();
}

function changePage(p: number) {
  page.value = p;
  loadData();
}

async function handleDelete(id: number) {
  if (!confirm("确定要删除该题目吗？")) return;
  await deleteQuestion(id);
  await loadData();
}

function getStemPreview(q: QuestionVO): string {
  const c = q.content;
  if ("stem" in c) return c.stem;
  return "-";
}

function openCreate() {
  editingId.value = null;
  dialogOpen.value = true;
}

function openEdit(q: QuestionVO) {
  editingId.value = q.id;
  // Pre-fill form data via initialData prop
  initialFormData.value = {
    type: q.type,
    content: q.content,
    difficulty: q.difficulty,
    subjectId: q.subjectId,
  };
  dialogOpen.value = true;
}

const initialFormData = ref<QuestionCreateReq | undefined>(undefined);

async function handleFormSubmit(req: QuestionCreateReq) {
  submitLoading.value = true;
  try {
    if (editingId.value) {
      await updateQuestion(editingId.value, req);
    } else {
      await createQuestion(req);
    }
    dialogOpen.value = false;
    initialFormData.value = undefined;
    await loadData();
  } finally {
    submitLoading.value = false;
  }
}

function closeDialog() {
  dialogOpen.value = false;
  initialFormData.value = undefined;
}

onMounted(() => {
  loadData();
  loadSubjects();
});
</script>

<template>
  <div class="space-y-4 max-w-screen-2xl mx-auto">
    <!-- Filters -->
    <div class="surface p-5 flex flex-wrap items-end gap-4">
      <div class="form-control">
        <label class="label"
          ><span class="label-text text-xs">科目</span></label
        >
        <select
          v-model="filterSubjectId"
          class="select select-bordered select-sm rounded-lg w-40"
          @change="applyFilters"
        >
          <option :value="undefined">全部</option>
          <option v-for="s in subjects" :key="s.id" :value="s.id">
            {{ s.name }}
          </option>
        </select>
      </div>
      <div class="form-control">
        <label class="label"
          ><span class="label-text text-xs">题型</span></label
        >
        <select
          v-model="filterType"
          class="select select-bordered select-sm rounded-lg w-40"
          @change="applyFilters"
        >
          <option :value="undefined">全部</option>
          <option value="SINGLE_CHOICE">单选题</option>
          <option value="MULTI_CHOICE">多选题</option>
          <option value="TRUE_FALSE">判断题</option>
          <option value="FILL_BLANK">填空题</option>
          <option value="SUBJECTIVE">主观题</option>
        </select>
      </div>
      <div class="form-control">
        <label class="label"
          ><span class="label-text text-xs">难度</span></label
        >
        <select
          v-model="filterDifficulty"
          class="select select-bordered select-sm rounded-lg w-32"
          @change="applyFilters"
        >
          <option :value="undefined">全部</option>
          <option :value="1">1</option>
          <option :value="2">2</option>
          <option :value="3">3</option>
          <option :value="4">4</option>
          <option :value="5">5</option>
        </select>
      </div>
      <button class="btn btn-ghost btn-sm rounded-lg" @click="resetFilters">
        重置
      </button>
      <div class="flex-1" />
      <button v-if="canEdit" class="btn btn-primary btn-sm rounded-lg" @click="openCreate">
        新建题目
      </button>
    </div>

    <!-- Table -->
    <div class="surface overflow-hidden">
      <table class="table table-zebra w-full">
        <thead>
          <tr class="bg-base-200/60 text-xs uppercase tracking-wider">
            <th class="h-12">ID</th>
            <th class="h-12">题型</th>
            <th class="h-12">题目内容</th>
            <th class="h-12">科目</th>
            <th class="h-12">难度</th>
            <th class="h-12 text-right">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="n in 5" v-if="loading" :key="`sk-${n}`" class="h-12">
            <td><div class="skeleton h-4 w-8" /></td>
            <td><div class="skeleton h-4 w-16" /></td>
            <td><div class="skeleton h-4 w-full max-w-xs" /></td>
            <td><div class="skeleton h-4 w-20" /></td>
            <td><div class="skeleton h-4 w-8" /></td>
            <td class="text-right">
              <div class="skeleton h-4 w-20 ml-auto" />
            </td>
          </tr>
          <tr v-if="!loading && !questions.length">
            <td colspan="6">
              <div class="grid place-items-center h-[320px]">
                <div class="text-center space-y-2">
                  <svg
                    class="w-12 h-12 mx-auto text-base-content/20"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                    stroke-width="1.5"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M9.172 16.172a4 4 0 015.656 0M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
                    />
                  </svg>
                  <p class="text-sm text-base-content/50">暂无题目数据</p>
                </div>
              </div>
            </td>
          </tr>
          <tr v-for="q in questions" :key="q.id" class="h-12">
            <td>{{ q.id }}</td>
            <td>
              <span class="badge badge-sm badge-outline">{{
                questionTypeMap[q.type]
              }}</span>
            </td>
            <td class="max-w-xs truncate">{{ getStemPreview(q) }}</td>
            <td>{{ q.subjectName }}</td>
            <td>{{ q.difficulty }}</td>
            <td class="text-right">
              <template v-if="canEdit">
                <button class="btn btn-ghost btn-sm" @click="openEdit(q)">
                  编辑
                </button>
                <button
                  class="btn btn-ghost btn-sm text-error"
                  @click="handleDelete(q.id)"
                >
                  删除
                </button>
              </template>
              <span v-else class="text-xs text-base-content/40">-</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div
      v-if="totalPages > 1"
      class="flex items-center justify-between surface p-3"
    >
      <span class="text-sm text-base-content/60"
        >共 {{ totalElements }} 条</span
      >
      <div class="join">
        <button
          class="join-item btn btn-sm"
          :disabled="page === 0"
          @click="changePage(page - 1)"
        >
          上一页
        </button>
        <button class="join-item btn btn-sm btn-disabled">
          {{ page + 1 }} / {{ totalPages }}
        </button>
        <button
          class="join-item btn btn-sm"
          :disabled="page >= totalPages - 1"
          @click="changePage(page + 1)"
        >
          下一页
        </button>
      </div>
    </div>

    <!-- Modal -->
    <div class="modal" :class="{ 'modal-open': dialogOpen }">
      <div
        class="modal-box max-w-2xl rounded-2xl shadow-xl max-h-[85vh] overflow-y-auto"
      >
        <h3 class="text-lg font-medium mb-4">
          {{ editingId ? "编辑题目" : "新建题目" }}
        </h3>
        <QuestionForm
          ref="formRef"
          :subjects="subjects"
          :initial-data="initialFormData"
          @submit="handleFormSubmit"
        />
        <div class="modal-action">
          <button class="btn btn-ghost rounded-lg" @click="closeDialog">
            取消
          </button>
          <button
            class="btn btn-primary rounded-lg"
            :class="{ loading: submitLoading }"
            :disabled="submitLoading"
            @click="formRef?.submit()"
          >
            保存
          </button>
        </div>
      </div>
      <form class="modal-backdrop" @click.prevent="closeDialog">
        <button>close</button>
      </form>
    </div>
  </div>
</template>
