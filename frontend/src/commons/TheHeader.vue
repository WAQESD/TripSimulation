<script setup>
import { RouterLink } from "vue-router";
import VSeperator from "../components/VSeperator.vue";
</script>

<template>
  <header>
    <div class="header-container">
      <RouterLink class="logo" to="/">Trippy</RouterLink>
      <div>
        <RouterLink class="login-btn" to="/login">Log in</RouterLink>
        <VSeperator :fontSize="'20px'" />
        <RouterLink class="signup-btn" to="/signup">Sign up</RouterLink>
      </div>
    </div>
    <div class="horizontal-line"></div>
  </header>
</template>

<style scoped>
header {
  padding: 16px 20px 0 20px;
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

div {
  font-size: 20px;
}

a {
  text-decoration: none;
  padding: 4px;
  color: black;
}

.logo {
  font-weight: bold;
  font-size: 36px;
}

.horizontal-line {
  border-bottom: 1px #9a9a9a solid;
}
</style>
