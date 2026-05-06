<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { register } from "@/api/modules/user";

const router = useRouter();
const authStore = useAuthStore();

const mode = ref<"login" | "register">("login");
const loading = ref(false);
const error = ref("");

const loginForm = ref({ username: "", password: "" });
const registerForm = ref({
  username: "",
  password: "",
  realName: "",
  studentId: "",
});
const confirmPassword = ref("");

async function handleLogin() {
  error.value = "";
  if (!loginForm.value.username || !loginForm.value.password) {
    error.value = "请填写用户名和密码";
    return;
  }
  loading.value = true;
  try {
    await authStore.doLogin(loginForm.value.username, loginForm.value.password);
    router.push("/dashboard");
  } catch (e: any) {
    error.value = e.message || "登录失败";
  } finally {
    loading.value = false;
  }
}

async function handleRegister() {
  error.value = "";
  const r = registerForm.value;
  if (!r.username || !r.password || !r.realName || !r.studentId) {
    error.value = "请填写所有必填项";
    return;
  }
  if (r.password !== confirmPassword.value) {
    error.value = "两次输入的密码不一致";
    return;
  }
  loading.value = true;
  try {
    await register(r);
    mode.value = "login";
    loginForm.value.username = r.username;
    error.value = "注册成功，请登录";
  } catch (e: any) {
    error.value = e.message || "注册失败";
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <div class="grid place-items-center h-full">
    <div class="glass w-105 rounded-2xl p-8 shadow-lg">
      <!-- Header -->
      <div class="text-center mb-8">
        <h1
          class="text-2xl font-semibold tracking-tight text-base-content mb-2"
        >
          在线考试系统
        </h1>
        <p class="text-sm text-base-content/60">
          {{ mode === "login" ? "欢迎回来，请登录" : "创建新账号" }}
        </p>
      </div>

      <!-- Error toast -->
      <div v-if="error" class="alert alert-error alert-sm mb-4 text-sm">
        {{ error }}
      </div>

      <!-- Login Form -->
      <form
        v-if="mode === 'login'"
        class="space-y-4"
        @submit.prevent="handleLogin"
      >
        <div class="form-control">
          <label class="label">
            <span class="label-text text-sm">用户名</span>
          </label>
          <input
            v-model="loginForm.username"
            type="text"
            placeholder="请输入用户名"
            class="input input-bordered w-full rounded-lg"
          />
        </div>

        <div class="form-control">
          <label class="label">
            <span class="label-text text-sm">密码</span>
          </label>
          <input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            class="input input-bordered w-full rounded-lg"
          />
        </div>

        <button
          type="submit"
          class="btn btn-primary w-full rounded-lg mt-2"
          :class="{ loading: loading }"
          :disabled="loading"
        >
          {{ loading ? "登录中..." : "登录" }}
        </button>

        <div class="text-center text-sm">
          <span class="text-base-content/60">还没有账号？</span>
          <button
            type="button"
            class="btn btn-link btn-sm px-1"
            @click="mode = 'register'"
          >
            立即注册
          </button>
        </div>
      </form>

      <!-- Register Form -->
      <form v-else class="space-y-4" @submit.prevent="handleRegister">
        <div class="form-control">
          <label class="label">
            <span class="label-text text-sm">用户名</span>
          </label>
          <input
            v-model="registerForm.username"
            type="text"
            placeholder="请输入用户名"
            class="input input-bordered w-full rounded-lg"
          />
        </div>

        <div class="form-control">
          <label class="label">
            <span class="label-text text-sm">真实姓名</span>
          </label>
          <input
            v-model="registerForm.realName"
            type="text"
            placeholder="请输入真实姓名"
            class="input input-bordered w-full rounded-lg"
          />
        </div>

        <div class="form-control">
          <label class="label">
            <span class="label-text text-sm">学号</span>
          </label>
          <input
            v-model="registerForm.studentId"
            type="text"
            placeholder="请输入学号"
            class="input input-bordered w-full rounded-lg"
          />
        </div>

        <div class="form-control">
          <label class="label">
            <span class="label-text text-sm">密码</span>
          </label>
          <input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
            class="input input-bordered w-full rounded-lg"
          />
        </div>

        <div class="form-control">
          <label class="label">
            <span class="label-text text-sm">确认密码</span>
          </label>
          <input
            v-model="confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            class="input input-bordered w-full rounded-lg"
          />
        </div>

        <button
          type="submit"
          class="btn btn-primary w-full rounded-lg mt-2"
          :class="{ loading: loading }"
          :disabled="loading"
        >
          {{ loading ? "注册中..." : "注册" }}
        </button>

        <div class="text-center text-sm">
          <span class="text-base-content/60">已有账号？</span>
          <button
            type="button"
            class="btn btn-link btn-sm px-1"
            @click="mode = 'login'"
          >
            去登录
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
