<script setup lang="ts">
import { ref, onMounted } from "vue";
import { listUsers, createUser, updateUser, deleteUser } from "@/api/modules/system";
import { useToast } from "@/composables/useToast";
import type { UserProfile, RoleType } from "@/types/user";

const users = ref<UserProfile[]>([]);
const loading = ref(false);
const page = ref(0);
const totalPages = ref(0);
const totalElements = ref(0);
const filterRole = ref<RoleType | undefined>(undefined);
const toast = useToast();

const dialogOpen = ref(false);
const editingUser = ref<UserProfile | null>(null);
const submitLoading = ref(false);
const form = ref({
  username: "",
  password: "",
  realName: "",
  studentId: "",
  roles: [] as RoleType[],
});

const roleMap: Record<RoleType, string> = {
  SYSTEM_ADMIN: "管理员",
  TEACHER: "教师",
  STUDENT: "学生",
};

async function loadData() {
  loading.value = true;
  try {
    const res = await listUsers({
      page: page.value,
      size: 20,
      role: filterRole.value,
    });
    users.value = res?.content || [];
    totalPages.value = res?.totalPages || 0;
    totalElements.value = res?.totalElements || 0;
  } finally {
    loading.value = false;
  }
}

function applyFilter() {
  page.value = 0;
  loadData();
}

function openCreate() {
  editingUser.value = null;
  form.value = { username: "", password: "", realName: "", studentId: "", roles: [] };
  dialogOpen.value = true;
}

function openEdit(u: UserProfile) {
  editingUser.value = u;
  form.value = {
    username: "",
    password: "",
    realName: u.realName,
    studentId: u.studentId || "",
    roles: u.roles as RoleType[],
  };
  dialogOpen.value = true;
}

function closeDialog() {
  dialogOpen.value = false;
}

async function handleSubmit() {
  if (!form.value.realName.trim()) {
    toast.error("请填写真实姓名");
    return;
  }
  if (!isCreate && !isCreate && !form.value.roles?.length) {
    toast.error("请选择至少一个角色");
    return;
  }
  if (!isCreate) {
    if (!form.value.roles?.length) {
      toast.error("请选择至少一个角色");
      return;
    }
  }
  // 新建模式额外校验
  if (!editingUser.value) {
    if (!form.value.username.trim()) { toast.error("请填写用户名"); return; }
    if (!form.value.password.trim()) { toast.error("请填写密码"); return; }
    if (!form.value.roles?.length) { toast.error("请选择至少一个角色"); return; }
  } else {
    if (!form.value.roles?.length) { toast.error("请选择至少一个角色"); return; }
  }

  submitLoading.value = true;
  try {
    if (editingUser.value) {
      await updateUser(editingUser.value.uid, {
        realName: form.value.realName.trim(),
        studentId: form.value.studentId.trim() || undefined,
        roles: form.value.roles,
      });
    } else {
      await createUser({
        username: form.value.username.trim(),
        password: form.value.password,
        realName: form.value.realName.trim(),
        studentId: form.value.studentId.trim() || undefined,
        roles: form.value.roles,
      });
    }
    closeDialog();
    await loadData();
  } finally {
    submitLoading.value = false;
  }
}

async function handleDelete(id: number) {
  if (!confirm("确定要删除该用户吗？")) return;
  await deleteUser(id);
  await loadData();
}

function toggleRole(role: RoleType) {
  const idx = form.value.roles.indexOf(role);
  if (idx === -1) {
    form.value.roles.push(role);
  } else {
    form.value.roles.splice(idx, 1);
  }
}

const isCreate = !editingUser.value;

function changePage(p: number) {
  page.value = p;
  loadData();
}

onMounted(loadData);
</script>

