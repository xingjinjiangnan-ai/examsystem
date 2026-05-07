<script setup lang="ts">
import { ref, computed, h } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { changePassword } from "@/api/modules/user";
import { useToast } from "@/composables/useToast";

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const toast = useToast();

const sidebarCollapsed = ref(false);
const sidebarWidth = computed(() => (sidebarCollapsed.value ? "w-16" : "w-60"));

const navItems = [
  { name: "Dashboard", title: "首页", path: "/dashboard", icon: HomeIcon },
  {
    name: "Subjects",
    title: "科目管理",
    path: "/subjects",
    icon: BookIcon,
    adminOnly: true,
  },
  {
    name: "Questions",
    title: "题目管理",
    path: "/questions",
    icon: FileTextIcon,
    teacherOrAdmin: true,
  },
  {
    name: "Registrations",
    title: "注册请求",
    path: "/registrations",
    icon: UserAddIcon,
    teacherOrAdmin: true,
  },
  {
    name: "Users",
    title: "用户管理",
    path: "/users",
    icon: UsersIcon,
    adminOnly: true,
  },
];

const visibleNavItems = computed(() =>
  navItems.filter((item) => {
    if (item.adminOnly && !authStore.isAdmin) return false;
    if (item.teacherOrAdmin && !authStore.isAdmin && !authStore.isTeacher) return false;
    return true;
  }),
);

function isActive(path: string) {
  return route.path === path;
}

async function handleLogout() {
  await authStore.doLogout();
  router.push("/login");
}

// Change password
const userMenuOpen = ref(false);
let menuTimer: ReturnType<typeof setTimeout> | null = null;

function openMenu() {
  if (menuTimer) clearTimeout(menuTimer);
  userMenuOpen.value = true;
}

function closeMenu() {
  menuTimer = setTimeout(() => {
    userMenuOpen.value = false;
  }, 150);
}

const pwDialogOpen = ref(false);
const pwLoading = ref(false);
const pwForm = ref({ oldPassword: "", newPassword: "", confirmPassword: "" });

function openChangePassword() {
  pwForm.value = { oldPassword: "", newPassword: "", confirmPassword: "" };
  pwDialogOpen.value = true;
}

function closeChangePassword() {
  pwDialogOpen.value = false;
}

async function handleChangePassword() {
  const { oldPassword, newPassword, confirmPassword } = pwForm.value;
  if (!oldPassword || !newPassword || !confirmPassword) {
    toast.error("请填写所有密码字段");
    return;
  }
  if (newPassword !== confirmPassword) {
    toast.error("两次输入的新密码不一致");
    return;
  }
  pwLoading.value = true;
  try {
    await changePassword({
      username: authStore.user!.username,
      oldPassword,
      newPassword,
    });
    toast.success("密码修改成功");
    closeChangePassword();
  } finally {
    pwLoading.value = false;
  }
}

function HomeIcon() {
  return h(
    "svg",
    {
      class: "w-5 h-5",
      fill: "none",
      viewBox: "0 0 24 24",
      stroke: "currentColor",
      "stroke-width": 2,
    },
    [
      h("path", {
        "stroke-linecap": "round",
        "stroke-linejoin": "round",
        d: "M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6",
      }),
    ],
  );
}

function BookIcon() {
  return h(
    "svg",
    {
      class: "w-5 h-5",
      fill: "none",
      viewBox: "0 0 24 24",
      stroke: "currentColor",
      "stroke-width": 2,
    },
    [
      h("path", {
        "stroke-linecap": "round",
        "stroke-linejoin": "round",
        d: "M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253",
      }),
    ],
  );
}

function FileTextIcon() {
  return h(
    "svg",
    {
      class: "w-5 h-5",
      fill: "none",
      viewBox: "0 0 24 24",
      stroke: "currentColor",
      "stroke-width": 2,
    },
    [
      h("path", {
        "stroke-linecap": "round",
        "stroke-linejoin": "round",
        d: "M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z",
      }),
    ],
  );
}

