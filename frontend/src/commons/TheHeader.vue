<script setup>
import { RouterLink, useRouter } from "vue-router";
import { useUserStore } from "../stores/user";
import VSeperator from "../components/VSeperator.vue";

const userStore = useUserStore();
const router = useRouter();

const logout = () => {
  localStorage.clear();
  userStore.setUserInfo(null);
  router.go(0);
};
</script>

<template>
  <header>
    <div class="header-container">
      <RouterLink class="logo" to="/">
        <img class="logo-icon" src="../assets/images/logo.svg" />
        <div class="logo-text">Trippy</div>
      </RouterLink>
      <div class="header-link-container">
        <div v-if="!userStore.userInfo">
          <RouterLink class="login-btn" to="/login">Login</RouterLink>
          <VSeperator :fontSize="'40px'" />
          <RouterLink class="signup-btn" to="/signup">Join</RouterLink>
        </div>
        <div v-else>
          <RouterLink class="mypage-btn" to="/mypage">MyPage</RouterLink>
          <VSeperator :fontSize="'40px'" />
          <a class="logout-btn" @click="logout">Logout</a>
        </div>
      </div>
    </div>
  </header>
</template>

<style scoped>
header {
  /* padding: 0 20px 0 20px; */
  box-sizing: border-box;
  width: 100%;
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: white;
  padding: 6px 60px 8px 60px;
}

div {
  font-size: 20px;
}

a {
  text-decoration: none;
  padding: 4px;
  color: black;
  font-family: "Baloo Chettan 2", sans-serif;
}

.logo {
  display: flex;
  align-items: center;
}

.logo-text {
  font-weight: bold;
  font-size: 26px;
  color: #37479e;
  font-family: "Baloo Chettan 2", sans-serif;
  margin-left: 6px;
}

.logo-icon {
  width: 24px;
  height: 24px;
}

.header-link-container {
  display: flex;
  align-items: center;
}

.logout-btn {
  cursor: pointer;
}
</style>