<template>
  <div class="space-y-4 max-w-screen-2xl mx-auto">
    <div class="flex items-center justify-between">
      <h2 class="text-lg font-medium text-base-content">用户管理</h2>
      <button class="btn btn-primary btn-sm rounded-lg" @click="openCreate">
        新建用户
      </button>
    </div>

    <div class="surface p-5 flex items-end gap-4">
      <div class="form-control">
        <label class="label"><span class="label-text text-xs">角色</span></label>
        <select
          v-model="filterRole"
          class="select select-bordered select-sm rounded-lg w-32"
          @change="applyFilter"
        >
          <option :value="undefined">全部</option>
          <option value="SYSTEM_ADMIN">管理员</option>
          <option value="TEACHER">教师</option>
          <option value="STUDENT">学生</option>
        </select>
      </div>
    </div>

    <div class="surface overflow-hidden min-h-[400px]">
      <table class="table table-zebra w-full">
        <thead>
          <tr class="bg-base-200/60 text-xs uppercase tracking-wider">
            <th class="h-12">ID</th>
            <th class="h-12">用户名</th>
            <th class="h-12">姓名</th>
            <th class="h-12">学号</th>
            <th class="h-12">角色</th>
            <th class="h-12 text-right">操作</th>
          </tr>
        </thead>
        <tbody>
          <template v-if="loading">
            <tr v-for="n in 5" :key="`sk-${n}`" class="h-12">
              <td><div class="skeleton h-4 w-8" /></td>
              <td><div class="skeleton h-4 w-24" /></td>
              <td><div class="skeleton h-4 w-16" /></td>
              <td><div class="skeleton h-4 w-20" /></td>
              <td><div class="skeleton h-4 w-28" /></td>
              <td class="text-right"><div class="skeleton h-4 w-20 ml-auto" /></td>
            </tr>
          </template>
          <tr v-else-if="!users.length">
            <td colspan="6">
              <div class="grid place-items-center h-[320px]">
                <div class="text-center space-y-2">
                  <svg class="w-12 h-12 mx-auto text-base-content/20" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                  </svg>
                  <p class="text-sm text-base-content/50">暂无用户数据</p>
                </div>
              </div>
            </td>
          </tr>
          <tr v-for="u in users" :key="u.uid" class="h-12">
            <td>{{ u.uid }}</td>
            <td>{{ u.username }}</td>
            <td>{{ u.realName }}</td>
            <td>{{ u.studentId || "-" }}</td>
            <td>
              <div class="flex gap-1 flex-wrap">
                <span v-for="r in u.roles" :key="r" class="badge badge-sm badge-outline">
                  {{ roleMap[r as RoleType] || r }}
                </span>
              </div>
            </td>
            <td class="text-right">
              <button class="btn btn-ghost btn-sm" @click="openEdit(u)">编辑</button>
              <button class="btn btn-ghost btn-sm text-error" @click="handleDelete(u.uid)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="totalPages > 1" class="flex items-center justify-between surface p-3">
      <span class="text-sm text-base-content/60">共 {{ totalElements }} 条</span>
      <div class="join">
        <button class="join-item btn btn-sm" :disabled="page === 0" @click="changePage(page - 1)">上一页</button>
        <button class="join-item btn btn-sm btn-disabled">{{ page + 1 }} / {{ totalPages }}</button>
        <button class="join-item btn btn-sm" :disabled="page >= totalPages - 1" @click="changePage(page + 1)">下一页</button>
      </div>
    </div>

    <!-- Modal -->
    <div class="modal" :class="{ 'modal-open': dialogOpen }">
      <div class="modal-box max-w-lg rounded-2xl shadow-xl">
        <h3 class="text-lg font-medium mb-4">
          {{ editingUser ? "编辑用户" : "新建用户" }}
        </h3>
        <div class="space-y-4">
          <template v-if="!editingUser">
            <div class="form-control">
              <label class="label"><span class="label-text text-sm">用户名</span></label>
              <input v-model="form.username" class="input input-bordered w-full rounded-lg" placeholder="请输入用户名" />
            </div>
            <div class="form-control">
              <label class="label"><span class="label-text text-sm">密码</span></label>
              <input v-model="form.password" type="password" class="input input-bordered w-full rounded-lg" placeholder="请输入密码" />
            </div>
          </template>
          <div class="form-control">
            <label class="label"><span class="label-text text-sm">真实姓名</span></label>
            <input v-model="form.realName" class="input input-bordered w-full rounded-lg" placeholder="请输入真实姓名" />
          </div>
          <div class="form-control">
            <label class="label"><span class="label-text text-sm">学号（选填）</span></label>
            <input v-model="form.studentId" class="input input-bordered w-full rounded-lg" placeholder="请输入学号" />
          </div>
          <div class="form-control">
            <label class="label"><span class="label-text text-sm">角色</span></label>
            <div class="flex gap-2">
              <label v-for="r in (['SYSTEM_ADMIN', 'TEACHER', 'STUDENT'] as RoleType[])" :key="r" class="label cursor-pointer gap-2">
                <input
                  type="checkbox"
                  :checked="form.roles.includes(r)"
                  class="checkbox checkbox-sm checkbox-primary"
                  @change="toggleRole(r)"
                />
                <span class="label-text">{{ roleMap[r] }}</span>
              </label>
            </div>
          </div>
        </div>
        <div class="modal-action">
          <button class="btn btn-ghost rounded-lg" @click="closeDialog">取消</button>
          <button
            class="btn btn-primary rounded-lg"
            :class="{ loading: submitLoading }"
            :disabled="submitLoading"
            @click="handleSubmit"
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