function MenuIcon() {
  return h(
    "svg",
    {
      class: "w-5 h-5",
      fill: "none",
      viewBox: "0 0 24 24",
      stroke: "currentColor",
      "stroke-width": 2,
    },
    [
      h("path", {
        "stroke-linecap": "round",
        "stroke-linejoin": "round",
        d: "M4 6h16M4 12h16M4 18h16",
      }),
    ],
  );
}

function UserIcon() {
  return h(
    "svg",
    {
      class: "w-5 h-5",
      fill: "none",
      viewBox: "0 0 24 24",
      stroke: "currentColor",
      "stroke-width": 2,
    },
    [
      h("path", {
        "stroke-linecap": "round",
        "stroke-linejoin": "round",
        d: "M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z",
      }),
    ],
  );
}

function KeyIcon() {
  return h(
    "svg",
    {
      class: "w-4 h-4",
      fill: "none",
      viewBox: "0 0 24 24",
      stroke: "currentColor",
      "stroke-width": 2,
    },
    [
      h("path", {
        "stroke-linecap": "round",
        "stroke-linejoin": "round",
        d: "M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z",
      }),
    ],
  );
}

function LogoutIcon() {
  return h(
    "svg",
    {
      class: "w-4 h-4",
      fill: "none",
      viewBox: "0 0 24 24",
      stroke: "currentColor",
      "stroke-width": 2,
    },
    [
      h("path", {
        "stroke-linecap": "round",
        "stroke-linejoin": "round",
        d: "M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1",
      }),
    ],
  );
}

function UserAddIcon() {
  return h(
    "svg",
    {
      class: "w-5 h-5",
      fill: "none",
      viewBox: "0 0 24 24",
      stroke: "currentColor",
      "stroke-width": 2,
    },
    [
      h("path", {
        "stroke-linecap": "round",
        "stroke-linejoin": "round",
        d: "M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z",
      }),
    ],
  );
}

function UsersIcon() {
  return h(
    "svg",
    {
      class: "w-5 h-5",
      fill: "none",
      viewBox: "0 0 24 24",
      stroke: "currentColor",
      "stroke-width": 2,
    },
    [
      h("path", {
        "stroke-linecap": "round",
        "stroke-linejoin": "round",
        d: "M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z",
      }),
    ],
  );
}
</script>

