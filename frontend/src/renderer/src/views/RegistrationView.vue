<script setup lang="ts">
import { ref, onMounted } from "vue";
import {
  listRegistrationRequests,
  approveRegistration,
  rejectRegistration,
} from "@/api/modules/user";
import type { UserProfile, RegistrationType } from "@/types/user";

const requests = ref<UserProfile[]>([]);
const loading = ref(false);
const page = ref(0);
const totalPages = ref(0);
const totalElements = ref(0);
const filterStatus = ref<RegistrationType | undefined>(undefined);

const statusMap: Record<string, { label: string; cls: string }> = {
  PENDING: { label: "待审核", cls: "badge-warning" },
  ACCEPTED: { label: "已批准", cls: "badge-success" },
  REJECTED: { label: "已拒绝", cls: "badge-error" },
};

async function loadData() {
  loading.value = true;
  try {
    const res = await listRegistrationRequests({
      page: page.value,
      size: 20,
      status: filterStatus.value,
    });
    requests.value = res?.content || [];
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

async function handleApprove(id: number) {
  await approveRegistration(id);
  await loadData();
}

async function handleReject(id: number) {
  await rejectRegistration(id);
  await loadData();
}

function changePage(p: number) {
  page.value = p;
  loadData();
}

onMounted(loadData);
</script>

<template>
  <div class="space-y-4 max-w-screen-2xl mx-auto">
    <div class="flex items-center justify-between">
      <h2 class="text-lg font-medium text-base-content">注册请求管理</h2>
    </div>

    <div class="surface p-5 flex items-end gap-4">
      <div class="form-control">
        <label class="label"><span class="label-text text-xs">状态</span></label>
        <select
          v-model="filterStatus"
          class="select select-bordered select-sm rounded-lg w-32"
          @change="applyFilter"
        >
          <option :value="undefined">全部</option>
          <option value="PENDING">待审核</option>
          <option value="ACCEPTED">已批准</option>
          <option value="REJECTED">已拒绝</option>
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
            <th class="h-12">状态</th>
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
              <td><div class="skeleton h-4 w-16" /></td>
              <td class="text-right"><div class="skeleton h-4 w-28 ml-auto" /></td>
            </tr>
          </template>
          <tr v-else-if="!requests.length">
            <td colspan="6">
              <div class="grid place-items-center h-[320px]">
                <div class="text-center space-y-2">
                  <svg class="w-12 h-12 mx-auto text-base-content/20" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01" />
                  </svg>
                  <p class="text-sm text-base-content/50">暂无注册请求</p>
                </div>
              </div>
            </td>
          </tr>
          <tr v-for="r in requests" :key="r.uid" class="h-12">
            <td>{{ r.uid }}</td>
            <td>{{ r.username }}</td>
            <td>{{ r.realName }}</td>
            <td>{{ r.studentId || "-" }}</td>
            <td>
              <span class="badge badge-sm" :class="statusMap[r.status]?.cls">
                {{ statusMap[r.status]?.label || r.status }}
              </span>
            </td>
            <td class="text-right">
              <template v-if="r.status === 'PENDING'">
                <button class="btn btn-ghost btn-sm text-success" @click="handleApprove(r.uid)">批准</button>
                <button class="btn btn-ghost btn-sm text-error" @click="handleReject(r.uid)">拒绝</button>
              </template>
              <span v-else class="text-xs text-base-content/40">已处理</span>
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
  </div>
</template>
