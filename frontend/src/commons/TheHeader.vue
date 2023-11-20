<script setup>
import { RouterLink } from "vue-router";
import VSeperator from "../components/VSeperator.vue";
</script>

<template>
  <header>
    <div class="header-container">
      <RouterLink class="logo" to="/">
        <img class="logo-icon" src="../assets/images/logo.svg" />
        <div class="logo-text">Trippy</div>
      </RouterLink>
      <div>
        <RouterLink class="login-btn" to="/login">Login</RouterLink>
        <VSeperator :fontSize="'40px'" />
        <RouterLink class="signup-btn" to="/signup">Join</RouterLink>
      </div>
    </div>
  </header>
</template>

<style scoped>
header {
  padding: 0 20px 0 20px;
  /* box-shadow: 0px 1.2px 2.2px rgba(0, 0, 0, 0.016), 0px 2.9px 5.3px rgba(0, 0, 0, 0.022),
    0px 5.4px 10px rgba(0, 0, 0, 0.028), 0px 9.6px 17.9px rgba(0, 0, 0, 0.034), 0px 18px 33.4px rgba(0, 0, 0, 0.044),
    0px 43px 80px rgba(0, 0, 0, 0.07); */
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
</style>