<template>
  <div class="h-full flex">
    <!-- Sidebar -->
    <aside
      :class="[
        'glass h-full flex flex-col transition-all duration-300',
        sidebarWidth,
      ]"
    >
      <!-- Logo area -->
      <div class="h-14 flex items-center px-4">
        <span
          v-if="!sidebarCollapsed"
          class="text-lg font-semibold tracking-tight text-base-content"
        >
          考试系统
        </span>
        <span v-else class="text-lg font-semibold">考</span>
      </div>

      <!-- Nav items -->
      <nav class="flex-1 py-4 space-y-1 px-2">
        <button
          v-for="item in visibleNavItems"
          :key="item.name"
          :class="[
            'w-full flex items-center gap-3 px-3 py-2.5 rounded-lg transition-colors duration-150 text-sm',
            isActive(item.path)
              ? 'bg-primary/15 text-primary font-medium'
              : 'text-base-content/70 hover:bg-base-100/40 hover:text-base-content',
          ]"
          @click="router.push(item.path)"
        >
          <component :is="item.icon" />
          <span v-if="!sidebarCollapsed">{{ item.title }}</span>
        </button>
      </nav>

      <!-- Bottom collapse toggle -->
      <div class="p-2 border-t border-base-300/30">
        <button
          class="w-full flex items-center justify-center gap-2 px-3 py-2 rounded-lg text-sm text-base-content/60 hover:bg-base-100/40 hover:text-base-content transition-colors"
          @click="sidebarCollapsed = !sidebarCollapsed"
        >
          <MenuIcon />
          <span v-if="!sidebarCollapsed">收起</span>
        </button>
      </div>
    </aside>

    <!-- Main area -->
    <div class="flex-1 flex flex-col min-w-0">
      <!-- Topbar -->
      <header
        class="h-14 glass flex items-center justify-between px-6 border-b border-base-300/30"
      >
        <h1 class="text-base font-medium text-base-content">
          {{ route.meta.title || "考试系统" }}
        </h1>

        <!-- User dropdown -->
        <div class="dropdown dropdown-end relative"
          @mouseenter="openMenu"
          @mouseleave="closeMenu"
        >
          <div
            tabindex="0"
            role="button"
            class="flex items-center gap-2 text-sm text-base-content/80 hover:text-base-content transition-colors cursor-pointer px-2 py-1 rounded-lg hover:bg-base-100/30"
          >
            <UserIcon />
            <span>{{ authStore.user?.realName || authStore.user?.username }}</span>
            <span
              v-for="role in authStore.user?.roles"
              :key="role"
              class="badge badge-sm"
              :class="{
                'badge-error': role === 'SYSTEM_ADMIN',
                'badge-info': role === 'TEACHER',
                'badge-primary': role === 'STUDENT'
              }"
            >
              {{
                role === "SYSTEM_ADMIN"
                  ? "管理员"
                  : role === "TEACHER"
                    ? "教师"
                    : "学生"
              }}
            </span>
          </div>
          <ul
            v-show="userMenuOpen"
            tabindex="0"
            class="dropdown-content menu menu-sm bg-base-100 rounded-lg shadow-xl z-[1] w-40 p-1 border border-base-300/50 absolute right-0 top-full"
            @mouseenter="openMenu"
            @mouseleave="closeMenu"
          >
            <li>
              <button class="flex items-center gap-2" @click="openChangePassword">
                <KeyIcon />
                <span>修改密码</span>
              </button>
            </li>
            <li>
              <button class="flex items-center gap-2 text-error" @click="handleLogout">
                <LogoutIcon />
                <span>退出登录</span>
              </button>
            </li>
          </ul>
        </div>
      </header>

      <!-- Content -->
      <main class="flex-1 overflow-auto p-6">
        <Transition name="fade" mode="out-in">
          <RouterView />
        </Transition>
      </main>
    </div>
  </div>

  <!-- Change Password Modal -->
  <div class="modal" :class="{ 'modal-open': pwDialogOpen }">
    <div class="modal-box max-w-md rounded-2xl shadow-xl">
      <h3 class="text-lg font-medium mb-4">修改密码</h3>
      <div class="space-y-4">
        <div class="form-control">
          <label class="label"><span class="label-text text-sm">旧密码</span></label>
          <input
            v-model="pwForm.oldPassword"
            type="password"
            class="input input-bordered w-full rounded-lg"
            placeholder="请输入旧密码"
          />
        </div>
        <div class="form-control">
          <label class="label"><span class="label-text text-sm">新密码</span></label>
          <input
            v-model="pwForm.newPassword"
            type="password"
            class="input input-bordered w-full rounded-lg"
            placeholder="请输入新密码"
          />
        </div>
        <div class="form-control">
          <label class="label"><span class="label-text text-sm">确认新密码</span></label>
          <input
            v-model="pwForm.confirmPassword"
            type="password"
            class="input input-bordered w-full rounded-lg"
            placeholder="请再次输入新密码"
          />
        </div>
      </div>
      <div class="modal-action">
        <button class="btn btn-ghost rounded-lg" @click="closeChangePassword">取消</button>
        <button
          class="btn btn-primary rounded-lg"
          :class="{ loading: pwLoading }"
          :disabled="pwLoading"
          @click="handleChangePassword"
        >
          保存
        </button>
      </div>
    </div>
    <form class="modal-backdrop" @click.prevent="closeChangePassword">
      <button>close</button>
    </form>
  </div>
</template>
